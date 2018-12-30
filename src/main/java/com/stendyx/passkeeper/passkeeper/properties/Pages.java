package com.stendyx.passkeeper.passkeeper.properties;


public enum Pages {
    REGISTER("register", "PassKeeper", false),
    MAIN("main", "PassKeeper", false),
    USERSETTINGS("userSettings", "PassKeeper", true),
    USERDELETE("userSettingsDelete", "PassKeeper", true),
    ADDPROJECT("addProject", "PassKeeper", true),
    LOGIN("login", "PassKeeper", false);


    private String page;
    private String title;
    private boolean isPopup;

    Pages(String page, String title, boolean isPopup) {
        this.page = page;
        this.title = title;
        this.isPopup = isPopup;
    }

    public String getPage() {
        return isPopup ? String.format("/UI/popup/%s.fxml", page) : String.format("/UI/%s.fxml", page);
    }

    public String getTitle() {
        return title;
    }

    public boolean isPopup() {
        return isPopup;
    }
}
