package edu.jsu.mcis.tas_fa20;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

public class Punch {
    enum PunchType {
        ClockOut,
        ClockIn,
        TimeOut;

        static public PunchType getFromTypeId(int punchTypeId) {
            switch (punchTypeId) {
                case 0:
                    return PunchType.ClockOut;
                case 1:
                    return PunchType.ClockIn;
                default:
                    return PunchType.TimeOut;
            }
        }

        public int getRawId() {
            switch (this) {
                case ClockOut:
                    return 0;
                case ClockIn:
                    return 1;
                default:
                    return 2;
            }
        }

        @Override
        public String toString() {
            switch (this) {
                case ClockOut:
                    return "CLOCKED OUT";
                case ClockIn:
                    return "CLOCKED IN";
                default:
                    return "TIMED OUT";
            }
        }
    }

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("E MM/dd/y HH:mm:ss");

    private final Badge badge;
    private final int terminalId;
    public final PunchType type;

    private final GregorianCalendar originalInstant = new GregorianCalendar();
    private final GregorianCalendar adjustedInstant = new GregorianCalendar();

    private int punchId = -1;

    private String adjustmentType = "None";

    public Punch(Badge badge, int terminalId, int punchTypeId) {
        this.badge = badge;
        this.terminalId = terminalId;
        this.type = PunchType.getFromTypeId(punchTypeId);
    }

    public int getId() {
        return this.punchId;
    }

    public void setId(int id) {
        this.punchId = id;
    }

    public String getBadgeid() {
        return this.badge.getBadgeId();
    }

    public int getTerminalid() {
        return this.terminalId;
    }

    public int getPunchtypeid() {
        return this.type.getRawId();
    }

    public String getAdjustmentType() {
        return this.adjustmentType;
    }

    public void adjust(Shift shift) {
        this.adjustedInstant.setTimeInMillis(originalInstant.getTimeInMillis());
        this.adjustedInstant.set(GregorianCalendar.SECOND, shift.startTime.getSecond());

        LocalTime punchTime = LocalTime.from(originalInstant.toInstant().atZone(ZoneId.systemDefault()));

        if (this.type != PunchType.TimeOut) {
            if (this.originalInstant.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY && this.originalInstant.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {
                if (this.type == PunchType.ClockIn) {
                    LocalTime earlyShiftStart = shift.startTime.minusMinutes(shift.interval);
                    LocalTime lateShiftStart = shift.startTime.plusMinutes(shift.gracePeriod);
                    LocalTime dockedShiftStart = shift.startTime.plusMinutes(shift.dock);

                    if (punchTime.isAfter(earlyShiftStart) && punchTime.isBefore(shift.startTime)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, shift.startTime.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, shift.startTime.getMinute());

                        this.adjustmentType = "Shift Start";
                    } else if (punchTime.isAfter(shift.lunchStartTime) && punchTime.isBefore(shift.lunchEndTime)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, shift.lunchEndTime.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, shift.lunchEndTime.getMinute());

                        this.adjustmentType = "Lunch Stop";
                    } else if (punchTime.isAfter(shift.startTime) && punchTime.isBefore(lateShiftStart)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, shift.startTime.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, shift.startTime.getMinute());

                        this.adjustmentType = "Shift Start";
                    } else if (punchTime.equals(dockedShiftStart) || punchTime.isAfter(lateShiftStart) && punchTime.isBefore(dockedShiftStart)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, dockedShiftStart.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, dockedShiftStart.getMinute());

                        this.adjustmentType = "Shift Dock";
                    }
                } else if (this.type == PunchType.ClockOut) {
                    LocalTime lateShiftEnd = shift.endTime.plusMinutes(shift.interval);
                    LocalTime earlyShiftEnd = shift.endTime.minusMinutes(shift.gracePeriod);
                    LocalTime dockedShiftEnd = shift.endTime.minusMinutes(shift.dock);

                    if (punchTime.isBefore(lateShiftEnd) && punchTime.isAfter(shift.endTime)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, shift.endTime.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, shift.endTime.getMinute());

                        this.adjustmentType = "Shift Stop";
                    } else if (punchTime.isAfter(shift.lunchStartTime) && punchTime.isBefore(shift.lunchEndTime)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, shift.lunchStartTime.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, shift.lunchStartTime.getMinute());

                        this.adjustmentType = "Lunch Start";
                    } else if (punchTime.isBefore(shift.endTime) && punchTime.isAfter(earlyShiftEnd)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, shift.endTime.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, shift.endTime.getMinute());

                        this.adjustmentType = "Shift Stop";
                    } else if (punchTime.equals(dockedShiftEnd) || punchTime.isAfter(dockedShiftEnd) && punchTime.isBefore(earlyShiftEnd)) {
                        this.adjustedInstant.set(GregorianCalendar.HOUR_OF_DAY, dockedShiftEnd.getHour());
                        this.adjustedInstant.set(GregorianCalendar.MINUTE, dockedShiftEnd.getMinute());

                        this.adjustmentType = "Shift Dock";
                    }
                }
            }

            if (this.adjustmentType.equals("None")) {
                int min = punchTime.getMinute();
                int mode = min % shift.interval;
                if (mode >= shift.interval / 2) {
                    min = shift.interval - mode;
                } else {
                    min = -mode;
                }

                if (min != 0) {
                    adjustedInstant.add(GregorianCalendar.MINUTE, min);
                    this.adjustmentType = "Interval Round";
                }
            }
        }
    }

    public String printOriginalTimestamp() {
        return "#" + this.getBadgeid() + " " + this.type + ": " + dateFormat.format(this.originalInstant.getTime()).toUpperCase();
    }

    public String printAdjustedTimestamp() {
        return "#" + this.getBadgeid() + " " + this.type + ": " + dateFormat.format(this.adjustedInstant.getTime()).toUpperCase() + " (" + this.adjustmentType + ")";
    }

    public GregorianCalendar getAdjustedInstant() {
        return this.adjustedInstant;
    }

    public long getOriginaltimestamp() {
        return this.originalInstant.getTimeInMillis();
    }

    public void setOriginaltimestamp(long timestampMillis) {
        this.originalInstant.setTimeInMillis(timestampMillis);
    }
}
