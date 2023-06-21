package com.futurefactorytech.reviewer.persistence_jpa.repository.springdata;

import com.futurefactorytech.reviewer.domain.entities.RefreshToken;
import com.futurefactorytech.reviewer.domain.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepositorySpringData extends CrudRepository<RefreshToken, Long> {

    @Query("SELECT t FROM RefreshToken t WHERE t.isValid = true AND t.user = :user AND t.sessionId = :sessionId")
    Optional<RefreshToken> findByUserAndSessionIdAndIsTokenValid(@Param("user")User user, @Param("sessionId")String sessionId);

    @Query("SELECT t FROM RefreshToken t WHERE t.isValid = true AND t.user = :user")
    Optional<RefreshToken> findByUserAndIsTokenValid(@Param("user") User user);

    Optional<RefreshToken> findBySessionId(String sessionId);

    @Query(value = "SELECT t FROM RefreshToken t WHERE t.isValid = true AND t.user = :user")
    List<RefreshToken> findAllByUserAndIsTokenValid(@Param("user") User user);
}
