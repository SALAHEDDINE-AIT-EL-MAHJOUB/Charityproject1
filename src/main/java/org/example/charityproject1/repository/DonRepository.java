package org.example.charityproject1.repository;

import org.example.charityproject1.model.Don;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DonRepository extends MongoRepository<Don, String> {
    // Find all donations by user ID
    List<Don> findByUtilisateurId(String utilisateurId);

    // Find all donations for a specific action
    List<Don> findByActionId(String actionId);
}