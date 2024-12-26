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
import java.time.LocalDate;
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
        log.info("Starting to create expense: %s".formatted(expenseDto));

        String username = userContextService.getAuthenticatedUsername();
        log.info("Authenticated username: %s".formatted(username));

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: %s has not been found".formatted(username)));
        log.info("User found: %s".formatted(user.getUsername()));

        Category category = categoryRepository.findCategoryByName(expenseDto.categoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category with name: %s has not been found".formatted(expenseDto.categoryName())));
        log.info("Category found: %s".formatted(category.getName()));


        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .date(expenseDto.date())
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .category(category)
                .owner(user)
                .build();


        BigDecimal categoryLimit = user.getCategoryLimits().get(category.getName());
        if (categoryLimit == null) {
            user.getExpenses().add(expense);
            user.setAvailableBudget(user.getAvailableBudget().subtract(expense.getPrice()));
            expenseDao.insertExpense(expense);
            userRepository.save(user);
            return;
        }
        BigDecimal totalSpent = user.getExpenses()
                        .stream()
                                .filter(expense1 -> expense1.getCategory().getName().equals(expense.getCategory().getName()))
                                        .map(Expense::getPrice)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSpent.add(expenseDto.price()).compareTo(categoryLimit) > 0) {
            throw new IllegalStateException("Exceeded category limit for " + expenseDto.categoryName());
        }

        expenseDao.insertExpense(expense);
        log.info("Expense saved to database: ".formatted(expense.getName()));

        user.setAvailableBudget(user.getAvailableBudget().subtract(expenseDto.price()));

        user.getExpenses().add(expense);
        BigDecimal newLimit = user.getCategoryLimits().get(category.getName()).subtract(expenseDto.price());
        user.getCategoryLimits().put(category.getName(), newLimit);
        userRepository.save(user);
        log.info("User updated with new expense.");
    }

    public ExpenseDto getExpenseDetails(UUID expenseId) {
       ExpenseDto expense = expenseDao.findExpenseById(expenseId)
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

    public BigDecimal totalSpendingForGivenCategory(String categoryName) {
        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal totalSpending = user.getExpenses()
                .stream()
                .filter(expense -> expense.getCategory().getName().equals(categoryName))
                .map(Expense::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Total spending for category %s: %s".formatted(categoryName, totalSpending));
        return totalSpending;
    }


    @Transactional
    public void deleteExpense(UUID expenseId) {

        Expense expense = expenseDao.findExpenseById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id: %s has not been found".formatted(expenseId)));

        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!expense.getOwner().getId().equals(user.getId())) {
            throw new NotAllowedException("User is not owner of expense");
        }

        BigDecimal updatedBudget = user.getAvailableBudget().add(expense.getPrice());
        user.setAvailableBudget(updatedBudget);
        expenseDao.deleteExpense(expense);
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

    public List<ExpenseDto> filterExpensesByCategory(String category) {
        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getExpenses()
                .stream()
                .map(expenseMapper)
                .filter(expenseDto -> expenseDto.categoryName().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> filterExpensesByDaysRange(Integer numberOfDays) {
        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LocalDate startDate = LocalDate.now().minusDays(numberOfDays + 1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        return user.getExpenses()
                .stream()
                .filter(expense -> expense.getDate().isAfter(startDate) && expense.getDate().isBefore(endDate))
                .map(expenseMapper)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> filterExpensesByDateRange(LocalDate from, LocalDate to) {
        String username = userContextService.getAuthenticatedUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getExpenses()
                .stream()
                .filter(expense -> (expense.getDate().isEqual(from) || expense.getDate().isAfter(from))
                        && (expense.getDate().isEqual(to) || expense.getDate().isBefore(to)))
                .map(expenseMapper)
                .collect(Collectors.toList());
    }
}
