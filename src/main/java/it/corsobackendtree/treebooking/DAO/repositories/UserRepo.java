package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.entities.UserDAO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends CrudRepository<UserDAO, UUID>{
    Optional<UserDAO> findByUsername(String username);
}
