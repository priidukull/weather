package models;

import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Data {
    @Id
    private BigInteger cityId;
    private String imperialData;
    private String metricsData;

    public Data(BigInteger cityId, Map<String, String> imperialData, Map<String, String> metricsData) {
        this.cityId = cityId;
        setImperialData(imperialData);
        setMetricsData(metricsData);
    }

    public Data() {

    }

    public BigInteger getCityId() {
        return cityId;
    }

    public void setCityId(BigInteger cityId) {
        this.cityId = cityId;
    }

    public Map<String, String> getImperialData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(imperialData, HashMap.class);
    }

    public void setImperialData(Map<String, String> imperialData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.imperialData = mapper.writeValueAsString(imperialData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getMetricsData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(metricsData, HashMap.class);
    }

    public void setMetricsData(Map<String, String> metricsData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.metricsData = mapper.writeValueAsString(metricsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
