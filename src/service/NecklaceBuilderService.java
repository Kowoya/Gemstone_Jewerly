package service;

import model.Gemstone;
import model.Necklace;

import java.util.ArrayList;
import java.util.List;

public class NecklaceBuilderService {
    /**
     * Збирає намисто з найцінніших каменів, що вкладаються в бюджет.
     * @param id        ідентифікатор намиста
     * @param name      назва намиста
     * @param available наявні камені
     * @param maxBudget максимальна загальна вартість
     * @param maxCount  максимальна кількість каменів
     * @return нове намисто
     */
    public Necklace buildNecklace(int id, String name, double maxBudget, List<Gemstone> available, int maxCount){
        if (maxBudget <= 0 || maxCount <= 0){
            throw new IllegalArgumentException("Budget and count must be positive");
        }
        List<Gemstone> sorted = new ArrayList<>(available);
        sorted.sort((a,b) -> Double.compare(b.calculateValue(), a.calculateValue()));

        Necklace necklace = new Necklace(id, name);
        double total = 0;
        for (Gemstone g : sorted){
            if (necklace.getGemstoneCount() >= maxCount){
                break;
            }
            double value = g.calculateValue();
            if (total + value <= maxBudget){
                necklace.addGemstone(g);
                total += value;
            }
        }
        return necklace;
    }
}
