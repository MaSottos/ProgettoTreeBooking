package it.corsobackendtree.treebooking.DAO;

import it.corsobackendtree.treebooking.DAO.entities.EventDAO;
import it.corsobackendtree.treebooking.DAO.entities.UserDAO;

import java.io.Serializable;
import java.util.Objects;

public class BookingId implements Serializable {
    private UserDAO user;
    private EventDAO event;

    //constructor
    public BookingId(UserDAO user, EventDAO event) {
        this.user = user;
        this.event = event;
    }
    public BookingId() {
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingId bookingId = (BookingId) o;
        return Objects.equals(user, bookingId.user) &&
                Objects.equals(event, bookingId.event);
    }
    @Override
    public int hashCode() {
        return Objects.hash(user, event);
    }
}
