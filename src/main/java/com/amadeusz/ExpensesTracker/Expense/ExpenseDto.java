package com.amadeusz.ExpensesTracker.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDto(String name, LocalDate date, BigDecimal price, Integer quantity) {
}
