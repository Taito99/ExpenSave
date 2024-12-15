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
        return userRepository.findById(id);
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
