package com.amadeusz.ExpensesTracker.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsUserById(UUID id);
    boolean existsUserByEmail(String email);

}
