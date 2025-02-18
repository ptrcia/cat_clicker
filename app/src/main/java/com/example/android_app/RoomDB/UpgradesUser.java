package com.example.android_app.RoomDB;

public class UpgradesUser {

    private int userLevel;
    private int userUpgrade;

    public UpgradesUser(int userUpgrade, int userLevel) {
        this.userUpgrade = userUpgrade;
        this.userLevel = userLevel;

    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserUpgrade() {
        return userUpgrade;
    }

    public void setUserUpgrade(int userUpgrade) {
        this.userUpgrade = userUpgrade;
    }
}
