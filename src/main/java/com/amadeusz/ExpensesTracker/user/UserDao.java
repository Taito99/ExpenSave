package com.amadeusz.ExpensesTracker.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    List<User> selectAllUsers();
    Optional<User> selectUserById(UUID id);
    Optional<User> selectUserByEmail(String email);
    void deleteCurrentUser(User user);
    void insertUser(User user);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(UUID id);

}
