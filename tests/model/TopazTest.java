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

class TopazTest {

    private static final double DELTA = 0.001;

    private Topaz createTopaz(ClarityGrade clarity, GemColor color) {
        return new Topaz("Test", 1.0, 1000, clarity, 9.2, color,
                Origin.UNKNOWN, "NONE");
    }

    private Topaz createTopaz(double weight) {
        return new Topaz("Test", weight, 1000, ClarityGrade.VS1, 9.2,
                GemColor.YELLOW, Origin.UNKNOWN, "NONE");
    }

    private Topaz createTopaz(Origin origin) {
        return new Topaz("Test", 1.0, 1000, ClarityGrade.VS1, 9.2,
                GemColor.YELLOW, origin, "NONE");
    }

    // ---------- Колір: правило ----------
    @ParameterizedTest
    @EnumSource(GemColor.class)
    void topazOfAnyColorIsCreated(GemColor color) {
        assertDoesNotThrow(() -> createTopaz(ClarityGrade.VS1, color));
    }

    // ---------- Колір: вартість ----------
    @ParameterizedTest
    @CsvSource({
            "ORANGE, 3000",
            "PINK, 2500",
            "YELLOW, 1000",
            "BLUE, 800",
            "BROWN, 600",
            "WHITE, 500"
    })
    void valueDependsOnColor(GemColor color, double expected) {
        Topaz topaz = createTopaz(ClarityGrade.VS1, color);
        assertEquals(expected, topaz.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class,
            names = {"ORANGE", "PINK", "YELLOW", "BLUE", "BROWN", "WHITE"},
            mode = EnumSource.Mode.EXCLUDE)
    void otherColorsDoNotChangeValue(GemColor color) {
        Topaz topaz = createTopaz(ClarityGrade.VS1, color);
        assertEquals(1000, topaz.calculateValue(), DELTA);
    }

    // ---------- Чистота ----------
    @ParameterizedTest
    @CsvSource({
            "FL, 1100",
            "IF, 1100",
            "VVS1, 1050",
            "VVS2, 1050",
            "VS1, 1000",
            "VS2, 1000",
            "SI1, 750",
            "SI2, 750",
            "I1, 500"
    })
    void valueDependsOnClarity(ClarityGrade clarity, double expected) {
        Topaz topaz = createTopaz(clarity, GemColor.YELLOW);
        assertEquals(expected, topaz.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @EnumSource(Origin.class)
    void originDoesNotAffectValue(Origin origin) {
        assertEquals(1000, createTopaz(origin).calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
            "0.5, 500",
            "1.0, 1000",
            "2.0, 2000"
    })
    void valueGrowsWithWeight(double weight, double expected) {
        assertEquals(expected, createTopaz(weight).calculateValue(), DELTA);
    }

    // ---------- Повний розрахунок ----------
    @Test
    void calculatesTotalValue() {
        Topaz topaz = new Topaz("Імперський", 3.2, 70, ClarityGrade.VS2,
                9.2, GemColor.ORANGE, Origin.BRAZIL, "NONE");
        // 70 * 3.2 * 1.0 * 3.0 = 672.0
        assertEquals(672.0, topaz.calculateValue(), DELTA);
    }
}