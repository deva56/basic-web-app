package com.futurefactorytech.reviewer.domain.repositories;

import com.futurefactorytech.reviewer.domain.entities.Authority;

import java.util.Optional;

public interface AuthorityRepository {

    Optional<Authority> findByName(String name);

    Authority saveAuthority(Authority authority);

    void deleteAuthority(Authority authority);
}
