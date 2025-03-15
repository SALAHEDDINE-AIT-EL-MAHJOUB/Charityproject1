package org.example.charityproject1.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Document(collection = "utilisateurs")
public class Utilisateurs {

    @Id
    private String userId;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @JsonIgnore // Hide password in API responses
    private String password;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9]{10}$", message = "Le téléphone doit contenir 10 chiffres")
    private String telephone;

    @NotBlank(message = "La localisation est obligatoire")
    private String localisation;

    private List<Don> historiqueDons;

    private String logoPath;
    private List<String> likedActions;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public List<Don> getHistoriqueDons() {
        return historiqueDons;
    }

    public void setHistoriqueDons(List<Don> historiqueDons) {
        this.historiqueDons = historiqueDons;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public List<String> getLikedActions() {
        return likedActions;
    }

    public void setLikedActions(List<String> likedActions) {
        this.likedActions = likedActions;
    }

    // Méthode pour ajouter une action aimée
    public void ajouterActionAimee(String actionId) {
        if (!this.likedActions.contains(actionId)) {
            this.likedActions.add(actionId);
        }
    }

    // Méthode pour supprimer une action aimée
    public void supprimerActionAimee(String actionId) {
        this.likedActions.remove(actionId);
    }
}