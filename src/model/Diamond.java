package model;

import enums.ClarityGrade;
import enums.CutType;

public class Diamond extends PreciousStone{
    public Diamond(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                   double transparencyIndex, String color, String origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, certificateNumber, cutType);
    }

    @Override
    public double calculateValue(){
        double weightSquared = getWeightCarats() * getWeightCarats();
        double clarityMultiplier = clarityMultiplierFor(getClarity());
        double rawValue = getPricePerCarat() * weightSquared * clarityMultiplier;
        return Math.round(rawValue * 100.0) / 100.0;
    }

    private double clarityMultiplierFor(ClarityGrade grade){
        return switch (grade){
            case FL, IF -> 1.5;
            case VVS1, VVS2 -> 1.3;
            case VS1, VS2 -> 1.1;
            case SI1, SI2 -> 1.0;
            case I1 -> 0.7;
        };
    }

}
