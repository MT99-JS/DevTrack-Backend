package com.devtrack.devtrack_backend.repository;

import com.devtrack.devtrack_backend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    Optional<Issue> findByIssueKey(String issueKey);

    boolean existsByIssueKey(String issueKey);

    long countByProjectId(Long projectId);

    Optional<Issue> findTopByProjectIdOrderByIdDesc(Long projectId);
}
