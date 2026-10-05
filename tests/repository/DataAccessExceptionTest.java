package repository;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DataAccessExceptionTest {

    @Test
    void constructorWithMessage() {
        DataAccessException exception = new DataAccessException("Error");

        assertEquals("Error", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithMessageAndCause() {
        SQLException cause = new SQLException("boom");

        DataAccessException exception = new DataAccessException("Error", cause);

        assertEquals("Error", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}