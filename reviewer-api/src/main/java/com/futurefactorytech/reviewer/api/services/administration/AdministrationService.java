package com.futurefactorytech.reviewer.api.services.administration;

import com.futurefactorytech.reviewer.api.dtos.administration.UserDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.PaginationDTO;

import java.util.List;

public interface AdministrationService {

    BasicOperationValueResultDTO<PaginationDTO<List<UserDTO>>> getAllUsers(String sortBy, Integer pageSize, Integer page);

}
