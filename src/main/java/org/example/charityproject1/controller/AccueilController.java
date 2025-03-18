package org.example.charityproject1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {
    // Mapping the root URL to the "accueil" page
    @GetMapping("/")
    public String afficherPageAccueil() {
        return "/accueil";  // This will load the "accueil.html" page from the templates directory
    }
}
