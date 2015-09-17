package weather.api;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.http.HTTPException;
import java.util.HashMap;
import java.util.Map;

public class WeatherApi {
    public static Map<String, String> getWeatherInfo(String zip) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://wsf.cdyne.com/WeatherWS/Weather.asmx";
        SOAPMessage response = soapConnection.call(createSOAPRequest(zip), url);
        SOAPBody body = response.getSOAPBody();
        if (body.getElementsByTagName("Success").item(0).getTextContent().toLowerCase().equals("false")) {
            throw new HTTPException(404);
        }
        String[] keys = new String[]{"State", "City", "Description",
                "Temperature", "RelativeHumidity", "Wind", "Pressure", "Visibility",
                "WindChill", "Remarks"};
        Map<String, String> weatherInfo = new HashMap<>();
        for (String key : keys) {
            String value = body.getElementsByTagName(key).item(0).getTextContent();
            weatherInfo.put(key, value);
        }
        soapConnection.close();
        return weatherInfo;
    }

    private static SOAPMessage createSOAPRequest(String zip) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelope/");

        MimeHeaders headers = message.getMimeHeaders();
        headers.addHeader("SOAPAction", "http://ws.cdyne.com/WeatherWS/GetCityWeatherByZIP");
        SOAPBody body = envelope.getBody();
        QName bodyName = new QName("http://ws.cdyne.com/WeatherWS/", "GetCityWeatherByZIP");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
        QName name = new QName("ZIP");
        SOAPElement symbol = bodyElement.addChildElement(name);
        symbol.addTextNode(zip);
        message.saveChanges();
        return message;
    }
}
