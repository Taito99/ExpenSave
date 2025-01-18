package com.amadeusz.ExpensesTracker.Expense;

import java.util.Optional;

import java.util.UUID;

public interface ExpenseDao {
    void saveExpanse(Expense expense);
    void deleteExpense(Expense expense);
    Optional<Expense> findExpenseById(UUID id);
}
