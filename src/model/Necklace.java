package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Намисто - набір каменів. Рахує загальну вагу й вартість,
 * сортує камені за цінністю та шукає їх за прозорістю.
 */

public class Necklace {
    private final int id;
    private final String name;
    private final List<Gemstone> gemstones;

    public Necklace(int id, String name){
        this(id, name, new ArrayList<>());
    }

    public Necklace(int id, String name, List<Gemstone> gemstones) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        if (gemstones == null) {
            throw new IllegalArgumentException("Gemstones can't be null");
        }
        this.id = id;
        this.name = name;
        this.gemstones = new ArrayList<>(gemstones);
    }

    public void addGemstone(Gemstone gemstone){
        if (gemstone == null) {
            throw new IllegalArgumentException("Gemstone can't be null");
        }
        gemstones.add(gemstone);
    }

    public boolean removeGemstone(Gemstone gemstone){
        return gemstones.remove(gemstone);
    }

    public List<Gemstone> getGemstones(){
        return Collections.unmodifiableList(gemstones);
    }

    public int getGemstoneCount() {return gemstones.size();}

    public double getTotalWeightCarats(){
        double total = 0;
        for (Gemstone g : gemstones){
            total += g.getWeightCarats();
        }
        return total;
    }

    public double getTotalValue(){
        double total = 0;
        for (Gemstone g : gemstones){
            total += g.calculateValue();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Сортує камені намиста за цінністю (вартістю каменю).
     * від найдорожчого до найдешевшого
     */
    public void sortByValue(){
        gemstones.sort((a, b) -> Double.compare(b.calculateValue(), a.calculateValue()));
    }

    /** Шукає камені з прозорістю в діапазоні від min до max. */
    public List<Gemstone> sortByClarity(double min, double max){
        List<Gemstone> result = new ArrayList<>();
        for (Gemstone g : gemstones){
            double transparency = g.getTransparencyIndex();
            if (transparency >= min && transparency <= max){
                result.add(g);
            }
        }
        return result;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}
