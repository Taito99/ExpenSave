package com.amadeusz.ExpensesTracker.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserDataAccessService implements UserDao{
    private final UserRepository userRepository;

    @Override
    public List<User> selectAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void deleteCurrentUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsPersonWithId(UUID id) {
        return userRepository.existsUserById(id);
    }
}
