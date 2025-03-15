package org.example.charityproject1.repository;
import org.example.charityproject1.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    // Find notifications by user ID
    List<Notification> findByUtilisateurId(String utilisateurId);

    // Find notifications by date range
    List<Notification> findByDateBetween(Date startDate, Date endDate);
}