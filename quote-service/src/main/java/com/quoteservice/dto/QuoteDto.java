package com.quoteservice.dto;

import com.quoteservice.enumeration.PlanType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class QuoteDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private Date dob;
    private boolean tobaccoUser;
    private PlanType planType;
    private double monthlyPremium;
    private boolean quoteAcceptance = false;
}
