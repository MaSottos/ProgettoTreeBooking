package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepo extends CrudRepository<EventDAO, UUID> {
    @Query("SELECT e FROM EventDAO e WHERE e.capacity-e.eventReservations.size > 0 AND e.datetime > ?1")
    List<EventDAO> findByCapacityGreaterThanZero(LocalDateTime now);

    List<EventDAO> findByDatetimeAfter(LocalDateTime now);
}
