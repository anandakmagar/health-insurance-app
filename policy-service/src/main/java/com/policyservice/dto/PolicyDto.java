package com.policyservice.dto;

import com.policyservice.enumeration.PlanType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class PolicyDto {
    private Long id;
    private String policyNumber;
    private String name;
    private String phone;
    private String email;
    private String password;
    private Date dob;
    private boolean tobaccoUser;
    private PlanType planType;
    private double monthlyPremium;
    private Timestamp createdTimestamp;
    private Date policyExpireDate;
}
