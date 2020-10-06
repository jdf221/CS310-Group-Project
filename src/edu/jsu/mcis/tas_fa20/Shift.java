package edu.jsu.mcis.tas_fa20;

public class Shift {
    public Shift(int id, String name, String startTime, String endTime, int interval, int gracePeriod, int dock, String lunchStartTime, String lunchEndTime, int lunchDeduct) {
        // Check pages 6 and 7 of TeamProjectOverview.pdf to learn what all of the variables mean
    }

    @Override
    public String toString() {
        // You need to return a string of the following format: "Shift Name: startTime - endTime (x minutes); Lunch: start - end (x minutes)"
        // Example: "Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)"
        return "";
    }
}
