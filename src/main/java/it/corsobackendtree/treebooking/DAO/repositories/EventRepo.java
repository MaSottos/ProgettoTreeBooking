package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventRepo extends CrudRepository<EventDAO, UUID> {

}
