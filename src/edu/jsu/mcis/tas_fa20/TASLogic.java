package edu.jsu.mcis.tas_fa20;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TASLogic {
    public static int calculateTotalMinutes(ArrayList<Punch> punchList, Shift shift) {
        int totalMinutes = 0;
        int punchCount = 0;
        GregorianCalendar lastInstant = null;

        for (Punch punch : punchList) {
            if (punch.type == Punch.PunchType.ClockIn) {
                punchCount++;
                lastInstant = punch.getAdjustedInstant();
            } else if (punch.type == Punch.PunchType.ClockOut) {
                punchCount++;
                if (lastInstant != null) {
                    totalMinutes += TimeUnit.MINUTES.convert(Math.abs(punch.getAdjustedInstant().getTimeInMillis() - lastInstant.getTimeInMillis()), TimeUnit.MILLISECONDS);
                }
            }
        }

        if (punchCount == 2 && totalMinutes >= shift.shiftDuration) {
            totalMinutes -= shift.lunchDuration;
        }

        return totalMinutes;
    }
}
