package com.bux.service;

import com.bux.model.Expense;
import com.bux.model.User;
import com.bux.repository.ExpenseRepository;
import com.bux.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Expense> getAllExpensesIncludingRecurring() {
        List<Expense> all = expenseRepository.findAllWithUser();

        List<Expense> virtualRecurring = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (Expense e : all) {
            if (Boolean.TRUE.equals(e.getIsRecurring())) {
                LocalDate original = e.getDate();
                boolean isThisMonth = original.getMonthValue() == now.getMonthValue()
                                      && original.getYear() == now.getYear();
                if (!isThisMonth) {
                    Expense recurring = new Expense();
                    recurring.setAmount(e.getAmount());
                    recurring.setCategory(e.getCategory());
                    recurring.setDescription(e.getDescription() + " (Recurring)");
                    recurring.setDate(LocalDate.of(now.getYear(), now.getMonthValue(), 1));
                    recurring.setIsRefund(false);
                    recurring.setIsRecurring(true);
                    recurring.setUser(e.getUser());
                    virtualRecurring.add(recurring);
                }
            }
        }
        all.addAll(virtualRecurring);
        return all;
    }

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> getAll() {
        return expenseRepository.findAllWithUser();
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user);
    }
}


