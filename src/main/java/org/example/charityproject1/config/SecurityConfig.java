package org.example.charityproject1.config;

import org.example.charityproject1.service.CustomUserDetailsService;
import org.example.charityproject1.service.OrganisationDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private OrganisationDetailsService organisationDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Filter chain for organization endpoints (e.g., login and dashboard)
    @Bean
    public SecurityFilterChain organisationSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/organisations/**")  // Only match /organisations/**
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("loginorganisation", "loginorganisation").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/organisations/login")              // Organization login page
                        .loginProcessingUrl("/organisations/login")       // Organization login processing URL
                        .successHandler(new CustomLoginSuccessHandler())  // Custom success handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/organisations/logout")               // Organization logout URL
                        .logoutSuccessUrl("/organisations/login")         // After logout, redirect to organization login page
                        .permitAll()
                )
                .authenticationManager(authenticationManager());
        return http.build();
    }

    // Filter chain for user endpoints (e.g., login and dashboard)
    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/loginorganisation", "/registerorganisation").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                              // User login page
                        .loginProcessingUrl("/login")                     // User login processing URL
                        .successHandler(new CustomLoginSuccessHandler())  // Custom success handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                             // User logout URL
                        .logoutSuccessUrl("/login")                       // After logout, redirect to user login page
                        .permitAll()
                )
                .authenticationManager(authenticationManager());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Shared AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider userAuthProvider = new DaoAuthenticationProvider();
        userAuthProvider.setUserDetailsService(customUserDetailsService);
        userAuthProvider.setPasswordEncoder(passwordEncoder());

        DaoAuthenticationProvider organisationAuthProvider = new DaoAuthenticationProvider();
        organisationAuthProvider.setUserDetailsService(organisationDetailsService);
        organisationAuthProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(userAuthProvider, organisationAuthProvider);
    }


}
