package com.amadeusz.ExpensesTracker.user;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getMonthlyBudget(),
                user.getAvailableBudget()
        );
    }
}
