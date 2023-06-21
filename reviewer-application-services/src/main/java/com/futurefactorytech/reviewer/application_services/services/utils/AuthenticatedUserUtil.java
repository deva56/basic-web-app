package com.futurefactorytech.reviewer.application_services.services.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Map;

public final class AuthenticatedUserUtil {

    private static final String SESSION = "session";

    public static String getLoggedInUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    public static Collection<? extends GrantedAuthority> getLoggedInUserRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public static String getLoggedInUserDetailsSession() {
        Map<String, String> authDetails = (Map<String, String>) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return authDetails.get(SESSION);
    }
}
