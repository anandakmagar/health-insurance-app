package com.notifcationservice.dto;

import com.notifcationservice.enumeration.PlanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
