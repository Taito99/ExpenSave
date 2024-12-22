package com.amadeusz.ExpensesTracker.Expense;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ExpenseDataAccessService implements ExpenseDao {
    private final ExpenseRepository expenseRepository;

    @Override
    public Expense insertExpense(Expense expense) {
        expenseRepository.save(expense);
        return expense;
    }

    @Override
    public void updateExpense(Expense expense) {
        expenseRepository.save(expense);

    }

    @Override
    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);

    }

    @Override
    public Optional<Expense> getExpenseById(UUID id) {
        return Optional.empty();
    }
}
