package edu.jsu.mcis.tas_fa20;

import org.junit.*;
import java.sql.Timestamp;
import static org.junit.Assert.*;

public class PunchTest {
    @Test
    public void testPrintOriginalTimestamp() {
        Punch thePunch = new Punch(new Badge("FF591F68", "Miller, Robert K"), 2, 0);
        thePunch.setOriginaltimestamp((long)1602457926 * 1000);

        assertEquals(
                "#FF591F68 CLOCKED IN: SUN 10/11/2020 06:12:06",
                thePunch.printOriginalTimestamp()
        );
    }

    @Test
    public void testIdSetterAndGetter() {
        Punch punch = new Punch(new Badge("FF591F68", "Miller, Robert K"), 2, 0);
        punch.setId(1);

        assertEquals(
                1,
                punch.getId()
        );
    }

    @Test
    public void testBadgeIdGetter() {
        Punch punch = new Punch(new Badge("FF591F68", "Miller, Robert K"), 2, 0);

        assertEquals(
                "FF591F68",
                punch.getBadgeid()
        );
    }

    @Test
    public void testTerminalIdGetter() {
        Punch punch = new Punch(new Badge("FF591F68", "Miller, Robert K"), 2, 0);

        assertEquals(
                2,
                punch.getTerminalid()
        );
    }

    @Test
    public void testPunchTypeIdGetter() {
        Punch punch = new Punch(new Badge("FF591F68", "Miller, Robert K"), 2, 0);

        assertEquals(
                0,
                punch.getPunchtypeid()
        );
    }

    @Test
    public void testOriginalTimestampSetterAndGetter() {
        long currentTimestamp = new Timestamp(System.currentTimeMillis()).getTime();

        Punch punch = new Punch(new Badge("FF591F68", "Miller, Robert K"), 2, 0);
        punch.setOriginaltimestamp(currentTimestamp);

        assertEquals(
                currentTimestamp,
                punch.getOriginaltimestamp()
        );
    }
}
