package ru.clevertec.newsManagement.servises.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.newsManagement.model.Roles;

import java.util.Optional;

public abstract class AbstractService {

    protected void checkAuthorization(String username, UserDetails userDetails, Roles roles) {
        if (hasRole(userDetails, roles)) {
            Optional
                    .of(userDetails)
                    .filter((details) -> details.getUsername().equals(username))
                    .filter(details -> hasRole(details, roles))
                    .orElseThrow(() -> new AccessDeniedException("Access is denied"));
        }
    }

    private boolean hasRole(UserDetails userDetails, Roles roles) {
        return userDetails
                .getAuthorities()
                .stream()
                .anyMatch((authority) -> authority.getAuthority().contains(roles.name()));
    }
}
