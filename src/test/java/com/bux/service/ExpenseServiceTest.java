package com.bux.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bux.repository.ExpenseRepository;
import com.bux.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bux.model.User;
import com.bux.model.Expense;


public class ExpenseServiceTest {

    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        userRepository = mock(UserRepository.class);
        expenseService = new ExpenseService(expenseRepository, userRepository);
    }

    @Test
    void testGetExpensesByUserId_ReturnsExpenses() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Expense expense = new Expense();
        expense.setAmount(100.0);
        expense.setUser(user);
        when(expenseRepository.findByUser(user)).thenReturn(List.of(expense));

        // Act
        List<Expense> result = expenseService.getExpensesByUserId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(100.0, result.get(0).getAmount());
    }

    @Test
    void testGetExpensesByUserId_UserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseService.getExpensesByUserId(99L);
        });

        assertEquals("User not found", exception.getMessage());
    }
    
    @Test
    void testGetAllExpensesIncludingRecurring() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Expense nonRecurring = new Expense();
        nonRecurring.setAmount(50.0);
        nonRecurring.setUser(user);
        nonRecurring.setIsRecurring(false);
        nonRecurring.setDate(LocalDate.now());

        Expense oldRecurring = new Expense();
        oldRecurring.setAmount(75.0);
        oldRecurring.setUser(user);
        oldRecurring.setIsRecurring(true);
        oldRecurring.setDate(LocalDate.now().minusMonths(1));
        oldRecurring.setDescription("Gym");

        Expense currentRecurring = new Expense();
        currentRecurring.setAmount(90.0);
        currentRecurring.setUser(user);
        currentRecurring.setIsRecurring(true);
        currentRecurring.setDate(LocalDate.now()); // should NOT duplicate

        List<Expense> mockExpenses = new ArrayList<>();
        mockExpenses.add(nonRecurring);
        mockExpenses.add(oldRecurring);
        mockExpenses.add(currentRecurring);

        when(expenseRepository.findAllWithUser()).thenReturn(mockExpenses);

        // Act
        List<Expense> result = expenseService.getAllExpensesIncludingRecurring();

        // Assert
        assertEquals(4, result.size()); // original 3 + 1 new virtual recurring
        assertTrue(result.stream()
        	    .anyMatch(e -> e.getDescription() != null && e.getDescription().contains("(Recurring)")));
    }

}

