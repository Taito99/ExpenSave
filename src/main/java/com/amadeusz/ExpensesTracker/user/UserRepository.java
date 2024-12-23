package com.amadeusz.ExpensesTracker.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsUserById(UUID id);
    boolean existsUserByEmail(String email);
    Optional<User> findUserById(UUID id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);

}
