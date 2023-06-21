package com.futurefactorytech.reviewer.api_rest.controllers.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futurefactorytech.reviewer.api.dtos.auth.ForgotPasswordRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.auth.RegisterRequestDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.services.auth.UserAuthenticationService;
import com.futurefactorytech.reviewer.api.types.auth.AuthenticationUserStates;
import com.futurefactorytech.reviewer.api.types.auth.RegisterUserStates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthenticationController.class},
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test registration case when no user is found with the existing username or email")
    void testRegisterWhenNoUserIsFoundWithExistingUsernameAndEmail() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user1", "user1@mail.com",
                "password");
        when(userAuthenticationService.registerUser(registerRequestDTO)).thenReturn(new BasicOperationResultDTO(
                HttpStatus.OK.value(), RegisterUserStates.USER_REGISTERED_SUCCESSFULLY.name()));
        ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)));
        response.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("Test setting of new user password after password reset")
    void testSetNewPassword() throws Exception {
        ForgotPasswordRequestDTO forgotPasswordRequestDTO = new ForgotPasswordRequestDTO(
                "token", "newPassword");
        when(userAuthenticationService.setNewPassword(
                forgotPasswordRequestDTO.token(), forgotPasswordRequestDTO.newPassword())).thenReturn(
                new BasicOperationResultDTO(HttpStatus.OK.value(),
                        AuthenticationUserStates.SUCCESSFULLY_CHANGED_USER_PASSWORD.name())
        );
        ResultActions response = mockMvc.perform(post("/api/v1/auth/set-new-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequestDTO)));
        response.andExpect(status().isOk()).andDo(print());
    }
}
