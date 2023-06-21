package com.futurefactorytech.reviewer.application_services.services.administration;

import com.futurefactorytech.reviewer.api.dtos.administration.UserDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.BasicOperationValueResultDTO;
import com.futurefactorytech.reviewer.api.dtos.shared.PaginationDTO;
import com.futurefactorytech.reviewer.api.services.administration.AdministrationService;
import com.futurefactorytech.reviewer.api.types.shared.OperationStates;
import com.futurefactorytech.reviewer.domain.entities.User;
import com.futurefactorytech.reviewer.domain.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Named
public class AdministrationServiceImpl implements AdministrationService {

    private final UserRepository userRepository;

    @Inject
    public AdministrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BasicOperationValueResultDTO<PaginationDTO<List<UserDTO>>> getAllUsers(String sortBy, Integer pageSize, Integer page) {
        try {
            Sort sortByValue = sortBy == null ? Sort.unsorted() : Sort.by(sortBy);
            Pageable pageRequest = PageRequest.of(page,
                    pageSize, sortByValue);
            Page<User> users = userRepository.findAll(pageRequest);
            List<UserDTO> userDTOS = users.get().map(user -> new UserDTO(user.getUsername(), user.getEmail(), user.isActivated(),
                    user.isDisabled(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))).collect(Collectors.toList());
            PaginationDTO<List<UserDTO>> paginationDTO = new PaginationDTO<>(users.getTotalPages(), users.getTotalElements(), users.getNumber(), userDTOS);
            return new BasicOperationValueResultDTO<>(HttpStatus.OK.value(), OperationStates.OPERATION_SUCCESSFUL.name(),
                    paginationDTO);
        } catch (Exception e) {
            return new BasicOperationValueResultDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), OperationStates.OPERATION_FAILED.name(),
                    null);
        }
    }
}
