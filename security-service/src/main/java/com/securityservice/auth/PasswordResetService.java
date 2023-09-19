package com.securityservice.auth;

import com.securityservice.auth.PasswordReset;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PasswordResetService {
    public void savePasswordResetCode(PasswordReset passwordReset);
    public Optional<PasswordReset> findByUsername(String username);
    Optional<PasswordReset> findByCode(String code);
}
