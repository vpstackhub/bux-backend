package com.bux.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.bux.dto.ExpenseRequest;
import com.bux.model.Expense;
import com.bux.model.User;
import com.bux.repository.ExpenseRepository;
import com.bux.service.ExpenseService;
import com.bux.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ExpenseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping("/expenses")
    public ResponseEntity<Map<String, Object>> addExpense(@RequestBody ExpenseRequest req) {
        User u = userService.findById(req.userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Expense e = new Expense();
        e.setAmount(req.amount);
        e.setCategory(req.category);
        e.setDescription(req.description);
        e.setDate(req.date);
        e.setIsRefund(req.isRefund != null && req.isRefund);
        e.setIsRecurring(req.isRecurring);
        e.setRecurringFrequency(req.recurringFrequency);
        e.setUser(u);  

        Expense saved = expenseService.save(e);

        Map<String,Object> resp = new HashMap<>();
        resp.put("expense", saved);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpensesIncludingRecurring();
    }

    @GetMapping("/expenses/category/{category}")
    public List<Expense> getExpensesByCategory(@PathVariable String category) {
        return expenseRepository.findByCategory(category);
    }

    @GetMapping("/expenses/user/{userId}")
    public List<Expense> getExpensesByUserId(@PathVariable Long userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user);
    }

    @PutMapping("/expenses/update/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense updatedExpense) {
        return expenseRepository.findById(id)
                .map(expense -> {
                    expense.setAmount(updatedExpense.getAmount());
                    expense.setCategory(updatedExpense.getCategory());
                    expense.setDescription(updatedExpense.getDescription());
                    expense.setDate(updatedExpense.getDate());
                    expense.setIsRefund(updatedExpense.getIsRefund());
                    return expenseRepository.save(expense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found with id " + id));
    }

    @PutMapping("/expenses/refund/{id}")
    public Expense markAsRefund(@PathVariable Long id) {
        return expenseRepository.findById(id)
                .map(expense -> {
                    expense.setIsRefund(true);
                    return expenseRepository.save(expense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found with id " + id));
    }

    @DeleteMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return "Expense deleted successfully!";
        } else {
            throw new RuntimeException("Expense not found with id " + id);
        }
    }

    @DeleteMapping("/expenses/reset")
    public String resetExpenses() {
        expenseRepository.deleteAll();
        return "All expenses have been reset successfully!";
    }
}




