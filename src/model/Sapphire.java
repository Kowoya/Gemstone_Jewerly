package model;

import enums.ClarityGrade;
import enums.CutType;

public class Sapphire extends PreciousStone{
    public Sapphire(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                   double transparencyIndex, String color, String origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, certificateNumber, cutType);
    }

    @Override
    public double calculateValue(){
        double rawValue = getPricePerCarat() * getWeightCarats();
        return Math.round(rawValue * 100.0) / 100.0;
    }
}
