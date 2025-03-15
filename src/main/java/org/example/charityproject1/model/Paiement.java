package org.example.charityproject1.model;


import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "paiements")
public class Paiement {

    @Id
    private String idPaiement; // Utilisation de String pour la compatibilité avec MongoDB

    @NotBlank(message = "La méthode de paiement est obligatoire")
    private String methode; // Exemple : "PayPal", "Stripe"

    @NotBlank(message = "Le statut du paiement est obligatoire")
    private String statut; // Exemple : "En attente", "Complété", "Échoué"

    @Positive(message = "Le montant doit être positif")
    private float montant;

    @NotBlank(message = "L'ID de la transaction est obligatoire")
    private String transactionId; // ID de transaction fourni par PayPal/Stripe

    // Getters et setters
    public String getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(String idPaiement) {
        this.idPaiement = idPaiement;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    // Méthode pour traiter un paiement (à implémenter avec PayPal/Stripe)
    public void traiterPaiement() {
        // Logique pour traiter le paiement via PayPal/Stripe
        // Exemple : appeler l'API PayPal/Stripe pour effectuer le paiement
    }

    // Méthode pour vérifier le statut d'un paiement
    public void verifierPaiement() {
        // Logique pour vérifier le statut du paiement via PayPal/Stripe
        // Exemple : appeler l'API PayPal/Stripe pour vérifier le statut
    }
}