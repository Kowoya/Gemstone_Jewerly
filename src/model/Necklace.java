package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Necklace {
    private final int id;
    private final String name;
    private final List<Gemstone> gemstones;

    public Necklace(int id, String name, List<Gemstone> gemstones){
        this.id = id;
        this.name = name;
        this.gemstones = new ArrayList<>();
    }

    public void addGemstone(Gemstone gemstone){
        gemstones.add(gemstone);
    }

    public boolean removeGemstone(Gemstone gemstone){
        return gemstones.remove(gemstone);
    }

    public List<Gemstone> getGemstones(){
        return Collections.unmodifiableList(gemstones);
    }

    public double getTotalWeightCarats(){
        return gemstones.stream()
                .mapToDouble(Gemstone::getWeightCarats)
                .sum();
    }

    public double getTotalValue(){
        return gemstones.stream()
                .mapToDouble(Gemstone::calculateValue)
                .sum();
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}
