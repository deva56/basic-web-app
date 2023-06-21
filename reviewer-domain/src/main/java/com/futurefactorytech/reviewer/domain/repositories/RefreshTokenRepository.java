package com.futurefactorytech.reviewer.domain.repositories;

import com.futurefactorytech.reviewer.domain.entities.RefreshToken;
import com.futurefactorytech.reviewer.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken saveToken(RefreshToken refreshToken);

    List<RefreshToken> saveTokens(List<RefreshToken> refreshTokens);

    Optional<RefreshToken> findByUserAndIsTokenValid(User user);

    Optional<RefreshToken> findBySessionId(String sessionId);

    Optional<RefreshToken> findByUserAndSessionIdAndIsTokenValid(User user, String sessionId);

    List<RefreshToken> findAllByUserAndIsTokenValid(User user);
}
