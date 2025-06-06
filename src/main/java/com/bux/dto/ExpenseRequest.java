package com.bux.dto;

import java.time.LocalDate;

public class ExpenseRequest {
  public Double amount;
  public String category;
  public String description;
  public LocalDate date;
  public Boolean isRefund;
  public Boolean isRecurring;
  public String recurringFrequency;
  public Long userId;            
}
