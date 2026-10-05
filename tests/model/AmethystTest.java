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
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmethystTest {

    private static final double DELTA = 0.001;

    private Amethyst createAmethyst(ClarityGrade clarity, Origin origin) {
        return new Amethyst("Test", 1.0, 1000, clarity, 8.7, GemColor.PURPLE,
                origin, "NONE");
    }

    private Amethyst createAmethyst(double weight) {
        return new Amethyst("Test", weight, 1000, ClarityGrade.VS1, 8.7,
                GemColor.PURPLE, Origin.UNKNOWN, "NONE");
    }

    // ---------- Колір ----------
    @Test
    void purpleAmethystIsCreated() {
        assertDoesNotThrow(() -> createAmethyst(ClarityGrade.VS1, Origin.UNKNOWN));
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class, names = "PURPLE",
            mode = EnumSource.Mode.EXCLUDE)
    void nonPurpleAmethystThrowsException(GemColor color) {
        assertThrows(IllegalArgumentException.class,
                () -> new Amethyst("Test", 1.0, 1000, ClarityGrade.VS1, 8.7,
                        color, Origin.UNKNOWN, "NONE"));
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
            "SI1, 800",
            "SI2, 800",
            "I1, 500"
    })
    void valueDependsOnClarity(ClarityGrade clarity, double expected) {
        Amethyst amethyst = createAmethyst(clarity, Origin.UNKNOWN);
        assertEquals(expected, amethyst.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @CsvSource({
            "ZAMBIA, 1300",
            "URUGUAY, 1300"
    })
    void valueDependsOnOrigin(Origin origin, double expected) {
        Amethyst amethyst = createAmethyst(ClarityGrade.VS1, origin);
        assertEquals(expected, amethyst.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = Origin.class, names = {"ZAMBIA", "URUGUAY"},
            mode = EnumSource.Mode.EXCLUDE)
    void otherOriginsDoNotChangeValue(Origin origin) {
        Amethyst amethyst = createAmethyst(ClarityGrade.VS1, origin);
        assertEquals(1000, amethyst.calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
            "0.5, 500",
            "1.0, 1000",
            "2.0, 2000"
    })
    void valueGrowsWithWeight(double weight, double expected) {
        assertEquals(expected, createAmethyst(weight).calculateValue(),
                DELTA);
    }

    // ---------- Повний розрахунок ----------
    @Test
    void calculatesTotalValue() {
        Amethyst amethyst = new Amethyst("Сибірський", 4.5, 45,
                ClarityGrade.VS1, 8.7, GemColor.PURPLE, Origin.ZAMBIA,
                "NONE");
        // 45 * 4.5 * 1.0 * 1.3 = 263.25
        assertEquals(263.25, amethyst.calculateValue(), DELTA);
    }
}