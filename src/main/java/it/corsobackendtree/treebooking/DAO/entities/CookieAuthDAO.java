package it.corsobackendtree.treebooking.DAO.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class CookieAuthDAO {
    @Id
    @Column(name = "user_id")
    private UUID id;

    private String cookieauth;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserDAO user;

    public CookieAuthDAO() {
    }

    public CookieAuthDAO(String cookieauth, UserDAO user) {
        this.cookieauth = cookieauth;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }
    public String getCookieauth() {
        return cookieauth;
    }
    public UserDAO getUser() {
        return user;
    }

    public void setCookieauth(String cookieauth) {
        this.cookieauth = cookieauth;
    }
    public void setUser(UserDAO user) {
        this.user = user;
    }
}
