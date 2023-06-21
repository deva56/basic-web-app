package com.futurefactorytech.reviewer.application_services.services.jwt;

import com.futurefactorytech.reviewer.api.services.jwt.JwtService;
import com.futurefactorytech.reviewer.api.types.jwt.JwtOperationResult;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Named
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_API = "auth";
    private static final String SESSION = "session";
    private static final String SUBJECT = "sub";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Inject
    public JwtAuthenticationFilter(JwtServiceImpl jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (request.getRequestURI().contains(AUTH_API) || authHeader == null || !authHeader.startsWith(BEARER_TOKEN)) {
            filterChain.doFilter(request, response);
        } else {
            String accessToken = authHeader.substring(7);
            JwtOperationResult<Map<String, Object>, Exception> jwtOperationResult = jwtService.extractAllClaimsMap(accessToken);
            if (jwtOperationResult.operationResult() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtOperationResult.operationResult().get(SUBJECT).toString());
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );
                    Map<String, String> authDetails = new HashMap<>();
                    authDetails.put(SESSION, jwtOperationResult.operationResult().get(SESSION).toString());
                    authToken.setDetails(authDetails);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
