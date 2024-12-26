package com.amadeusz.ExpensesTracker.Expense;

import java.util.Optional;

import java.util.UUID;

public interface ExpenseDao {
    void insertExpense(Expense expense);
    void updateExpense(Expense expense);
    void deleteExpense(Expense expense);
    Optional<Expense> findExpenseById(UUID id);
}
