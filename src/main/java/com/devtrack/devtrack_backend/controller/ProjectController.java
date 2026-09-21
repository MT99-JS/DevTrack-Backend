package com.devtrack.devtrack_backend.controller;

import com.devtrack.devtrack_backend.dto.ProjectRequest;
import com.devtrack.devtrack_backend.entity.Project;
import com.devtrack.devtrack_backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN', 'PROJECT_MANAGER')"
    )
    @PostMapping
    public Project createProject(
            @Valid @RequestBody ProjectRequest request) {

        return projectService.createProject(request);
    }

    @PutMapping("/{id}")
    public Project updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request
    ) {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id
    ) {
        projectService.deleteProject(id);

        return ResponseEntity.noContent().build();
    }

}
