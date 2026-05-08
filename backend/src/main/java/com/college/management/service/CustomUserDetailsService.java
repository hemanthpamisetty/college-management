package com.college.management.service;

import com.college.management.entity.User;
import com.college.management.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[AUTH-DEBUG] Attempting login for: " + username);
        
        User user = userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByRegistrationNumber(username)
                .orElseThrow(() -> {
                    System.out.println("[AUTH-DEBUG] Login failed: User not found for " + username);
                    return new UsernameNotFoundException("User not found with email or registration number: " + username);
                }));

        System.out.println("[AUTH-DEBUG] User found: " + user.getEmail() + " with role: " + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
