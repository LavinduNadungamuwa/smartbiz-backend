package com.smartbiz.service.impl;

import com.smartbiz.dto.ExpenseRequestDto;
import com.smartbiz.dto.ExpenseResponseDto;
import com.smartbiz.entity.Business;
import com.smartbiz.entity.Expense;
import com.smartbiz.entity.User;
import com.smartbiz.repository.BusinessRepository;
import com.smartbiz.repository.ExpenseRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              BusinessRepository businessRepository,
                              UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ExpenseResponseDto saveExpense(ExpenseRequestDto request) {
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .notes(request.getNotes())
                .business(business)
                .user(user)
                .build();

        Expense saved = expenseRepository.save(expense);
        return mapToDto(saved);
    }

    @Override
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ExpenseResponseDto getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        return expense == null ? null : mapToDto(expense);
    }

    @Override
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto request) {
        Expense existing = expenseRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setTitle(request.getTitle());
        existing.setCategory(request.getCategory());
        existing.setAmount(request.getAmount());
        existing.setExpenseDate(request.getExpenseDate());
        existing.setNotes(request.getNotes());
        existing.setBusiness(business);
        existing.setUser(user);

        Expense updated = expenseRepository.save(existing);
        return mapToDto(updated);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    private ExpenseResponseDto mapToDto(Expense expense) {
        return ExpenseResponseDto.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .notes(expense.getNotes())
                .businessId(expense.getBusiness().getId())
                .userId(expense.getUser().getId())
                .build();
    }
}