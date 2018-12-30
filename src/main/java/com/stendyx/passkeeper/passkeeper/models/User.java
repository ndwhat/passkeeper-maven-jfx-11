package com.stendyx.passkeeper.passkeeper.models;

public class User {

    private String login;
    private com.stendyx.passkeeper.passkeeper.objects.User user;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public com.stendyx.passkeeper.passkeeper.objects.User getUser() {
        return user;
    }

    public void setUser(com.stendyx.passkeeper.passkeeper.objects.User user) {
        this.user = user;
    }
}
