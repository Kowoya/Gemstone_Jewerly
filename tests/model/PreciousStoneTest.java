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

class PreciousStoneTest {
    private static class TestPrecousStone extends PreciousStone{
        TestPrecousStone(String certificateNumber, CutType cutType){
            super("Test", 2.5, 1000, ClarityGrade.VS1,
                    8.5, GemColor.BLUE, Origin.BRAZIL, certificateNumber,
                    cutType);
        }

        @Override
        public double calculateValue(){
            return 100.0;
        }
    }

    // ---------- Коректне створення та геттери ----------
    private TestPrecousStone createValid(){
        return new TestPrecousStone("GIA-123", CutType.ROUND);
    }

    @Test
    void constructorStoresAllFields(){
        TestPrecousStone stone = createValid();
        assertEquals("GIA-123", stone.getCertificateNumber());
        assertEquals(CutType.ROUND, stone.getCutType());
    }

    // ---------- Форма огранювання ----------
    @Test
    void cutTypeThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestPrecousStone("GIA-123", null));
    }

    // ---------- Сертифікат ----------
    @Test
    void stoneIsCertificated(){
        assertTrue(new TestPrecousStone("GIA-123", CutType.ROUND).isCertified());
    }

    @Test
    void nullCertificate(){
        assertFalse(new TestPrecousStone(null, CutType.ROUND).isCertified());
    }

    @Test
    void emptyCertificate(){
        assertFalse(new TestPrecousStone("", CutType.ROUND).isCertified());
    }

    @Test
    void blankCertificate(){
        assertFalse(new TestPrecousStone(" ", CutType.ROUND).isCertified());
    }
}
