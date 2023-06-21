package com.futurefactory.reviewer.application_services.services.auth;

import com.futurefactorytech.reviewer.api.dtos.auth.RegisterRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.services.jwt.JwtService;
import com.futurefactorytech.reviewer.api.services.mail.MailerService;
import com.futurefactorytech.reviewer.application_services.services.auth.UserAuthenticationServiceImpl;
import com.futurefactorytech.reviewer.domain.entities.Authority;
import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.*;
import com.futurefactorytech.reviewer.domain.types.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private ValidationTokenRepository validationTokenRepository;
    @Mock
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MailerService mailerService;
    @Mock
    private AuthorityRepository authorityRepository;
    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;
    private final List<Authority> USER_ROLES = List.of(new Authority(Role.ROLE_USER.name()));

    @Test
    @DisplayName("Test the case when new user is registered and there is no existing user with the given email" +
            "or username")
    void testRegisterUserIfUserNotExisting() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user1", "user1@email.com",
                "password");
        User registeredUser = new User(registerRequestDTO.username(), "encryptedPassword",
                registerRequestDTO.email(), USER_ROLES);
        when(passwordEncoder.encode(registerRequestDTO.password())).thenReturn("encryptedPassword");
        when(userRepository.findByEmailOrUsername(registerRequestDTO.email(), registerRequestDTO.username())).thenReturn(Optional.empty());
        when(userRepository.add(any())).thenReturn(null);
        when(userRepository.findByEmail(registerRequestDTO.email())).thenReturn(Optional.of(registeredUser));
        doNothing().when(mailerService).sendActivationEmail(eq(registerRequestDTO.email()), any());
        when(validationTokenRepository.add(any())).thenReturn(null);
        BasicOperationResultDTO result = userAuthenticationService.registerUser(registerRequestDTO);
        assertThat(result.getStatusCode()).isEqualTo(200);
        verify(userRepository, times(1)).add(any());
        verify(userRepository, times(1)).findByEmailOrUsername(registerRequestDTO.email(), registerRequestDTO.username());
        verify(userRepository, times(1)).findByEmail(registerRequestDTO.email());
        verify(mailerService, times(1)).sendActivationEmail(eq(registerRequestDTO.email()), any());
        verify(validationTokenRepository, times(1)).add(any());
    }
}
