package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig implements ConnectionProvider {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig(String url, String user, String password) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Url can't be empty");
        }
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User can't be empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password can't be null");
        }
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DatabaseConfig load(String fileName) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(fileName)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + fileName, e);
        }
        return new DatabaseConfig(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }
}