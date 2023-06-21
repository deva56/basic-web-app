package com.futurefactorytech.reviewer.api.dtos.auth;

public record ForgotPasswordRequestDTO(String token, String newPassword) {
}
