package com.hackathon.inout.middleman.classes;


/**
 * Created by Danish on 13-Aug-16.
 */

public class Message {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    int user;
    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Message(String message, int user) {
        this.message = message;
        this.user = user;
    }
}
