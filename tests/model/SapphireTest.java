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

class SapphireTest {
    private static final double DELTA = 0.001;

    private Sapphire createSapphire(ClarityGrade clarity, GemColor color, CutType cut, Origin origin){
        return new Sapphire("Test", 1.0, 1000, clarity,
                6.0, color, origin, "SSEF-123", cut);
    }

    private Sapphire createSapphire(double weight){
        return new Sapphire("Test", weight, 1000, ClarityGrade.SI1,
                6.0, GemColor.BLUE, Origin.BRAZIL, "SSEF-123", CutType.EMERALD);
    }

    // ---------- Колір ----------
    @Test
    void redSapphireThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> createSapphire(ClarityGrade.SI1, GemColor.RED, CutType.EMERALD, Origin.UNKNOWN));
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class, names = "RED",
        mode = EnumSource.Mode.EXCLUDE)

    void nonRedSapphireIsCreated(GemColor color){
        assertDoesNotThrow(() -> createSapphire(ClarityGrade.SI1, color, CutType.EMERALD, Origin.UNKNOWN));
    }

    @ParameterizedTest
    @CsvSource({
            "ORANGE, 1235",
            "BLUE, 950",
            "PINK, 855",
            "PURPLE, 665",
            "YELLOW, 570",
            "GREEN, 475",
            "WHITE, 380"
    })

    void valueDependsOnColor(GemColor color, double expected){
        Sapphire sapphire = createSapphire(ClarityGrade.SI1, color, CutType.EMERALD, Origin.UNKNOWN);
        assertEquals(expected, sapphire.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class, names = {"ORANGE", "BLUE", "PINK", "PURPLE",
            "YELLOW", "GREEN", "WHITE", "RED"}, mode = EnumSource.Mode.EXCLUDE)
    void otherColorsUseDefaultMultiplier(GemColor color){
        Sapphire sapphire = createSapphire(ClarityGrade.SI1, color, CutType.EMERALD, Origin.UNKNOWN);
        assertEquals(475, sapphire.calculateValue(), DELTA);
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
            "SI1, 950",
            "SI2, 950",
            "I1, 700"
    })

    void valueDependsOnClarity(ClarityGrade clarity, double expected){
        Sapphire sapphire = createSapphire(clarity, GemColor.BLUE, CutType.EMERALD, Origin.UNKNOWN);
        assertEquals(expected, sapphire.calculateValue(), DELTA);
    }

    // ---------- Форма огранювання ----------
    @ParameterizedTest
    @CsvSource({
            "CUSHION, 1045",
            "OVAL, 1045",
            "ROUND, 997.5",
            "EMERALD, 950",
            "RADIANT, 950",
            "PEAR, 950",
            "ASSCHER, 902.5",
            "HEART, 902.5",
            "PRINCESS, 855",
            "MARQUISE, 855"
    })

    void valueDependsOnCutType(CutType cut, double expected){
        Sapphire sapphire = createSapphire(ClarityGrade.SI1, GemColor.BLUE, cut, Origin.UNKNOWN);
        assertEquals(expected, sapphire.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @CsvSource({
            "KASHMIR, 1710",
            "MYANMAR, 1330",
            "SRI_LANKA, 1235",
            "MADAGASCAR, 1045",
            "THAILAND, 855",
            "AUSTRALIA, 855"
    })

    void valueDependsOnOrigin(Origin origin, double expected){
        Sapphire sapphire = createSapphire(ClarityGrade.SI1, GemColor.BLUE, CutType.EMERALD, origin);
        assertEquals(expected, sapphire.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = Origin.class, names = {"KASHMIR", "MYANMAR", "SRI_LANKA",
        "MADAGASCAR", "THAILAND", "AUSTRALIA"}, mode = EnumSource.Mode.EXCLUDE)
    void otherOriginsDoNotChangeValue(Origin origin){
        Sapphire sapphire = createSapphire(ClarityGrade.SI1, GemColor.BLUE, CutType.EMERALD, origin);
        assertEquals(950, sapphire.calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
            "0.5, 475",
            "1.0, 950",
            "2.0, 1900"
    })

    void valueGrowsWithWeight(double weight, double expected){
        assertEquals(expected, createSapphire(weight).calculateValue(), DELTA);
    }

    // ---------- Повний розрахунок ----------
    @Test
    void calculateTotalValue(){
        Sapphire sapphire = new Sapphire("Кашмірська ніч", 1.1, 2900,
                ClarityGrade.VVS2, 8.5, GemColor.BLUE, Origin.KASHMIR,
                "SSEF-123", CutType.CUSHION);
        // 2900 * 1.1 * 1.25 * 1.0 * 1.1 * 1.8 = 7895.25
        assertEquals(7895.25, sapphire.calculateValue(), DELTA);
    }
}
