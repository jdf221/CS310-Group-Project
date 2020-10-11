package edu.jsu.mcis.tas_fa20;

public class Badge {

    String badgeId;
    // You should create any private variables you need and assign them from the class constructor

    public Badge(String badgeId, String employeeName) {
this.badgeId = badgeId;
    }

    public String getBadgeId() {
        // Here you need to return the badgeId
        return this.badgeId;
    }

    @Override
    public String toString() {
        // You need to return a string of the following format: "#badgeId (employeeName)"
        return "";
    }
}
