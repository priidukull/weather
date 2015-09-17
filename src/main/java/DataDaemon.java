import weather.CollectWeatherInfo;

import java.sql.SQLException;

public class DataDaemon {
    private static CollectWeatherInfo collectWeatherInfo = new CollectWeatherInfo();

    public static void main(String[] args) {
        try {
            collectWeatherInfo.process();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
