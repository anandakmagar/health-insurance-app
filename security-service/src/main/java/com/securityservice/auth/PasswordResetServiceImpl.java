package com.securityservice.auth;

import com.securityservice.auth.PasswordReset;
import com.securityservice.auth.PasswordResetRepository;
import com.securityservice.auth.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Override
    public void savePasswordResetCode(PasswordReset passwordReset) {
        passwordResetRepository.save(passwordReset);
    }

    @Override
    public Optional<PasswordReset> findByUsername(String username) {
        return Optional.of(passwordResetRepository.findByUsername(username).get());
    }

    @Override
    public Optional<PasswordReset> findByCode(String code) {
        return Optional.of(passwordResetRepository.findByCode(code).get());
    }
}
