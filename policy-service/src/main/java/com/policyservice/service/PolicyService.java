package com.policyservice.service;

import com.policyservice.dto.PolicyDto;
import org.springframework.stereotype.Service;

@Service
public interface PolicyService {
    public PolicyDto findByPolicyNumber(String policyNumber);
    public void deletePolicy(String policyNumber);
}
