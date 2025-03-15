package org.example.charityproject1.repository;

import org.example.charityproject1.model.Categorie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategorieRepository extends MongoRepository<Categorie, String> {
    // Find category by name (case-insensitive search)
    Categorie findByNom(String nom);
}