package com.college.management.dto;

import com.college.management.entity.Role;

public class AuthResponse {
    private String message;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private boolean success;

    public AuthResponse() {}

    public AuthResponse(String message, Long userId, String name, String email, Role role, boolean success) {
        this.message = message;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.success = success;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public static class AuthResponseBuilder {
        private String message;
        private Long userId;
        private String name;
        private String email;
        private Role role;
        private boolean success;

        public AuthResponseBuilder message(String message) { this.message = message; return this; }
        public AuthResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public AuthResponseBuilder name(String name) { this.name = name; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder role(Role role) { this.role = role; return this; }
        public AuthResponseBuilder success(boolean success) { this.success = success; return this; }

        public AuthResponse build() {
            return new AuthResponse(message, userId, name, email, role, success);
        }
    }
}
