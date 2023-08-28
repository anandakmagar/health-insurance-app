package com.quoteservice.service;

import com.quoteservice.dto.QuoteDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public interface QuoteService {
    public CompletableFuture<QuoteDto> generateQuote(QuoteDto quoteDto);
    public String acceptQuote(Long id);
    public Optional<QuoteDto> getQuoteById(Long id);
}
