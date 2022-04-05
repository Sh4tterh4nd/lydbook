package com.lydbook.audiobook.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * User role.
 */
public enum UserRole implements GrantedAuthority {

    /**
     * Admin user role.
     */
    ADMIN,
    /**
     * User user role.
     */
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
