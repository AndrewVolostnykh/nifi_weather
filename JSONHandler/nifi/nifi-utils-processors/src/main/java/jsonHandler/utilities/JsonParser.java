package jsonHandler.utilities;

import org.json.JSONObject;

/**This class will take JSONObject and gives out
 * data that will added to DB*/
public class JsonParser {
    private JSONObject jsonObject;

    public JsonParser(String jsonString) {
        jsonObject = new JSONObject(jsonString);
    }

    public String getCoords()
    {
        JSONObject coords = jsonObject.getJSONObject("coord");
        Double lon = (Double)coords.get("lon");
        Double lat = (Double)coords.get("lat");
        return lon + ":" + lat;
    }

    public String getCityName()
    {
        return jsonObject.getString("name");
    }

    public double getTemperature()
    {
        return   (Double) jsonObject
                .getJSONObject("main")
                .get("temp");
    }

    public Integer getHumidity()
    {
        return (Integer) jsonObject.getJSONObject("main").get("humidity");
    }

}
