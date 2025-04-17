package org.example.charityproject1.controller;

import org.example.charityproject1.model.ActionCharite;
import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.repository.ActionChariteRepository;
import org.example.charityproject1.repository.OrganisationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private OrganisationsRepository organisationsRepository;

    @Autowired
    private ActionChariteRepository actionChariteRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
        if (!orgOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }

        // Charger les actions de l'organisation
        List<ActionCharite> actions = actionChariteRepository.findAllByOrganisationId(orgId);
        model.addAttribute("actions", actions);

        return "organisation/dashboard";
    }
}