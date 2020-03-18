package jsonHandler.utilities;

import org.junit.Test;

import static junit.framework.TestCase.*;

public class JsonParserTest {
    private String inputJsonString =
            "{\n" +
            "   \"coord\":{\n" +
            "      \"lon\":-0.13,\n" +
            "      \"lat\":51.51\n" +
            "   },\n" +
            "   \"weather\":[\n" +
            "      {\n" +
            "         \"id\":300,\n" +
            "         \"main\":\"Drizzle\",\n" +
            "         \"description\":\"light intensity drizzle\",\n" +
            "         \"icon\":\"09d\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"base\":\"stations\",\n" +
            "   \"main\":{\n" +
            "      \"temp\":280.32,\n" +
            "      \"pressure\":1012,\n" +
            "      \"humidity\":81,\n" +
            "      \"temp_min\":279.15,\n" +
            "      \"temp_max\":281.15\n" +
            "   },\n" +
            "   \"visibility\":10000,\n" +
            "   \"wind\":{\n" +
            "      \"speed\":4.1,\n" +
            "      \"deg\":80\n" +
            "   },\n" +
            "   \"clouds\":{\n" +
            "      \"all\":90\n" +
            "   },\n" +
            "   \"dt\":1485789600,\n" +
            "   \"sys\":{\n" +
            "      \"type\":1,\n" +
            "      \"id\":5091,\n" +
            "      \"message\":0.0103,\n" +
            "      \"country\":\"GB\",\n" +
            "      \"sunrise\":1485762037,\n" +
            "      \"sunset\":1485794875\n" +
            "   },\n" +
            "   \"id\":2643743,\n" +
            "   \"name\":\"London\",\n" +
            "   \"cod\":200\n" +
            "}";

    @Test
    public void generalTest()
    {
        JsonParser parser = new JsonParser(inputJsonString);

        // get coords method test
        assertNotNull(parser.getCoords());
        assertEquals("-0.13:51.51", parser.getCoords());

        // get city name method test
        assertNotNull(parser.getCityName());
        assertEquals("London", parser.getCityName());

        // get temperature method test
        assertEquals(280.32, parser.getTemperature());

        //get humidity method test
        assertNotNull(parser.getHumidity());

    }

    @Test
    public void parserPlusModel()
    {
        JsonParser parser = new JsonParser(inputJsonString);
        DbAccess model = new DbAccess();

        model.insertIntoTable(parser.getCityName(), parser.getCoords(), parser.getHumidity(), parser.getTemperature());
    }
}
