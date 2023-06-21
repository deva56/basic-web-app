package com.futurefactorytech.reviewer.persistence_jpa.repository.implementation;

import com.futurefactorytech.reviewer.domain.entities.ValidationToken;
import com.futurefactorytech.reviewer.domain.repositories.ValidationTokenRepository;
import com.futurefactorytech.reviewer.persistence_jpa.repository.springdata.ValidationTokenRepositorySpringData;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

@Named
public class ValidationTokenRepositoryImpl implements ValidationTokenRepository {

    private final ValidationTokenRepositorySpringData validationTokenRepositorySpringData;

    @Inject
    public ValidationTokenRepositoryImpl(ValidationTokenRepositorySpringData validationTokenRepositorySpringData) {
        this.validationTokenRepositorySpringData = validationTokenRepositorySpringData;
    }

    @Override
    public ValidationToken add(ValidationToken validationToken) {
        return validationTokenRepositorySpringData.save(validationToken);
    }

    @Override
    public Optional<ValidationToken> findByTokenValue(String tokenValue) {
        return validationTokenRepositorySpringData.findByTokenValue(tokenValue);
    }
}
