package org.example.charityproject1.repository;

import org.example.charityproject1.model.ActionCharite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActionChariteRepository extends MongoRepository<ActionCharite, String> {
    // Find actions by category ID
    List<ActionCharite> findByCategorieId(String categorieId);

    // Find actions by title (case-insensitive search)
    List<ActionCharite> findByTitre(String titre);

    // Récupérer les actions d’une organisation
    List<ActionCharite> findByOrganisationId(String organisationId);


}