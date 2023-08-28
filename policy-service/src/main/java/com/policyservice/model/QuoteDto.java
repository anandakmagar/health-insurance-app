package com.policyservice.model;

import com.policyservice.enumeration.PlanType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class QuoteDto {
    private Long id;
    private String name;
    private String phone;
    @Column(unique = true)
    private String email;
    private String password;
    private Date dob;
    private boolean tobaccoUser;
    @Enumerated(EnumType.STRING)
    private PlanType planType;
    private double monthlyPremium;
    private boolean quoteAcceptance = false;
}
