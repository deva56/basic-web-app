package com.futurefactorytech.reviewer.api.dtos.auth;

public record LoginRequestDTO(String usernameEmail, String password, boolean rememberMe) {

}
