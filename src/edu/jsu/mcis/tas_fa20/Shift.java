package edu.jsu.mcis.tas_fa20;

import java.time.LocalTime;
import java.time.Duration;

public class Shift {
    public final int id;
    public final String name;
    public final LocalTime startTime;
    public final LocalTime endTime;
    public final int interval;
    public final int gracePeriod;
    public final int dock;
    public final LocalTime lunchStartTime;
    public final LocalTime lunchEndTime;
    public final int lunchDeduct;

    public final long shiftDuration;
    public final long lunchDuration;

    public Shift(int id, String name, String startTime, String endTime, int interval, int gracePeriod, int dock, String lunchStartTime, String lunchEndTime, int lunchDeduct) {
        // Check pages 6 and 7 of TeamProjectOverview.pdf to learn what all of the variables mean
        this.id = id;
        this.name = name;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        this.lunchDeduct = lunchDeduct;


        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.lunchStartTime = LocalTime.parse(lunchStartTime);
        this.lunchEndTime = LocalTime.parse(lunchEndTime);

        this.shiftDuration = Duration.between(this.startTime, this.endTime).toMinutes();
        this.lunchDuration = Duration.between(this.lunchStartTime, this.lunchEndTime).toMinutes();
    }


    @Override
    public String toString() {
        // You need to return a string of the following format: "Shift Name: startTime - endTime (x minutes); Lunch: start - end (x minutes)"
        // Example: "Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)"
        return name + ": " + startTime + " - " + endTime + " (" + this.shiftDuration + " minutes); Lunch: " + lunchStartTime + " - " + lunchEndTime + " (" + this.lunchDuration + " minutes)";
    }
}
