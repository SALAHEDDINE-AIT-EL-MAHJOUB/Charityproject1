package org.example.charityproject1.repository;

import org.example.charityproject1.model.Paiement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaiementRepository extends MongoRepository<Paiement, String> {
    // Find payments by status
    List<Paiement> findByStatut(String statut);

    // Find payments by transaction ID
    Paiement findByTransactionId(String transactionId);
}