package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;

/**
 * Діамант. Вартість визначається за "4C": вагою, чистотою, кольором
 * та огранюванням. Походження на ціну діаманта практично не впливає,
 * на відміну від кольорових дорогоцінних каменів.
 */

public class Diamond extends PreciousStone{
    public Diamond(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                   double transparencyIndex, GemColor color, Origin origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, certificateNumber, cutType);
    }

    @Override
    public double calculateValue() {
        double weight;
        if (getWeightCarats() >= 1) {
            weight = getWeightCarats() * getWeightCarats();
        } else {
            weight = getWeightCarats();
        }
        double rawValue = getPricePerCarat() * weight
                * clarityMultiplierFor(getClarity())
                * colorMultiplierFor(getColor())
                * cutTypeMultiplierFor(getCutType());
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

    private double colorMultiplierFor(GemColor colour){
        return switch (colour){
            case BROWN -> 0.8;
            case YELLOW -> 1.3;
            case BLUE -> 3.0;
            case PINK -> 4.0;
            case RED -> 6.0;
            default -> 1.0;
        };
    }

    private double cutTypeMultiplierFor(CutType cut){
        return switch (cut){
            case ROUND -> 1.2;
            case PRINCESS -> 1.05;
            case OVAL, CUSHION, RADIANT -> 1.0;
            case PEAR, EMERALD, ASSCHER -> 0.95;
            case MARQUISE, HEART -> 0.9;
        };
    }

}
