package model;

import enums.ClarityGrade;
import enums.GemColor;
import enums.Origin;

/**
 * Аметист - фіолетовий різновид кварцу. Ціна залежить переважно
 * від насиченості кольору, яка визначається родовищем: найцінніші -
 * темно-фіолетові аметисти із Замбії та Уругваю.
 */

public class Amethyst extends SemiPreciousStone{
    public Amethyst(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                    double transparencyIndex, GemColor color, Origin origin, String treatmentType) {
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, treatmentType);
        if (color != GemColor.PURPLE) {
            throw new IllegalArgumentException("Amethyst must be purple");
        }
    }

    @Override
    public double calculateValue(){
        double rawValue = getPricePerCarat() * getWeightCarats()
                * clarityMultiplierFor(getClarity())
                * originMultiplierFor(getOrigin());
        return Math.round(rawValue * 100.0) / 100.0;
    }
    /**
     * Множник чистоти. Аметисти зазвичай чисті на око, тому висока
     * чистота майже не додає ціни, а помітні включення сильно знижують.
     */
    private double clarityMultiplierFor(ClarityGrade grade) {
        return switch (grade) {
            case FL, IF -> 1.1;
            case VVS1, VVS2 -> 1.05;
            case VS1, VS2 -> 1.0;
            case SI1, SI2 -> 0.8;
            case I1 -> 0.5;
        };
    }
    /**
     * Множник походження. Від родовища залежить насиченість кольору.
     */
    private double originMultiplierFor(Origin origin) {
        return switch (origin) {
            case ZAMBIA, URUGUAY -> 1.3;
            default -> 1.0;
        };
    }
}
