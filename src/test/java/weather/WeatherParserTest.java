package weather;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class WeatherParserTest extends TestCase {
    Map<String, String> weatherInfo;
    Map<String, String> weatherDataImperial;

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        weatherInfo = new HashMap<>();
        weatherDataImperial = new HashMap<>();
    }

    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {
        weatherInfo = null;
    }

    public void testParseWeatherDataImperial() throws Exception {
        weatherDataImperial.put("WindDirection", "NE");
        weatherDataImperial.put("WindSpeed", "5");
        weatherDataImperial.put("WindUnit", "MPH");
        weatherDataImperial.put("Temperature", "60");
        weatherDataImperial.put("TemperatureUnit", "F");
        weatherDataImperial.put("Description", "Cloudy");
        weatherDataImperial.put("RelativeHumidity", "83");
        weatherDataImperial.put("RelativeHumidityUnit", "%");
        weatherDataImperial.put("Pressure", "30.01");
        weatherDataImperial.put("PressureUnit", "inHg");
        weatherDataImperial.put("PressureDirection", "falling");

        weatherInfo.put("Wind", "NE5");
        weatherInfo.put("Temperature", "60");
        weatherInfo.put("Description", "Cloudy");
        weatherInfo.put("RelativeHumidity", "83");
        weatherInfo.put("Remarks", null);
        weatherInfo.put("State", "MA");
        weatherInfo.put("Visibility", null);
        weatherInfo.put("City", "Agawam");
        weatherInfo.put("WindChill", null);
        weatherInfo.put("Pressure", "30.01F");

        Map<String, String> actual = WeatherParser.parseImperialData(weatherInfo);

        assertEquals(weatherDataImperial, actual);
    }
}
