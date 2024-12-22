package com.amadeusz.ExpensesTracker.Expense;

import java.util.Optional;

import java.util.UUID;

public interface ExpenseDao {
    Expense insertExpense(Expense expense);
    void updateExpense(Expense expense);
    void deleteExpense(Expense expense);
    Optional<Expense> getExpenseById(UUID id);
}
