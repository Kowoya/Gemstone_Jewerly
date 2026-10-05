package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;

/**
 * Смарагд. Завжди зелений. Включення для смарагдів природні, тому
 * чистота впливає на вартість удвічі слабше, ніж для інших каменів.
 * Ціна сильно залежить від чистоти, форми й походження.
 */

public class Emerald extends PreciousStone{
    public Emerald(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                   double transparencyIndex, GemColor color, Origin origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, certificateNumber, cutType);
        if(color != GemColor.GREEN){
            throw new IllegalArgumentException("Emerald have to be green");
        }
    }

    @Override
    public double calculateValue() {
        double rawValue = getPricePerCarat() * getWeightCarats()
                * clarityMultiplierFor(getClarity())
                * cutTypeMultiplierFor(getCutType())
                * originMultiplierFor(getOrigin());
        return Math.round(rawValue * 100.0) / 100.0;
    }
    //Множник чистоти
    private double clarityMultiplierFor(ClarityGrade grade){
        return switch (grade){
            case FL, IF -> 1.25;
            case VVS1, VVS2 -> 1.15;
            case VS1, VS2 -> 1.05;
            case SI1, SI2 -> 1.0;
            case I1 -> 0.85;
        };
    }
    /**
     * Множник форми огранювання. Для смарагду найкраща класична
     * смарагдова огранка, форми з гострими кутами легко відколюються.
     */
    private double cutTypeMultiplierFor(CutType cut){
        return switch (cut){
            case EMERALD -> 1.1;
            case ASSCHER, RADIANT, CUSHION, OVAL, PEAR -> 1.0;
            case ROUND -> 0.95;
            case PRINCESS, MARQUISE, HEART -> 0.9;
        };
    }
    /**
     * Множник походження. Найцінніші смарагди - колумбійські.
     */
    private double originMultiplierFor(Origin origin){
        return switch (origin){
            case COLOMBIA -> 1.4;
            case AFGHANISTAN -> 1.2;
            case ZAMBIA -> 1.1;
            default -> 1.0;
        };
    }
}
