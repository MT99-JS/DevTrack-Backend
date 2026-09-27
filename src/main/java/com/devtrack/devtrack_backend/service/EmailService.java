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

    public void sendIssueCreatedEmail(String to, String issueKey, String title) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject("New Issue Created: " + issueKey);
            message.setText(
                    "A new issue has been created.\n\n" +
                            "Issue: " + issueKey + "\n" +
                            "Title: " + title
            );

            System.out.println("Attempting to send email to: " + to);

            mailSender.send(message);

            System.out.println("Email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("FAILED TO SEND EMAIL TO: " + to);
            e.printStackTrace();
        }
    }
}
