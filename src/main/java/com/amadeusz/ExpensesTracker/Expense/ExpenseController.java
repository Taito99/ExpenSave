package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses/")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<String> createExpense(@RequestBody ExpenseDto expenseDto) {
        log.info("Received request to create expense: {}", expenseDto);

        try {
            expenseService.createExpense(expenseDto);
            log.info("Expense created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Expense created successfully");
        } catch (IllegalStateException e) {
            log.error("Error creating expense: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            log.error("Error creating expense: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping(value = "{expenseId}")
    public ResponseEntity<ExpenseDto> getExpenseDetails(@PathVariable UUID expenseId) {
        return ResponseEntity.ok(expenseService.getExpenseDetails(expenseId));
    }
    @DeleteMapping(value = "/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("total-category-spending/{categoryName}")
    public ResponseEntity<BigDecimal> totalSpendingForCategory(@PathVariable String categoryName) {
        log.info("Received request to get total spending for category: {}", categoryName);
        try {
            BigDecimal totalSpending = expenseService.totalSpendingForGivenCategory(categoryName);
            return ResponseEntity.ok(totalSpending);
        } catch (ResourceNotFoundException e) {
            log.error("Error fetching total spending: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/list-all-expenses")
    public ResponseEntity<List<ExpenseDto>> getAllExpensesOfUser() {
        log.info("Received request to fetch all expenses for authenticated user.");
        try {
            List<ExpenseDto> expenses = expenseService.getAllExpensesOfUser();
            return ResponseEntity.ok(expenses);
        } catch (ResourceNotFoundException e) {
            log.error("Error fetching expenses: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("filter-by-category/{categoryName}")
    public ResponseEntity<List<ExpenseDto>> filterExpensesByCategory(@PathVariable String categoryName) {
        log.info("Received request to filter expenses by category: {}", categoryName);
        try {
            List<ExpenseDto> expenses = expenseService.filterExpensesByCategory(categoryName);
            return ResponseEntity.ok(expenses);
        } catch (ResourceNotFoundException e) {
            log.error("Error fetching filtered expenses: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("filter-by-days-range/{numberOfDays}")
    public ResponseEntity<List<ExpenseDto>> filterExpensesByDays(@PathVariable Integer numberOfDays) {
        try {
            List<ExpenseDto> expenses = expenseService.filterExpensesByDaysRange(numberOfDays);
            return ResponseEntity.ok(expenses);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("filter-by-date-range/{from}/{to}")
    public ResponseEntity<List<ExpenseDto>> filterExpensesByDateRange(@PathVariable LocalDate from, @PathVariable LocalDate to) {
        try {
            List<ExpenseDto> expenses = expenseService.filterExpensesByDateRange(from, to);
            return ResponseEntity.ok(expenses);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

