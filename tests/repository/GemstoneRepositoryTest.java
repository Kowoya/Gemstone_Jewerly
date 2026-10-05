package repository;

import database.ConnectionProvider;
import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import model.Amethyst;
import model.Diamond;
import model.Gemstone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GemstoneRepositoryTest {
    private ConnectionProvider connectionProvider;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private GemstoneRowMapper mapper;
    private GemstoneRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        connectionProvider = mock(ConnectionProvider.class);
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        mapper = mock(GemstoneRowMapper.class);

        when(connectionProvider.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(GemstoneRepository.FIND_ALL_SQL)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        repository = new GemstoneRepository(connectionProvider, mapper);
    }

    @Test
    void findAllReturnsGemstoneForEveryRow() throws SQLException {
        Gemstone diamond = new Diamond("Зоря", 1.2, 5200, ClarityGrade.VVS1,
                9.6, GemColor.WHITE, Origin.BOTSWANA, "GIA-1", CutType.ROUND);
        Gemstone amethyst = new Amethyst("Замбійський", 4.5, 45, ClarityGrade.VS1,
                8.7, GemColor.PURPLE, Origin.ZAMBIA, "NONE");
        when(resultSet.next()).thenReturn(true, true, false);
        when(mapper.map(resultSet)).thenReturn(diamond, amethyst);

        List<Gemstone> result = repository.findAll();

        assertEquals(List.of(diamond, amethyst), result);
        verify(mapper, times(2)).map(resultSet);
    }

    @Test
    void findAllReturnsEmptyListWhenTableIsEmpty() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        List<Gemstone> result = repository.findAll();

        assertTrue(result.isEmpty());
        verify(mapper, never()).map(resultSet);
    }

    @Test
    void findAllClosesAllResources() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        repository.findAll();

        verify(resultSet).close();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void findAllWrapsSqlException() throws SQLException {
        SQLException error = new SQLException("boom");
        when(statement.executeQuery()).thenThrow(error);

        DataAccessException exception =
                assertThrows(DataAccessException.class, () -> repository.findAll());

        assertSame(error, exception.getCause());
        verify(connection).close();
    }
}