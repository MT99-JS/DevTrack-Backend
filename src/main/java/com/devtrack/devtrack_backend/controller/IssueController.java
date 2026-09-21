package com.devtrack.devtrack_backend.controller;

import com.devtrack.devtrack_backend.dto.*;
import com.devtrack.devtrack_backend.entity.Issue;
import com.devtrack.devtrack_backend.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<IssueResponse> getIssues(
            @RequestParam(required = false) Long projectId
    ) {
        if (projectId != null) {
            return issueService.getIssuesByProject(projectId);
        }

        return issueService.getAllIssues();
    }

    @GetMapping("/{id}")
    public IssueResponse getIssue(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('QA')")
    public IssueResponse createIssue(
             @Valid @RequestBody IssueRequest request
    ) {
        System.out.println(request.toString());
        return issueService.createIssue(request);
    }

    @PutMapping("/{id}")
    public IssueResponse updateIssue(
            @PathVariable Long id,
            @Valid @RequestBody IssueRequest request) {

        return issueService.updateIssue(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(
            @PathVariable Long id) {

        issueService.deleteIssue(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public IssueResponse updateIssueStatus(
            @PathVariable Long id,
            @Valid @RequestBody IssueStatusRequest request) {

        return issueService.updateIssueStatus(
                id,
                request.getStatus()
        );
    }

}
