package org.example.charityproject1.repository;

import org.example.charityproject1.model.SuperAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SuperAdminRepository extends MongoRepository<SuperAdmin, String> {
    // Find super admin by email
    Optional<SuperAdmin> findByEmail(String email);
}