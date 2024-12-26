package com.amadeusz.ExpensesTracker.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDataAccessService implements UserDao{
    private final UserRepository userRepository;

    @Override
    public Optional<User> selectUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void deleteCurrentUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

}
