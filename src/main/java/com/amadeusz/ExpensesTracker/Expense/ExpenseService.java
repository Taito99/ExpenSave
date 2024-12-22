package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.category.Category;
import com.amadeusz.ExpensesTracker.category.CategoryRepository;
import com.amadeusz.ExpensesTracker.exeptions.ResourceNotFoundException;
import com.amadeusz.ExpensesTracker.user.User;
import com.amadeusz.ExpensesTracker.user.UserContextService;
import com.amadeusz.ExpensesTracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseDao expenseDao;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    public void createExpense(ExpenseDto expenseDto) {
        UUID userId = UUID.fromString(userContextService.getAuthenticatedUserId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: %s has been not found".formatted(userId)));
        Category category = categoryRepository.findCategoryByName(expenseDto.categoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category with name: %s has been not found".formatted(expenseDto.categoryName())));


       Expense expense = expenseDao.insertExpense(
                Expense.builder()
                        .name(expenseDto.name())
                        .date(expenseDto.date())
                        .price(expenseDto.price())
                        .quantity(expenseDto.quantity())
                        .category(category)
                        .owner(user)
                        .build()
        );

       user.getExpenses().add(expense);
    }

}
