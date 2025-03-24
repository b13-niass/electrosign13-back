package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityContextTokenProvider implements TokenProvider {
    private final JwtDecoder jwtDecoder;
    @Override
    public String getToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return "Bearer "+jwtAuth.getToken().getTokenValue();
        }

        throw new IllegalStateException("No JWT token found in security context");
    }

    public String getEmailFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String tokenValue = jwtAuth.getToken().getTokenValue();
            Jwt jwt = jwtDecoder.decode(tokenValue);
            return jwt.getClaim("email");
        }

        throw new IllegalStateException("No JWT token found in security context");
    }
}
