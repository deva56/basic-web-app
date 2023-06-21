package com.futurefactorytech.reviewer.api.dtos.account;

import java.util.List;

public record AccountDTO(String username, String email, List<String> authorities) {
}
