package com.futurefactorytech.reviewer.persistence_jpa.repository.springdata;

import com.futurefactorytech.reviewer.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositorySpringData extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findFirstByEmailOrUsername(String email, String username);
}
