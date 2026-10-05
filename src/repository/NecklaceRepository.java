package repository;

import database.ConnectionProvider;
import model.Gemstone;
import model.Necklace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NecklaceRepository {
    static final String UPSERT_NECKLACE_SQL =
            "INSERT INTO necklaces (id, name) VALUES (?, ?) "
                    + "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name";
    static final String DELETE_LINKS_SQL =
            "DELETE FROM necklace_gemstones WHERE necklace_id = ?";
    static final String INSERT_LINK_SQL =
            "INSERT INTO necklace_gemstones (necklace_id, gemstone_id, position) "
                    + "VALUES (?, ?, ?)";
    static final String FIND_NECKLACE_SQL =
            "SELECT name FROM necklaces WHERE id = ?";
    static final String FIND_GEMSTONES_SQL =
            "SELECT g.* FROM gemstones g "
                    + "JOIN necklace_gemstones ng ON ng.gemstone_id = g.id "
                    + "WHERE ng.necklace_id = ? ORDER BY ng.position";

    private final ConnectionProvider connectionProvider;
    private final GemstoneRowMapper mapper;

    public NecklaceRepository(ConnectionProvider connectionProvider,
                              GemstoneRowMapper mapper) {
        this.connectionProvider = connectionProvider;
        this.mapper = mapper;
    }

    public void save(Necklace necklace) {
        for (Gemstone gemstone : necklace.getGemstones()) {
            if (gemstone.getId() <= 0) {
                throw new IllegalArgumentException(
                        "All gemstones must be saved before the necklace");
            }
        }
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try {
                saveNecklace(connection, necklace);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Cannot save necklace " + necklace.getId(), e);
        }
    }

    private void saveNecklace(Connection connection, Necklace necklace)
            throws SQLException {
        try (PreparedStatement upsert = connection.prepareStatement(UPSERT_NECKLACE_SQL);
             PreparedStatement deleteLinks = connection.prepareStatement(DELETE_LINKS_SQL);
             PreparedStatement insertLink = connection.prepareStatement(INSERT_LINK_SQL)) {

            upsert.setInt(1, necklace.getId());
            upsert.setString(2, necklace.getName());
            upsert.executeUpdate();

            deleteLinks.setInt(1, necklace.getId());
            deleteLinks.executeUpdate();

            int position = 1;
            for (Gemstone gemstone : necklace.getGemstones()) {
                insertLink.setInt(1, necklace.getId());
                insertLink.setInt(2, gemstone.getId());
                insertLink.setInt(3, position);
                insertLink.executeUpdate();
                position++;
            }
        }
    }

    public Optional<Necklace> findById(int id) {
        try (Connection connection = connectionProvider.getConnection()) {
            String name = findName(connection, id);
            if (name == null) {
                return Optional.empty();
            }
            List<Gemstone> gemstones = findGemstones(connection, id);
            return Optional.of(new Necklace(id, name, gemstones));
        } catch (SQLException e) {
            throw new DataAccessException("Cannot load necklace " + id, e);
        }
    }

    private String findName(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_NECKLACE_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
                return null;
            }
        }
    }

    private List<Gemstone> findGemstones(Connection connection, int id)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_GEMSTONES_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                List<Gemstone> gemstones = new ArrayList<>();
                while (rs.next()) {
                    gemstones.add(mapper.map(rs));
                }
                return gemstones;
            }
        }
    }
}