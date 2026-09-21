package com.devtrack.devtrack_backend.dto;

import com.devtrack.devtrack_backend.entity.IssuePriority;
import com.devtrack.devtrack_backend.entity.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class IssueRequest {
    @Override
    public String toString() {
        return "IssueRequest{" +
                "projectId=" + projectId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", assigneeId=" + assigneeId +
                ", reporterId=" + reporterId +
                ", labels=" + labels +
                '}';
    }

    @NotNull(message = "Project is required")
    private Long projectId;

    @NotBlank(message = "Issue title is required")
    private String title;

    private String description;

    @NotNull(message = "Issue type is required")
    private IssueType type;

    @NotNull(message = "Priority is required")
    private IssuePriority priority;

    private Long assigneeId;

    @NotNull(message = "Reporter is required")
    private Long reporterId;

    private Set<String> labels;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }
}