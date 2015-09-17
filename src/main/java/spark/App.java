package spark;

import models.City;
import models.Data;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import spark.template.velocity.VelocityTemplateEngine;
import weather.db.PostgresHelper;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class App {
    private static PostgresHelper client = new PostgresHelper();

    public static void main(String[] args) throws SQLException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        org.hibernate.Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query queryFromCity = session.createQuery("from City");
        List<City> cities = queryFromCity.list();

        staticFileLocation("/public");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("template", "templates/index.vtl");
        model.put("cities", cities);

        get("/", (req, res) -> {
            String selectedCity = req.queryParams("city");
            Data data = null;
            if (selectedCity != null) {
                BigInteger cityId = BigInteger.valueOf(Long.valueOf(selectedCity));
                SessionFactory factory = new Configuration().configure().buildSessionFactory();
                org.hibernate.Session sess = factory.openSession();
                sess.beginTransaction();
                data = sess.get(Data.class, cityId);
            }
            model.put("data", data);
            String measurements = req.queryParams("measurements");
            if (measurements == null) {
                measurements = "imperial";
            }
            model.put("measurements", measurements);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
    }
}