package weather;

import models.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import weather.api.WeatherApi;
import weather.db.PostgresHelper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.lang.Thread.sleep;
import static weather.WeatherParser.parseImperialData;
import static weather.WeatherParser.parseMetricsData;

public class CollectWeatherInfo {
    private static PostgresHelper client = new PostgresHelper();
    String zip;
    private static int retrySeconds = 300;

    public void process() throws SQLException {
        System.out.println("Started process CollectWeatherInfo");
        while (true) {
            collectData();
            Integer ONE_HOUR_IN_MILLISECONDS = 1000 * 60 * 60;
            System.out.println("Finished collecting weather info. Will collect again in an hour");
            try {
                Thread.sleep(ONE_HOUR_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void collectData() throws SQLException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String selectAll = "SELECT * FROM weather.city";
        ResultSet resultSet = client.execQuery(selectAll);
        while(resultSet.next()) {
            BigInteger id = BigInteger.valueOf(resultSet.getInt("id"));
            zip = resultSet.getString("zip");
            Map<String, String> rawData = getRawData();
            Map<String, String> imperialData = parseImperialData(rawData);
            Map<String, String> metricsData = parseMetricsData(imperialData);
            Data weatherData = new Data(id, imperialData, metricsData);

            session.beginTransaction();
            session.save(weatherData);
            session.getTransaction().commit();
        }
    }

    /* The wrapper is in place to specify behavior for when the request fails */
    private Map<String, String> getRawData() {
        try {
            Map<String, String> weatherInfo = WeatherApi.getWeatherInfo(zip);
            retrySeconds = 300;
            return weatherInfo;
        } catch (Exception e) {
            e.printStackTrace();
            sleepBeforeRetry();
            retrySeconds *= 2;
            return getRawData();
        }
    }

    private void sleepBeforeRetry() {
        try {
            System.out.println("Getting weather info failed. Will try again in " + retrySeconds + " seconds");
            sleep(retrySeconds * 1000);
        } catch (InterruptedException e1) {
            System.out.println("Sleep interrupted:");
            e1.printStackTrace();
        }
    }
}
