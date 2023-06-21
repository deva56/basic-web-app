package com.futurefactorytech.reviewer.api.services.account;

import com.futurefactorytech.reviewer.api.dtos.account.AccountDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;

public interface AccountService {

    BasicOperationResultDTO logoutFromAllSessions();

    BasicOperationResultDTO logoutFromCurrentSession();

    BasicOperationResultDTO logoutFromAllButCurrentSession();

    BasicOperationValueResultDTO<AccountDTO> getUserAccountDetails();

}
