package com.example.proburok;

public class User {

    private String firstname;
    private String lastname;
    private String password;
    private String username;
    private String location;


    public User(String firstname, String lastname, String password, String username, String location) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.username = username;
        this.location = location;

    }

    public User() {

    }

    public String getFirstname() {
        return firstname; //получает значение
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;// Устанавливает новое значение для firstname
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
