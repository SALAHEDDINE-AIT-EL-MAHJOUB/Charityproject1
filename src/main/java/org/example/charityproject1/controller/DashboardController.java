package org.example.charityproject1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.repository.UtilisateursRepository;
import org.example.charityproject1.service.UtilisateursService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private UtilisateursService utilisateursService;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @GetMapping("/user/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // First try to get authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        logger.info("Retrieved from SecurityContext - user email: {}", email);

        // If we got anonymousUser, try to get from session
        if ("anonymousUser".equals(email)) {
            // Try to get from session
            email = (String) session.getAttribute("user_email");
            logger.info("Retrieved from session - user email: {}", email);

            if (email == null) {
                logger.error("No authenticated user found in session");
                return "redirect:/auth/login/user";
            }
        }

        // Try to find the user by email
        Optional<Utilisateurs> utilisateurOpt = utilisateursRepository.findByEmail(email);
        if (!utilisateurOpt.isPresent()) {
            logger.error("No user found for email: {}", email);
            return "redirect:/auth/login/user";
        }

        Utilisateurs utilisateur = utilisateurOpt.get();
        model.addAttribute("user", utilisateur);

        return "user/dashboard";
    }
}