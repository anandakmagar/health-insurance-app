package com.securityservice.controller;

import com.securityservice.dto.QuoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@RestController
@RequestMapping("/api/secured")
public class SecuredController {
    @Autowired
    private WebClient.Builder webclientBuilder;

    @PostMapping("/accept/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> acceptQuote(@PathVariable Long id){
        String response = webclientBuilder.build().post()
                .uri("http://localhost:8081/api/quote/accept/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Optional<QuoteDto>> getQuoteById(@PathVariable Long id){
        Optional<QuoteDto> quoteDto = Optional.ofNullable(webclientBuilder.build().get()
                .uri("http://localhost:8081/api/quote/" + id)
                .retrieve()
                .bodyToMono(QuoteDto.class)
                .block());
        return ResponseEntity.ok(quoteDto);
    }
}
