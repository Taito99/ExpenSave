package com.amadeusz.ExpensesTracker.user.dtos;

import com.amadeusz.ExpensesTracker.user.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {


    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getMonthlyBudget(),
                user.getAvailableBudget()
        );
    }
}
