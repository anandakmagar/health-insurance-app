package com.policyservice.service;

import com.policyservice.dto.NotificationDto;
import com.policyservice.dto.PolicyDto;
import com.policyservice.event.QuoteAcceptedEvent;
import com.policyservice.model.Policy;
import com.policyservice.model.QuoteDto;
import com.policyservice.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Calendar;
import java.util.Date;

@Service
public class PolicyServiceImpl implements PolicyService {
    @Autowired
    PolicyRepository policyRepository;

    public PolicyDto mapToDto(Policy policy){
        PolicyDto policyDto = new PolicyDto();
        policyDto.setPolicyNumber(policy.getPolicyNumber());
        policyDto.setName(policy.getName());
        policyDto.setEmail(policy.getEmail());
        policyDto.setDob(policy.getDob());
        policyDto.setId(policy.getId());
        policyDto.setPassword(policy.getPassword());
        policyDto.setPhone(policy.getPhone());
        policyDto.setPolicyExpireDate(policy.getPolicyExpireDate());
        policyDto.setCreatedTimestamp(policy.getCreatedTimestamp());
        policyDto.setPlanType(policy.getPlanType());
        policyDto.setTobaccoUser(policy.isTobaccoUser());
        policyDto.setMonthlyPremium(policy.getMonthlyPremium());

        return policyDto;
    }

    @KafkaListener(topics = "policyTopic")
    public void handlePolicyCreation(QuoteAcceptedEvent quoteAcceptedEvent) {
        Long acceptedQuoteId = quoteAcceptedEvent.getQuoteNumber();

        WebClient webClient = WebClient.create("http://localhost:8092/quote-service");
        QuoteDto acceptedQuote = webClient.get()
                .uri("/api/quote/{id}", acceptedQuoteId)
                .retrieve()
                .bodyToMono(QuoteDto.class)
                .block();

        if (acceptedQuote != null) {
            Policy policy = createPolicyFromQuoteDto(acceptedQuote);
            Policy policy2 = policyRepository.save(policy);

            String recipient = policy.getEmail();
            String subject = "Policy Generated";
            String body = "Dear " + policy2.getName() + ", " +
                    "\nYour health insurance policy has been generated. " +
                    "\nPolicy Details\n" +
                    "Policy Number: " + policy2.getPolicyNumber() + "\n" +
                    "Policy Type: " + policy2.getPlanType() + "\n" +
                    "Monthly Premium: $" + policy2.getMonthlyPremium() + "\n" +
                    "Policy Activation Date: " + policy2.getCreatedTimestamp() + "\n" +
                    "Policy Expiration Date: " + policy2.getPolicyExpireDate() + "\n" +
                    "\nThank you for choosing our services.";

            NotificationDto notificationDto = new NotificationDto(recipient, subject, body);

            webClient = WebClient.create("http://localhost:8092/notification-service");
            ClientResponse response = webClient.post()
                    .uri("/api/notification")
                    .body(BodyInserters.fromValue(notificationDto))
                    .exchange()
                    .block();
            }
        }


    private Policy createPolicyFromQuoteDto(QuoteDto quoteDto) {
        Policy policy = new Policy();
        policy.setPolicyNumber(generatePolicyNumber());
        policy.setName(quoteDto.getName());
        policy.setPhone(quoteDto.getPhone());
        policy.setEmail(quoteDto.getEmail());
        policy.setPassword(quoteDto.getPassword());
        policy.setDob(quoteDto.getDob());
        policy.setTobaccoUser(quoteDto.isTobaccoUser());
        policy.setPlanType(quoteDto.getPlanType());
        policy.setMonthlyPremium(quoteDto.getMonthlyPremium());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        policy.setPolicyExpireDate(calendar.getTime());

        return policy;
    }
    private String generatePolicyNumber() {
        return "POL" + System.currentTimeMillis();
    }

    @Override
    public PolicyDto findByPolicyNumber(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        PolicyDto policyDto = mapToDto(policy);
        return policyDto;
    }

    @Override
    public void deletePolicy(String policyNumber) {

    }
}
