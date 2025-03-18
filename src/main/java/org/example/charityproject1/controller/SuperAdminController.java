package org.example.charityproject1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.model.SuperAdmin;
import org.example.charityproject1.repository.SuperAdminRepository;
import org.example.charityproject1.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private SuperAdminService superAdminService;
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if admin is logged in
        String adminEmail = (String) session.getAttribute("admin_email");
        if (adminEmail == null) {
            return "redirect:/auth/login/superadmin";
        }

        // Get admin details
        Optional<SuperAdmin> adminOptional = superAdminRepository.findByEmail(adminEmail);
        if (!adminOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/superadmin";
        }

        // Add admin to model
        model.addAttribute("admin", adminOptional.get());

        // Note: The flash attribute will be automatically added to the model
        // No need to explicitly add "successMessage" here

        return "superadmin/dashboard";
    }

    // REST API endpoints for future implementation

    @PostMapping("/api/organisations/{id}/validate")
    @ResponseBody
    public ResponseEntity<?> validateOrganisation(@PathVariable String id) {
        try {
            // Add debug logging
            System.out.println("Attempting to validate organization with ID: " + id);

            superAdminService.validateOrganisation(id);

            // Return success response
            return ResponseEntity.ok().body(Map.of("status", "success", "message", "Organisation validée avec succès"));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error validating organization: " + e.getMessage());
            e.printStackTrace();

            // Return error response
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // Delete an organisation
    @DeleteMapping("/api/organisations/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteOrganisation(@PathVariable String id) {
        superAdminService.deleteOrganisation(id);
        return ResponseEntity.ok("Organisation deleted successfully");
    }

    // Get all organisations
    @GetMapping("/api/organisations")
    @ResponseBody
    public ResponseEntity<List<Organisations>> getAllOrganisations() {
        List<Organisations> organisations = superAdminService.getAllOrganisations();
        return ResponseEntity.ok(organisations);
    }

    // Add these methods to your existing SuperAdminController class

    // View all organisations in a web page
    @GetMapping("/organisations")
    public String viewAllOrganisations(HttpSession session, Model model) {
        // Check if admin is logged in
        String adminEmail = (String) session.getAttribute("admin_email");
        if (adminEmail == null) {
            return "redirect:/auth/login/superadmin";
        }

        // Get admin details for the navbar/header
        Optional<SuperAdmin> adminOptional = superAdminRepository.findByEmail(adminEmail);
        if (!adminOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/superadmin";
        }

        // Add admin to model
        model.addAttribute("admin", adminOptional.get());

        // Get all organisations and add to model
        List<Organisations> organisations = superAdminService.getAllOrganisations();
        model.addAttribute("organisations", organisations);

        return "superadmin/organisations";
    }

    // Placeholder for charities page
    @GetMapping("/charities")
    public String viewCharities(HttpSession session, Model model) {
        // Check if admin is logged in
        String adminEmail = (String) session.getAttribute("admin_email");
        if (adminEmail == null) {
            return "redirect:/auth/login/superadmin";
        }

        // Get admin details
        Optional<SuperAdmin> adminOptional = superAdminRepository.findByEmail(adminEmail);
        if (!adminOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/superadmin";
        }

        // Add admin to model
        model.addAttribute("admin", adminOptional.get());

        // This page will be implemented later

        return "superadmin/charities";
    }
}