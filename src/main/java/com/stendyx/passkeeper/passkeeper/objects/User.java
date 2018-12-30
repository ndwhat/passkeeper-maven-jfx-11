package com.stendyx.passkeeper.passkeeper.objects;

import java.io.Serializable;

public class User implements Serializable {

    // Fields
    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
