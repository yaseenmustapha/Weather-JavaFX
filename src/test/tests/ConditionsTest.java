package tests;

import model.Conditions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionsTest {
    private Conditions conditions;

    @Test
    public void checkGetters() {
        conditions = new Conditions(1565337600, "Summary", "icon", 1.0, 2.0,
                21.00, 22.00, 2.00, 67, 1000, 5.0,
                90);
        assertEquals(conditions.getSummary(), "Summary");
        assertEquals(conditions.getIcon(), "icon");
        assertEquals(conditions.getPrecipIntensity(), 1.0);
        assertEquals(conditions.getUvIndex(), 2.0);
        assertEquals(conditions.getTemperature(), 21.00);
        assertEquals(conditions.getApparentTemperature(), 22.00);
        assertEquals(conditions.getDewPoint(), 2.00);
        assertEquals(conditions.getHumidity(), 67);
        assertEquals(conditions.getPressure(), 1000);
        assertEquals(conditions.getWindSpeed(), 5.0);
        assertEquals(conditions.getWindBearing(), 90);
    }
}
