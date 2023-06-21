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
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100, name = "token_value")
    private String tokenValue;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(columnDefinition = "timestamp with time zone")
    private Instant createdAt;

    @Column(columnDefinition = "timestamp with time zone")
    private Instant expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ForgotPasswordToken(String tokenValue, Instant createdAt, Instant expiresAt, User user) {
        this.tokenValue = tokenValue;
        this.isUsed = false;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public boolean isTokenValid() {
        return Instant.now().isBefore(this.getExpiresAt()) && !this.isUsed();
    }
}
