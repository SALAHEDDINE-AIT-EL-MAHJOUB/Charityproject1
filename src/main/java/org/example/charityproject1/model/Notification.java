package org.example.charityproject1.model;


import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String idNotification;

    @NotBlank(message = "L'ID de l'utilisateur est obligatoire")
    private String utilisateurId;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    @NotNull(message = "La date est obligatoire")
    private Date date;

    // Getters et setters
    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}