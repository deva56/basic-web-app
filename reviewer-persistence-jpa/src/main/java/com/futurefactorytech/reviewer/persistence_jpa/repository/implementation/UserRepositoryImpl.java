package com.futurefactorytech.reviewer.persistence_jpa.repository.implementation;

import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.UserRepository;
import com.futurefactorytech.reviewer.persistence_jpa.repository.springdata.UserRepositorySpringData;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Named
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositorySpringData userRepositorySpringData;

    @Inject
    public UserRepositoryImpl(UserRepositorySpringData userRepositorySpringData) {
        this.userRepositorySpringData = userRepositorySpringData;
    }

    @Override
    public User add(User user) {
        return userRepositorySpringData.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepositorySpringData.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepositorySpringData.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositorySpringData.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailOrUsername(String email, String username) {
        return userRepositorySpringData.findFirstByEmailOrUsername(email, username);
    }

    @Override
    public void deleteById(Long id) {
        userRepositorySpringData.deleteById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepositorySpringData.findAll(pageable);
    }
}
