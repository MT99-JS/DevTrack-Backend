package com.devtrack.devtrack_backend.dto.kafka;

import com.devtrack.devtrack_backend.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IssueCreatedConsumer {

    private final EmailService emailService;

    public IssueCreatedConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "issue-created",
            groupId = "notification-service"
    )
    public void consume(IssueCreatedEvent event) {

        System.out.println("Received issue: " + event.getIssueKey());

        System.out.println("Title: " + event.getTitle());

        System.out.println("Assignee: " + event.getAssigneeEmail());

        emailService.sendIssueAssignedEmail(
                event.getAssigneeEmail(),
                event.getIssueKey(),
                event.getTitle()
        );
    }
}
