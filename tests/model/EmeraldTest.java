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

class EmeraldTest {
    private static final double DELTA = 0.001;

    private Emerald createEmerald(ClarityGrade clarity, CutType cut, Origin origin){
        return new Emerald("Test", 1.0, 1000, clarity,
                7.0, GemColor.GREEN, origin, "GRS-123", cut);
    }

    private Emerald createEmerald(double weight){
        return new Emerald("Test", weight, 1000, ClarityGrade.SI1, 7.0,
                GemColor.GREEN, Origin.BRAZIL, "GRS-123", CutType.OVAL);
    }

    // ---------- Колір ----------
    @Test
    void greenEmeraldIsCreated(){
        assertDoesNotThrow(() -> createEmerald(ClarityGrade.SI1, CutType.OVAL, Origin.BRAZIL));
    }

    @ParameterizedTest
    @EnumSource(value = GemColor.class, names = "GREEN",
        mode = EnumSource.Mode.EXCLUDE)
    void nonGreenEmeraldThrowsException(GemColor color){
        assertThrows(IllegalArgumentException.class,
                () -> new Emerald("Test", 1.0, 1000, ClarityGrade.SI1,
                        7.0, color, Origin.BRAZIL, "GRS-123",
                        CutType.OVAL));
    }

    // ---------- Чистота ----------
    @ParameterizedTest
    @CsvSource({
            "FL, 1250",
            "IF, 1250",
            "VVS1, 1150",
            "VVS2, 1150",
            "VS1, 1050",
            "VS2, 1050",
            "SI1, 1000",
            "SI2, 1000",
            "I1, 850"
    })

    void valueDependsOnClarity(ClarityGrade clarity, double expected){
        Emerald emerald = createEmerald(clarity, CutType.OVAL, Origin.BRAZIL);
        assertEquals(expected, emerald.calculateValue(), DELTA);
    }

    // ---------- Форма огранювання ----------
    @ParameterizedTest
    @CsvSource({
            "EMERALD, 1100",
            "ASSCHER, 1000",
            "RADIANT, 1000",
            "CUSHION, 1000",
            "OVAL, 1000",
            "PEAR, 1000",
            "ROUND, 950",
            "PRINCESS, 900",
            "MARQUISE, 900",
            "HEART, 900"
    })

    void valueDependsOnCutType(CutType cut, double expected){
        Emerald emerald = createEmerald(ClarityGrade.SI1, cut, Origin.BRAZIL);
        assertEquals(expected, emerald.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @CsvSource({
            "COLOMBIA, 1400",
            "AFGHANISTAN, 1200",
            "ZAMBIA, 1100"
    })

    void valueDependsOnOrigin(Origin origin, double expected){
        Emerald emerald = createEmerald(ClarityGrade.SI1, CutType.OVAL, origin);
        assertEquals(expected, emerald.calculateValue(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(value = Origin.class, names = {"COLOMBIA", "AFGHANISTAN", "ZAMBIA"},
        mode = EnumSource.Mode.EXCLUDE)

    void otherOriginsDoNotChangeValue(Origin origin){
        Emerald emerald = createEmerald(ClarityGrade.SI1, CutType.OVAL, origin);
        assertEquals(1000, emerald.calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
            "0.5, 500",
            "1.0, 1000",
            "2.0, 2000"
    })

    void valueGrowsWithWeight(double weight, double expected){
        assertEquals(expected, createEmerald(weight).calculateValue(), DELTA);
    }

    // ---------- Повний розрахунок ----------
    @Test
    void createTotalValue(){
        Emerald emerald = new Emerald("Муссо", 1.3, 2600,
                ClarityGrade.I1, 6.8, GemColor.GREEN, Origin.COLOMBIA,
                "GRS-123", CutType.EMERALD);
        // 2600 * 1.3 * 0.85 * 1.1 * 1.4 = 4424.42
        assertEquals(4424.42, emerald.calculateValue(), DELTA);
    }
}
