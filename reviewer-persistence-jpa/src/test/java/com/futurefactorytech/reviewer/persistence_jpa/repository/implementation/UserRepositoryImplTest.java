package com.futurefactorytech.reviewer.persistence_jpa.repository.implementation;

import com.futurefactorytech.reviewer.domain.entities.Authority;
import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.types.Role;
import com.futurefactorytech.reviewer.persistence_jpa.repository.springdata.UserRepositorySpringData;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositorySpringData userRepositorySpringData;
    private User user;
    private UserRepositoryImpl userRepository;
    private final Long userId = 1L;
    private final List<Authority> USER_ROLES = List.of(new Authority(Role.ROLE_USER.name()));

    @PostConstruct
    void setupBeforeAll() {
        userRepository = new UserRepositoryImpl(userRepositorySpringData);
    }

    @BeforeEach
    void setupBeforeEach() {
        userRepositorySpringData.deleteAll();
        user = new User("user1", "user1@email.com", "password", USER_ROLES);
    }

    @Test
    void testSaveNewUser() {
        User testUser = userRepository.add(user);
        assertThat(testUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindById() {
        Optional<User> dbUser;
        dbUser = userRepository.findById(userId);
        assertThat(dbUser).isEmpty();
        User testUser = userRepository.add(user);
        dbUser = userRepository.findById(testUser.getId());
        assertThat(dbUser).isNotEmpty();
        assertThat(dbUser.get().getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    void testFindByUsername() {
        Optional<User> dbUser;
        dbUser = userRepository.findByUsername(user.getUsername());
        assertThat(dbUser).isEmpty();
        userRepository.add(user);
        dbUser = userRepository.findByUsername(user.getUsername());
        assertThat(dbUser).isNotEmpty();
        assertThat(dbUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindByEmail() {
        Optional<User> dbUser;
        dbUser = userRepository.findByEmail(user.getEmail());
        assertThat(dbUser).isEmpty();
        userRepository.add(user);
        dbUser = userRepository.findByEmail(user.getEmail());
        assertThat(dbUser).isNotEmpty();
        assertThat(dbUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindByEmailOrUsername() {
        Optional<User> dbUser;
        dbUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        assertThat(dbUser).isEmpty();
        userRepository.add(user);
        dbUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        assertThat(dbUser).isNotEmpty();
        assertThat(dbUser.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(dbUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testDeleteById() {
        Optional<User> dbUser;
        dbUser = userRepository.findById(userId);
        assertThat(dbUser).isEmpty();
        User testUser = userRepository.add(user);
        dbUser = userRepository.findById(testUser.getId());
        assertThat(dbUser).isNotEmpty();
        assertThat(testUser.getId()).isEqualTo(dbUser.get().getId());
        userRepository.deleteById(testUser.getId());
        dbUser = userRepository.findById(testUser.getId());
        assertThat(dbUser).isEmpty();
    }

}
