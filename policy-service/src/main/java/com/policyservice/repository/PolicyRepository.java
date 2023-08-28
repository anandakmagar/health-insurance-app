package com.policyservice.repository;

import com.policyservice.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    public Policy findByPolicyNumber(String policyNumber);
}
