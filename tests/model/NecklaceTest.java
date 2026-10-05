package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NecklaceTest {
    private static final double DELTA = 0.001;

    private Gemstone gemValue(double value){
        Gemstone gem = mock(Gemstone.class);
        when(gem.calculateValue()).thenReturn(value);
        return gem;
    }

    private Gemstone gemWeight(double weight){
        Gemstone gem = mock(Gemstone.class);
        when(gem.getWeightCarats()).thenReturn(weight);
        return gem;
    }

    private Gemstone gemTransparency(double transparency){
        Gemstone gem = mock(Gemstone.class);
        when(gem.getTransparencyIndex()).thenReturn(transparency);
        return gem;
    }

    // ---------- Створення ----------
    @Test
    void constructorStoresIdAndName(){
        Necklace necklace = new Necklace(1, "Вечірнє намисто");
        assertEquals(1, necklace.getId());
        assertEquals("Вечірнє намисто", necklace.getName());
    }

    @Test
    void necklaceIsEmpty(){
        Necklace newNecklace = new Necklace(1, "Test");
        assertEquals(0, newNecklace.getGemstoneCount());
        assertTrue(newNecklace.getGemstones().isEmpty());
    }

    @Test
    void constructorStoresGivenGemstones(){
        Gemstone first = mock(Gemstone.class);
        Gemstone second = mock(Gemstone.class);

        Necklace necklace = new Necklace(1, "Test", List.of(first, second));

        assertEquals(List.of(first, second), necklace.getGemstones());
    }

    @Test
    void constructorCopiesGemstoneList(){
        List<Gemstone> source = new ArrayList<>();
        source.add(mock(Gemstone.class));
        Necklace necklace = new Necklace(1, "Test", source);

        source.add(mock(Gemstone.class));
        assertEquals(1, necklace.getGemstoneCount());
    }

    @Test
    void nullNameThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new Necklace(1, null));
    }

    @Test
    void emptyNameThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new Necklace(1, ""));
    }

    @Test
    void blankNameThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new Necklace(1, " "));
    }

    @Test
    void nullGemstoneThrowsException(){
        assertThrows(IllegalArgumentException.class,
                () -> new Necklace(1, "Test", null));
    }

    // ---------- Додавання ----------
    @Test
    void addGemstonesIntoNecklace(){
        Necklace necklace = new Necklace(1, "Test");
        Gemstone gem = mock(Gemstone.class);

        necklace.addGemstone(gem);
        assertEquals(List.of(gem), necklace.getGemstones());
    }

    @Test
    void addNullGemstoneThrowsException(){
        Necklace necklace = new Necklace(1, "Test");
        assertThrows(IllegalArgumentException.class,
                () -> necklace.addGemstone(null));
    }

    // ---------- Видалення ----------
    @Test
    void removeGemstoneFromNecklace(){
        Gemstone gem = mock(Gemstone.class);
        Necklace necklace = new Necklace(1, "Test", List.of(gem));

        assertTrue(necklace.removeGemstone(gem));
        assertEquals(0, necklace.getGemstoneCount());
    }

    @Test
    void removeMissingGemstoneReturnsFalse(){
        Necklace necklace = new Necklace(1, "Test", List.of(mock(Gemstone.class)));

        assertFalse(necklace.removeGemstone(mock(Gemstone.class)));
        assertEquals(1, necklace.getGemstoneCount());
    }

    @Test
    void listCannotBeModifiedFromOutside(){
        Necklace necklace = new Necklace(1, "Test");
        assertThrows(UnsupportedOperationException.class,
                () -> necklace.getGemstones().add(mock(Gemstone.class)));
    }

    // ---------- Загальна вага ----------
    @ParameterizedTest
    @CsvSource({
            "1.2, 0.95, 3.2, 5.35",
            "1.0, 2.0, 3.0, 6.0"
    })
    void totalWeightIsSumOfWeights(double first, double second,
                                   double third, double expected){
        Necklace necklace = new Necklace(1, "Test", List.of(gemWeight(first),
                gemWeight(second), gemWeight(third)));
        assertEquals(expected, necklace.getTotalWeightCarats(), DELTA);
    }

    @Test
    void totalWeightOfEmptyNecklaceIsZero(){
        assertEquals(0, new Necklace(1, "Test").getTotalWeightCarats(), DELTA);
    }

    // ---------- Загальна вартість ----------
    @Test
    void totalValueIsSumOfValues(){
        Gemstone diamond = gemValue(11681.28);
        Gemstone ruby = gemValue(5862.45);
        Gemstone topaz = gemValue(672.0);
        Necklace necklace = new Necklace(1, "Test", List.of(diamond, ruby, topaz));

        assertEquals(18215.73, necklace.getTotalValue(), DELTA);
        verify(diamond, times(1)).calculateValue();
        verify(ruby, times(1)).calculateValue();
        verify(topaz, times(1)).calculateValue();
    }

    @Test
    void totalValueIsRoundedToTwoDecimals(){
        Necklace necklace = new Necklace(1, "Test", List.of(gemValue(100.111), gemValue(200.222)));
        // 100.111 + 200.222 = 300.333 -> 300.33
        assertEquals(300.33, necklace.getTotalValue(), DELTA);
    }

    @Test
    void zeroValueOfEmptyNecklaceIsZero(){
        assertEquals(0, new Necklace(1, "Test").getTotalValue(), DELTA);
    }

    // ---------- Сортування за цінністю ----------
    @Test
    void sortByValue(){
        Gemstone cheap = gemValue(100);
        Gemstone expensive = gemValue(5000);
        Gemstone middle = gemValue(1000);
        Necklace necklace = new Necklace(1, "Test", List.of(cheap, expensive, middle));

        necklace.sortByValue();
        assertEquals(List.of(expensive, middle, cheap), necklace.getGemstones());
    }

    // ---------- Пошук за прозорістю ----------
    @Test
    void findsGemstonesInTransparencyRange(){
        Gemstone cloudy = gemTransparency(5.0);
        Gemstone clear = gemTransparency(8.0);
        Gemstone veryClear = gemTransparency(9.5);
        Necklace necklace = new Necklace(1, "Test", List.of(cloudy, clear, veryClear));

        List<Gemstone> found = necklace.sortByClarity(7.0, 9.0);

        assertEquals(List.of(clear), found);
    }

    @Test
    void transparencyRangeIncludesBoundaries(){
        Gemstone atMin = gemTransparency(7.0);
        Gemstone atMax = gemTransparency(9.0);
        Necklace necklace = new Necklace(1, "Test", List.of(atMin, atMax));

        List<Gemstone> found = necklace.sortByClarity(7.0, 9.0);
        assertEquals(List.of(atMin, atMax), found);
    }

    @Test
    void noGemstonesFoundOutsideRange(){
        Necklace necklace = new Necklace(1, "Test", List.of(gemTransparency(3.0), gemTransparency(9.5)));
        assertTrue(necklace.sortByClarity(7.0, 9.0).isEmpty());
    }

    @Test
    void searchDoesNotChangeNecklace(){
        Necklace necklace = new Necklace(1, "Test", List.of(gemTransparency(3.0), gemTransparency(8.0)));

        necklace.sortByClarity(7.0, 9.0);
        assertEquals(2, necklace.getGemstoneCount());
    }
}
