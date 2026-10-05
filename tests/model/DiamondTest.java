package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DiamondTest {
    private static final double DELTA = 0.001;

    private Diamond createDiamond(double weight, ClarityGrade clarity,
                                  GemColor color, CutType cut){
        return new Diamond("Test", weight, 1000, clarity, 9.0,
                color, Origin.BOTSWANA, "GIA-123", cut);
    }

    private Diamond createDiamond(Origin origin){
        return new Diamond("Test", 1.0, 1000,  ClarityGrade.SI1, 9.0,
                GemColor.WHITE, origin, "GIA-123", CutType.OVAL);
    }

    // ---------- Чистота ----------
    @ParameterizedTest
    @CsvSource({
            "FL, 1500",
            "IF, 1500",
            "VVS1, 1300",
            "VVS2, 1300",
            "VS1, 1100",
            "VS2, 1100",
            "SI1, 1000",
            "SI2, 1000",
            "I1, 700"
    })

    void valueDependsOnClarity(ClarityGrade clarity, double expected){
        Diamond diamond = createDiamond(1.0, clarity, GemColor.WHITE,
                CutType.OVAL);
        assertEquals(expected, diamond.calculateValue(), DELTA);
    }

    // ---------- Колір ----------
    @ParameterizedTest
    @CsvSource({
            "WHITE, 1000",
            "BROWN, 800",
            "YELLOW, 1300",
            "BLUE, 3000",
            "PINK, 4000",
            "RED, 6000",
            "GREEN, 1000",
            "PURPLE, 1000",
            "ORANGE, 1000"
    })

    void valueDependsOnColor(GemColor color, double expected){
        Diamond diamond = createDiamond(1.0, ClarityGrade.SI1, color,
                CutType.OVAL);
        assertEquals(expected, diamond.calculateValue(), DELTA);
    }

    // ---------- Форма огранювання ----------
    @ParameterizedTest
    @CsvSource({
            "ROUND, 1200",
            "PRINCESS, 1050",
            "OVAL, 1000",
            "CUSHION, 1000",
            "RADIANT, 1000",
            "PEAR, 950",
            "EMERALD, 950",
            "ASSCHER, 950",
            "MARQUISE, 900",
            "HEART, 900"
    })

    void valueDependsOnCutType(CutType cut, double expected){
        Diamond diamond = createDiamond(1.0, ClarityGrade.SI1, GemColor.WHITE,
                cut);
        assertEquals(expected, diamond.calculateValue(), DELTA);
    }

    // ---------- Вага ----------
    @ParameterizedTest
    @CsvSource({
            "0.5, 500",
            "1.0, 1000",
            "1.5, 2250",
            "2.0, 4000"
    })

    void valueGrowsSquaredFromOneCarat(double weight, double expected){
        Diamond diamond = createDiamond(weight, ClarityGrade.SI1, GemColor.WHITE,
                CutType.OVAL);
        assertEquals(expected, diamond.calculateValue(), DELTA);
    }

    // ---------- Походження ----------
    @ParameterizedTest
    @EnumSource(Origin.class)

    void originDoesNotEffectValue(Origin origin){
        assertEquals(1000, createDiamond(origin).calculateValue(), DELTA);
    }

    // ---------- Повний розрахунок і округлення ----------
    @Test
    void calculateTotalValue(){
        Diamond diamond = new Diamond("Зоря", 1.2, 5200, ClarityGrade.VVS1,
                9.6, GemColor.WHITE, Origin.BOTSWANA, "GIA-2231456",
                CutType.ROUND);
        // 5200 * 1.2^2 * 1.3 * 1.0 * 1.2 = 11681.28
        assertEquals(11681.28, diamond.calculateValue(), DELTA);
    }

    @Test
    void valueIsRoundedToTwoDecimals(){
        Diamond diamond = new Diamond("Test", 1.0, 333.33,
                ClarityGrade.SI1, 9.0, GemColor.WHITE, Origin.BOTSWANA,
                "GIA-123", CutType.OVAL);
        //333.333 * 1.0 * 1.0 * 1.0 * 1.0 = 333.333
        assertEquals(333.33, diamond.calculateValue(), DELTA);
    }
}
