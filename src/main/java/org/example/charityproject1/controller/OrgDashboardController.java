package org.example.charityproject1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class OrgDashboardController {
    @GetMapping("/organisations/dashboard")
    public String afficherPageAccueil() {
        return "/organisations/dashboard";  // This will load the "accueil.html" page from the templates directory
    }
}
