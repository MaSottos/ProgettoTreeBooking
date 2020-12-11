package it.corsobackendtree.treebooking.views;

import it.corsobackendtree.treebooking.Gender;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UserViewNoPsw {
    public static ZoneId defaultZoneId = ZoneId.systemDefault();
    private final String username;
    private final String name;
    private final String surname;
    private final Date birthDate;
    private final Gender gender;

    //Constructor
    public UserViewNoPsw(String username, String name, String surname, LocalDate birthDate, Gender gender) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = Date.from(birthDate.atStartOfDay(defaultZoneId).toInstant());;
        this.gender = gender;
    }

    //Getters
    public String getUsername() {
        return username;
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
