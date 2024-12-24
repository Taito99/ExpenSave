package com.amadeusz.ExpensesTracker.Expense;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseDto(
        String name,
        String categoryName,
        BigDecimal price,
        Integer quantity,
        @JsonIgnore UUID ownerId,
        LocalDate date
) {
}

