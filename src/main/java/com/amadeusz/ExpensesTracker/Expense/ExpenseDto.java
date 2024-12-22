package com.amadeusz.ExpensesTracker.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDto(
        String name,
        String categoryName,
        BigDecimal price,
        Integer quantity,
        LocalDate date
) {
}

