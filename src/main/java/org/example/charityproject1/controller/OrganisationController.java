package org.example.charityproject1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.model.Action;
import org.example.charityproject1.repository.OrganisationsRepository;
import org.example.charityproject1.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.List; 
import java.time.LocalDate;
import org.example.charityproject1.model.ActionCharite;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/organisation")
public class OrganisationController {
    @Autowired
    private ActionRepository actionRepository;
    
    @Autowired
    private OrganisationsRepository organisationsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        
        Organisations organisation = orgOptional.get();
        
        // Use the MongoDB ID to fetch actions
        String orgIdForAction = organisation.getId();
        List<Action> actions = actionRepository.findByOrganisationId(orgIdForAction);
        
        model.addAttribute("actions", actions);
        model.addAttribute("organisation", organisation);

        return "organisation/dashboard";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("nom") String nom,
                               @RequestParam(value = "numeroIdentif", required = false) String newNumeroIdentif,
                               @RequestParam("adresseLegale") String adresseLegale,
                               @RequestParam("description") String description,
                               @RequestParam("contactprincipal") String contactPrincipal,
                               @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
        if (!orgOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }

        // Validate form data
        if (nom == null || nom.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "name-invalid");
            redirectAttributes.addFlashAttribute("profileErrorMessage", "Le nom de l'organisation est obligatoire.");
            return "redirect:/organisation/dashboard?profileError=true";
        }

        // Validate numeroIdentif
        if (newNumeroIdentif == null || newNumeroIdentif.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "id-empty");
            redirectAttributes.addFlashAttribute("profileErrorMessage", "Le numéro d'identification est obligatoire.");
            return "redirect:/organisation/dashboard?profileError=true";
        }

        if (adresseLegale == null || adresseLegale.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "address-invalid");
            redirectAttributes.addFlashAttribute("profileErrorMessage", "L'adresse légale est obligatoire.");
            return "redirect:/organisation/dashboard?profileError=true";
        }

        if (description == null || description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "description-invalid");
            redirectAttributes.addFlashAttribute("profileErrorMessage", "La description est obligatoire.");
            return "redirect:/organisation/dashboard?profileError=true";
        }

        if (contactPrincipal == null || contactPrincipal.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("profileError", "contact-invalid");
            redirectAttributes.addFlashAttribute("profileErrorMessage", "Le contact principal est obligatoire.");
            return "redirect:/organisation/dashboard?profileError=true";
        }

        Organisations organisation = orgOptional.get();
        organisation.setValideParAdmin(false);

        // Check if ID changed and is unique
        if (!newNumeroIdentif.equals(orgId)) {
            Optional<Organisations> existingOrg = organisationsRepository.findByNumeroIdentif(newNumeroIdentif);
            if (existingOrg.isPresent() && !existingOrg.get().getIdOrganisation().equals(organisation.getIdOrganisation())) {
                redirectAttributes.addFlashAttribute("profileError", "id-exists");
                redirectAttributes.addFlashAttribute("profileErrorMessage", "Ce numéro d'identification est déjà utilisé.");
                return "redirect:/organisation/dashboard?profileError=true";
            }

            // Update ID and logout
            organisation.setNumeroIdentif(newNumeroIdentif);
            organisationsRepository.save(organisation);

            session.invalidate();
            redirectAttributes.addFlashAttribute("message", "Votre numéro d'identification a été modifié. Veuillez vous reconnecter.");
            return "redirect:/auth/login/organisation";
        }

        // Update organisation data
        organisation.setNom(nom);
        organisation.setAdresseLegale(adresseLegale);
        organisation.setDescription(description);
        organisation.setContactPrincipal(contactPrincipal);

        // Process logo if provided
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                // Check file size (max 2MB)
                if (logoFile.getSize() > 2 * 1024 * 1024) {
                    redirectAttributes.addFlashAttribute("profileError", "logo-invalid");
                    redirectAttributes.addFlashAttribute("profileErrorMessage", "La taille du logo ne doit pas dépasser 2MB.");
                    return "redirect:/organisation/dashboard?profileError=true";
                }

                // Check file type
                String contentType = logoFile.getContentType();
                if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                    redirectAttributes.addFlashAttribute("profileError", "logo-invalid");
                    redirectAttributes.addFlashAttribute("profileErrorMessage", "Le format du logo doit être JPG ou PNG.");
                    return "redirect:/organisation/dashboard?profileError=true";
                }

                byte[] bytes = logoFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                organisation.setLogo(base64Image);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("profileError", "logo-invalid");
                redirectAttributes.addFlashAttribute("profileErrorMessage", "Erreur lors du téléchargement du logo.");
                return "redirect:/organisation/dashboard?profileError=true";
            }
        }

        organisationsRepository.save(organisation);
        redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès.");
        return "redirect:/organisation/dashboard";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
        if (!orgOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }

        Organisations organisation = orgOptional.get();

        // Check if current password is correct
        if (!passwordEncoder.matches(currentPassword, organisation.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "password-incorrect");
            redirectAttributes.addFlashAttribute("errorMessage", "Le mot de passe actuel est incorrect.");
            return "redirect:/organisation/dashboard?passwordError=true";
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "password-mismatch");
            redirectAttributes.addFlashAttribute("errorMessage", "Les nouveaux mots de passe ne correspondent pas.");
            return "redirect:/organisation/dashboard?passwordError=true";
        }

        // Check if new password is different from current
        if (passwordEncoder.matches(newPassword, organisation.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "password-same");
            redirectAttributes.addFlashAttribute("errorMessage", "Le nouveau mot de passe doit être différent de l'ancien.");
            return "redirect:/organisation/dashboard?passwordError=true";
        }

        // Password strength validation
        if (newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("passwordError", "password-weak");
            redirectAttributes.addFlashAttribute("errorMessage", "Le mot de passe doit contenir au moins 8 caractères.");
            return "redirect:/organisation/dashboard?passwordError=true";
        }

        // Update password
        organisation.setPassword(passwordEncoder.encode(newPassword));
        organisationsRepository.save(organisation);

        // Log out the user
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Votre mot de passe a été modifié avec succès. Veuillez vous reconnecter.");
        return "redirect:/auth/login/organisation";
    }

    @GetMapping("/actions")
    public String actionsPage(Model model, HttpSession session) {
        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        // Retrieve the organization
        Optional<Organisations> organisation = organisationsRepository.findByNumeroIdentif(orgId);
        if (!organisation.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }
        
        // Use the MongoDB ID directly
        String orgIdForAction = organisation.get().getId();
        
        // Use the correct ID to match what's stored in the actions
        List<Action> actions = actionRepository.findByOrganisationId(orgIdForAction);
        
        model.addAttribute("actions", actions);
        model.addAttribute("organisation", organisation.get());

        return "organisation/dashboard";
    }

    @PostMapping("/actions")
    public String createAction(@RequestParam("titre") String titre,
                          @RequestParam("description") String description,
                          @RequestParam("date") String date,
                          @RequestParam("lieu") String lieu,
                          @RequestParam("objectifCollecte") int objectifCollecte,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
    
        try {
            String orgId = (String) session.getAttribute("org_identifier");
            if (orgId == null) {
                return "redirect:/auth/login/organisation";
            }

            Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
            if (!orgOptional.isPresent()) {
                session.invalidate();
                return "redirect:/auth/login/organisation";
            }

            Organisations organisation = orgOptional.get();

            // Validate form fields
            if (titre == null || titre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le titre est obligatoire");
                return "redirect:/organisation/dashboard";
            }

            if (description == null || description.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "La description est obligatoire");
                return "redirect:/organisation/dashboard";
            }

            // Create new action
            Action action = new Action();
            action.setTitre(titre);
            action.setDescription(description);
            
            try {
                // Convert string date to LocalDate
                LocalDate localDate = LocalDate.parse(date);
                action.setDate(localDate);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Format de date invalide");
                return "redirect:/organisation/dashboard";
            }
            
            action.setLieu(lieu);
            action.setObjectifCollecte(objectifCollecte);
            
            // Use the MongoDB ID directly
            String orgIdForAction = organisation.getId();
            action.setOrganisationId(orgIdForAction);
            
            // Initialize new fields
            action.setMontantActuel(0);  // Start with 0 amount collected
            action.setNombreParticipants(0);  // Start with 0 participants

            // Save the action
            actionRepository.save(action);
            
            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", "Action créée avec succès!");
            
            return "redirect:/organisation/dashboard";
            
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite: " + e.getMessage());
            return "redirect:/organisation/dashboard";
        }
    }

    @PostMapping("/actions/{id}/archive")
    public String archiveAction(@PathVariable("id") String actionId,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
    
        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
        if (!orgOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }

        Organisations organisation = orgOptional.get();
    
        // Find the action to archive
        Optional<Action> actionOptional = actionRepository.findById(actionId);
        if (!actionOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Action non trouvée");
            return "redirect:/organisation/dashboard";
        }
    
        Action action = actionOptional.get();
    
        // Ensure the action belongs to this organisation
        if (!action.getOrganisationId().equals(organisation.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous n'êtes pas autorisé à archiver cette action");
            return "redirect:/organisation/dashboard";
        }
    
        // Initialize archivedActionsCharite list if null
        if (organisation.getArchivedActionsCharite() == null) {
            organisation.setArchivedActionsCharite(new ArrayList<>());
        }
    
        // Convert Action to ActionCharite if necessary
        // This depends on your implementation details
        ActionCharite archivedAction = convertToActionCharite(action);
    
        // Add to archived actions
        organisation.getArchivedActionsCharite().add(archivedAction);
    
        // Save organisation with the archived action
        organisationsRepository.save(organisation);
    
        // Delete the action from active actions
        actionRepository.deleteById(actionId);
    
        redirectAttributes.addFlashAttribute("successMessage", "L'action a été archivée avec succès");
        return "redirect:/organisation/dashboard";
    }

    @PostMapping("/actions/{id}/delete")
    public String deleteAction(@PathVariable("id") String actionId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
    
        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
        if (!orgOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }

        Organisations organisation = orgOptional.get();
    
        // Find the action to delete
        Optional<Action> actionOptional = actionRepository.findById(actionId);
        if (!actionOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Action non trouvée");
            return "redirect:/organisation/dashboard";
        }
    
        Action action = actionOptional.get();
    
        // Ensure the action belongs to this organisation
        if (!action.getOrganisationId().equals(organisation.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous n'êtes pas autorisé à supprimer cette action");
            return "redirect:/organisation/dashboard";
        }
    
        // Delete the action
        actionRepository.deleteById(actionId);
    
        redirectAttributes.addFlashAttribute("successMessage", "L'action a été supprimée avec succès");
        return "redirect:/organisation/dashboard";
    }

    @GetMapping("/actions/archived")
    public String archivedActions(Model model, HttpSession session) {
        String orgId = (String) session.getAttribute("org_identifier");
        if (orgId == null) {
            return "redirect:/auth/login/organisation";
        }

        Optional<Organisations> orgOptional = organisationsRepository.findByNumeroIdentif(orgId);
        if (!orgOptional.isPresent()) {
            session.invalidate();
            return "redirect:/auth/login/organisation";
        }
        
        Organisations organisation = orgOptional.get();
        List<ActionCharite> archivedActions = organisation.getArchivedActionsCharite();
        
        // If no archived actions, initialize as empty list to avoid null pointer
        if (archivedActions == null) {
            archivedActions = new ArrayList<>();
        }
        
        model.addAttribute("archivedActions", archivedActions);
        model.addAttribute("organisation", organisation);
        model.addAttribute("showArchived", true);
        
        // Also fetch active actions to keep them in the model
        String orgIdForAction = organisation.getId();
        List<Action> activeActions = actionRepository.findByOrganisationId(orgIdForAction);
        model.addAttribute("actions", activeActions);
        
        return "organisation/dashboard";
    }

    // Helper method to convert Action to ActionCharite 
    // Adjust this method based on your model structure
    private ActionCharite convertToActionCharite(Action action) {
        ActionCharite actionCharite = new ActionCharite();
    
        // Set all required fields
        actionCharite.setIdAction(action.getId());
        actionCharite.setTitre(action.getTitre());
        actionCharite.setDescription(action.getDescription());
        // Convert LocalDate to Date if needed
        actionCharite.setDate(java.sql.Date.valueOf(action.getDate()));
        actionCharite.setLieu(action.getLieu());
        actionCharite.setObjectifCollecte(action.getObjectifCollecte());
        actionCharite.setMontantActuel(action.getMontantActuel());
        actionCharite.setOrganisationId(action.getOrganisationId());
    
        return actionCharite;
    }
}