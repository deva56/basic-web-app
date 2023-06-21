package com.futurefactorytech.reviewer.persistence_jpa.repository.implementation;

import com.futurefactorytech.reviewer.domain.entities.ForgotPasswordToken;
import com.futurefactorytech.reviewer.domain.repositories.ForgotPasswordTokenRepository;
import com.futurefactorytech.reviewer.persistence_jpa.repository.springdata.ForgotPasswordTokenRepositorySpringData;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

@Named
public class ForgotPasswordTokenRepositoryImpl implements ForgotPasswordTokenRepository {

    private final ForgotPasswordTokenRepositorySpringData forgotPasswordTokenRepositorySpringData;

    @Inject
    public ForgotPasswordTokenRepositoryImpl(ForgotPasswordTokenRepositorySpringData forgotPasswordTokenRepositorySpringData) {
        this.forgotPasswordTokenRepositorySpringData = forgotPasswordTokenRepositorySpringData;
    }

    @Override
    public Optional<ForgotPasswordToken> findByTokenValue(String tokenValue) {
        return forgotPasswordTokenRepositorySpringData.findByTokenValue(tokenValue);
    }

    @Override
    public ForgotPasswordToken add(ForgotPasswordToken forgotPasswordToken) {
        return forgotPasswordTokenRepositorySpringData.save(forgotPasswordToken);
    }
}
