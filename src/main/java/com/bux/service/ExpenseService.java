package com.bux.service;

import com.bux.model.Expense;
import com.bux.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
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
                    // Set date to first day of current month
                    recurring.setDate(LocalDate.of(now.getYear(), now.getMonthValue(), 1));
                    recurring.setIsRefund(false);
                    recurring.setIsRecurring(true);
                    // Carry over the user reference
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
}
