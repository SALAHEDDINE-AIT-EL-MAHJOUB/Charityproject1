package org.example.charityproject1.config;

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
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/login").permitAll() // Allow access to registration and login
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/dashboard") // Redirect to dashboard after login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Redirect to login page after logout
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define the PasswordEncoder bean
    }
}