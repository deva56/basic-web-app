package com.futurefactorytech.reviewer.application_services.services.account;

import com.futurefactorytech.reviewer.api.dtos.account.AccountDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.services.account.AccountService;
import com.futurefactorytech.reviewer.api.types.auth.AuthenticationUserStates;
import com.futurefactorytech.reviewer.api.types.shared.OperationStates;
import com.futurefactorytech.reviewer.application_services.services.utils.AuthenticatedUserUtil;
import com.futurefactorytech.reviewer.domain.entities.RefreshToken;
import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.RefreshTokenRepository;
import com.futurefactorytech.reviewer.domain.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Inject
    public AccountServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public BasicOperationResultDTO logoutFromAllSessions() {
        Optional<User> user = userRepository.findByUsername(AuthenticatedUserUtil.getLoggedInUserName());
        if (user.isPresent()) {
            List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserAndIsTokenValid(user.get());
            for (RefreshToken refreshToken : refreshTokens) {
                refreshToken.setValid(false);
            }
            refreshTokenRepository.saveTokens(refreshTokens);
            SecurityContextHolder.clearContext();
            return new BasicOperationResultDTO(HttpStatus.OK.value(), AuthenticationUserStates.LOGOUT_SUCCESSFUL.name());
        }
        logger.error(String.format("Error while logging out user with username: %s and sessionId: %s",
                AuthenticatedUserUtil.getLoggedInUserName(), AuthenticatedUserUtil.getLoggedInUserDetailsSession()));
        return new BasicOperationResultDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), AuthenticationUserStates.LOGOUT_ERROR.name());
    }

    @Override
    public BasicOperationResultDTO logoutFromCurrentSession() {
        Optional<User> user = userRepository.findByUsername(AuthenticatedUserUtil.getLoggedInUserName());
        if (user.isPresent()) {
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserAndSessionIdAndIsTokenValid(user.get(),
                    AuthenticatedUserUtil.getLoggedInUserDetailsSession());
            if (refreshToken.isPresent()) {
                refreshToken.get().setValid(false);
                refreshTokenRepository.saveToken(refreshToken.get());
                SecurityContextHolder.clearContext();
                return new BasicOperationResultDTO(HttpStatus.OK.value(), AuthenticationUserStates.LOGOUT_SUCCESSFUL.name());
            }
        }
        logger.error(String.format("Error while logging out user with username: %s and sessionId: %s",
                AuthenticatedUserUtil.getLoggedInUserName(), AuthenticatedUserUtil.getLoggedInUserDetailsSession()));
        return new BasicOperationResultDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), AuthenticationUserStates.LOGOUT_ERROR.name());
    }

    @Override
    public BasicOperationResultDTO logoutFromAllButCurrentSession() {
        Optional<User> user = userRepository.findByUsername(AuthenticatedUserUtil.getLoggedInUserName());
        if (user.isPresent()) {
            List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserAndIsTokenValid(user.get());
            String sessionId = AuthenticatedUserUtil.getLoggedInUserDetailsSession();
            for (RefreshToken refreshToken : refreshTokens) {
                if (!refreshToken.getSessionId().equals(sessionId)) {
                    refreshToken.setValid(false);
                }
            }
            refreshTokenRepository.saveTokens(refreshTokens);
            return new BasicOperationResultDTO(HttpStatus.OK.value(), AuthenticationUserStates.LOGOUT_SUCCESSFUL.name());
        }
        logger.error(String.format("Error while logging out user with username: %s and sessionId: %s",
                AuthenticatedUserUtil.getLoggedInUserName(), AuthenticatedUserUtil.getLoggedInUserDetailsSession()));
        return new BasicOperationResultDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), AuthenticationUserStates.LOGOUT_ERROR.name());
    }

    @Override
    public BasicOperationValueResultDTO<AccountDTO> getUserAccountDetails() {
        Optional<User> user = userRepository.findByUsername(AuthenticatedUserUtil.getLoggedInUserName());
        if (user.isPresent()) {
            AccountDTO accountDTO = new AccountDTO(user.get().getUsername(), user.get().getEmail(),
                    user.get().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            return new BasicOperationValueResultDTO<>(HttpStatus.OK.value(), OperationStates.OPERATION_SUCCESSFUL.name(), accountDTO);
        }
        return new BasicOperationValueResultDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), OperationStates.OPERATION_FAILED.name(), null);
    }
}
