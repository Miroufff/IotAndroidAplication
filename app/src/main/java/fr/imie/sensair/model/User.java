package fr.imie.sensair.model;

import java.io.Serializable;

/**
 * Created by mirouf on 11/04/17.
 */

public class User implements Serializable
{
    private String firstname;
    private String lastname;
    private String login;
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;

        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;

        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;

        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;

        return this;
    }
}
