package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GemstoneTest {
    private Gemstone createGemstone() {
        return new Diamond("Зоря", 1.2, 5200, ClarityGrade.VVS1,
                9.6, GemColor.WHITE, Origin.BOTSWANA, "GIA-1", CutType.ROUND);
    }

    private static class TestGemstone extends Gemstone{
        TestGemstone(String name, double weightCarats, double pricePerCarat,
                     ClarityGrade clarity, double transparencyIndex,
                     GemColor color, Origin origin){
            super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin);
        }

        @Override
        public double calculateValue(){
            return 100.0;
        }
    }

    // ---------- Коректне створення та геттери ----------
    private TestGemstone createValid(){
        return new TestGemstone("Test", 2.5, 1000, ClarityGrade.VS1,
                8.5, GemColor.BLUE, Origin.BRAZIL);
    }

    @Test
    void constructorStoresAllFields(){
        TestGemstone gem = createValid();
        assertEquals("Test", gem.getName());
        assertEquals(2.5, gem.getWeightCarats());
        assertEquals(1000, gem.getPricePerCarat());
        assertEquals(ClarityGrade.VS1, gem.getClarity());
        assertEquals(8.5, gem.getTransparencyIndex());
        assertEquals(GemColor.BLUE, gem.getColor());
        assertEquals(Origin.BRAZIL, gem.getOrigin());
    }
    //----------- ID -------------
    @Test
    void newGemstoneHasZeroId() {
        Gemstone gemstone = createGemstone();

        assertEquals(0, gemstone.getId());
    }

    @Test
    void setIdSavesPositiveId() {
        Gemstone gemstone = createGemstone();

        gemstone.setId(5);

        assertEquals(5, gemstone.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void setIdThrowsForNotPositiveId(int id) {
        Gemstone gemstone = createGemstone();

        assertThrows(IllegalArgumentException.class, () -> gemstone.setId(id));
    }

    // ---------- Назва ----------
    @Test
    void nullNameThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone(null, 2.5, 1000, ClarityGrade.VS1,
                8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    @Test
    void blankNameThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone(" ", 2.5, 1000, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    @Test
    void emptyNameThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("", 2.5, 1000, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    // ---------- Вага ----------
    @Test
    void zeroWeightThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 0, 1000, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    @Test
    void negativeWeightThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", -1.5, 1000, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    // ---------- Ціна ----------
    @Test
    void zeroPriceThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, 0, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    @Test
    void negativePriceThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, -100, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    // ---------- Чистота ----------
    @Test
    void nullClarityThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, 1000, null,
                        8.5, GemColor.BLUE, Origin.BRAZIL));
    }

    // ---------- Прозорість ----------
    @Test
    void transparencyBelowZeroThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, 1000, ClarityGrade.VS1,
                        -0.1, GemColor.BLUE, Origin.BRAZIL));
    }

    @Test
    void transparencyAboveTenThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, 1000, ClarityGrade.VS1,
                        10.1, GemColor.BLUE, Origin.BRAZIL));
    }

    @Test
    void transparencyBoundariesAreAllowed(){
        assertDoesNotThrow(() -> new TestGemstone("Test", 2.5, 1000,
                ClarityGrade.VS1, 0, GemColor.BLUE, Origin.BRAZIL));
        assertDoesNotThrow(() -> new TestGemstone("Test", 2.5, 1000,
                ClarityGrade.VS1, 10, GemColor.BLUE, Origin.BRAZIL));
    }

    // ---------- Колір ----------
    @Test
    void nullColorThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, 1000, ClarityGrade.VS1,
                        8.5, null, Origin.BRAZIL));
    }

    // ---------- Походження ----------
    @Test
    void nullOriginThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new TestGemstone("Test", 2.5, 1000, ClarityGrade.VS1,
                        8.5, GemColor.BLUE, null));
    }
}
