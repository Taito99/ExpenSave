package com.amadeusz.ExpensesTracker.user.dtos;

import java.math.BigDecimal;

public record CategoryLimitDto(String categoryName, BigDecimal limit) {
}
