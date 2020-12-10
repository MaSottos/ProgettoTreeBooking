package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

public interface EventRepo extends CrudRepository<EventDAO, UUID> {
    List<EventDAO> findByCapacityGreaterThan(Integer intero);
}
