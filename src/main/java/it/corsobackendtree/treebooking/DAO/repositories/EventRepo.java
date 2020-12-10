package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepo extends CrudRepository<EventDAO, UUID> {

    public List<EventDAO> findByCapacityGreaterThan(Integer intero);

}
