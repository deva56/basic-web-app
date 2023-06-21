package com.futurefactorytech.reviewer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, name = "is_valid")
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100, nullable = false, name = "session_id")
    private String sessionId;

    @Column(columnDefinition = "timestamp with time zone")
    private Instant createdAt;

    @Column(columnDefinition = "timestamp with time zone")
    private Instant expiresAt;

    public RefreshToken(User user, String sessionId, Instant expiresAt) {
        this.isValid = true;
        this.user = user;
        this.sessionId = sessionId;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

}
