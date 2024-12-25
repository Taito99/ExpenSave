package com.amadeusz.ExpensesTracker.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    void findExpenseById(UUID id);

    @Query("SELECT SUM (e.price) FROM Expense e WHERE e.owner.id= :userId AND e.category.name = :categoryName")
    BigDecimal findTotalSpendingByUserAndCategory(@Param("userId")UUID userId, @Param("categoryName")String categoryName);

}
