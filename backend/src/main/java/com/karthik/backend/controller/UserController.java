package com.karthik.backend.controller;

import com.karthik.backend.api.ApiResponse;
import com.karthik.backend.dto.LoginRequestDTO;
import com.karthik.backend.dto.LoginResponseDTO;
import com.karthik.backend.dto.UserRequestDTO;
import com.karthik.backend.dto.UserResponseDTO;
import com.karthik.backend.service.AuthenticationService;
import com.karthik.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService,
                          AuthenticationService authenticationService) {

        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(
            @Valid @RequestBody UserRequestDTO request) {

        UserResponseDTO responseDTO = userService.registerUser(request);

        ApiResponse<UserResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "User registered successfully",
                        responseDTO
                );

        return ResponseEntity.ok(response);
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO responseDTO = authenticationService.login(request);

        ApiResponse<LoginResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Login successful",
                        responseDTO
                );

        return ResponseEntity.ok(response);
    }
}