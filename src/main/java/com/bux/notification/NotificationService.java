package com.bux.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public boolean sendBudgetExceededEmail(String to, double total, double limit) {
        System.out.println("📢 Budget exceeded alert!");
        System.out.println("To: " + to);
        System.out.println("Total spending: $" + total + " (limit: $" + limit + ")");
        // Just simulating a successful email send for now
        return true;
    }
}

