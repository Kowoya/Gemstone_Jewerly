package model;

import enums.ClarityGrade;
import enums.GemColor;
import enums.Origin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GarnetTest {

    private static final double DELTA = 0.001;

    private Garnet createGarnet(ClarityGrade clarity, GemColor color) {
        return new Garnet("Test", 1.0, 1000, clarity, 7.6, color,
                Origin.UNKNOWN, "NONE");
    }

    private Garnet createGarnet(double weight) {
        return new Garnet("Test", weight, 1000, ClarityGrade.VS1, 7.6,
                GemColor.RED, Origin.UNKNOWN, "NONE");
    }

    private Garnet createGarnet(Origin origin) {
        return new Garnet("Test", 1.0, 1000, ClarityGrade.VS1, 7.6,
                GemColor.RED, origin, "NONE");
    }

    // ---------- Колір ----------
    @ParameterizedTest
    @EnumSource(GemColor.class)
    void garnetOfAnyColorIsCreated(GemColor color) {
        assertDoesNotThrow(() -> createGarnet(ClarityGrade.VS1, color));
    }

    @ParameterizedTest
    @CsvSource({
            "GREEN, 2500",
            "ORANGE, 1500",
            "PURPLE, 1300",
            "PINK, 1200",
            "BROWN, 800"
    })
    void valueDependsOnColor(GemColor color, double expected) {
        Garnet garnet = createGarnet(ClarityGrade.VS1, color);
        assertEquals(expected, garnet.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class,
            names = {"GREEN", "ORANGE", "PURPLE", "PINK", "BROWN"},
            mode = EnumSource.Mode.EXCLUDE)
    void otherColorsDoNotChangeValue(GemColor color) {
        Garnet garnet = createGarnet(ClarityGrade.VS1, color);
        assertEquals(1000, garnet.calculateValue(), DELTA);
    }

    // ---------- Чистота ----------
    @ParameterizedTest
    @CsvSource({
            "FL, 1200",
            "IF, 1200",
            "VVS1, 1100",
            "VVS2, 1100",
            "VS1, 1000",
            "VS2, 1000",
            "SI1, 850",
            "SI2, 850",
            "I1, 600"
    })
    void valueDependsOnClarity(ClarityGrade clarity, double expected) {
        Garnet garnet = createGarnet(clarity, GemColor.RED);
        assertEquals(expected, garnet.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @EnumSource(Origin.class)
    void originDoesNotAffectValue(Origin origin) {
        assertEquals(1000, createGarnet(origin).calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
            "0.5, 500",
            "1.0, 1000",
            "2.0, 2000"
    })
    void valueGrowsWithWeight(double weight, double expected) {
        assertEquals(expected, createGarnet(weight).calculateValue(),
                DELTA);
    }

    // ---------- Повний розрахунок ----------
    @Test
    void calculatesTotalValue() {
        Garnet garnet = new Garnet("Демантоїд", 1.1, 900, ClarityGrade.SI2,
                7.6, GemColor.GREEN, Origin.NAMIBIA, "NONE");
        // 900 * 1.1 * 0.85 * 2.5 = 2103.75
        assertEquals(2103.75, garnet.calculateValue(), DELTA);
    }
}