package model;

import enums.ClarityGrade;

public class Garnet extends SemiPreciousStone{
    public Garnet(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                 double transparencyIndex, String color, String origin, String treatmentType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, treatmentType);
    }

    @Override
    public double calculateValue(){
        double rawValue = getPricePerCarat() * getWeightCarats();
        return Math.round(rawValue * 100.0) / 100.0;
    }
}
