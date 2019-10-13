package tests;

import model.Alert;
import model.Location;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User u;

    @BeforeEach
    public void setup() {
        u = new User("Name", "email@gmail.com");
    }

    @Test
    public void subscriber() {
        Location l = new Location("Vancouver", "10", "12");
        u.addLocation(l);
        u.setSubscribed(true);
        assertTrue(u.isSubscribed());
        l.setAlert(new Alert("Warning", 1565337600, 1565341200, "Description",
                "https://www.google.ca/"));
        u.update(l, u);
    }

    @Test
    public void gettersSetters() {
        Location l = new Location("Vancouver", "10", "12");
        u.addLocation(l);
        assertTrue(u.getLocations().contains(l));
        u.removeLocation(l);
        assertFalse(u.getLocations().contains(l));
        assertEquals(u.getName(), "Name");
        assertEquals(u.getEmail(), "email@gmail.com");
        u.setMetric(false);
        assertFalse(u.isMetric());
        u.setCurrentUser();
        assertEquals(User.getCurrentUser(), u);
        User newUser = new User("Test", "test@gmail.com");
        newUser.setCurrentUser();
        assertEquals(User.getCurrentUser(), newUser);
        assertEquals(u, new User("Name", "differentemail@gmail.com"));
        assertNotEquals(u, null);
        assertNotEquals(u, l);
    }

    @Test
    public void emailTest() {
        u = new User(null, null);
        Location l = new Location("Vancouver", "10", "12");
        u.addLocation(l);
        u.setSubscribed(true);
        l.setAlert(new Alert("Warning", 1565337600, 1565341200, "Description",
                "https://www.google.ca/"));
    }
}
