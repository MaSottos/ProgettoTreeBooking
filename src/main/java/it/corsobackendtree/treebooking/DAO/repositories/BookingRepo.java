package it.corsobackendtree.treebooking.DAO.repositories;

import it.corsobackendtree.treebooking.DAO.BookingId;
import it.corsobackendtree.treebooking.DAO.entities.BookingDAO;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepo extends CrudRepository<BookingDAO, BookingId> {

}
