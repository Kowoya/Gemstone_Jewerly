package repository;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import model.Gemstone;
import model.PreciousStone;
import model.SemiPreciousStone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GemstoneRowMapperTest {
    private static final double DELTA = 0.001;

    private ResultSet resultSet;
    private GemstoneRowMapper mapper;

    @BeforeEach
    void setUp() throws SQLException {
        resultSet = mock(ResultSet.class);
        mapper = new GemstoneRowMapper();

        when(resultSet.getInt("id")).thenReturn(7);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getDouble("weight_carats")).thenReturn(1.5);
        when(resultSet.getDouble("price_per_carat")).thenReturn(1000.0);
        when(resultSet.getString("clarity")).thenReturn("VS1");
        when(resultSet.getDouble("transparency_index")).thenReturn(8.5);
        when(resultSet.getString("origin")).thenReturn("BRAZIL");
    }

    @ParameterizedTest
    @CsvSource({
            "DIAMOND, WHITE, Diamond",
            "RUBY, RED, Ruby",
            "SAPPHIRE, BLUE, Sapphire",
            "EMERALD, GREEN, Emerald"
    })
    void mapCreatesPreciousStone(String type, String color, String expectedClass)
            throws SQLException {
        when(resultSet.getString("type")).thenReturn(type);
        when(resultSet.getString("color")).thenReturn(color);
        when(resultSet.getString("certificate_number")).thenReturn("GIA-1");
        when(resultSet.getString("cut_type")).thenReturn("OVAL");

        Gemstone gemstone = mapper.map(resultSet);

        assertEquals(expectedClass, gemstone.getClass().getSimpleName());
        assertCommonFields(gemstone, color);
        PreciousStone precious = (PreciousStone) gemstone;
        assertEquals("GIA-1", precious.getCertificateNumber());
        assertEquals(CutType.OVAL, precious.getCutType());
    }

    @ParameterizedTest
    @CsvSource({
            "AMETHYST, PURPLE, Amethyst",
            "TOPAZ, ORANGE, Topaz",
            "GARNET, RED, Garnet"
    })
    void mapCreatesSemiPreciousStone(String type, String color, String expectedClass)
            throws SQLException {
        when(resultSet.getString("type")).thenReturn(type);
        when(resultSet.getString("color")).thenReturn(color);
        when(resultSet.getString("treatment_type")).thenReturn("NONE");

        Gemstone gemstone = mapper.map(resultSet);

        assertEquals(expectedClass, gemstone.getClass().getSimpleName());
        assertCommonFields(gemstone, color);
        SemiPreciousStone semiPrecious = (SemiPreciousStone) gemstone;
        assertEquals("NONE", semiPrecious.getTreatmentType());
    }

    @Test
    void mapThrowsForUnknownType() throws SQLException {
        when(resultSet.getString("type")).thenReturn("OPAL");
        when(resultSet.getString("color")).thenReturn("WHITE");

        assertThrows(DataAccessException.class, () -> mapper.map(resultSet));
    }

    @Test
    void mapThrowsForUnknownClarity() throws SQLException {
        when(resultSet.getString("type")).thenReturn("DIAMOND");
        when(resultSet.getString("color")).thenReturn("WHITE");
        when(resultSet.getString("clarity")).thenReturn("XX");

        assertThrows(IllegalArgumentException.class, () -> mapper.map(resultSet));
    }

    private void assertCommonFields(Gemstone gemstone, String color) {
        assertEquals(7, gemstone.getId());
        assertEquals("Test", gemstone.getName());
        assertEquals(1.5, gemstone.getWeightCarats(), DELTA);
        assertEquals(1000.0, gemstone.getPricePerCarat(), DELTA);
        assertEquals(ClarityGrade.VS1, gemstone.getClarity());
        assertEquals(8.5, gemstone.getTransparencyIndex(), DELTA);
        assertEquals(GemColor.valueOf(color), gemstone.getColor());
    }
}