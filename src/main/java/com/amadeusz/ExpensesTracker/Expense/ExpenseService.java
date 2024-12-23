package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.category.Category;
import com.amadeusz.ExpensesTracker.category.CategoryRepository;
import com.amadeusz.ExpensesTracker.exeptions.ResourceNotFoundException;
import com.amadeusz.ExpensesTracker.user.User;
import com.amadeusz.ExpensesTracker.user.UserContextService;
import com.amadeusz.ExpensesTracker.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {
    private final ExpenseDao expenseDao;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public void createExpense(ExpenseDto expenseDto) {
        log.info("Starting to create expense: {}", expenseDto);

        String username = userContextService.getAuthenticatedUsername();
        log.info("Authenticated username: {}", username);

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: %s has not been found".formatted(username)));
        log.info("User found: {}", user.getUsername());

        Category category = categoryRepository.findCategoryByName(expenseDto.categoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category with name: %s has not been found".formatted(expenseDto.categoryName())));
        log.info("Category found: {}", category.getName());


        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .date(expenseDto.date())
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .category(category)
                .owner(user)
                .build();

        expenseRepository.save(expense);
        log.info("Expense saved to database: {}", expense);

        BigDecimal currentBudget = user.getAvailableBudget().subtract(expenseDto.price());
        user.setAvailableBudget(currentBudget);
        log.info("Updated user's available budget: {}", currentBudget);

        user.getExpenses().add(expense);
        userRepository.save(user);
        log.info("User updated with new expense.");
    }

    public Expense getExpenseDetails(UUID expenseId) {
        log.info("Fetching details for expense ID: {}", expenseId);

        return expenseDao.getExpenseById(expenseId).orElseThrow(
                () -> new ResourceNotFoundException("Expense with id: %s has not been found".formatted(expenseId)));
    }

    @Transactional
    public void deleteExpense(UUID expenseId) {
        log.info("Attempting to delete expense ID: {}", expenseId);

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id: %s has not been found".formatted(expenseId)));

        User owner = expense.getOwner();
        BigDecimal updatedBudget = owner.getAvailableBudget().add(expense.getPrice());
        owner.setAvailableBudget(updatedBudget);
        log.info("Restored user's budget after deleting expense. New budget: {}", updatedBudget);

        expenseRepository.delete(expense);
        userRepository.save(owner);
        log.info("Expense deleted and user updated.");
    }
}
