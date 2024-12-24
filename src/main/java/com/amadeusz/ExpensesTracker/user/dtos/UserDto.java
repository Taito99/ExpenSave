package com.amadeusz.ExpensesTracker.user.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.UUID;

public record UserDto(
                      @JsonIgnore UUID id,
                      String firstName,
                      String lastName,
                      String username,
                      String email,
                      BigDecimal monthlyBudget,
                      BigDecimal availableBudget ) {}
