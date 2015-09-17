package weather.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


/*
 * @author Aldo Ziflaj
 * http://examples.javacodegeeks.com/core-java/java-postgresql-example/
 * */
public class PostgresHelper {

    private static Connection conn;

    private String host = DbContract.HOST;
    private String dbName = DbContract.DB_NAME;

    public PostgresHelper() {
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() throws SQLException, ClassNotFoundException {
        if (host.isEmpty() || dbName.isEmpty()) {
            throw new SQLException("Database credentials missing");
        }

        Class.forName("org.postgresql.Driver");
        this.conn = DriverManager.getConnection(
                this.host + this.dbName);
        return true;
    }

    public ResultSet execQuery(String query) throws SQLException {
        return this.conn.createStatement().executeQuery(query);
    }

    public Connection getConn() {
        return conn;
    }
}
