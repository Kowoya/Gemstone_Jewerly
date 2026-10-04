package model;

import enums.ClarityGrade;
import enums.GemColor;
import enums.Origin;

public abstract class Gemstone {
    private final String name;
    private final double weightCarats;
    private final double pricePerCarat;
    private final ClarityGrade clarity;
    private final double transparencyIndex;
    private final GemColor color;
    private final Origin origin;

    protected Gemstone(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                       double transparencyIndex, GemColor color, Origin origin) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name can't be null");
        }
        if (weightCarats <= 0){
            throw new IllegalArgumentException("Weight can't be less or equal to 0");
        }
        if (pricePerCarat <= 0){
            throw new IllegalArgumentException("Price per Carat can't be less or equal to 0");
        }
        if (transparencyIndex <0 || transparencyIndex > 10){
            throw new IllegalArgumentException("Transparency index have to be between 0 and 10");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color can't be null");
        }
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

    public GemColor getColor(){
        return color;
    }

    public Origin getOrigin(){
        return origin;
    }

    public abstract double calculateValue();
}
