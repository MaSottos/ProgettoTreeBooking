package it.corsobackendtree.treebooking.views;

import it.corsobackendtree.treebooking.Gender;

import java.time.LocalDate;

public class UserViewNoPsw {

    private final String username;
    private final String name;
    private final String surname;
    private final LocalDate birthDate;
    private final Gender gender;

    //Constructor
    public UserViewNoPsw(String username, String name, String surname, LocalDate birthDate, Gender gender) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
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
    public LocalDate getBirthDate() {
        return birthDate;
    }
}
