package com.policyservice.controller;

import com.policyservice.dto.PolicyDto;
import com.policyservice.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {
    @Autowired
    private PolicyService policyService;

    @GetMapping("/{policyNumber}")
    public ResponseEntity<PolicyDto> findByPolicyNumber(@PathVariable String policyNumber){
        PolicyDto policyDto = policyService.findByPolicyNumber(policyNumber);
        return ResponseEntity.ok(policyDto);
    }
}
