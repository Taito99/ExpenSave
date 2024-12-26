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
    public void insertExpense(Expense expense) {
        expenseRepository.save(expense);
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
    public Optional<Expense> findExpenseById(UUID id) {
        return expenseRepository.findById(id);
    }
}
