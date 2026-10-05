package repository;

import database.ConnectionProvider;
import model.Gemstone;
import model.PreciousStone;
import model.SemiPreciousStone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class GemstoneRepository {
    static final String FIND_ALL_SQL = "SELECT * FROM gemstones ORDER BY id";

    private final ConnectionProvider connectionProvider;
    private final GemstoneRowMapper mapper;

    public GemstoneRepository(ConnectionProvider connectionProvider,
                              GemstoneRowMapper mapper) {
        this.connectionProvider = connectionProvider;
        this.mapper = mapper;
    }

    public List<Gemstone> findAll() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = statement.executeQuery()) {

            List<Gemstone> gemstones = new ArrayList<>();
            while (rs.next()) {
                gemstones.add(mapper.map(rs));
            }
            return gemstones;
        } catch (SQLException e) {
            throw new DataAccessException("Cannot load gemstones", e);
        }
    }

    static final String INSERT_SQL = "INSERT INTO gemstones (type, name, weight_carats, "
            + "price_per_carat, clarity, transparency_index, color, origin, "
            + "certificate_number, cut_type, treatment_type) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    public void save(Gemstone gemstone) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setString(1, gemstone.getClass().getSimpleName().toUpperCase());
            statement.setString(2, gemstone.getName());
            statement.setDouble(3, gemstone.getWeightCarats());
            statement.setDouble(4, gemstone.getPricePerCarat());
            statement.setString(5, gemstone.getClarity().name());
            statement.setDouble(6, gemstone.getTransparencyIndex());
            statement.setString(7, gemstone.getColor().name());
            statement.setString(8, gemstone.getOrigin().name());

            if (gemstone instanceof PreciousStone precious) {
                statement.setString(9, precious.getCertificateNumber());
                statement.setString(10, precious.getCutType().name());
                statement.setNull(11, Types.VARCHAR);
            } else {
                SemiPreciousStone semi = (SemiPreciousStone) gemstone;
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.VARCHAR);
                statement.setString(11, semi.getTreatmentType());
            }

            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                gemstone.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Cannot save gemstone " + gemstone.getName(), e);
        }
    }
}