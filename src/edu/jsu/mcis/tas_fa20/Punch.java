package edu.jsu.mcis.tas_fa20;

public class Punch {
    private String adjustmentType = null;

    public Punch(Badge badge, int terminalId, int punchTypeId) {
        // punchTypeId can be 0, 1, or 2.
        // 0 = clocked in, 1 = clocked out, 2 = timed out

        // You should also set the originalTimestamp to the current timestamp in this constructor
    }

    // You should update all of the below functions to do what they say they do
    // You will need to create various private variables inside this class
    public int getId() {
        return 0;
    }

    public void setId(int id) {

    }


    public String getBadgeid() {
        return null;
    }

    public int getTerminalid() {
        return 0;
    }

    public int getPunchtypeid() {
        return 0;
    }

    public String printOriginalTimestamp() {
        // Should return a string of the following format
        // "#badgeId CLOCKED IN: WED 09/05/2018 07:00:07"
        // Look into the GregorianCalendar class

        return null;
    }

    public long getOriginaltimestamp() {
        return 0;
    }

    public void setOriginaltimestamp(long originalTimestamp) {

    }
}
