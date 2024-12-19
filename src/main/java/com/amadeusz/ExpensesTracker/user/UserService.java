package com.amadeusz.ExpensesTracker.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;
}
