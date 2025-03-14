package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.TokenProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextTokenProvider implements TokenProvider {
    @Override
    public String getToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return "Bearer "+jwtAuth.getToken().getTokenValue();
        }

        throw new IllegalStateException("No JWT token found in security context");
    }
}
