package com.smartbiz.controller;

import com.smartbiz.dto.ExpenseRequestDto;
import com.smartbiz.dto.ExpenseResponseDto;
import com.smartbiz.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponseDto createExpense(@RequestBody ExpenseRequestDto request) {
        return expenseService.saveExpense(request);
    }

    @GetMapping
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public ExpenseResponseDto getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(@PathVariable Long id,
                                            @RequestBody ExpenseRequestDto request) {
        return expenseService.updateExpense(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}