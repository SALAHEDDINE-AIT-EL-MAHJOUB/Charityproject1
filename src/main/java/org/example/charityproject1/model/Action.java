package org.example.charityproject1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "actions")
public class Action {
    @Id
    private String id;
    private String titre;
    private String description;
    private LocalDate date;
    private String lieu;
    private int objectifCollecte;
    private String organisationId;
    // Add these missing fields
    private float montantActuel;
    private int nombreParticipants;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getObjectifCollecte() {
        return objectifCollecte;
    }

    public void setObjectifCollecte(int objectifCollecte) {
        this.objectifCollecte = objectifCollecte;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    // Add getters and setters for new fields
    public float getMontantActuel() {
        return montantActuel;
    }

    public void setMontantActuel(float montantActuel) {
        this.montantActuel = montantActuel;
    }

    public int getNombreParticipants() {
        return nombreParticipants;
    }

    public void setNombreParticipants(int nombreParticipants) {
        this.nombreParticipants = nombreParticipants;
    }
}