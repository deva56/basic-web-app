package com.futurefactorytech.reviewer.api_rest.controllers.account;

import com.futurefactorytech.reviewer.api.dtos.account.AccountDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.services.account.AccountService;
import jakarta.inject.Inject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/logout-from-all-sessions")
    public ResponseEntity<BasicOperationResultDTO> logoutFromAllSessions() {
        BasicOperationResultDTO logoutResult = accountService.logoutFromAllSessions();
        return ResponseEntity.status(logoutResult.getStatusCode()).body(logoutResult);
    }

    @PostMapping("/logout-from-current-session")
    public ResponseEntity<BasicOperationResultDTO> logoutFromCurrentSession() {
        BasicOperationResultDTO logoutResult = accountService.logoutFromCurrentSession();
        return ResponseEntity.status(logoutResult.getStatusCode()).body(logoutResult);
    }

    @PostMapping("/logout-from-all-but-current-session")
    public ResponseEntity<BasicOperationResultDTO> logoutFromAllButCurrentSession() {
        BasicOperationResultDTO logoutResult = accountService.logoutFromAllButCurrentSession();
        return ResponseEntity.status(logoutResult.getStatusCode()).body(logoutResult);
    }

    @GetMapping("/user-account-details")
    public ResponseEntity<BasicOperationValueResultDTO<AccountDTO>> getUserAccountDetails() {
        BasicOperationValueResultDTO<AccountDTO> result = accountService.getUserAccountDetails();
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
