package weather.api;


import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

public class WeatherApiTest extends TestCase {

    @Before
    public void setUp() {

    }

    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {

    }

    public void testGetWeatherInfo() throws Exception {
        WeatherApi.getWeatherInfo("10001");
    }
}
