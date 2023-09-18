package com.quoteservice.controller;

import com.quoteservice.dto.QuoteDto;
import com.quoteservice.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/quote")
public class QuoteController {
    @Autowired
    private QuoteService quoteService;
    @PostMapping("/check")
    public CompletableFuture<ResponseEntity<QuoteDto>> generateQuote(@RequestBody QuoteDto quoteDto) {
        return quoteService.generateQuote(quoteDto)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<String> acceptQuote(@PathVariable Long id){
        String response = quoteService.acceptQuote(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<QuoteDto>> getQuoteById(@PathVariable Long id){
        Optional<QuoteDto> quoteDto = quoteService.getQuoteById(id);
        return ResponseEntity.ok(quoteDto);
    }
}
