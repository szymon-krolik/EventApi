package com.example.praca.service;

import com.example.praca.model.ConfirmationToken;
import com.example.praca.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository CONFIRMATION_TOKEN_REPOSITORY;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        CONFIRMATION_TOKEN_REPOSITORY.save(confirmationToken);
    }

    public void deleteConfirmationToken(Long confirmationToken) {
        CONFIRMATION_TOKEN_REPOSITORY.deleteById(confirmationToken);
    }

    public void saveForgotPasswordToken(ConfirmationToken forgotPasswordToken) {
        CONFIRMATION_TOKEN_REPOSITORY.save(forgotPasswordToken);
    }

    public void deleteForgotPasswordToken(Long forgotPasswordToken) {
        CONFIRMATION_TOKEN_REPOSITORY.deleteById(forgotPasswordToken);
    }
}