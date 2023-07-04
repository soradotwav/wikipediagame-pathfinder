package net.soradotwav;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySQLConnect {

    private static HikariDataSource dataSource;
    public static final String BASE_URL = "https://en.wikipedia.org/wiki/";

    static {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/wikigame");
        config.setUsername("root");
        config.setPassword("password");

        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    public static boolean addToDB(String subsite, String currCategories, String currPortals) throws Exception {

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            subsite = URLDecoder.decode(subsite, "UTF-8");
            subsite.replace("%2B", "+");

            String sql = "INSERT INTO wikigame_dataset (urlString, categories, portals, lastUpdated) " +
                         "SELECT ?, ?, ?, CURDATE() WHERE NOT EXISTS (" +
                         "  SELECT urlString FROM wikigame_dataset WHERE urlString = ? LIMIT 1)";
            
            try(PreparedStatement addEntry = connection.prepareStatement(sql)) {
                addEntry.setString(1, BASE_URL + subsite);
                addEntry.setString(2, currCategories);
                addEntry.setString(3, currPortals);
                addEntry.setString(4, BASE_URL + subsite);

                int dbFeedback = addEntry.executeUpdate();

                if (dbFeedback != 0) {
                    connection.commit();
                    return true;
                }
            }

        } catch (Exception e) {
            throw e;
        }

        return false;
    }

    public static boolean isInDatabase(String subsite) {

        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            subsite = URLDecoder.decode(subsite, "UTF-8");
            subsite.replace("%2B", "+");

            String query = "SELECT * FROM wikigame_dataset WHERE urlString = ?";
            
            try(PreparedStatement check = connection.prepareStatement(query)) {
                check.setString(1, BASE_URL + subsite);
                
                try(ResultSet result = check.executeQuery()) {
                    return result.next();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}