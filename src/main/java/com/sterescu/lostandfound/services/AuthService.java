package com.sterescu.lostandfound.services;

import com.sterescu.lostandfound.entities.User;
import com.sterescu.lostandfound.payloads.ApiResponse;
import com.sterescu.lostandfound.payloads.JwtAuthenticationResponse;
import com.sterescu.lostandfound.payloads.LoginPayload;
import com.sterescu.lostandfound.payloads.SignUpPayload;
import com.sterescu.lostandfound.repositories.UserRepository;
import com.sterescu.lostandfound.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @Autowired
    public AuthService(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider,
                       final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(LoginPayload loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    public ResponseEntity<ApiResponse> registerUser(SignUpPayload signUpPayload) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpPayload.getEmail()))) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpPayload.getUsername()))) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = User.builder()
                .email(signUpPayload.getEmail())
                .name(signUpPayload.getUsername())
                .password(signUpPayload.getPassword())
                .username(signUpPayload.getUsername())
                .build();

        user.setPassword(encodePassword(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{usernameOrEmail}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
