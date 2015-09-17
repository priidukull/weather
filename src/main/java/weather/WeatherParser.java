package weather;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class WeatherParser {
    static Map<String, String> parseImperialData(Map<String, String> weatherInfo) {
        Map<String, String> weatherData = new HashMap<>();
        String wind = weatherInfo.get("Wind");

        String windDirection = "";
        String windSpeed = "";

        for (int i = 0; i < wind.length(); i++){
            char c = wind.charAt(i);
            if (Character.isLetter(c)) {
                windDirection += c;
            } else if (Character.isDigit(c)) {
                windSpeed += c;
            }
        }

        String pressure = weatherInfo.get("Pressure");
        String pressureAmount = pressure.substring(0, pressure.length() - 1);
        String pressureDirection = getPressureDirection(pressure);
        weatherData.put("WindDirection", windDirection);
        weatherData.put("WindSpeed", windSpeed);
        weatherData.put("WindUnit", "MPH");
        weatherData.put("Temperature", weatherInfo.get("Temperature"));
        weatherData.put("TemperatureUnit", "F");
        weatherData.put("Description", weatherInfo.get("Description"));
        weatherData.put("RelativeHumidity", weatherInfo.get("RelativeHumidity"));
        weatherData.put("RelativeHumidityUnit", "%");
        weatherData.put("Pressure", pressureAmount);
        weatherData.put("PressureUnit", "inHg");
        weatherData.put("PressureDirection", pressureDirection);
        return weatherData;
    }

    private static String getPressureDirection(String pressure) {
        String pressureDirection = pressure.substring(pressure.length() - 1);
        if (pressureDirection.equals("F")) {return "falling";};
        if (pressureDirection.equals("S")) {return "steady";};
        if (pressureDirection.equals("R")) {return "rising";};
        return pressureDirection;
    }

    static Map<String, String> parseMetricsData(Map<String, String> imperialData) {
        Map<String, String> metricsData = new HashMap<>();
        BigDecimal windSpeed = BigDecimal.valueOf(Float.valueOf(imperialData.get("WindSpeed")) * 0.44704);
        windSpeed = windSpeed.setScale(1, RoundingMode.HALF_UP);
        BigDecimal temperature = BigDecimal.valueOf((Float.valueOf(imperialData.get("Temperature")) - 32.0) * 5 / 9);
        temperature = temperature.setScale(1, RoundingMode.HALF_UP);
        BigDecimal pressureAmount = BigDecimal.valueOf(Float.valueOf(imperialData.get("Pressure")) * 33.8637526);
        pressureAmount = pressureAmount.setScale(1, RoundingMode.HALF_UP);

        metricsData.put("WindDirection", imperialData.get("WindDirection"));
        metricsData.put("WindSpeed", String.valueOf(windSpeed));
        metricsData.put("WindUnit", "m/s");
        metricsData.put("Temperature", String.valueOf(temperature));
        metricsData.put("TemperatureUnit", "C");
        metricsData.put("Description", imperialData.get("Description"));
        metricsData.put("RelativeHumidity", imperialData.get("RelativeHumidity"));
        metricsData.put("RelativeHumidityUnit", "%");
        metricsData.put("Pressure", String.valueOf(pressureAmount));
        metricsData.put("PressureUnit", "mmHg");
        metricsData.put("PressureDirection", imperialData.get("PressureDirection"));
        return metricsData;
    }
}
