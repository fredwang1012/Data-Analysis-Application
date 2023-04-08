package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e;
    private Event other;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Sensor open at door");
        d = Calendar.getInstance().getTime();
        other = new Event("Sensor open at door");
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e.getDescription());
        assertEquals(d, e.getDate());
        assertNotEquals(e, null);
        assertNotEquals("Sensor", e.getDescription());
        assertEquals(e, e);
        assertNotEquals(e, "Test");
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(other.hashCode(), e.hashCode());
    }
}
