package com.quoteservice.service;

import com.quoteservice.enumeration.PlanType;
import com.quoteservice.dto.QuoteDto;
import com.quoteservice.event.QuoteAcceptedEvent;
import com.quoteservice.model.Quote;
import com.quoteservice.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class QuoteServiceImpl implements QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private KafkaTemplate<String, QuoteAcceptedEvent> kafkaTemplate;
    public QuoteDto mapToDto(Quote quote){
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setId(quote.getId());
        quoteDto.setName(quote.getName());
        quoteDto.setPhone(quote.getPhone());
        quoteDto.setEmail(quote.getEmail());
        quoteDto.setPassword(quote.getPassword());
        quoteDto.setDob(quote.getDob());
        quoteDto.setTobaccoUser(quote.isTobaccoUser());
        quoteDto.setPlanType(quote.getPlanType());
        quoteDto.setQuoteAcceptance(quote.isQuoteAcceptance());
        quoteDto.setMonthlyPremium(quote.getMonthlyPremium());

        return quoteDto;
    }
    public Quote mapToQuote(QuoteDto quoteDto){
        Quote quote = new Quote();
        quote.setId(quoteDto.getId());
        quote.setName(quoteDto.getName());
        quote.setPhone(quoteDto.getPhone());
        quote.setEmail(quoteDto.getEmail());
        quote.setPassword(quoteDto.getPassword());
        quote.setDob(quoteDto.getDob());
        quote.setTobaccoUser(quoteDto.isTobaccoUser());
        quote.setPlanType(quoteDto.getPlanType());
        quote.setQuoteAcceptance(quoteDto.isQuoteAcceptance());
        quote.setMonthlyPremium(quoteDto.getMonthlyPremium());
        return quote;
    }

    public double calculateAge(Date dob){
        Date currentDate = new Date();
        Date dateOfBirth = dob;

        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarBirth = Calendar.getInstance();
        calendarCurrent.setTime(new Date());
        calendarBirth.setTime(dateOfBirth);

        int years = calendarCurrent.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR);
        int months = calendarCurrent.get(Calendar.MONTH) - calendarBirth.get(Calendar.MONTH);
        int days = calendarCurrent.get(Calendar.DAY_OF_MONTH) - calendarBirth.get(Calendar.DAY_OF_MONTH);

        // Calculate the fractional part of the age in months
        double fractionalAge = 0.0;

        if (days < 0) {
            months--;
            calendarCurrent.add(Calendar.MONTH, -1);
            int maxDayInPrevMonth = calendarCurrent.getActualMaximum(Calendar.DAY_OF_MONTH);
            days += maxDayInPrevMonth;
            fractionalAge = (double) days / maxDayInPrevMonth;
        } else {
            fractionalAge = (double) days / calendarBirth.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        double age = years + (months / 12.0) + (fractionalAge / 12.0);

        return age;
    }

    @Override
    @Async
    public CompletableFuture<QuoteDto> generateQuote(QuoteDto quoteDto) {
        double standardBasePremium = 100.00;
        double premiumBasePremium = 200.00;
        double tobaccoUserCharge = 50.00;
        double above21Charge = 50.00;
        double above50Charge = 50.00;
        double monthlyPremium = standardBasePremium;
        if (quoteDto.getPlanType().equals(PlanType.STANDARD) &&
                calculateAge(quoteDto.getDob()) <= 21 &&
                !quoteDto.isTobaccoUser()){
            monthlyPremium = standardBasePremium;
        }
        if (quoteDto.getPlanType().equals(PlanType.STANDARD) &&
                calculateAge(quoteDto.getDob()) <= 21 &&
                quoteDto.isTobaccoUser()){
            monthlyPremium = standardBasePremium + tobaccoUserCharge;
        }
        if (quoteDto.getPlanType().equals(PlanType.STANDARD) &&
                calculateAge(quoteDto.getDob()) > 21 && calculateAge(quoteDto.getDob()) <= 50 &&
                !quoteDto.isTobaccoUser()) {
            monthlyPremium = standardBasePremium + above21Charge;
        }
        if (quoteDto.getPlanType().equals(PlanType.STANDARD) &&
                calculateAge(quoteDto.getDob()) > 21 && calculateAge(quoteDto.getDob()) <= 50 &&
                quoteDto.isTobaccoUser()) {
            monthlyPremium = standardBasePremium + above21Charge + tobaccoUserCharge;
        }
        if (quoteDto.getPlanType().equals(PlanType.STANDARD) &&
                calculateAge(quoteDto.getDob()) > 50 &&
                !quoteDto.isTobaccoUser()) {
            monthlyPremium = standardBasePremium + above50Charge;
        }
        if (quoteDto.getPlanType().equals(PlanType.STANDARD) &&
                calculateAge(quoteDto.getDob()) > 50 &&
                !quoteDto.isTobaccoUser()) {
            monthlyPremium = standardBasePremium + above50Charge + tobaccoUserCharge;
        }

        if (quoteDto.getPlanType().equals(PlanType.PREMIUM) &&
                calculateAge(quoteDto.getDob()) <= 21 &&
                !quoteDto.isTobaccoUser()){
            monthlyPremium = premiumBasePremium;
        }
        if (quoteDto.getPlanType().equals(PlanType.PREMIUM) &&
                calculateAge(quoteDto.getDob()) <= 21 &&
                quoteDto.isTobaccoUser()){
            monthlyPremium = premiumBasePremium + tobaccoUserCharge;
        }
        if (quoteDto.getPlanType().equals(PlanType.PREMIUM) &&
                calculateAge(quoteDto.getDob()) > 21 && calculateAge(quoteDto.getDob()) <= 50 &&
                !quoteDto.isTobaccoUser()) {
            monthlyPremium = premiumBasePremium + above21Charge;
        }
        if (quoteDto.getPlanType().equals(PlanType.PREMIUM) &&
                calculateAge(quoteDto.getDob()) > 21 && calculateAge(quoteDto.getDob()) <= 50 &&
                quoteDto.isTobaccoUser()) {
            monthlyPremium = premiumBasePremium + above21Charge + tobaccoUserCharge;
        }
        if (quoteDto.getPlanType().equals(PlanType.PREMIUM) &&
                calculateAge(quoteDto.getDob()) > 50 &&
                !quoteDto.isTobaccoUser()) {
            monthlyPremium = premiumBasePremium + above50Charge;
        }
        if (quoteDto.getPlanType().equals(PlanType.PREMIUM) &&
                calculateAge(quoteDto.getDob()) > 50 &&
                quoteDto.isTobaccoUser()) {
            monthlyPremium = premiumBasePremium + above50Charge + tobaccoUserCharge;
        }

        quoteDto.setMonthlyPremium(monthlyPremium);
        Quote quote = mapToQuote(quoteDto);
        Quote savedQuote = quoteRepository.save(quote);

        return CompletableFuture.completedFuture(mapToDto(savedQuote));
    }

    @Override
    public String acceptQuote(Long id) {
        if (quoteRepository.findById(id).isPresent()) {
            Quote quote = quoteRepository.findById(id).get();
            quote.setQuoteAcceptance(true);
            quoteRepository.save(quote);
            kafkaTemplate.send("policyTopic", new QuoteAcceptedEvent(quote.getId()));
            return "Quote with ID " + id + " has been accepted.";
        } else {
            return "Quote with ID " + id + " not found.";
        }
    }

    @Override
    public Optional<QuoteDto> getQuoteById(Long id){
        Optional<Quote> quote = quoteRepository.findById(id);
        if (quote.isPresent()){
            Quote quote2 = quote.get();
            return Optional.ofNullable(mapToDto(quote2));
        }
        return null;
    }
}
