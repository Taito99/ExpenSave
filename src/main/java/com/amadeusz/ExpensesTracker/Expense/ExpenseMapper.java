package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.exeptions.MapperNullException;
import com.amadeusz.ExpensesTracker.exeptions.InvalidExpenseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.function.Function;

@Service
public class ExpenseMapper implements Function<Expense, ExpenseDto> {

    @Override
    public ExpenseDto apply(Expense expense) {
        if (expense == null) {
            throw new MapperNullException("Expense in ExpenseMapper cannot be null");
        }

        validateExpense(expense);

        return new ExpenseDto(
                expense.getName(),
                expense.getCategory().getName(),
                expense.getPrice(),
                expense.getQuantity(),
                expense.getOwner().getId(),
                expense.getDate()
        );
    }

    private void validateExpense(Expense expense) {
    if (expense.getPrice() == null || expense.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidExpenseException("Price must be greater than zero");
    }
    if (expense.getQuantity() == null || expense.getQuantity() <= 0) {
        throw new InvalidExpenseException("Quantity must be greater than zero");
    }
    if (expense.getCategory() == null || expense.getCategory().getName() == null) {
        throw new InvalidExpenseException("Category cannot be null");
    }
    if (expense.getOwner() == null || expense.getOwner().getId() == null) {
        throw new InvalidExpenseException("Owner cannot be null");
    }

    if (expense.getDate() == null) {
        throw new InvalidExpenseException("Date cannot be null");
    }
}


}
