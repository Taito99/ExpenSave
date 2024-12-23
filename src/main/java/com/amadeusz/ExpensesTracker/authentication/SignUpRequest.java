package com.amadeusz.ExpensesTracker.authentication;

public record SignUpRequest(String firstName,
                            String lastName,
                            String username,
                            String email,
                            String password) {
}
