package edu.jsu.mcis.tas_fa20;

import org.junit.*;
import static org.junit.Assert.*;

public class BadgeTest {
    @Test
    public void testToString() {
        assertEquals(
                "#FF591F68 (Miller, Robert K)",
                new Badge("FF591F68", "Miller, Robert K").toString()
        );
    }

    @Test
    public void testIdGetter() {
        assertEquals(
                "FF591F68",
                new Badge("FF591F68", "Miller, Robert K").getId()
        );
    }
}
