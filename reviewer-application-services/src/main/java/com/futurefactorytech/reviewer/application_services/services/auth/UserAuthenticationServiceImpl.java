package com.futurefactorytech.reviewer.application_services.services.auth;

import com.futurefactorytech.reviewer.api.dtos.auth.LoginRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.auth.RegisterRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.services.auth.UserAuthenticationService;
import com.futurefactorytech.reviewer.api.services.jwt.JwtService;
import com.futurefactorytech.reviewer.api.services.mail.MailerService;
import com.futurefactorytech.reviewer.api.types.auth.AuthenticationUserStates;
import com.futurefactorytech.reviewer.api.types.auth.RegisterUserStates;
import com.futurefactorytech.reviewer.api.types.auth.TokenExpirationTimes;
import com.futurefactorytech.reviewer.api.types.jwt.JwtOperationResult;
import com.futurefactorytech.reviewer.api.types.mail.MailSendingStates;
import com.futurefactorytech.reviewer.domain.entities.*;
import com.futurefactorytech.reviewer.domain.repositories.*;
import com.futurefactorytech.reviewer.domain.types.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.*;

@Named
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private static final String ROLES = "roles";
    private static final String SESSION = "session";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ValidationTokenRepository validationTokenRepository;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailerService mailerService;
    private final AuthorityRepository authorityRepository;
    private final Logger logger = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

    @Inject
    public UserAuthenticationServiceImpl(UserRepository userRepository,
                                         RefreshTokenRepository refreshTokenRepository,
                                         ValidationTokenRepository validationTokenRepository,
                                         ForgotPasswordTokenRepository forgotPasswordTokenRepository,
                                         PasswordEncoder passwordEncoder,
                                         JwtService jwtService,
                                         AuthenticationManager authenticationManager,
                                         MailerService mailerService,
                                         AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.validationTokenRepository = validationTokenRepository;
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailerService = mailerService;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public BasicOperationResultDTO registerUser(RegisterRequestDTO registerRequestDTO) {

        Optional<User> possibleExistingUser = userRepository.findByEmailOrUsername(
                registerRequestDTO.email(),
                registerRequestDTO.username());
        if (possibleExistingUser.isEmpty()) {
            List<Authority> authorities = new ArrayList<>();
            Optional<Authority> authority = authorityRepository.findByName(Role.ROLE_USER.name());
            authority.ifPresent(authorities::add);
            userRepository.add(new User(registerRequestDTO.username(),
                    passwordEncoder.encode(registerRequestDTO.password()),
                    registerRequestDTO.email(),
                    authorities));
            BasicOperationResultDTO validationEmailResult = sendValidationEmail(registerRequestDTO.email());
            if (validationEmailResult.getStatusCode() == 200) {
                return new BasicOperationResultDTO(HttpStatus.OK.value(), RegisterUserStates.USER_REGISTERED_SUCCESSFULLY.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.FORBIDDEN.value(), MailSendingStates.ERROR_WHILE_SENDING_EMAIL.name());
            }
        } else {
            if (possibleExistingUser.get().getUsername().equals(registerRequestDTO.username())) {
                return new BasicOperationResultDTO(HttpStatus.CONFLICT.value(),
                        RegisterUserStates.USER_WITH_SAME_USERNAME_EXISTS.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.CONFLICT.value(),
                        RegisterUserStates.USER_WITH_SAME_EMAIL_EXISTS.name());
            }
        }
    }

    @Override
    public BasicOperationValueResultDTO<String> authenticateUser(LoginRequestDTO loginRequestDTO) {

        Optional<User> user = userRepository.findByEmailOrUsername
                (loginRequestDTO.usernameEmail(),
                        loginRequestDTO.usernameEmail());
        if (user.isEmpty()) {
            return new BasicOperationValueResultDTO<>(HttpStatus.NOT_FOUND.value(),
                    AuthenticationUserStates.WRONG_CREDENTIALS_USER_NOT_FOUND.name(), null);
        } else {
            if (!user.get().isActivated()) {
                return new BasicOperationValueResultDTO<>(HttpStatus.UNAUTHORIZED.value(),
                        AuthenticationUserStates.ACCOUNT_NOT_ACTIVATED.name(), null);
            } else {
                try {
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.usernameEmail(),
                            loginRequestDTO.password()
                    ));
                    if (authentication.isAuthenticated()) {
                        String accessToken = doAuthentication(loginRequestDTO.rememberMe(), user.get());
                        return new BasicOperationValueResultDTO<>(HttpStatus.OK.value(),
                                AuthenticationUserStates.AUTHENTICATION_SUCCESSFUL.name(), accessToken);
                    }
                } catch (AuthenticationException e) {
                    if (e instanceof BadCredentialsException) {
                        return new BasicOperationValueResultDTO<>(HttpStatus.UNAUTHORIZED.value(),
                                AuthenticationUserStates.WRONG_CREDENTIALS_INVALID_PASSWORD.name(), null);
                    }
                }
            }
        }
        logger.error("Unknown error occurred while authenticating user with credentials: " + loginRequestDTO.usernameEmail());
        return new BasicOperationValueResultDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                AuthenticationUserStates.UNKNOWN_ERROR_OCCURRED.name(), null);
    }

    private String doAuthentication(boolean rememberMe, User user) {
        String sessionId = UUID.randomUUID().toString();
        Map<String, Object> extraJwtClaims = new HashMap<>();
        extraJwtClaims.put(ROLES, user.getAuthorities());
        extraJwtClaims.put(SESSION, sessionId);
        RefreshToken refreshToken;
        String accessToken = jwtService.generateToken(extraJwtClaims,
                user.getUsername(),
                (int) TokenExpirationTimes.ACCESS_TOKEN.getExpirationTimeMilliseconds());
        if (rememberMe) {
            refreshToken = new RefreshToken(user, sessionId,
                    Instant.now().plusSeconds(TokenExpirationTimes.REFRESH_TOKEN_REMEMBER_ME.getExpirationTimeSeconds()));
        } else {
            refreshToken = new RefreshToken(user, sessionId,
                    Instant.now().plusSeconds(TokenExpirationTimes.REFRESH_TOKEN.getExpirationTimeSeconds()));
        }
        refreshTokenRepository.saveToken(refreshToken);
        return accessToken;
    }

    @Override
    public BasicOperationValueResultDTO<String> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_TOKEN)) {
            String accessToken = authHeader.substring(7);
            JwtOperationResult<Map<String, Object>, Exception> jwtOperationResult = jwtService.extractAllClaimsMap(accessToken);
            if (jwtOperationResult.possibleOperationException() != null) {
                if (jwtOperationResult.possibleOperationException() instanceof ExpiredJwtException expiredJwtException) {
                    Optional<User> user = userRepository.findByUsername(expiredJwtException.getClaims().getSubject());
                    if (user.isPresent()) {
                        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserAndSessionIdAndIsTokenValid(user.get(),
                                expiredJwtException.getClaims().get(SESSION).toString());
                        if (refreshToken.isPresent()) {
                            if (!refreshToken.get().isExpired()) {
                                String newAccessToken = doRefreshingToken(expiredJwtException);
                                return new BasicOperationValueResultDTO<>(HttpStatus.OK.value(),
                                        AuthenticationUserStates.NEW_ACCESS_TOKEN_GENERATED_SUCCESSFULLY.name(), newAccessToken);
                            }
                        } else {
                            logger.error(String.format("Unknown error occurred while refreshing accessToken: %s", accessToken));
                        }
                    } else {
                        logger.error(String.format("Unknown error occurred while refreshing accessToken: %s", accessToken));
                    }

                } else {
                    logger.error(String.format("JWT error while refreshing accessToken: %s", accessToken), jwtOperationResult);
                }
            } else {
                logger.error(String.format("Unknown error occurred while refreshing accessToken: %s", accessToken));
            }
        }
        return new BasicOperationValueResultDTO<>(HttpStatus.UNAUTHORIZED.value(),
                AuthenticationUserStates.REFRESH_TOKEN_EXPIRED.name(), null);
    }

    private String doRefreshingToken(ExpiredJwtException expiredJwtException) {
        Map<String, Object> extraJwtClaims = new HashMap<>();
        extraJwtClaims.put(ROLES, expiredJwtException.getClaims().get(ROLES));
        extraJwtClaims.put(SESSION, expiredJwtException.getClaims().get(SESSION));
        return jwtService.generateToken(extraJwtClaims,
                expiredJwtException.getClaims().getSubject(),
                (int) TokenExpirationTimes.ACCESS_TOKEN.getExpirationTimeMilliseconds());
    }

    @Override
    public BasicOperationResultDTO sendValidationEmail(String email) {
        String validationToken = UUID.randomUUID().toString();
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                mailerService.sendActivationEmail(email, validationToken);
                validationTokenRepository.add(
                        new ValidationToken(validationToken, Instant.now(),
                                Instant.now().plusSeconds(TokenExpirationTimes.VALIDATION_TOKEN.getExpirationTimeSeconds()),
                                user.get()));
                return new BasicOperationResultDTO(HttpStatus.OK.value(), MailSendingStates.EMAIL_SENT_SUCCESSFULLY.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.NOT_FOUND.value(), MailSendingStates.ERROR_WHILE_SENDING_EMAIL_USER_NOT_FOUND.name());
            }
        } catch (Exception e) {
            return new BasicOperationResultDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), MailSendingStates.ERROR_WHILE_SENDING_EMAIL.name());
        }
    }

    @Override
    public BasicOperationResultDTO verifyValidationCode(String token) {
        Optional<ValidationToken> validationToken = validationTokenRepository.findByTokenValue(token);
        if (validationToken.isPresent()) {
            if (validationToken.get().isTokenValid()) {
                validationToken.get().setValidated(true);
                User user = validationToken.get().getUser();
                user.setActivated(true);
                validationTokenRepository.add(validationToken.get());
                userRepository.add(user);
                return new BasicOperationResultDTO(HttpStatus.OK.value(), AuthenticationUserStates.SUCCESSFULLY_ACTIVATED_USER.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.UNAUTHORIZED.value(), AuthenticationUserStates.ERROR_WHILE_ACTIVATING_USER_TOKEN_EXPIRED.name());
            }
        }
        return new BasicOperationResultDTO(HttpStatus.NOT_FOUND.value(), AuthenticationUserStates.ERROR_WHILE_ACTIVATING_USER_TOKEN_NOT_FOUND.name());
    }

    @Override
    public BasicOperationResultDTO sendForgotPasswordEmail(String email) {
        String forgotPasswordToken = UUID.randomUUID().toString();
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                mailerService.sendForgotPasswordEmail(email, forgotPasswordToken);
                forgotPasswordTokenRepository.add(
                        new ForgotPasswordToken(forgotPasswordToken, Instant.now(),
                                Instant.now().plusSeconds(TokenExpirationTimes.FORGOT_PASSWORD_TOKEN.getExpirationTimeSeconds()),
                                user.get()));
                return new BasicOperationResultDTO(HttpStatus.OK.value(), MailSendingStates.EMAIL_SENT_SUCCESSFULLY.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.NOT_FOUND.value(), MailSendingStates.ERROR_WHILE_SENDING_EMAIL_USER_NOT_FOUND.name());
            }
        } catch (Exception e) {
            return new BasicOperationResultDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), MailSendingStates.ERROR_WHILE_SENDING_EMAIL.name());
        }
    }

    @Override
    public BasicOperationResultDTO verifyForgotPasswordCode(String token) {
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordTokenRepository.findByTokenValue(token);
        if (forgotPasswordToken.isPresent()) {
            if (Instant.now().isBefore(forgotPasswordToken.get().getExpiresAt())) {
                return new BasicOperationResultDTO(HttpStatus.OK.value(), AuthenticationUserStates.FORGOT_PASSWORD_TOKEN_VALID.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.UNAUTHORIZED.value(), AuthenticationUserStates.FORGOT_PASSWORD_TOKEN_EXPIRED.name());
            }
        } else {
            return new BasicOperationResultDTO(HttpStatus.NOT_FOUND.value(), AuthenticationUserStates.FORGOT_PASSWORD_TOKEN_NOT_FOUND.name());
        }
    }

    @Override
    public BasicOperationResultDTO setNewPassword(String token, String newPassword) {
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordTokenRepository.findByTokenValue(token);
        if (forgotPasswordToken.isPresent()) {
            if (forgotPasswordToken.get().isTokenValid()) {
                User user = forgotPasswordToken.get().getUser();
                ForgotPasswordToken invalidatedToken = forgotPasswordToken.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                invalidatedToken.setUsed(true);
                userRepository.add(user);
                forgotPasswordTokenRepository.add(invalidatedToken);
                return new BasicOperationResultDTO(HttpStatus.OK.value(),
                        AuthenticationUserStates.SUCCESSFULLY_CHANGED_USER_PASSWORD.name());
            } else {
                return new BasicOperationResultDTO(HttpStatus.UNAUTHORIZED.value(), AuthenticationUserStates.FORGOT_PASSWORD_TOKEN_EXPIRED.name());
            }
        } else {
            return new BasicOperationResultDTO(HttpStatus.NOT_FOUND.value(),
                    AuthenticationUserStates.FORGOT_PASSWORD_TOKEN_NOT_FOUND.name());
        }
    }
}
