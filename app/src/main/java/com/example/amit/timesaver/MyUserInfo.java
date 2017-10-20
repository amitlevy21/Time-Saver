package com.example.amit.timesaver;

import java.io.Serializable;

/**
 * Created by amit on 20/10/17.
 */

public class MyUserInfo implements Serializable{

    private String email;
    private String fullName;

    public MyUserInfo() {
    }

    public MyUserInfo(String email, String fullName) {

        this.email = email;
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
