package com.futurefactory.reviewer.application_services.services.account;

import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.types.auth.TokenExpirationTimes;
import com.futurefactorytech.reviewer.application_services.services.account.AccountServiceImpl;
import com.futurefactorytech.reviewer.domain.entities.Authority;
import com.futurefactorytech.reviewer.domain.entities.RefreshToken;
import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.RefreshTokenRepository;
import com.futurefactorytech.reviewer.domain.repositories.UserRepository;
import com.futurefactorytech.reviewer.domain.types.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private final String USERNAME = "user";
    private final String PASSWORD = "password";
    private final String SESSION_ID = "sessionId";
    private final String USER_EMAIL = "user@email.com";
    private final List<Authority> USER_ROLES = List.of(new Authority(Role.ROLE_USER.name()));
    private final Collection<? extends GrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));
    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setupBeforeEach() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                USERNAME,
                PASSWORD,
                AUTHORITIES);
        Map<String, String> authDetails = new HashMap<>();
        String SESSION = "session";
        authDetails.put(SESSION, SESSION_ID);
        authenticationToken.setDetails(authDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Test logging user out of all sessions")
    void testLogoutFromAllSessions() {
        Optional<User> user = Optional.of(new User(USERNAME, PASSWORD, USER_EMAIL, USER_ROLES));
        List<RefreshToken> refreshTokens = List.of(new RefreshToken(user.get(), SESSION_ID,
                Instant.now().plusSeconds(TokenExpirationTimes.REFRESH_TOKEN_REMEMBER_ME.getExpirationTimeSeconds())));
        when(userRepository.findByUsername(user.get().getUsername())).thenReturn(user);
        when(refreshTokenRepository.findAllByUserAndIsTokenValid(user.get())).thenReturn(refreshTokens);
        BasicOperationResultDTO result = accountService.logoutFromAllSessions();
        assertThat(result.getStatusCode()).isEqualTo(200);
        verify(refreshTokenRepository, times(1)).saveTokens(refreshTokens);
    }

    @Test
    @DisplayName("Test logging user out of all sessions except the current session")
    void testLogoutFromAllButCurrentSession() {
        Optional<User> user = Optional.of(new User(USERNAME, PASSWORD, USER_EMAIL, USER_ROLES));
        List<RefreshToken> refreshTokens = List.of(new RefreshToken(user.get(), SESSION_ID,
                Instant.now().plusSeconds(TokenExpirationTimes.REFRESH_TOKEN_REMEMBER_ME.getExpirationTimeSeconds())));
        when(userRepository.findByUsername(user.get().getUsername())).thenReturn(user);
        when(refreshTokenRepository.findAllByUserAndIsTokenValid(user.get())).thenReturn(refreshTokens);
        BasicOperationResultDTO result = accountService.logoutFromAllButCurrentSession();
        assertThat(result.getStatusCode()).isEqualTo(200);
        verify(refreshTokenRepository, times(1)).saveTokens(refreshTokens);
    }

    @Test
    @DisplayName("Test logging user out of current session")
    void testLogoutFromCurrentSession() {
        Optional<User> user = Optional.of(new User(USERNAME, PASSWORD, USER_EMAIL, USER_ROLES));
        Optional<RefreshToken> refreshToken = Optional.of(new RefreshToken(user.get(), SESSION_ID,
                Instant.now().plusSeconds(TokenExpirationTimes.REFRESH_TOKEN_REMEMBER_ME.getExpirationTimeSeconds())));
        when(userRepository.findByUsername(user.get().getUsername())).thenReturn(user);
        when(refreshTokenRepository.findByUserAndSessionIdAndIsTokenValid(user.get(), SESSION_ID)).thenReturn(refreshToken);
        BasicOperationResultDTO result = accountService.logoutFromCurrentSession();
        assertThat(result.getStatusCode()).isEqualTo(200);
        verify(refreshTokenRepository, times(1)).saveToken(refreshToken.get());
    }
}
