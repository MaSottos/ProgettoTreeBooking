package it.corsobackendtree.treebooking.DAO.entities;

import it.corsobackendtree.treebooking.Gender;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class UserDAO {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String username;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Gender gender;
    private String password;

    private Integer salt;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<EventDAO> events = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BookingDAO> userReservations = new ArrayList<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CookieAuthDAO cookieAuthDAO;

    //constructors
    public UserDAO() {
    }
    public UserDAO(String username, String password, String name, String surname, Gender gender, LocalDate birthDate, Integer salt) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.password = password;
        this.salt=salt;
    }

    //getters
    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public Gender getGender() {
        return gender;
    }
    public String getPassword() {
        return password;
    }
    public List<EventDAO> getEvents() {
        return events;
    }
    public List<BookingDAO> getUserReservations() {
        return userReservations;
    }
    public CookieAuthDAO getCookieAuthDAO() {
        return cookieAuthDAO;
    }
    public Integer getSalt() {
        return salt;
    }
    //setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setCookieAuthDAO(CookieAuthDAO cookieAuthDAO) {
        this.cookieAuthDAO = cookieAuthDAO;
    }
    public void setSalt(Integer salt) { this.salt = salt;}

    //hashcode&equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDAO userDAO = (UserDAO) o;
        return Objects.equals(id, userDAO.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
