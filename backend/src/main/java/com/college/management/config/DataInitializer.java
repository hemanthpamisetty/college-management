package com.college.management.config;

import com.college.management.entity.Role;
import com.college.management.entity.User;
import com.college.management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------------------------");
        System.out.println("[AUTH-SETUP] Checking for Admin account...");
        
        if (!userRepository.existsByEmail("admin@college.edu")) {
            User admin = User.builder()
                    .name("System Admin")
                    .email("admin@college.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .registrationNumber("ADMIN001")
                    .build();
            
            userRepository.save(admin);
            System.out.println("[AUTH-SETUP] Admin account CREATED: admin@college.edu / admin123");
        } else {
            System.out.println("[AUTH-SETUP] Admin account already EXISTS in database.");
        }
        System.out.println("---------------------------------------------------------");
    }
}
