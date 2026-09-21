package com.devtrack.devtrack_backend.service;

import com.devtrack.devtrack_backend.dto.AttachmentResponse;
import com.devtrack.devtrack_backend.entity.Issue;
import com.devtrack.devtrack_backend.entity.IssueAttachment;
import com.devtrack.devtrack_backend.exception.IssueNotFoundException;
import com.devtrack.devtrack_backend.repository.IssueAttachmentRepository;
import com.devtrack.devtrack_backend.repository.IssueRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class IssueAttachmentService {

    private final IssueRepository issueRepository;
    private final IssueAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    public IssueAttachmentService(
            IssueRepository issueRepository,
            IssueAttachmentRepository attachmentRepository,
            FileStorageService fileStorageService) {

        this.issueRepository = issueRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    public AttachmentResponse upload(
            Long issueId,
            MultipartFile file) throws IOException {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue not found with id: " + issueId
                        )
                );

        String fileUrl = fileStorageService.store(file);

        IssueAttachment attachment = new IssueAttachment();

        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileUrl(fileUrl);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setIssue(issue);

        IssueAttachment saved =
                attachmentRepository.save(attachment);

        return mapToResponse(saved);
    }

    private AttachmentResponse mapToResponse(
            IssueAttachment attachment) {

        AttachmentResponse response =
                new AttachmentResponse();

        response.setId(attachment.getId());
        response.setFileName(attachment.getFileName());
        response.setFileUrl(attachment.getFileUrl());
        response.setContentType(attachment.getContentType());
        response.setFileSize(attachment.getFileSize());

        return response;
    }

    public List<AttachmentResponse> getAttachments(Long issueId) {

        if (!issueRepository.existsById(issueId)) {
            throw new IssueNotFoundException(
                    "Issue not found with id: " + issueId
            );
        }

        return attachmentRepository.findByIssueId(issueId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void delete(Long attachmentId) throws IOException {

        IssueAttachment attachment =
                attachmentRepository.findById(attachmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attachment not found with id: "
                                                + attachmentId
                                )
                        );

        fileStorageService.delete(
                attachment.getFileUrl()
        );

        attachmentRepository.delete(attachment);
    }

}
