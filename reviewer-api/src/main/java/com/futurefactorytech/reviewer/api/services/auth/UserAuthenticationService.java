package com.futurefactorytech.reviewer.api.services.auth;

import com.futurefactorytech.reviewer.api.dtos.auth.LoginRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.auth.RegisterRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserAuthenticationService {

    BasicOperationResultDTO registerUser(RegisterRequestDTO registerRequestDTO);

    BasicOperationValueResultDTO<String> authenticateUser(LoginRequestDTO loginRequestDTO);

    BasicOperationValueResultDTO<String> refreshToken(HttpServletRequest request);

    BasicOperationResultDTO sendValidationEmail(String email);

    BasicOperationResultDTO verifyValidationCode(String token);

    BasicOperationResultDTO sendForgotPasswordEmail(String email);

    BasicOperationResultDTO verifyForgotPasswordCode(String token);

    BasicOperationResultDTO setNewPassword(String token, String newPassword);
}
