package com.amadeusz.ExpensesTracker.user;

import com.amadeusz.ExpensesTracker.authentication.AuthenticationService;
import com.amadeusz.ExpensesTracker.authentication.JwtAuthenticationResponse;
import com.amadeusz.ExpensesTracker.authentication.SignInRequestDto;
import com.amadeusz.ExpensesTracker.authentication.SignUpRequest;
import com.amadeusz.ExpensesTracker.user.dtos.BudgetInitDto;
import com.amadeusz.ExpensesTracker.user.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/user/")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequestDto request) {
        return authenticationService.signIn(request);
    }

    @GetMapping
    public UserDto getUserDetails() {
        return userService.getCurrentUserDetails();
    }

    @PostMapping("set-initial-budget")
    public ResponseEntity<Void> setInitialBudget(@RequestBody BudgetInitDto amount) {
        userService.setInitialBudget(amount);
        return ResponseEntity.noContent().build();
    }
}
