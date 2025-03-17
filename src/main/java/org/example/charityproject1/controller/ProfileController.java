package org.example.charityproject1.controller;

import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.repository.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @GetMapping
    public String viewProfile(Model model, @AuthenticationPrincipal User user) {
        Utilisateurs utilisateur = utilisateursRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + user.getUsername()));

        model.addAttribute("utilisateur", utilisateur);
        return "profile/view"; // Return the profile view
    }

    // Afficher le formulaire de modification
    @GetMapping("/edit")
    public String modifyProfile(Model model, @AuthenticationPrincipal User user) {
        Utilisateurs utilisateur = utilisateursRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + user.getUsername()));
        model.addAttribute("utilisateur", utilisateur);
        return "profile/edit"; // Retourne la vue de modification
    }

    // Mettre à jour le profil
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Utilisateurs updatedUtilisateur,
                                @RequestParam(value = "logo", required = false) MultipartFile logo,
                                @AuthenticationPrincipal User user) {
        Utilisateurs utilisateur = utilisateursRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + user.getUsername()));

        // Mise à jour des champs sauf l'email
        utilisateur.setNom(updatedUtilisateur.getNom());
        utilisateur.setTelephone(updatedUtilisateur.getTelephone());
        utilisateur.setLocalisation(updatedUtilisateur.getLocalisation());

        // Gestion du logo (Base64)
        if (logo != null && !logo.isEmpty()) {
            String base64Logo = convertToBase64(logo);
            utilisateur.setLogoPath(base64Logo); // Store the Base64 string in the database
        }

        utilisateursRepository.save(utilisateur);
        return "redirect:/profile";
    }

    // Convertir une image en Base64
    private String convertToBase64(MultipartFile logo) {
        try {
            byte[] bytes = logo.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
