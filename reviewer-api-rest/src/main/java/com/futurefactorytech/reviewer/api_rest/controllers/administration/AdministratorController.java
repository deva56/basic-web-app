package com.futurefactorytech.reviewer.api_rest.controllers.administration;

import com.futurefactorytech.reviewer.api.dtos.administration.UserDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.PaginationDTO;
import com.futurefactorytech.reviewer.api.services.administration.AdministrationService;
import jakarta.inject.Inject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administration")
@PreAuthorize("hasRole('ADMIN')")
public class AdministratorController {

    private final AdministrationService administrationService;

    @Inject
    public AdministratorController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<BasicOperationValueResultDTO<PaginationDTO<List<UserDTO>>>> getAllUsers(@RequestParam(required = false) String sortBy,
                                                                                   @RequestParam Integer pageSize,
                                                                                   @RequestParam Integer page) {

        BasicOperationValueResultDTO<PaginationDTO<List<UserDTO>>> resultDTO = administrationService.getAllUsers(sortBy, pageSize, page);
        return ResponseEntity.status(resultDTO.getStatusCode()).body(resultDTO);
    }
}
