package edu.jsu.mcis.tas_fa20;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Absenteeism {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-y");

    public final String badgeId;
    public final GregorianCalendar payPeriodStart;
    public final double percentage;

    public Absenteeism(String badgeId, long payPeriodStartTimestamp, double percentage) {
        this.badgeId = badgeId;
        this.payPeriodStart = new GregorianCalendar();
        this.payPeriodStart.setTimeInMillis(payPeriodStartTimestamp);
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "#" + this.badgeId + " (Pay Period Starting " + dateFormat.format(this.payPeriodStart.getTime()) + "): " + new DecimalFormat("#.00").format(this.percentage) + "%";
    }
}
