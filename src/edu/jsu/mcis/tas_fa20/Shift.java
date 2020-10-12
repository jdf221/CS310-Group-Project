package edu.jsu.mcis.tas_fa20;

public class Shift
{
    private int ID;
    private String Name;
    private String Start;
    private String End;
    private int I;
    private int Grace;
    private int D;
    private String LStart;
    private String LEnd;
    private int LD;
    private int Time;
    private int STime;
    private int ETime;
    private int LSTime;
    private int LETime;

    public Shift(int id, String name, String startTime, String endTime, int interval, int gracePeriod, int dock, String lunchStartTime, String lunchEndTime, int lunchDeduct)
    {
        // Check pages 6 and 7 of TeamProjectOverview.pdf to learn what all of the variables mean
        ID = id;
        Name = name;
        Start = startTime.substring(0, 5);
        End = endTime.substring(0, 5);
        I = interval;
        Grace = gracePeriod;
        D = dock;
        LStart = lunchStartTime.substring(0, 5);
        LEnd = lunchEndTime.substring(0, 5);
        LD = lunchDeduct;
        Time = 24;
        STime = 720;
        ETime = 1230;
        LSTime = 990;
        LETime = 1020;



    }


    @Override
    public String toString()
    {
        // You need to return a string of the following format: "Shift Name: startTime - endTime (x minutes); Lunch: start - end (x minutes)"
        // Example: "Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)"

        return Name + ":" + " " + Start + " " + "-" + " " + End + " " + "(" + (ETime - STime) + " " + "minutes" + ")" + ";" + " " + "Lunch" + ":" + " " + LStart +
                " " + "-" + " " + LEnd + " " + "(" + (LETime - LSTime) + " " + "minutes" + ")";
    }
}