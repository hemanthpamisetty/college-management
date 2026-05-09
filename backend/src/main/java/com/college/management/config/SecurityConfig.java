package com.college.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in this demo, though not recommended for production
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/css/**", "/images/**", "/js/**", "/api/auth/**").permitAll()
                .requestMatchers("/uploads/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/students/*/documents/upload").hasRole("ADMIN")
                .requestMatchers("/api/documents/*/download").authenticated()
                .requestMatchers("/api/timetable/**").authenticated()
                .requestMatchers("/api/attendance/**").authenticated()
                .requestMatchers("/api/results/**").authenticated()
                .requestMatchers("/api/examinations/**").authenticated()
                .requestMatchers("/api/events/**").authenticated()
                .requestMatchers("/api/library/**").authenticated()
                .requestMatchers("/api/fees/**").authenticated()
                .requestMatchers("/api/students/**").authenticated()
                .requestMatchers("/api/documents/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .usernameParameter("email") // This maps to the 'email' field in the form
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
