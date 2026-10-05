package repository;

import database.ConnectionProvider;
import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import model.Amethyst;
import model.Diamond;
import model.Gemstone;
import model.Necklace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NecklaceRepositoryTest {
    private ConnectionProvider connectionProvider;
    private Connection connection;
    private GemstoneRowMapper mapper;
    private NecklaceRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        connectionProvider = mock(ConnectionProvider.class);
        connection = mock(Connection.class);
        mapper = mock(GemstoneRowMapper.class);

        when(connectionProvider.getConnection()).thenReturn(connection);

        repository = new NecklaceRepository(connectionProvider, mapper);
    }

    private Gemstone createDiamond(int id) {
        Gemstone diamond = new Diamond("Зоря", 1.2, 5200, ClarityGrade.VVS1,
                9.6, GemColor.WHITE, Origin.BOTSWANA, "GIA-1", CutType.ROUND);
        diamond.setId(id);
        return diamond;
    }

    private Gemstone createAmethyst(int id) {
        Gemstone amethyst = new Amethyst("Замбійський", 4.5, 45, ClarityGrade.VS1,
                8.7, GemColor.PURPLE, Origin.ZAMBIA, "NONE");
        amethyst.setId(id);
        return amethyst;
    }

    // ---------- save ----------

    @Test
    void saveWritesNecklaceAndGemstonesInTransaction() throws SQLException {
        PreparedStatement upsert = mock(PreparedStatement.class);
        PreparedStatement deleteLinks = mock(PreparedStatement.class);
        PreparedStatement insertLink = mock(PreparedStatement.class);
        when(connection.prepareStatement(NecklaceRepository.UPSERT_NECKLACE_SQL)).thenReturn(upsert);
        when(connection.prepareStatement(NecklaceRepository.DELETE_LINKS_SQL)).thenReturn(deleteLinks);
        when(connection.prepareStatement(NecklaceRepository.INSERT_LINK_SQL)).thenReturn(insertLink);
        Necklace necklace = new Necklace(1, "Вечірнє",
                List.of(createDiamond(3), createAmethyst(8)));

        repository.save(necklace);

        verify(upsert).setInt(1, 1);
        verify(upsert).setString(2, "Вечірнє");
        verify(upsert).executeUpdate();

        verify(deleteLinks).setInt(1, 1);
        verify(deleteLinks).executeUpdate();

        verify(insertLink, times(2)).setInt(1, 1);
        verify(insertLink).setInt(2, 3);
        verify(insertLink).setInt(3, 1);
        verify(insertLink).setInt(2, 8);
        verify(insertLink).setInt(3, 2);
        verify(insertLink, times(2)).executeUpdate();

        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection, never()).rollback();
        verify(connection).close();
    }

    @Test
    void saveThrowsWhenGemstoneHasNoId() throws SQLException {
        Gemstone notSaved = new Diamond("Новий", 1.0, 1000, ClarityGrade.VS1,
                9.0, GemColor.WHITE, Origin.CANADA, "GIA-2", CutType.OVAL);
        Necklace necklace = new Necklace(1, "Вечірнє", List.of(notSaved));

        assertThrows(IllegalArgumentException.class, () -> repository.save(necklace));
        verify(connectionProvider, never()).getConnection();
    }

    @Test
    void saveRollsBackWhenInsertFails() throws SQLException {
        PreparedStatement upsert = mock(PreparedStatement.class);
        PreparedStatement deleteLinks = mock(PreparedStatement.class);
        PreparedStatement insertLink = mock(PreparedStatement.class);
        when(connection.prepareStatement(NecklaceRepository.UPSERT_NECKLACE_SQL)).thenReturn(upsert);
        when(connection.prepareStatement(NecklaceRepository.DELETE_LINKS_SQL)).thenReturn(deleteLinks);
        when(connection.prepareStatement(NecklaceRepository.INSERT_LINK_SQL)).thenReturn(insertLink);
        SQLException error = new SQLException("boom");
        when(insertLink.executeUpdate()).thenThrow(error);
        Necklace necklace = new Necklace(1, "Вечірнє", List.of(createDiamond(3)));

        DataAccessException exception =
                assertThrows(DataAccessException.class, () -> repository.save(necklace));

        assertSame(error, exception.getCause());
        verify(connection).rollback();
        verify(connection, never()).commit();
        verify(connection).close();
    }

    @Test
    void saveWrapsConnectionError() throws SQLException {
        when(connectionProvider.getConnection()).thenThrow(new SQLException("no db"));
        Necklace necklace = new Necklace(1, "Вечірнє", List.of(createDiamond(3)));

        assertThrows(DataAccessException.class, () -> repository.save(necklace));
    }

    // ---------- findById ----------

    @Test
    void findByIdReturnsNecklaceWithGemstones() throws SQLException {
        PreparedStatement nameStatement = mock(PreparedStatement.class);
        ResultSet nameResult = mock(ResultSet.class);
        PreparedStatement gemstonesStatement = mock(PreparedStatement.class);
        ResultSet gemstonesResult = mock(ResultSet.class);
        when(connection.prepareStatement(NecklaceRepository.FIND_NECKLACE_SQL)).thenReturn(nameStatement);
        when(nameStatement.executeQuery()).thenReturn(nameResult);
        when(nameResult.next()).thenReturn(true);
        when(nameResult.getString("name")).thenReturn("Вечірнє");
        when(connection.prepareStatement(NecklaceRepository.FIND_GEMSTONES_SQL)).thenReturn(gemstonesStatement);
        when(gemstonesStatement.executeQuery()).thenReturn(gemstonesResult);
        when(gemstonesResult.next()).thenReturn(true, true, false);
        Gemstone diamond = createDiamond(3);
        Gemstone amethyst = createAmethyst(8);
        when(mapper.map(gemstonesResult)).thenReturn(diamond, amethyst);

        Optional<Necklace> result = repository.findById(1);

        assertTrue(result.isPresent());
        Necklace necklace = result.get();
        assertEquals(1, necklace.getId());
        assertEquals("Вечірнє", necklace.getName());
        assertEquals(List.of(diamond, amethyst), necklace.getGemstones());
        verify(nameStatement).setInt(1, 1);
        verify(gemstonesStatement).setInt(1, 1);
        verify(connection).close();
    }

    @Test
    void findByIdReturnsEmptyWhenNecklaceNotFound() throws SQLException {
        PreparedStatement nameStatement = mock(PreparedStatement.class);
        ResultSet nameResult = mock(ResultSet.class);
        when(connection.prepareStatement(NecklaceRepository.FIND_NECKLACE_SQL)).thenReturn(nameStatement);
        when(nameStatement.executeQuery()).thenReturn(nameResult);
        when(nameResult.next()).thenReturn(false);

        Optional<Necklace> result = repository.findById(99);

        assertTrue(result.isEmpty());
        verify(connection, never()).prepareStatement(NecklaceRepository.FIND_GEMSTONES_SQL);
    }

    @Test
    void findByIdWrapsSqlException() throws SQLException {
        when(connectionProvider.getConnection()).thenThrow(new SQLException("no db"));

        assertThrows(DataAccessException.class, () -> repository.findById(1));
    }
}