package org.example.charityproject1.repository;

import org.example.charityproject1.model.Organisations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrganisationsRepository extends MongoRepository<Organisations, String> {
    // Find organisations by name (case-insensitive search)
    List<Organisations> findByNom(String nom);

    // Find organisations by validation status
    List<Organisations> findByValideParAdmin(boolean valideParAdmin);

    // 🔹 Find organizations that have created charity actions
    @Query("{ 'charityActions' : { $exists: true, $not: { $size: 0 } } }")
    List<Organisations> findOrganizationsWithCharityActions();


}
