package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TASDatabase {
    private Connection connection = null;

    public TASDatabase() {
        try {
            String server = ("jdbc:mysql://localhost/tas?serverTimezone=CST");
            String username = "tasuser";
            String password = "team";

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            this.connection = DriverManager.getConnection(server, username, password);
            if (!this.connection.isValid(0)) {
                System.err.println("Database connection failed");
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public Badge getBadge(String badgeId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT description FROM badge WHERE id=?");
            preparedStatement.setString(1, badgeId);
            ResultSet result = preparedStatement.executeQuery();

            result.next();
            return new Badge(badgeId, result.getString(1));
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public Punch getPunch(int punchId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM punch WHERE id=?");
            preparedStatement.setInt(1, punchId);
            ResultSet result = preparedStatement.executeQuery();

            result.next();
            Punch punch = new Punch(this.getBadge(result.getString(3)), result.getInt(2), result.getInt(5));
            punch.setOriginaltimestamp(result.getTimestamp(4).getTime() * 1000);
            punch.setId(result.getInt(1));

            return punch;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public Shift getShift(int shiftId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shift WHERE id=?");
            preparedStatement.setInt(1, shiftId);
            ResultSet result = preparedStatement.executeQuery();

            result.next();
            return new Shift(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5), result.getInt(6), result.getInt(7), result.getString(8), result.getString(9), result.getInt(10));
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public Shift getShift(Badge badge) {
        try {
            PreparedStatement selectShiftId = connection.prepareStatement("SELECT shiftid FROM employee WHERE badgeid=?");
            selectShiftId.setString(1, badge.getBadgeId());
            ResultSet shiftIdResult = selectShiftId.executeQuery();
            shiftIdResult.next();

            PreparedStatement selectShift = connection.prepareStatement("SELECT * FROM shift WHERE id=?");
            selectShift.setInt(1, shiftIdResult.getInt(1));
            ResultSet shiftResult = selectShift.executeQuery();
            shiftResult.next();

            return new Shift(shiftResult.getInt(1), shiftResult.getString(2), shiftResult.getString(3), shiftResult.getString(4), shiftResult.getInt(5), shiftResult.getInt(6), shiftResult.getInt(7), shiftResult.getString(8), shiftResult.getString(9), shiftResult.getInt(10));
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public int insertPunch(Punch punch) {
        try {
            PreparedStatement insertPunch = connection.prepareStatement("INSERT INTO punch VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            insertPunch.setInt(1, punch.getId());
            insertPunch.setInt(2, punch.getTerminalid());
            insertPunch.setString(3, punch.getBadgeid());
            insertPunch.setTimestamp(4, new Timestamp(punch.getOriginaltimestamp()));
            insertPunch.setInt(5, punch.getPunchtypeid());
            insertPunch.executeUpdate();

            ResultSet autoIncrementResult = insertPunch.getGeneratedKeys();
            autoIncrementResult.next();

            return autoIncrementResult.getInt(1);
        } catch (Exception e) {
            System.err.println(e.toString());
            return -1;
        }
    }

    public ArrayList<Punch> getDailyPunchList(Badge badge, long timestamp) {
        try {
            PreparedStatement selectPunchesOnDay = connection.prepareStatement("SELECT id FROM punch WHERE badgeid=? AND originaltimestamp BETWEEN ? AND ?");
            PreparedStatement selectFirstPunchOnNextDay = connection.prepareStatement("SELECT id, punchtypeid FROM punch WHERE badgeid=? AND originaltimestamp BETWEEN ? AND ? ORDER BY originaltimestamp ASC");

            GregorianCalendar firstDayBeginning = new GregorianCalendar();
            firstDayBeginning.setTimeInMillis(timestamp);
            firstDayBeginning.set(Calendar.HOUR_OF_DAY, 0);
            firstDayBeginning.set(Calendar.MINUTE, 0);
            firstDayBeginning.set(Calendar.SECOND, 0);
            firstDayBeginning.set(Calendar.MILLISECOND, 0);

            GregorianCalendar firstDayEnding = new GregorianCalendar();
            firstDayEnding.setTimeInMillis(firstDayBeginning.getTimeInMillis());
            firstDayBeginning.set(Calendar.HOUR_OF_DAY, 23);
            firstDayBeginning.set(Calendar.MINUTE, 59);
            firstDayBeginning.set(Calendar.SECOND, 59);
            firstDayBeginning.set(Calendar.MILLISECOND, 999);

            GregorianCalendar secondDayBeginning = new GregorianCalendar();
            secondDayBeginning.add(Calendar.DAY_OF_MONTH, 1);
            secondDayBeginning.setTimeInMillis(timestamp);
            secondDayBeginning.set(Calendar.HOUR_OF_DAY, 0);
            secondDayBeginning.set(Calendar.MINUTE, 0);
            secondDayBeginning.set(Calendar.SECOND, 0);
            secondDayBeginning.set(Calendar.MILLISECOND, 0);

            GregorianCalendar secondDayEnding = new GregorianCalendar();
            secondDayEnding.setTimeInMillis(secondDayBeginning.getTimeInMillis());
            secondDayEnding.set(Calendar.HOUR_OF_DAY, 23);
            secondDayEnding.set(Calendar.MINUTE, 59);
            secondDayEnding.set(Calendar.SECOND, 59);
            secondDayEnding.set(Calendar.MILLISECOND, 999);


            selectPunchesOnDay.setString(1, badge.getBadgeId());
            selectPunchesOnDay.setTimestamp(2, new Timestamp(firstDayBeginning.getTimeInMillis()));
            selectPunchesOnDay.setTimestamp(3, new Timestamp(firstDayEnding.getTimeInMillis()));
            ResultSet result = selectPunchesOnDay.executeQuery();

            ArrayList<Punch> punchArray = new ArrayList<Punch>();
            while(result.next()) {
                punchArray.add(this.getPunch(result.getInt(1)));
            }

            selectFirstPunchOnNextDay.setString(1, badge.getBadgeId());
            selectFirstPunchOnNextDay.setTimestamp(2, new Timestamp(secondDayBeginning.getTimeInMillis()));
            selectFirstPunchOnNextDay.setTimestamp(3, new Timestamp(secondDayEnding.getTimeInMillis()));
            ResultSet secondResult = selectFirstPunchOnNextDay.executeQuery();

            System.out.println(selectFirstPunchOnNextDay);
            secondResult.next();
            int punchTypeId = secondResult.getInt(2);
            if(punchTypeId == 1 || punchTypeId == 2) {
                punchArray.add(this.getPunch(secondResult.getInt(1)));
            }

            return punchArray;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
