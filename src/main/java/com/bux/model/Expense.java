package com.bux.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String category;
    private String description;
    private LocalDate date;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "recurring_frequency")
    private String recurringFrequency;

    @Column(name = "is_refund", nullable = false)
    private Boolean isRefund = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @Transient
    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
    
    @JsonProperty("username")
    public String getUsername() {
      return user != null ? user.getUsername() : null;
    }

    public Expense() {
    }

    public Expense(Double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }
    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }
    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    public Boolean getIsRefund() {
        return isRefund;
    }
    public void setIsRefund(Boolean isRefund) {
        this.isRefund = isRefund;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }  

    @Override
    public String toString() {
        return "Expense [id=" + id
            + ", amount=" + amount
            + ", category=" + category
            + ", description=" + description
            + ", date=" + date
            + ", isRecurring=" + isRecurring
            + ", recurringFrequency=" + recurringFrequency
            + ", isRefund=" + isRefund
            + ", user=" + user + "]";
    }
}


