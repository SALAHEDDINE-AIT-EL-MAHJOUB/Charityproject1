package org.example.charityproject1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.service.UtilisateursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AuthController {

    @Autowired
    private UtilisateursService utilisateursService;

    // Registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateurs());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute Utilisateurs utilisateur, // Bind the form data to the Utilisateurs object
            @RequestParam("logoPath") String logoPath, // Handle the Base64-encoded string
            Model model
    ) {
        try {
            System.out.println("Attempting to register user: " + utilisateur.getEmail()); // Debug statement

            // Check if the logo file is empty
            if (logoPath != null && !logoPath.isEmpty()) {
                // Set the logo path in the model
                utilisateur.setLogoPath(logoPath); // Manually set the logoPath field
            }

            // Register the user
            utilisateursService.registerUser(utilisateur);
            System.out.println("User registered successfully: " + utilisateur.getEmail()); // Debug statement
            return "redirect:/login"; // Redirect to login page after successful registration
        } catch (RuntimeException e) {
            System.out.println("Error registering user: " + utilisateur.getEmail() + ", Error: " + e.getMessage()); // Debug statement
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


    @PostMapping("/login")
    public String loginUser() {
        // Spring Security will handle login and redirection after authentication
        return "redirect:/dashboard"; // You don't need to manually authenticate the user here
    }

}