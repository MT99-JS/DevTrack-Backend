package com.devtrack.devtrack_backend.controller;

import com.devtrack.devtrack_backend.dto.AttachmentResponse;
import com.devtrack.devtrack_backend.service.IssueAttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueAttachmentController {

    private final IssueAttachmentService attachmentService;

    public IssueAttachmentController(
            IssueAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/{issueId}/attachments")
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long issueId,
            @RequestParam("file") MultipartFile file)
            throws IOException {

        AttachmentResponse response =
                attachmentService.upload(issueId, file);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{issueId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> getAttachments(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                attachmentService.getAttachments(issueId)
        );
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long attachmentId)
            throws IOException {

        attachmentService.delete(attachmentId);

        return ResponseEntity.noContent().build();
    }
}
