package com.devtrack.devtrack_backend.dto;

import com.devtrack.devtrack_backend.entity.IssueStatus;
import jakarta.validation.constraints.NotNull;

public class IssueStatusRequest {

    @NotNull(message = "Status is required")
    private IssueStatus status;

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}
