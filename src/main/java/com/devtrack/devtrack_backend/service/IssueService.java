package com.devtrack.devtrack_backend.service;

import com.devtrack.devtrack_backend.dto.IssueRequest;
import com.devtrack.devtrack_backend.dto.IssueResponse;
import com.devtrack.devtrack_backend.entity.*;
import com.devtrack.devtrack_backend.exception.IssueNotFoundException;
import com.devtrack.devtrack_backend.exception.ProjectNotFoundException;
import com.devtrack.devtrack_backend.exception.UserNotFoundException;
import com.devtrack.devtrack_backend.repository.IssueRepository;
import com.devtrack.devtrack_backend.repository.LabelRepository;
import com.devtrack.devtrack_backend.repository.ProjectRepository;
import com.devtrack.devtrack_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public IssueService(
            IssueRepository issueRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository, LabelRepository labelRepository
    ) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    public List<IssueResponse> getAllIssues() {
        return issueRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public IssueResponse  getIssueById(Long id) {

        Issue issue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue not found with id: " + id
                        )
                );

        return mapToResponse(issue);
    }

    public List<IssueResponse> getIssuesByProject(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
              new ProjectNotFoundException(
                    "Project not found with id: " + projectId
            );
        }

        return issueRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public IssueResponse  createIssue(IssueRequest request) {

        var project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: "
                                        + request.getProjectId()
                        )
                );

        var reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Reporter not found with id: "
                                        + request.getReporterId()
                        )
                );

        var assignee = request.getAssigneeId() != null
                ? userRepository.findById(request.getAssigneeId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Assignee not found with id: "
                                        + request.getAssigneeId()
                        )
                )
                : null;

        Issue issue = new Issue();

        issue.setIssueKey(generateIssueKey(project.getProjectKey(),project.getId()));
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setType(request.getType());
        issue.setStatus(IssueStatus.TODO);
        issue.setPriority(request.getPriority());
        issue.setProject(project);
        issue.setReporter(reporter);
        issue.setAssignee(assignee);
        issue.setLabels(resolveLabels(request.getLabels()));

        Issue savedIssue = issueRepository.save(issue);

        return mapToResponse(savedIssue);
    }

    private String generateIssueKey(String projectKey, Long projectId) {

        Optional<Issue> latestIssue =
                issueRepository.findTopByProjectIdOrderByIdDesc(projectId);

        if (latestIssue.isEmpty()) {
            return projectKey + "-101";
        }

        String latestKey = latestIssue.get().getIssueKey();

        int lastNumber = Integer.parseInt(
                latestKey.substring(latestKey.lastIndexOf("-") + 1)
        );

        return projectKey + "-" + (lastNumber + 1);
    }

    private IssueResponse mapToResponse(Issue issue) {

        IssueResponse response = new IssueResponse();

        response.setId(issue.getId());
        response.setIssueKey(issue.getIssueKey());
        response.setTitle(issue.getTitle());
        response.setDescription(issue.getDescription());
        response.setType(issue.getType());
        response.setStatus(issue.getStatus());
        response.setPriority(issue.getPriority());

        response.setProjectId(issue.getProject().getId());
        response.setProjectKey(issue.getProject().getProjectKey());

        if (issue.getAssignee() != null) {
            response.setAssigneeId(issue.getAssignee().getId());
            response.setAssigneeName(issue.getAssignee().getName());
            response.setAssigneeAvatar(issue.getAssignee().getAvatar());
        }

        response.setReporterId(issue.getReporter().getId());
        response.setReporterName(issue.getReporter().getName());
        response.setReporterAvatar(issue.getReporter().getAvatar());
        response.setLabels(
                issue.getLabels()
                        .stream()
                        .map(Label::getName)
                        .collect(Collectors.toSet()));


        return response;
    }

    public IssueResponse updateIssue(Long issueId, IssueRequest request) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IssueNotFoundException("Issue not found with id: " + issueId)
                );

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + request.getProjectId()
                        )
                );

        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Reporter not found with id: " + request.getReporterId()
                        )
                );

        User assignee = null;

        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() ->
                            new UserNotFoundException(
                                    "Assignee not found with id: " + request.getAssigneeId()
                            )
                    );
        }

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setType(request.getType());
        issue.setPriority(request.getPriority());
        issue.setProject(project);
        issue.setAssignee(assignee);
        issue.setReporter(reporter);

        Issue updatedIssue = issueRepository.save(issue);

        return mapToResponse(updatedIssue);
    }

    public void deleteIssue(Long id) {

        if (!issueRepository.existsById(id)) {
            throw new IssueNotFoundException(
                    "Issue not found with id: " + id
            );
        }

        issueRepository.deleteById(id);
    }
    public IssueResponse updateIssueStatus(
            Long issueId,
            IssueStatus status) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue not found with id: " + issueId
                        )
                );

        issue.setStatus(status);

        Issue updatedIssue = issueRepository.save(issue);

        return mapToResponse(updatedIssue);
    }

    private Set<Label> resolveLabels(Set<String> labelNames) {

        Set<Label> labels = new HashSet<>();

        if (labelNames == null) {
            return labels;
        }

        for (String labelName : labelNames) {

            String normalizedName = labelName.trim().toLowerCase();

            if (normalizedName.isEmpty()) {
                continue;
            }

            Label label = labelRepository
                    .findByNameIgnoreCase(normalizedName)
                    .orElseGet(() -> {
                        Label newLabel = new Label();
                        newLabel.setName(normalizedName);
                        return labelRepository.save(newLabel);
                    });

            labels.add(label);
        }

        return labels;
    }

}
