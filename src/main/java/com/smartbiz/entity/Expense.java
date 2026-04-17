package com.smartbiz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String category;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDateTime expenseDate;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        if (this.expenseDate == null) {
            this.expenseDate = LocalDateTime.now();
        }
    }
}