package tests;

import model.Conditions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionsTest {
    private Conditions conditions;

    @Test
    public void checkGetters() {
        conditions = new Conditions(1565337600, "Summary", "icon", 1.0, 0.1,
                21.00, 22.00, 2.00, 67.0, 1000.0, 5.0,
                90);
        assertEquals(conditions.getSummary(), "Summary");
        assertEquals(conditions.getIcon(), "icon");
        assertEquals(conditions.getPrecipIntensity(), 1.0);
        assertEquals(conditions.getPrecipProbability(), 0.1);
        assertEquals(conditions.getTemperature(), 21.00);
        assertEquals(conditions.getApparentTemperature(), 22.00);
        assertEquals(conditions.getDewPoint(), 2.00);
        assertEquals(conditions.getHumidity(), 67.0);
        assertEquals(conditions.getPressure(), 1000.0);
        assertEquals(conditions.getWindSpeed(), 5.0);
        assertEquals(conditions.getWindBearing(), 90);
    }
}
