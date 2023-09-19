package com.securityservice.auth;

import com.securityservice.quote.QuoteDto;
import com.securityservice.quote.QuoteAcceptedEvent;
import com.securityservice.token.Token;
import com.securityservice.token.TokenRepository;
import com.securityservice.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user){
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserToken.isEmpty()){
            return;
        }
        validUserToken.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow();
        revokeAllUserTokens(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }

    public String generateCode(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow();
        String passwordResetCode = null;
        if (user != null) {
            Random random = new Random();
            int randomNumber = random.nextInt(1000000000);
            passwordResetCode = Integer.toString(randomNumber);
        }
        return passwordResetCode;
    }

    public void sendCode(String username) {
        String code = generateCode(username);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(username);
        message.setSubject("Your password reset code is provided below");
        message.setText(code);

        javaMailSender.send(message);

        Optional<PasswordReset> optionalPasswordReset = passwordResetRepository.findByUsername(username);
        if (optionalPasswordReset.isPresent()) {
            PasswordReset passwordReset = optionalPasswordReset.get();
            if (username.equals(passwordReset.getUsername())) {
                passwordReset.setCode(code);
                passwordResetRepository.save(passwordReset);
            }
        } else {
            PasswordReset passwordReset = new PasswordReset();
            passwordReset.setUsername(username);
            passwordReset.setCode(code);
            passwordResetRepository.save(passwordReset);
        }
    }

    public String changePassword(String resetCode, String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Optional<PasswordReset> passwordResetOptional = passwordResetRepository.findByUsername(username);
        if (userOptional.isPresent() && passwordResetOptional.isPresent()) {
            User user = userOptional.get();
            PasswordReset passwordReset = passwordResetOptional.get();
            if (resetCode.equals(passwordReset.getCode())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return "Password changed successfully";
            } else {
                return "Invalid reset code";
            }
        } else {
            return "User or reset code not found";
        }
    }

    @KafkaListener(topics = "userTopic")
    public void autoRegister(QuoteAcceptedEvent quoteAcceptedEvent) {
        Long acceptedQuoteId = quoteAcceptedEvent.getQuoteNumber();
        WebClient webClient = WebClient.create("http://localhost:8092/quote-service");
        QuoteDto acceptedQuote = webClient.get()
                .uri("/api/quote/{id}", acceptedQuoteId)
                .retrieve()
                .bodyToMono(QuoteDto.class)
                .block();

        if (acceptedQuote != null) {
            User user = createUserFromQuoteDto(acceptedQuote);
            User user2 = userRepository.save(user);
        }
    }

    private User createUserFromQuoteDto(QuoteDto acceptedQuote) {
        User user = new User();
        user.setName(acceptedQuote.getName());
        user.setUsername(acceptedQuote.getEmail());
        user.setPassword(passwordEncoder.encode(acceptedQuote.getPassword()));
        user.setRole(Role.CUSTOMER);

        return user;
    }
}
