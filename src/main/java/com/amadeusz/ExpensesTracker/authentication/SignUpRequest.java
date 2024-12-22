package com.amadeusz.ExpensesTracker.authentication;

import java.math.BigDecimal;

public record SignUpRequest(String firstName,
                            String lastName,
                            String email,
                            String password) {
}
