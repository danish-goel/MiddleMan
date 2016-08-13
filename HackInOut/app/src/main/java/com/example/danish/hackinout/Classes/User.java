package com.example.danish.hackinout.Classes;



/**
 * Created by Danish on 13-Aug-16.
 */

public class User {
    String name;
    String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public  User(String name,String email)
    {
        this.name=name;
        this.email=email;
    }
}
