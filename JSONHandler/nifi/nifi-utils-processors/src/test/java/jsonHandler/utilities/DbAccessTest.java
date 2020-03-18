package jsonHandler.utilities;

import org.junit.Test;

public class DbAccessTest {
    @Test
    public void insertIntoWeatherTest()
    {
        DbAccess model = new DbAccess();
        model.insertIntoTable("Test City", "00:00", 20, 50.50);
    }
}
