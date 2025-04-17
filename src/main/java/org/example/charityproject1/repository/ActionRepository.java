package org.example.charityproject1.repository;

import org.example.charityproject1.model.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActionRepository extends MongoRepository<Action, String> {
    List<Action> findByOrganisationId(String organisationId);
}