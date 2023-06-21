package com.futurefactorytech.reviewer.domain.repositories;

import com.futurefactorytech.reviewer.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {

    User add(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUsername(String email, String username);

    void deleteById(Long id);

    Page<User> findAll(Pageable pageable);
}
