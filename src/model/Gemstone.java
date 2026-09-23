package model;

import enums.ClarityGrade;

public abstract class Gemstone {
    private final String name;
    private final double weightCarats;
    private final double pricePerCarat;
    private final ClarityGrade clarity;
    private final double transparencyIndex;
    private final String color;
    private final String origin;

    protected Gemstone(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity, double transparencyIndex, String color, String origin) {
        this.name = name;
        this.weightCarats = weightCarats;
        this.pricePerCarat = pricePerCarat;
        this.clarity = clarity;
        this.transparencyIndex = transparencyIndex;
        this.color = color;
        this.origin = origin;
    }

    public String getName(){
        return name;
    }

    public double getWeightCarats(){
        return weightCarats;
    }

    public double getPricePerCarat(){
        return pricePerCarat;
    }

    public ClarityGrade getClarity(){
        return clarity;
    }

    public double getTransparencyIndex(){
        return transparencyIndex;
    }

    public String getColor(){
        return color;
    }

    public String getOrigin(){
        return origin;
    }

    public abstract double calculateValue();
}
