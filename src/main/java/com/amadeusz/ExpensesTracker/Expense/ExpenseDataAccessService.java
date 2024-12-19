package com.amadeusz.ExpensesTracker.Expense;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ExpenseDataAccessService {
    private final ExpenseRepository expenseRepository;
}
