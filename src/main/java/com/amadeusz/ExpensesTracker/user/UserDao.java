package com.amadeusz.ExpensesTracker.user;

import java.util.Optional;

public interface UserDao {
    Optional<User> selectUserByUsername(String username);

    void deleteCurrentUser(User user);
    boolean existsPersonWithEmail(String email);

}
