package com.futurefactorytech.reviewer.api.types.auth;

public enum AuthenticationUserStates {

    AUTHENTICATION_SUCCESSFUL,

    WRONG_CREDENTIALS_USER_NOT_FOUND,

    WRONG_CREDENTIALS_INVALID_PASSWORD,

    ACCOUNT_NOT_ACTIVATED,

    REFRESH_TOKEN_EXPIRED,

    NEW_ACCESS_TOKEN_GENERATED_SUCCESSFULLY,

    LOGOUT_ERROR,

    LOGOUT_SUCCESSFUL,

    ERROR_WHILE_ACTIVATING_USER_TOKEN_NOT_FOUND,

    ERROR_WHILE_ACTIVATING_USER_TOKEN_EXPIRED,

    SUCCESSFULLY_ACTIVATED_USER,

    SUCCESSFULLY_CHANGED_USER_PASSWORD,

    FORGOT_PASSWORD_TOKEN_VALID,

    FORGOT_PASSWORD_TOKEN_EXPIRED,

    FORGOT_PASSWORD_TOKEN_NOT_FOUND,

    UNKNOWN_ERROR_OCCURRED
}
