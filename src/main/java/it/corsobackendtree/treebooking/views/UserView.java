package it.corsobackendtree.treebooking.views;

import it.corsobackendtree.treebooking.Gender;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UserView {
    public static ZoneId defaultZoneId = ZoneId.systemDefault();
    private final String username;
    private final String name;
    private final String surname;
    private final Date birthDate;
    private final Gender gender;
    private final String password;

    //Constructor
    public UserView(String username, String name, String surname, LocalDate birthDate, Gender gender, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = Date.from(birthDate.atStartOfDay(defaultZoneId).toInstant());
        this.gender = gender;
        this.password = password;
    }

    //Getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Gender getGender() {
        return gender;
    }
    public Date getBirthDate() {
        return birthDate;
    }
}
