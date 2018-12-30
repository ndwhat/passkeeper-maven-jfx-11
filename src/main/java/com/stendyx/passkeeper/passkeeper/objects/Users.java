package com.stendyx.passkeeper.passkeeper.objects;

import com.stendyx.passkeeper.passkeeper.libs.Methods;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.RegisterValidator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Users extends IOObjects<HashMap<String, User>> implements Serializable {


    //All users
    private HashMap<String, User> Users = new HashMap<>();


    public Users() {
        super(com.stendyx.passkeeper.passkeeper.properties.Objects.USERS.getFile());
        if (isObjectsExists()) {
            Users = getObjects();
        }
    }

    public User getUser(String login) {
        if (isLoginExists(login)) {
            return Users.get(login);
        }

        return null;
    }


    public boolean isLoginExists(String login) {
        return Users.containsKey(login);
    }


    public void deleteUser(String login) {
        if (isLoginExists(login)) {
            Users.remove(login);
            writeToFile(Users);
        }
    }

    public Validator addUser(String login, String password, String name) {

        RegisterValidator registerValidator = new RegisterValidator();
        Validator status = registerValidator.validate(login);

        if (status.isError())
            return status;

        String newPassword = Methods.hashString(password);
        Users.put(login, new User(name, newPassword));
        writeToFile(Users);
        return Validator.SUCCESS_ADD;
    }

    public void updateUser(String login, String password, String name) {

        if (isLoginExists(login)) {
            Users.put(login, new User(name, (password)));
            writeToFile(Users);
        }
        else {
            Logger.getGlobal().log(Level.WARNING, "User with login: " + login + " not found");
        }
    }

    public Validator authorize(String login, String password) {
        String newPassword = Methods.hashString(password);

        if (isLoginExists(login)) {
            if (Users.get(login).getPassword().equals(newPassword)) {
                return Validator.SUCCESS_REGISTER;
            }
        }
        return Validator.ERROR_LOGIN;

    }
}

