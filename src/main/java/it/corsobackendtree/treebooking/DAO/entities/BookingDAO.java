package it.corsobackendtree.treebooking.DAO.entities;

import it.corsobackendtree.treebooking.DAO.BookingId;

import javax.persistence.*;

@Entity
@IdClass(BookingId.class)
public class BookingDAO {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private UserDAO user;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private EventDAO event;

    //constructor
    public BookingDAO(UserDAO user, EventDAO event) {
        this.user = user;
        this.event = event;
    }
    public BookingDAO() {
    }

    //getters
    public UserDAO getUser() {
        return user;
    }
    public EventDAO getEvent() {
        return event;
    }
}
