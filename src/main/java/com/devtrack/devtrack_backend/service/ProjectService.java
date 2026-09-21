package com.devtrack.devtrack_backend.service;

import com.devtrack.devtrack_backend.dto.ProjectRequest;
import com.devtrack.devtrack_backend.entity.Project;
import com.devtrack.devtrack_backend.exception.DuplicateProjectKeyException;
import com.devtrack.devtrack_backend.exception.ProjectNotFoundException;
import com.devtrack.devtrack_backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {

        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + id
                        )
                );
    }

    public Project createProject(ProjectRequest request) {

        String projectKey = request.getProjectKey().toUpperCase();

        if (projectRepository.existsByProjectKey(projectKey)) {
            throw new DuplicateProjectKeyException(
                    "Project key already exists: " + projectKey
            );
        }

        Project project = new Project();

        project.setProjectKey(projectKey);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setImage(request.getImage());

        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {

        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(
                    "Project not found with id: " + id
            );
        }

        projectRepository.deleteById(id);
    }

    public Project updateProject(Long id, ProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + id
                        )
                );

        String projectKey = request.getProjectKey().toUpperCase();

        if (!project.getProjectKey().equals(projectKey)
                && projectRepository.existsByProjectKey(projectKey)) {

            throw new DuplicateProjectKeyException(
                    "Project key already exists: " + projectKey
            );
        }

        project.setProjectKey(projectKey);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setImage(request.getImage());

        return projectRepository.save(project);
    }

}
