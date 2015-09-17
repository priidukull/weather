import weather.db.CSVLoader;
import weather.db.PostgresHelper;

import java.sql.SQLException;

public class Initiate {
    static PostgresHelper client = new PostgresHelper();

    public static void main(String[] args) {
        initiate();
    }

    private static void initiate() {
        try {
            if (client.connect()) {
                System.out.println("DB connected");
                try {

                    CSVLoader loader = new CSVLoader(client.getConn());

                    loader.loadCSV("us_postal_codes.csv", "weather.city", false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
