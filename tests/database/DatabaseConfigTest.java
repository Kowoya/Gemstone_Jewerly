package database;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseConfigTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/gemstone_jewelry";

    @Test
    void constructorSavesUrlAndUser() {
        DatabaseConfig config = new DatabaseConfig(URL, "postgres", "secret");

        assertEquals(URL, config.getUrl());
        assertEquals("postgres", config.getUser());
    }

    @Test
    void constructorThrowsWhenUrlIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new DatabaseConfig(null, "postgres", "secret"));
    }

    @Test
    void constructorThrowsWhenUrlIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new DatabaseConfig("  ", "postgres", "secret"));
    }

    @Test
    void constructorThrowsWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new DatabaseConfig(URL, null, "secret"));
    }

    @Test
    void constructorThrowsWhenUserIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new DatabaseConfig(URL, "", "secret"));
    }

    @Test
    void constructorThrowsWhenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new DatabaseConfig(URL, "postgres", null));
    }

    @Test
    void constructorAllowsEmptyPassword() {
        DatabaseConfig config = new DatabaseConfig(URL, "postgres", "");

        assertEquals("postgres", config.getUser());
    }

    @Test
    void loadReadsValuesFromFile() throws IOException {
        Path file = Files.createTempFile("db", ".properties");
        Files.writeString(file, "db.url=" + URL + "\n"
                + "db.user=postgres\n"
                + "db.password=secret\n");

        DatabaseConfig config = DatabaseConfig.load(file.toString());

        assertEquals(URL, config.getUrl());
        assertEquals("postgres", config.getUser());
        Files.delete(file);
    }

    @Test
    void loadThrowsWhenFileIsMissing() {
        assertThrows(IllegalStateException.class,
                () -> DatabaseConfig.load("no-such-file.properties"));
    }

    @Test
    void loadThrowsWhenUrlIsMissingInFile() throws IOException {
        Path file = Files.createTempFile("db", ".properties");
        Files.writeString(file, "db.user=postgres\ndb.password=secret\n");

        assertThrows(IllegalArgumentException.class,
                () -> DatabaseConfig.load(file.toString()));
        Files.delete(file);
    }

    @Test
    void getConnectionThrowsWhenNoDriverForUrl() {
        DatabaseConfig config = new DatabaseConfig("jdbc:unknown:test", "user", "secret");

        assertThrows(SQLException.class, config::getConnection);
    }
}