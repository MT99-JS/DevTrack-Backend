package com.devtrack.devtrack_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendIssueAssignedEmail(
            String email,
            String issueKey,
            String title) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(
                "You have been assigned " + issueKey
        );

        message.setText(
                "Hello,\n\n" +
                        "You have been assigned a new issue.\n\n" +
                        "Issue: " + issueKey + "\n" +
                        "Title: " + title + "\n\n" +
                        "Please log in to DevTrack to view the issue."
        );

        mailSender.send(message);
    }
}