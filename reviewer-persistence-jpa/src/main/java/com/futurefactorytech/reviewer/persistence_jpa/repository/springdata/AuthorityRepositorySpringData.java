package com.futurefactorytech.reviewer.persistence_jpa.repository.springdata;

import com.futurefactorytech.reviewer.domain.entities.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepositorySpringData extends CrudRepository<Authority, Long> {

    Optional<Authority> findByName(String name);
}
