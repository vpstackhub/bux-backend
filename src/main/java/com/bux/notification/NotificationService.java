package com.bux.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendBudgetExceededEmail(String toEmail, double totalSpent, double budget) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("🚨 Budget Exceeded Alert!");
            message.setText("You have spent $" + totalSpent + " which exceeds your budget of $" + budget + ".");
            mailSender.send(message);
            System.out.println("✅ Email successfully sent to " + toEmail);
            return true; 
        } catch (Exception e) {
            System.err.println("❌ Failed to send email. Reason: " + e.getMessage());
            return false; 
        }
    }
}


