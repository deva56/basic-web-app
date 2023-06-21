package com.futurefactorytech.reviewer.api.types.auth;

public enum TokenExpirationTimes {

    ACCESS_TOKEN(120_000, 120),

    REFRESH_TOKEN(300_000, 300),

    REFRESH_TOKEN_REMEMBER_ME(600_000, 600),

    VALIDATION_TOKEN(900_000, 900),

    FORGOT_PASSWORD_TOKEN(900_000, 900);

    private final long expirationTimeMilliseconds;
    private final long expirationTimeSeconds;

    TokenExpirationTimes(long expirationTimeMilliseconds, long expirationTimeSeconds) {

        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
        this.expirationTimeSeconds = expirationTimeSeconds;
    }

    public long getExpirationTimeMilliseconds() {
        return expirationTimeMilliseconds;
    }

    public long getExpirationTimeSeconds() {
        return expirationTimeSeconds;
    }
}
