package com.futurefactorytech.reviewer.api_rest.controllers.authentication;

import com.futurefactorytech.reviewer.api.dtos.auth.ForgotPasswordRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.auth.LoginRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.auth.RegisterRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicValueDTO;
import com.futurefactorytech.reviewer.api.services.auth.UserAuthenticationService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @Inject
    public AuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<BasicOperationResultDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        BasicOperationResultDTO result = userAuthenticationService.registerUser(registerRequestDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<BasicOperationValueResultDTO<String>> authenticate(@RequestBody LoginRequestDTO loginRequestDTO) {
        BasicOperationValueResultDTO<String> result = userAuthenticationService.authenticateUser(loginRequestDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BasicOperationValueResultDTO<String>> refreshToken(HttpServletRequest request) {
        BasicOperationValueResultDTO<String> refreshTokenResult = userAuthenticationService.refreshToken(request);
        return ResponseEntity.status(refreshTokenResult.getStatusCode()).body(refreshTokenResult);
    }

    @PostMapping("/send-validation-email")
    public ResponseEntity<BasicOperationResultDTO> sendValidationEmail(@RequestBody BasicValueDTO<String> email) {
        BasicOperationResultDTO result = userAuthenticationService.sendValidationEmail(email.value());
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/verify-validation-code")
    public ResponseEntity<BasicOperationResultDTO> verifyValidationCode(@RequestBody BasicValueDTO<String> validationCode) {
        BasicOperationResultDTO result = userAuthenticationService.verifyValidationCode(validationCode.value());
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/send-forgot-password-email")
    public ResponseEntity<BasicOperationResultDTO> sendForgotPasswordEmail(@RequestBody BasicValueDTO<String> email) {
        BasicOperationResultDTO result = userAuthenticationService.sendForgotPasswordEmail(email.value());
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/verify-forgot-password-code")
    public ResponseEntity<BasicOperationResultDTO> verifyForgotPasswordCode(@RequestBody BasicValueDTO<String> forgotPasswordCode) {
        BasicOperationResultDTO result = userAuthenticationService.verifyForgotPasswordCode(forgotPasswordCode.value());
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<BasicOperationResultDTO> setNewPassword(@RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        BasicOperationResultDTO result = userAuthenticationService.setNewPassword(forgotPasswordRequestDTO.token(),
                forgotPasswordRequestDTO.newPassword());
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
