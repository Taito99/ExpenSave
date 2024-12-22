package com.amadeusz.ExpensesTracker.Expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    void findExpenseById(UUID id);
}
