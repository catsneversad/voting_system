package com.example.final_project.model;

import org.springframework.security.core.GrantedAuthority;

public enum AvailableRoles implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
