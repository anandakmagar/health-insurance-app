package com.quoteservice.model;

import com.quoteservice.enumeration.PlanType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table
@Data
@RequiredArgsConstructor
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
