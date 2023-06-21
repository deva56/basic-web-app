package com.futurefactorytech.reviewer.application_services.services.auth;

import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Named
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Inject
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.error("No user found with username: " + username);
            return null;
        } else {
            return user.get();
        }
    }
}
