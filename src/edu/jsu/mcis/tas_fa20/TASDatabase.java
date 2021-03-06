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
            punch.setOriginaltimestamp(result.getTimestamp(4).getTime());
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
            selectShiftId.setString(1, badge.getId());
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
            insertPunch.setInt(1, 0);
            insertPunch.setInt(2, punch.getTerminalid());
            insertPunch.setString(3, punch.getBadgeid());
            insertPunch.setTimestamp(4, new Timestamp(punch.getOriginaltimestamp()));
            insertPunch.setInt(5, punch.getPunchtypeid());
            insertPunch.executeUpdate();

            ResultSet autoIncrementResult = insertPunch.getGeneratedKeys();
            autoIncrementResult.next();

            return autoIncrementResult.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.toString());
            return -1;
        }
    }

    public void insertAbsenteeism(Absenteeism absenteeism) {
        try {
            PreparedStatement deleteAbsenteeism = connection.prepareStatement("DELETE FROM absenteeism WHERE badgeid=? AND payperiod=?");
            deleteAbsenteeism.setString(1, absenteeism.badgeId);
            deleteAbsenteeism.setTimestamp(2, new Timestamp(absenteeism.payPeriodStart.getTimeInMillis()));
            deleteAbsenteeism.executeUpdate();

            PreparedStatement insertAbsenteeism = connection.prepareStatement("INSERT INTO absenteeism VALUES(?,?,?)");
            insertAbsenteeism.setString(1, absenteeism.badgeId);
            insertAbsenteeism.setTimestamp(2, new Timestamp(absenteeism.payPeriodStart.getTimeInMillis()));
            insertAbsenteeism.setDouble(3, absenteeism.percentage);
            insertAbsenteeism.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }

    public Absenteeism getAbsenteeism(String badgeId, long startTimestamp) {
        try {
            PreparedStatement selectAbsenteeism = connection.prepareStatement("SELECT * FROM absenteeism WHERE badgeid=? AND payperiod=?");
            selectAbsenteeism.setString(1, badgeId);
            selectAbsenteeism.setTimestamp(2, new Timestamp(startTimestamp));
            ResultSet results = selectAbsenteeism.executeQuery();
            results.next();

            return new Absenteeism(results.getString(1), results.getTimestamp(2).getTime(), results.getDouble(3));
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    private ArrayList<Punch> getPunchListBetweenDates(Badge badge, GregorianCalendar startDate, GregorianCalendar endDate) {
        try {
            PreparedStatement selectPunchesOnDay = this.connection.prepareStatement("SELECT id FROM punch WHERE badgeid=? AND originaltimestamp BETWEEN ? AND ?");
            PreparedStatement selectFirstPunchOnNextDay = this.connection.prepareStatement("SELECT id, punchtypeid FROM punch WHERE badgeid=? AND originaltimestamp BETWEEN ? AND ? ORDER BY originaltimestamp ASC LIMIT 1");

            GregorianCalendar realFirstDate = (GregorianCalendar) startDate.clone();
            realFirstDate.set(Calendar.HOUR_OF_DAY, 0);
            realFirstDate.set(Calendar.MINUTE, 0);
            realFirstDate.set(Calendar.SECOND, 0);
            realFirstDate.set(Calendar.MILLISECOND, 0);

            GregorianCalendar realEndDate = (GregorianCalendar) endDate.clone();
            realEndDate.set(Calendar.HOUR_OF_DAY, 23);
            realEndDate.set(Calendar.MINUTE, 59);
            realEndDate.set(Calendar.SECOND, 59);
            realEndDate.set(Calendar.MILLISECOND, 999);

            GregorianCalendar nextDayBeginning = (GregorianCalendar) endDate.clone();
            nextDayBeginning.add(Calendar.DAY_OF_MONTH, 1);
            nextDayBeginning.set(Calendar.HOUR_OF_DAY, 0);
            nextDayBeginning.set(Calendar.MINUTE, 0);
            nextDayBeginning.set(Calendar.SECOND, 0);
            nextDayBeginning.set(Calendar.MILLISECOND, 0);

            GregorianCalendar nextDayEnding = (GregorianCalendar) endDate.clone();
            nextDayEnding.set(Calendar.HOUR_OF_DAY, 23);
            nextDayEnding.set(Calendar.MINUTE, 59);
            nextDayEnding.set(Calendar.SECOND, 59);
            nextDayEnding.set(Calendar.MILLISECOND, 999);


            selectPunchesOnDay.setString(1, badge.getId());
            selectPunchesOnDay.setTimestamp(2, new Timestamp(realFirstDate.getTimeInMillis()));
            selectPunchesOnDay.setTimestamp(3, new Timestamp(realEndDate.getTimeInMillis()));
            ResultSet result = selectPunchesOnDay.executeQuery();

            ArrayList<Punch> punchArray = new ArrayList<Punch>();
            while (result.next()) {
                punchArray.add(this.getPunch(result.getInt(1)));
            }


            selectFirstPunchOnNextDay.setString(1, badge.getId());
            selectFirstPunchOnNextDay.setTimestamp(2, new Timestamp(nextDayBeginning.getTimeInMillis()));
            selectFirstPunchOnNextDay.setTimestamp(3, new Timestamp(nextDayEnding.getTimeInMillis()));
            ResultSet secondResult = selectFirstPunchOnNextDay.executeQuery();

            if(secondResult.next()) {
                int punchTypeId = secondResult.getInt(2);

                if (punchTypeId == 2 || punchTypeId == 3) {
                    punchArray.add(this.getPunch(secondResult.getInt(1)));
                }
            }

            return punchArray;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public ArrayList<Punch> getDailyPunchList(Badge badge, long timestamp) {
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(timestamp);

        return this.getPunchListBetweenDates(badge, date, date);
    }

    public ArrayList<Punch> getPayPeriodPunchList(Badge badge, long startTimestamp) {
        GregorianCalendar startDate = new GregorianCalendar();
        startDate.setTimeInMillis(startTimestamp);

        while(startDate.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {
            startDate.add(GregorianCalendar.DAY_OF_MONTH, -1);
        }

        GregorianCalendar endDate = (GregorianCalendar) startDate.clone();
        endDate.add(Calendar.DAY_OF_MONTH, 6);

        System.out.println(badge.id);
        System.out.println(startTimestamp);
        System.out.println(endDate.getTimeInMillis());


        return this.getPunchListBetweenDates(badge, startDate, endDate);
    }

    public void close() {
        try {
            this.connection.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
