package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RubyTest {
    private static final double DELTA = 0.001;

    private Ruby createRuby(ClarityGrade clarity, CutType cut, Origin origin){
        return new Ruby("Test", 1.0, 1000, clarity,
                8.2, GemColor.RED, origin, "GRS-234",
                cut);
    }

    private Ruby createRuby(double weight){
        return new Ruby("Test", weight, 1000, ClarityGrade.SI1,
                8.2, GemColor.RED, Origin.UNKNOWN, "GRS-234",
                CutType.ROUND);
    }

    // ---------- Колір ----------
    @Test
    void redRubyIsCreated(){
        assertDoesNotThrow(() -> createRuby(ClarityGrade.SI1, CutType.ROUND, Origin.UNKNOWN));
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class, names = "RED",
        mode = EnumSource.Mode.EXCLUDE)

    void nonRedRubyThrowsException(GemColor color){
        assertThrows(IllegalArgumentException.class,
                () -> new Ruby("Test", 1.0, 1000, ClarityGrade.SI1, 7.0,
                        color, Origin.UNKNOWN, "GRS-234", CutType.ROUND));
    }

    // ---------- Чистота ----------
    @ParameterizedTest
    @CsvSource({
            "FL, 1400",
            "IF, 1400",
            "VVS1, 1250",
            "VVS2, 1250",
            "VS1, 1100",
            "VS2, 1100",
            "SI1, 1000",
            "SI2, 1000",
            "I1, 750"
    })

    void valueDependsOnClarity(ClarityGrade clarity, double expected){
        Ruby ruby = createRuby(clarity, CutType.ROUND, Origin.UNKNOWN);
        assertEquals(expected, ruby.calculateValue(), DELTA);
    }

    // ---------- Форма огранювання ----------
    @ParameterizedTest
    @CsvSource({
            "CUSHION, 1100",
            "OVAL, 1100",
            "RADIANT, 1000",
            "PEAR, 1000",
            "ROUND, 1000",
            "EMERALD, 950",
            "ASSCHER, 950",
            "HEART, 950",
            "PRINCESS, 900",
            "MARQUISE, 900"
    })

    void valueDependsOnCutType(CutType cut, double expected){
        Ruby ruby = createRuby(ClarityGrade.SI1, cut, Origin.UNKNOWN);
        assertEquals(expected, ruby.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @CsvSource({
            "MYANMAR, 1500",
            "MOZAMBIQUE, 1150",
            "MADAGASCAR, 1050",
            "SRI_LANKA, 1050",
            "THAILAND, 950"
    })

    void valueDependsOnOrigin(Origin origin, double expected){
        Ruby ruby = createRuby(ClarityGrade.SI1, CutType.ROUND, origin);
        assertEquals(expected, ruby.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = Origin.class, names = {"MYANMAR", "MOZAMBIQUE", "MADAGASCAR",
        "SRI_LANKA", "THAILAND"}, mode = EnumSource.Mode.EXCLUDE)

    void otherOriginsDoNotChangeValue(Origin origin){
        Ruby ruby = createRuby(ClarityGrade.SI1, CutType.ROUND, origin);
        assertEquals(1000, ruby.calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
        "0.5, 500",
        "1.0, 1000",
        "2.0, 2000"
    })

    void valueGrowsWithWeight(double weight, double expected){
        assertEquals(expected, createRuby(weight).calculateValue(), DELTA);
    }

    // ---------- Повний розрахунок ----------
    @Test
    void calculateTotalValue(){
        Ruby ruby = new Ruby("Голубина кров", 0.95, 3400, ClarityGrade.VS1,
                8.2, GemColor.RED, Origin.MYANMAR, "GRS-1100235",
                CutType.CUSHION);
        // 3400 * 0.95 * 1.1 * 1.1 * 1.5 = 5862.45
        assertEquals(5862.45, ruby.calculateValue(), DELTA);
    }
}
