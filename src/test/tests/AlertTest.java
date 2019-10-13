package tests;

import model.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertTest {
    private Alert alert;

    @BeforeEach
    public void setup() {
        alert = new Alert("Warning", 1565337600, 1565341200, "Description",
                "https://www.google.ca/");
    }

    @Test
    public void checkExpires() {
        assertEquals(alert.getExpires(), "2019-08-09 02:00:00");
    }

    @Test
    public void checkDescription() {
        assertEquals(alert.getDescription(), "Description");
    }

    @Test
    public void checkUri() {
        assertEquals(alert.getUri(), "https://www.google.ca/");
    }

    @Test
    public void checkGetters() {
        assertEquals(alert.getNotified(), false);
        assertEquals(alert.getTitle(), "Warning");
        assertEquals(alert.getTime(), "2019-08-09 01:00:00");
    }


    @Test
    public void setNotified() {
        assertFalse(alert.getNotified());
        alert.setNotified(true);
        assertTrue(alert.getNotified());
    }
}
