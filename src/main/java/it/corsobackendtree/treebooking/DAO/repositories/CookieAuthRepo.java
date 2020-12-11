package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.entities.CookieAuthDAO;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface CookieAuthRepo extends CrudRepository<CookieAuthDAO, UUID> {
    Optional<CookieAuthDAO> findByCookieauth(String cookie);
}
