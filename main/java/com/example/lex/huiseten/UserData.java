package com.example.lex.huiseten;

/**
 * Created by lex on 3/27/2017.
 */

public class UserData {

    public String email = "";
    public String password = "";
    public String username = "";

    public void UserData(){}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
