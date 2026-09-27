package com.devtrack.devtrack_backend.dto.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class IssueEventProducer {

    private final KafkaTemplate<String, IssueCreatedEvent> kafkaTemplate;

    public IssueEventProducer(
            KafkaTemplate<String, IssueCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishIssueCreated(IssueCreatedEvent event) {

        kafkaTemplate.send(
                "issue-created",
                event.getIssueKey(),
                event
        );
    }
}
