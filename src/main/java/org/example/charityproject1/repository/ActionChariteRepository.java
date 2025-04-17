package org.example.charityproject1.repository;

import org.example.charityproject1.model.ActionCharite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActionChariteRepository extends MongoRepository<ActionCharite, String> {
    List<ActionCharite> findAllByOrganisationId(String organisationId);
}