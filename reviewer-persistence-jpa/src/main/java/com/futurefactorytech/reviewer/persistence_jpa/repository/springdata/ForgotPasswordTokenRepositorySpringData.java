package com.futurefactorytech.reviewer.persistence_jpa.repository.springdata;

import com.futurefactorytech.reviewer.domain.entities.ForgotPasswordToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ForgotPasswordTokenRepositorySpringData extends CrudRepository<ForgotPasswordToken, Long> {

    Optional<ForgotPasswordToken> findByTokenValue(String tokenValue);
}
