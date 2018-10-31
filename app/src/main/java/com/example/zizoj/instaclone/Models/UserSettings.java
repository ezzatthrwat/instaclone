package com.example.zizoj.instaclone.Models;

public class UserSettings {

    private Users user;
    private UsersAccountSettings settings;

    public UserSettings(Users user, UsersAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }

    public UserSettings() {

    }


    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UsersAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(UsersAccountSettings settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", settings=" + settings +
                '}';
    }
}
