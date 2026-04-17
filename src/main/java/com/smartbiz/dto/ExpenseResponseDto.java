package com.smartbiz.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ExpenseResponseDto {
    private Long id;
    private String title;
    private String category;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private String notes;
    private Long businessId;
    private Long userId;
}