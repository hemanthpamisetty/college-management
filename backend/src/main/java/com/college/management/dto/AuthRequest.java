package com.college.management.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {

    @NotBlank(message = "Username/Email is required")
    private String email; // Will be used for finding by REG NO or EMAIL

    @NotBlank(message = "Password is required")
    private String password;

    public AuthRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
