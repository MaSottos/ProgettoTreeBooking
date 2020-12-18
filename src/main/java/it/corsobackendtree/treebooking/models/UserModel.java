package it.corsobackendtree.treebooking.models;

import it.corsobackendtree.treebooking.Gender;
import java.time.LocalDate;

public class UserModel {
    private final String username;
    private final String password;
    private final String name;
    private final String surname;
    private final Gender gender;
    private final LocalDate birthDate;
    private final Integer salt;

    //Constructor
    public UserModel(String username, String password, String name, String surname, Gender gender, LocalDate birthDate, Integer salt) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.salt = salt;
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
    public Integer getSalt() {
        return salt;
    }
}
