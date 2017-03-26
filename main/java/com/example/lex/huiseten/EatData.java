package com.example.lex.huiseten;

/**
 * Created by lex on 3/20/2017.
 */

public class EatData {

    public String username;
    public Boolean eating;
    public Boolean hasComments;
    public String comments;

    public EatData(){}

    public EatData(String Username, Boolean Eating,Boolean HasComments, String Comments) {
        username = Username;
        eating = Eating;
        hasComments = HasComments;
        comments = Comments;
    }

    public Boolean getEating() {
        return eating;
    }

    public Boolean getHasComments() {
        return hasComments;
    }

    public String getUsername() {
        return username;
    }

    public String getComments() {
        return comments;
    }
}