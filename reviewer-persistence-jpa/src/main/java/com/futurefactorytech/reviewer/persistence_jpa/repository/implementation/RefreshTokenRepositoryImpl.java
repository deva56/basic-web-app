package com.futurefactorytech.reviewer.persistence_jpa.repository.implementation;

import com.futurefactorytech.reviewer.domain.entities.RefreshToken;
import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.RefreshTokenRepository;
import com.futurefactorytech.reviewer.persistence_jpa.repository.springdata.RefreshTokenRepositorySpringData;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenRepositorySpringData refreshTokenRepositorySpringData;

    @Inject
    public RefreshTokenRepositoryImpl(RefreshTokenRepositorySpringData refreshTokenRepositorySpringData) {
        this.refreshTokenRepositorySpringData = refreshTokenRepositorySpringData;
    }

    @Override
    public RefreshToken saveToken(RefreshToken refreshToken) {
        return refreshTokenRepositorySpringData.save(refreshToken);
    }

    @Override
    public List<RefreshToken> saveTokens(List<RefreshToken> refreshTokens) {
        return (List<RefreshToken>) refreshTokenRepositorySpringData.saveAll(refreshTokens);
    }

    @Override
    public Optional<RefreshToken> findByUserAndIsTokenValid(User user) {
        return refreshTokenRepositorySpringData.findByUserAndIsTokenValid(user);
    }

    @Override
    public Optional<RefreshToken> findBySessionId(String sessionId) {
        return refreshTokenRepositorySpringData.findBySessionId(sessionId);
    }

    @Override
    public Optional<RefreshToken> findByUserAndSessionIdAndIsTokenValid(User user, String sessionId) {
        return refreshTokenRepositorySpringData.findByUserAndSessionIdAndIsTokenValid(user, sessionId);
    }

    @Override
    public List<RefreshToken> findAllByUserAndIsTokenValid(User user) {
        return refreshTokenRepositorySpringData.findAllByUserAndIsTokenValid(user);
    }
}
