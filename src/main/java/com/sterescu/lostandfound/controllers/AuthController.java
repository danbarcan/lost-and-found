package com.sterescu.lostandfound.controllers;

import com.sterescu.lostandfound.payloads.ApiResponse;
import com.sterescu.lostandfound.payloads.JwtAuthenticationResponse;
import com.sterescu.lostandfound.payloads.LoginPayload;
import com.sterescu.lostandfound.payloads.SignUpPayload;
import com.sterescu.lostandfound.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginPayload loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpPayload signUpPayload) {
        return authService.registerUser(signUpPayload);
    }
}