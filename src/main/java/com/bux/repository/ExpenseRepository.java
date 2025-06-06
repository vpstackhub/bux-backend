package com.bux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bux.model.Expense;
import com.bux.model.User;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expense> findByCategory(String category);
    
    @Query("SELECT e FROM Expense e JOIN FETCH e.user")
    List<Expense> findAllWithUser();
    List<Expense> findByUser(User user);

  }


