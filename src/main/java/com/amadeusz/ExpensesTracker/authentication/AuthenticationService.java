package com.amadeusz.ExpensesTracker.authentication;

import com.amadeusz.ExpensesTracker.exeptions.DuplicateResourceException;
import com.amadeusz.ExpensesTracker.user.Role;
import com.amadeusz.ExpensesTracker.user.User;
import com.amadeusz.ExpensesTracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        if(userRepository.existsUserByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if(userRepository.existsUserByEmail(request.username())) {
            throw new DuplicateResourceException("Username already exists");
        }


        var user = User
                .builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();


        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = userRepository.findUserByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

}
