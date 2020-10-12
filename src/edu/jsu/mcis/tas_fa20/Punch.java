package edu.jsu.mcis.tas_fa20;

import java.text.SimpleDateFormat;
import java.util.*;

public class Punch {
    private Badge Badge;
    private String adjustmentType = null;
    private int PunchID = -1;
    private int TerminalID = -1;
    private int punchTypeID = -1;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    private GregorianCalendar gcal;
    private Date date;

    public Punch(Badge badge, int termId, int punchTId) {
        // Punch IDs are assigned by the database so you need to set the punch ID to a default value (I recommend -1) then we can use setId() to set the real ID
        // punchTypeId can be 0, 1, or 2.
        // 0 = clocked in, 1 = clocked out, 2 = timed out
        Badge = badge;
        TerminalID = termId;
        punchTypeID = punchTId;
        gcal = new GregorianCalendar();
        date = new Date();
        // You should also set the originalTimestamp to the current timestamp in this constructor
    }

    // You should update all of the below functions to do what they say they do
    // You will need to create various private variables inside this class
    public int getId() {
        return PunchID;
    }

    public void setId(int id) {
        // The punch ID is only ever assigned after the class is created.
        // So you will need to initialize the variable with a default value of `-1`
        PunchID = id;
    }

    public String getBadgeid() {
        return this.Badge.getBadgeId();
    }

    public int getTerminalid() {
        return TerminalID;
    }

    public int getPunchtypeid() {
        return punchTypeID;
    }

    public String printOriginalTimestamp() {
        // Should return a string of the following format
        // "#badgeId CLOCKED IN: WED 09/05/2018 07:00:07"
        // Look into the GregorianCalendar class

        String clock; // 0 = clocked in, 1 = clocked out, 2 = timed out
        if (punchTypeID == 0) { clock = " CLOCKED IN: "; }
        else if (punchTypeID == 1) { clock = " CLOCKED OUT: "; }
        else { clock = " TIMED OUT: "; }
        System.out.println(gcal.getTime().getTime());
        return ("#" + getBadgeid() + clock + dowFormat(gcal) + " " + formatter.format(gcal.getTime().getTime()));
    }

    public String dowFormat (GregorianCalendar gc) {
        String result;
        if (gc.get(gc.DAY_OF_WEEK) == 1) { result = "SUN"; }
        else if (gc.get(gc.DAY_OF_WEEK) == 2) { result = "MON"; }
        else if (gc.get(gc.DAY_OF_WEEK) == 3) { result = "TUE"; }
        else if (gc.get(gc.DAY_OF_WEEK) == 4) { result = "WED"; }
        else if (gc.get(gc.DAY_OF_WEEK) == 5) { result = "THU"; }
        else if (gc.get(gc.DAY_OF_WEEK) == 6) { result = "FRI"; }
        else { result = "SAT"; }
        return result;
    }

    public long getOriginaltimestamp() {
        return gcal.getTimeInMillis();
    }

    public void setOriginaltimestamp(long l) {
        gcal.setTimeInMillis(l);
    }
}
