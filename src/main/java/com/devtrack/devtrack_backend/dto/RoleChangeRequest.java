package com.devtrack.devtrack_backend.dto;

import com.devtrack.devtrack_backend.entity.Role;
import jakarta.validation.constraints.NotNull;

public class RoleChangeRequest {

    @NotNull(message = "Role is required")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}