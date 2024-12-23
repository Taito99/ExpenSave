package com.amadeusz.ExpensesTracker.user;

import com.amadeusz.ExpensesTracker.exeptions.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final UserContextService userContextService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


    public void deleteCurrentUser(String username) {
        User user = userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + username));
        userDao.deleteCurrentUser(user);
    }

    public UserDto getCurrentUserDetails() {
        String username = userContextService.getAuthenticatedUsername();
        User user = userDao.selectUserByUsername(username)
                .orElseThrow(() -> new DuplicateResourceException("User not found"));

        return UserMapper.toUserDto(user);
    }
}

