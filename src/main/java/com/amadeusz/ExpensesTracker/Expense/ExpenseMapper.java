package com.amadeusz.ExpensesTracker.Expense;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ExpenseMapper implements Function<Expense, ExpenseDto> {
    @Override
    public ExpenseDto apply(Expense expense) {
        return new ExpenseDto(
                expense.getName(),
                expense.getCategory().getName(),
                expense.getPrice(),
                expense.getQuantity(),
                expense.getOwner().getId(),
                expense.getDate()
        );
    }
}
