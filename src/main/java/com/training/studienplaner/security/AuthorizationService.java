package com.training.studienplaner.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component("authz")
public class AuthorizationService {
    public boolean canAccessAny(Object user, String... roles) {
        if (!(user instanceof UserDetails userDetails)) {
            return false;
        }

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            String granted = authority.getAuthority();

            if ("ROLE_ADMIN".equals(granted)) return true;

            for (String role : roles) {
                if (granted.equals("ROLE_" + role)) return true;
            }
        }

        return false;
    }
}
