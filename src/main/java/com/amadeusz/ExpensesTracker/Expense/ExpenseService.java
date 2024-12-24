package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.category.Category;
import com.amadeusz.ExpensesTracker.category.CategoryRepository;
import com.amadeusz.ExpensesTracker.exeptions.NotAllowedException;
import com.amadeusz.ExpensesTracker.exeptions.ResourceNotFoundException;
import com.amadeusz.ExpensesTracker.user.User;
import com.amadeusz.ExpensesTracker.user.UserContextService;
import com.amadeusz.ExpensesTracker.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {
    private final ExpenseDao expenseDao;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

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

        user.setAvailableBudget(user.getAvailableBudget().subtract(expenseDto.price()));

        user.getExpenses().add(expense);
        userRepository.save(user);
        log.info("User updated with new expense.");
    }

    public ExpenseDto getExpenseDetails(UUID expenseId) {
       ExpenseDto expense = expenseDao.getExpenseById(expenseId)
               .map(expenseMapper)
               .orElseThrow(() -> new ResourceNotFoundException("Expense with id: %s has been not found".formatted(expenseId)));
       String username = userContextService.getAuthenticatedUsername();
       User user = userRepository.findUserByUsername(username)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));
       if(!expense.ownerId().equals(user.getId())) {
           throw new NotAllowedException("User is not owner of expense");
       }
       return expense;

    }

    @Transactional
    public void deleteExpense(UUID expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id: %s has not been found".formatted(expenseId)));

        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!expense.getOwner().getId().equals(user.getId())) {
            throw new NotAllowedException("User is not owner of expense");
        }

        BigDecimal updatedBudget = user.getAvailableBudget().add(expense.getPrice());
        user.setAvailableBudget(updatedBudget);

        expenseRepository.delete(expense);
        userRepository.save(user);
    }

    public List<ExpenseDto> getAllExpensesOfUser() {
        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getExpenses()
                .stream()
                .map(expenseMapper)
                .collect(Collectors.toList());

    }
}
