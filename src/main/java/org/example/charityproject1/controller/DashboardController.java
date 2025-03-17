package org.example.charityproject1.controller;

import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.service.UtilisateursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

    @Autowired
    private UtilisateursService utilisateursService;

    // Endpoint to show the dashboard and retrieve user info
    @RequestMapping("/dashboard")
    public String showDashboard(Model model) {
        // Get the current authenticated user's email (username)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // This is the email or username of the logged-in user

        // Retrieve the user by email from the database
        Utilisateurs utilisateur = utilisateursService.findByemail(email);

        // If user is not found, redirect to login page
        if (utilisateur == null) {
            return "redirect:/login";
        }

        // Add the utilisateur object to the model so it can be accessed in the view
        model.addAttribute("utilisateur", utilisateur);

        // Return the dashboard view
        return "dashboard";  // Replace with the actual name of your dashboard template
    }
}
