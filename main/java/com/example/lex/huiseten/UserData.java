package com.example.lex.huiseten;

/**
 * Created by lex on 3/27/2017.
 */

public class UserData {

    public String email = "";
    public String password = "";
    public String username = "";
    public Boolean isnew = false;

    public void UserData(){}

    // set email
    public void setEmail(String email) {
        this.email = email;
    }

    // set password
    public void setPassword(String password) {
        this.password = password;
    }

    // set username
    public void setUsername(String username) {
        this.username = username;
    }

    // set if the user is new
    public void setIsnew(Boolean isnew) {
        this.isnew = isnew;
    }

    // get email
    public String getEmail() {
        return email;
    }

    // get password
    public String getPassword() {
        return password;
    }

    // get username
    public String getUsername() {
        return username;
    }

    // get if the user is new
    public Boolean getIsnew() {
        return isnew;
    }
}
