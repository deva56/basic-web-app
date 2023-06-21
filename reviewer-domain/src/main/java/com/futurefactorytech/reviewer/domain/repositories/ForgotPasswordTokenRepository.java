package com.futurefactorytech.reviewer.domain.repositories;

import com.futurefactorytech.reviewer.domain.entities.ForgotPasswordToken;

import java.util.Optional;

public interface ForgotPasswordTokenRepository {

    Optional<ForgotPasswordToken> findByTokenValue(String tokenValue);

    ForgotPasswordToken add(ForgotPasswordToken forgotPasswordToken);
}
