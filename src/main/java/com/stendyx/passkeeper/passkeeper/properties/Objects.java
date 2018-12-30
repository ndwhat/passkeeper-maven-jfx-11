package com.stendyx.passkeeper.passkeeper.properties;

import java.io.File;

public enum Objects {
    USERS(new File("conf/users.ser")),
    WORKSPACES(new File("conf/workspaces.ser"));


    File file;

    Objects(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
