package edu.jsu.mcis.tas_fa20;

import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {
    @Test
    public void testToString() {
        assertEquals(
                "Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)",
                new Shift(1, "Shift 2", "12:00", "20:30", 15, 5, 15, "16:30", "17:00", 360).toString()
        );
    }
}
