package com.stendyx.passkeeper.passkeeper.properties;


public enum Validator {
    // successes
    SUCCESS_REGISTER(0, "Successfully registered", false),
    SUCCESS_DELETE(1, "Succesfully deleted", false),
    SUCCESS_ADD(2, "Succesfully added", false),
    SUCCESS_UPDATE(3, "Succesfully updated", false),
    SUCCESS_COPIED(11, "Succesfully copied", false),
    SUCCESS_CHANGE(4, "Succesfully changed", false),

    // errors
    ERROR_LOGIN_ALREADY_CREATED(5, "Login is already created", true),
    ERROR_LOGIN(6, "Uncorrect data", true),
    ERROR_ALREADY_EXISTS(7, "Data is already exists", true),
    ERROR_DIFFERENT_PASS(9, "Passwords are different", true),
    ERROR_NO_DATA(10, "Data is empty", true),
    ;

    private int messageID;
    private String message;
    private boolean isError;


    public int getMessageID() {
        return messageID;
    }

    Validator(int messageID, String message, boolean isError) {
        this.messageID = messageID;
        this.message = message;
        this.isError = isError;

    }

    public boolean isError() {
        return isError;
    }

    public String getMessage() {
        return message;
    }


}

