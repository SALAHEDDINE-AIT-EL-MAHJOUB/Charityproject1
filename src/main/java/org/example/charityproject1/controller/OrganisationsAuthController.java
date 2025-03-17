package org.example.charityproject1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.service.OrganisationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class OrganisationsAuthController {

    @Autowired
    private OrganisationsService organisationsService;

    // Registration form
    @GetMapping("/registerorganisation")
    public String showRegistrationForm(Model model) {
        model.addAttribute("organisation", new Organisations());
        return "organisations/register"; // Make sure this points to the correct view file in "organisations"
    }

    @PostMapping("/registerorganisation")
    public String registerOrganisation(
            @ModelAttribute Organisations organisation,
            @RequestParam("logo") String logo,
            Model model
    ) {
        try {
            // Validate and register the organisation
            System.out.println("Attempting to register user: " + organisation.getNumeroIdentif()); // Debug statement
            // Check if the logo file is empty
            if (logo != null && !logo.isEmpty()) {
                // Set the logo path in the model
                organisation.setLogo(logo); // Manually set the logoPath field
            }

            organisationsService.registerOrganisation(organisation);
            System.out.println("User registered successfully: " + organisation.getNumeroIdentif()); // Debug statement
            return "redirect:organisations/login"; // Redirect to login page after successful registration
        } catch (RuntimeException e) {
            System.out.println("Error registering user: " + organisation.getNumeroIdentif() + ", Error: " + e.getMessage()); // Debug statement
            model.addAttribute("error", e.getMessage()); // Show the error in the registration form
            return "organisations/register"; // Return the register page with the error message
        }
    }

    // Login form
    @GetMapping("/loginorganisation")
    public String showLoginForm() {
        return "organisations/login"; // Ensure this points to the correct login view in "organisations"
    }


    @PostMapping("/loginorganisation")
    public String loginOrganisation() {
        // Spring Security will handle login and redirection after authentication
        return "redirect:organisations/dashboard"; // After successful login, redirect to the dashboard
    }
    @GetMapping("/organisations/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Perform logout logic if needed, or you can rely on Spring Security's default behavior
        SecurityContextHolder.clearContext(); // Clear the authentication context
        request.getSession().invalidate(); // Invalidate the session

        // Redirect to the login page after logout
        return "redirect:/organisations/login"; // Or wherever you'd like to redirect the user
    }

}
