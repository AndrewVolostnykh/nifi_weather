package jsonHandler.utilities;

import java.sql.*;
import java.time.LocalDateTime;

/**This class will add data to DB PostgreSQL */
public class DbAccess {
    private Connection connection;

    public void connect() throws ClassNotFoundException
    {
        try{
            String name = "postgres";
            String password = "1679438520";
            String url = "jdbc:postgresql://localhost:5432/opinov8_weather";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, name, password);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void insertIntoTable(String cityName, String coords, Integer humidity, Double temperature)
    {

        try {
            this.connect();

            PreparedStatement preparedStatement = connection.prepareStatement("insert into \"weather\"(\"actualTime\", \"cityName\", \"coords\", \"humidity\", \"temperature\") values(?,?,?,?,?)");
            preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()));
            preparedStatement.setString(2, cityName);
            preparedStatement.setString(3, coords);
            preparedStatement.setInt(4, humidity);
            preparedStatement.setDouble(5, temperature);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
