package com.ift2905.chat;

import java.io.Serializable;

/**
 * Created by Antoine on 16-04-05.
 */

public class User implements Serializable {

    private String username;


    public User(String username){

        this.username = username;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
}