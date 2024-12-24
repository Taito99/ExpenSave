package com.amadeusz.ExpensesTracker.user;

import com.amadeusz.ExpensesTracker.Expense.Expense;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    List<User> selectAllUsers();
    Optional<User> selectUserById(UUID id);
    Optional<User> selectUserByUsername(String username);
    Optional<User> selectUserByEmail(String email);
    void deleteCurrentUser(User user);
    void insertUser(User user);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(UUID id);

}
