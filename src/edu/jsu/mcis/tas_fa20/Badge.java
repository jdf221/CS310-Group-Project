package edu.jsu.mcis.tas_fa20;

public class Badge {

    // You should create any private variables you need and assign them from the class constructor
    String id;
    String employeeName;

    public Badge(String badgeId, String employeeName) {
        this.id = badgeId;
        this.employeeName = employeeName;
    }

    public String getBadgeId() {
        // Here you need to return the badgeId
        return this.id;
    }

    @Override
    public String toString() {
        // You need to return a string of the following format: "#badgeId (employeeName)"
        return "#" + this.id + " (" + this.employeeName + ")";
    }
}
