package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;

import javax.lang.model.util.Elements;

/**
 * Рубін - червоний різновид корунду. Ціна сильно залежить від
 * походження: найцінніші рубіни видобувають у М'янмі (Бірмі).
 */

public class Ruby extends PreciousStone{
    public Ruby(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                double transparencyIndex, GemColor color, Origin origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, certificateNumber, cutType);
        if (color != GemColor.RED){
            throw new IllegalArgumentException("Ruby have to be red");
        }
    }

    @Override
    public double calculateValue(){
        double rawValue = getPricePerCarat() * getWeightCarats()
                * clarityMultiplierFor(getClarity())
                * cutTypeMultiplierFor(getCutType())
                * originMultiplierFor(getOrigin());
        return Math.round(rawValue * 100.0) / 100.0;
    }
    /**
     * Множник чистоти. Невеликі включення для рубінів звичні,
     * тому чистота впливає слабше, ніж для діаманта.
     */
    private double clarityMultiplierFor(ClarityGrade grade) {
        return switch (grade) {
            case FL, IF -> 1.4;
            case VVS1, VVS2 -> 1.25;
            case VS1, VS2 -> 1.1;
            case SI1, SI2 -> 1.0;
            case I1 -> 0.75;
        };
    }
    /**
     * Множник форми огранювання. Овал і кушон найкраще зберігають
     * вагу та колір рубіна.
     */
    private double cutTypeMultiplierFor(CutType cut) {
        return switch (cut) {
            case OVAL, CUSHION -> 1.1;
            case ROUND, PEAR, RADIANT -> 1.0;
            case EMERALD, ASSCHER, HEART -> 0.95;
            case PRINCESS, MARQUISE -> 0.9;
        };
    }
    /**
     * Множник походження. Бірманські рубіни - найцінніші.
     */
    private double originMultiplierFor(Origin origin) {
        return switch (origin) {
            case MYANMAR -> 1.5;
            case MOZAMBIQUE -> 1.15;
            case MADAGASCAR, SRI_LANKA -> 1.05;
            case THAILAND -> 0.95;
            default -> 1.0;
        };
    }
}
