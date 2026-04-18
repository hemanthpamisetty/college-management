package com.college.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "Username/Email is required")
    private String email; // Will be used for finding by REG NO or EMAIL

    @NotBlank(message = "Password is required")
    private String password;
}
