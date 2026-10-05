package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SemiPreciousStoneTest {
    private static class TestSemiPreciousStone extends SemiPreciousStone{
        TestSemiPreciousStone(String treatmentType){
            super("Test", 2.5, 1000, ClarityGrade.VS1,
            8.5, GemColor.BLUE, Origin.BRAZIL, treatmentType);
        }

        @Override
        public double calculateValue(){
            return 100.0;
        }
    }

    // ---------- Коректне створення та геттери ----------
    private TestSemiPreciousStone createValid(){
        return new TestSemiPreciousStone("HEATED");
    }

    @Test
    void constructorStoresAllFields(){
        TestSemiPreciousStone stone = createValid();
        assertEquals("HEATED", stone.getTreatmentType());
    }

    // ---------- Тип обробки ----------
    @Test
    void nullTreatmentType(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestSemiPreciousStone(null));
    }

    @Test
    void emptyTreatmentType(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestSemiPreciousStone(""));
    }

    @Test
    void blankTreatmentType(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestSemiPreciousStone(" "));
    }
}
