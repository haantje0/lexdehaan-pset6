package com.example.lex.huiseten;

/**
 * Created by lex on 3/20/2017.
 */

public class EatData {

    public String username;
    public Boolean eating;
    public Boolean hasComments = false;
    public String comments = "";

    // firebase constructor
    public EatData(){}

    // constructor
    public EatData(String Username, Boolean Eating) {
        username = Username;
        eating = Eating;
    }

    // constructor
    public EatData(String Username, Boolean Eating, String Comments) {
        username = Username;
        eating = Eating;
        hasComments = true;
        comments = Comments;
    }

    // get eating
    public Boolean getEating() {
        return eating;
    }

    // get has comments
    public Boolean getHasComments() {
        return hasComments;
    }

    // get username
    public String getUsername() {
        return username;
    }

    // get comments
    public String getComments() {
        return comments;
    }
}
