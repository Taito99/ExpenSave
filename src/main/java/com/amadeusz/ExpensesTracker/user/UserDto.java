package com.amadeusz.ExpensesTracker.user;

import java.math.BigDecimal;

public record UserDto(String firstName, String lastName, String username, String email, BigDecimal monthlyBudget, BigDecimal availableBudget ) {
}
