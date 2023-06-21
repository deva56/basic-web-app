package com.futurefactorytech.reviewer.persistence_jpa.repository.implementation;

import com.futurefactorytech.reviewer.domain.entities.Authority;
import com.futurefactorytech.reviewer.domain.repositories.AuthorityRepository;
import com.futurefactorytech.reviewer.persistence_jpa.repository.springdata.AuthorityRepositorySpringData;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;

@Named
public class AuthorityRepositoryImpl implements AuthorityRepository {

    private final AuthorityRepositorySpringData authorityRepositorySpringData;

    @Inject
    public AuthorityRepositoryImpl(AuthorityRepositorySpringData authorityRepositorySpringData) {
        this.authorityRepositorySpringData = authorityRepositorySpringData;
    }

    @Override
    public Optional<Authority> findByName(String name) {
        return authorityRepositorySpringData.findByName(name);
    }

    @Override
    public Authority saveAuthority(Authority authority) {
        return authorityRepositorySpringData.save(authority);
    }

    @Override
    public void deleteAuthority(Authority authority) {
        authorityRepositorySpringData.delete(authority);
    }
}
