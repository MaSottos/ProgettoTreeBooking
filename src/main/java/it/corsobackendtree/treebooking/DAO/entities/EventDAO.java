package it.corsobackendtree.treebooking.DAO.entities;

import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class EventDAO {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private LocalDateTime datetime;
    private String place;
    private Integer capacity;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserDAO owner;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BookingDAO> eventReservations = new ArrayList<>();

    //constructors
    public EventDAO(String name, LocalDateTime datetime, String place, Integer capacity, UserDAO owner) {
        this.name = name;
        this.datetime = datetime;
        this.place = place;
        this.capacity = capacity;
        this.owner = owner;
    }
    public EventDAO() {
    }

    //getters
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getDatetime() {
        return datetime;
    }
    public String getPlace() {
        return place;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public UserDAO getOwner() {
        return owner;
    }
    public List<BookingDAO> getEventReservations() {
        return eventReservations;
    }

    public Boolean getOwned(UserDAO user){
        return owner.equals(user);
    }

    public Boolean getJoined(UserDAO user){
        return eventReservations.stream().anyMatch(b->b.getUser().equals(user));
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDatetime(LocalDateTime date) {
        this.datetime = date;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    public void setOwner(UserDAO owner) {
        this.owner = owner;
    }
    public void addUserReservation(UserDAO user) { eventReservations.add(new BookingDAO(user, this)); }
    public void removeUserReservation(BookingDAO bookingDAO) { eventReservations.remove(bookingDAO); }

    //hashcode&equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDAO eventDAO = (EventDAO) o;
        return Objects.equals(id, eventDAO.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
