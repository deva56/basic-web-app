package com.futurefactorytech.reviewer.domain.repositories;

import com.futurefactorytech.reviewer.domain.entities.ValidationToken;

import java.util.Optional;

public interface ValidationTokenRepository {

    Optional<ValidationToken> findByTokenValue(String tokenValue);

    ValidationToken add(ValidationToken validationToken);
}
