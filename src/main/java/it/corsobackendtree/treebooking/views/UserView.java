package it.corsobackendtree.treebooking.views;

import java.time.LocalDate;

public class UserView {
    public enum Gender {
        MALE,FEMALE,OTHER;
    }

    private final String username;
    private final String password;
    private final String name;
    private final String surname;
    private final Gender gender;
    private final LocalDate birthDate;

    //Constructor
    public UserView(String username, String password, String name, String surname, Gender gender, LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
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
    public LocalDate getBirthDate() {
        return birthDate;
    }
}
