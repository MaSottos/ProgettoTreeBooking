package it.corsobackendtree.treebooking.DAO.entities;

import javax.persistence.*;

@Entity
public class CookieAuthDAO {
=======
import javax.persistence.*;

    @Id
    @Column(name = "user_id")
    private Long id;

    private String cookieAuth;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserDAO user;

    public CookieAuthDAO() {
    }

    public CookieAuthDAO(String cookieAuth, UserDAO user) {
        this.cookieAuth = cookieAuth;
        this.user = user;
    }

    public String getCookieAuth() {
        return cookieAuth;
    }

    public UserDAO getUser() {
        return user;
    }
}
