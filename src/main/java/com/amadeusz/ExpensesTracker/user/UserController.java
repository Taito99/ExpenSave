package com.amadeusz.ExpensesTracker.user;

import com.amadeusz.ExpensesTracker.authentication.AuthenticationService;
import com.amadeusz.ExpensesTracker.authentication.JwtAuthenticationResponse;
import com.amadeusz.ExpensesTracker.authentication.SignInRequestDto;
import com.amadeusz.ExpensesTracker.authentication.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequestDto request) {
        return authenticationService.signIn(request);
    }
}
