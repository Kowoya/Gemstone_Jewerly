package service;

import model.Gemstone;
import model.Necklace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NecklaceBuilderServiceTest {
    private final NecklaceBuilderService builder = new NecklaceBuilderService();

    private Gemstone gemValue(double value){
        Gemstone gem = mock(Gemstone.class);
        when(gem.calculateValue()).thenReturn(value);
        return gem;
    }

    // ---------- Створене намисто ----------
    @Test
    void necklaceHasGivenIdAndName(){
        Necklace necklace = builder.buildNecklace(7, "Вечірнє намисто", 1000,
                List.of(gemValue(100)), 5);
        assertEquals(7, necklace.getId());
        assertEquals("Вечірнє намисто", necklace.getName());
    }

    // ---------- Відбір каменів ----------
    @Test
    void takesAllGemstonesWhenTheyFit(){
        Gemstone diamond = gemValue(11681.28);
        Gemstone ruby = gemValue(5862.45);
        Gemstone topaz = gemValue(672.0);
        Necklace necklace = builder.buildNecklace(1, "Test", 20000,
                List.of(diamond, ruby, topaz), 5);
        assertEquals(List.of(diamond, ruby, topaz), necklace.getGemstones());
    }

    @Test
    void skipsGemstoneThatExceedsRemainingBudget(){
        Gemstone diamond = gemValue(11681.28);
        Gemstone ruby = gemValue(5862.45);
        Gemstone topaz = gemValue(672.0);
        Necklace necklace = builder.buildNecklace(1, "Test", 15000,
                List.of(diamond, ruby, topaz), 5);
        assertEquals(List.of(diamond, topaz), necklace.getGemstones());
    }

    @Test
    void skipsTooExpensiveGemstone(){
        Gemstone expensive = gemValue(5000);
        Gemstone middle = gemValue(300);
        Gemstone cheap = gemValue(100);
        Necklace necklace = builder.buildNecklace(1, "Test", 1000,
                List.of(expensive, middle, cheap), 5);
        assertEquals(List.of(middle, cheap), necklace.getGemstones());
    }

    @Test
    void gemstoneThatExactlyFillsBudgetIsTaken(){
        Gemstone first = gemValue(1000);
        Gemstone second = gemValue(200);
        Necklace necklace = builder.buildNecklace(1, "Test", 1000,
                List.of(first, second), 5);
        assertEquals(List.of(first), necklace.getGemstones());
    }

    @Test
    void stopsWhenMaxCountReached(){
        Gemstone first = gemValue(500);
        Gemstone second = gemValue(400);
        Gemstone third = gemValue(300);
        Gemstone fourth = gemValue(200);
        Necklace necklace = builder.buildNecklace(1, "Test", 2000,
                List.of(fourth, third, second, first), 3);
        assertEquals(List.of(first, second, third), necklace.getGemstones());
    }

    @Test
    void returnsEmptyNecklaceWhenNothingFits(){
        Necklace necklace = builder.buildNecklace(1, "Test", 100,
                List.of(gemValue(500), gemValue(300)), 5);
        assertTrue(necklace.getGemstones().isEmpty());
    }

    @Test
    void returnsEmptyNecklaceWhenNoGemstonesAvailable(){
        Necklace necklace = builder.buildNecklace(1, "Test", 1000,
                List.of(), 5);
        assertTrue(necklace.getGemstones().isEmpty());
    }

    // ---------- Вхідні дані не змінюються ----------
    @Test
    void doesNotChangeAvailableList(){
        Gemstone cheap = gemValue(100);
        Gemstone expensive = gemValue(5000);
        List<Gemstone> available = new ArrayList<>(List.of(cheap, expensive));

        builder.buildNecklace(1, "Test", 1000, available, 5);
        assertEquals(List.of(cheap, expensive), available);
    }

    @Test
    void checksValueOfEveryAvailableGemstone(){
        Gemstone first = gemValue(100);
        Gemstone second = gemValue(200);
        builder.buildNecklace(1, "Test", 1000,
                List.of(first, second), 5);
        verify(first, atLeastOnce()).calculateValue();
        verify(second, atLeastOnce()).calculateValue();
    }

    // ---------- Некоректні параметри ----------
    @ParameterizedTest
    @CsvSource({
            "0, 5",
            "-100, 5",
            "1000, 0",
            "1000, -1"
    })
    void invalidBudgetOrCountThrowsException(double budget, int count){
        assertThrows(IllegalArgumentException.class,
                () -> builder.buildNecklace(1, "Test", budget, List.of(gemValue(100)),
                        count));
    }
}
