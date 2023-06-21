package com.futurefactorytech.reviewer.persistence_jpa.repository.springdata;

import com.futurefactorytech.reviewer.domain.entities.ValidationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ValidationTokenRepositorySpringData extends CrudRepository<ValidationToken, Long> {

    Optional<ValidationToken> findByTokenValue(String tokenValue);
}
