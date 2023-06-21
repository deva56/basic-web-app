package com.futurefactorytech.reviewer.api.dtos.administration;

import java.util.List;

public record UserDTO(String username, String email, boolean isActivated, boolean isDisabled, List<String> authorities) {
}
