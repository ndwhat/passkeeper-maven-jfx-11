package com.stendyx.passkeeper.passkeeper.objects.workspace;

import java.io.Serializable;

public class Project implements Serializable {


    String projectName;
    String protocol;
    String host;
    String port;
    String userName;
    String userPassword;


    public Project(String projectName, String protocol, String host, String port, String userName, String userPassword) {

        this.projectName = projectName;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public void setProject(Project project) {

        this.projectName = project.getProjectName();
        this.protocol = project.getProtocol();
        this.host = project.getHost();
        this.port = project.getPort();
        this.userName = project.getUserName();
        this.userPassword = project.getUserPassword();

    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
