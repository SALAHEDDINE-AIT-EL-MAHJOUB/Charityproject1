package org.example.charityproject1.repository;

import org.example.charityproject1.model.SuperAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuperAdminRepository extends MongoRepository<SuperAdmin, String> {
    // Find super admin by email
    SuperAdmin findByEmail(String email);
}