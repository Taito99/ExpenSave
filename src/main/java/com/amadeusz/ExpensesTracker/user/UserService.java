package com.amadeusz.ExpensesTracker.user;

import com.amadeusz.ExpensesTracker.Expense.ExpenseDto;
import com.amadeusz.ExpensesTracker.Expense.ExpenseMapper;
import com.amadeusz.ExpensesTracker.exeptions.NotAllowedException;
import com.amadeusz.ExpensesTracker.exeptions.ResourceNotFoundException;
import com.amadeusz.ExpensesTracker.user.dtos.BudgetInitDto;
import com.amadeusz.ExpensesTracker.user.dtos.UserDto;
import com.amadeusz.ExpensesTracker.user.dtos.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final UserDtoMapper userMapper;
    private final ExpenseMapper expenseMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


    public void deleteCurrentUser(String username) {
        User user = userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        userDao.deleteCurrentUser(user);
    }

    public UserDto getCurrentUserDetails() {
        String username = userContextService.getAuthenticatedUsername();
        return userDao.selectUserByUsername(username)
                .map(userMapper)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    public void setInitialBudget(BudgetInitDto request) {
        String username = userContextService.getAuthenticatedUsername();
        User user = userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotAllowedException("Initial budget can not be 0 or less");
        }

        user.setMonthlyBudget(user.getMonthlyBudget().add(request.amount()));
        user.setAvailableBudget(user.getAvailableBudget().add(request.amount()));
        userRepository.save(user);
    }

}

