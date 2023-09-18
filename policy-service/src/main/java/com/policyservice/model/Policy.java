package com.policyservice.model;

import com.policyservice.enumeration.PlanType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table
@Data
@RequiredArgsConstructor
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String policyNumber;
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
    @CreationTimestamp
    private Timestamp createdTimestamp;
    @Temporal(TemporalType.DATE)
    private Date policyExpireDate;
    @PrePersist
    public void calculatePolicyExpireDate() {
        if (createdTimestamp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createdTimestamp);
            calendar.add(Calendar.YEAR, 1);
            policyExpireDate = calendar.getTime();
        }
    }
}
