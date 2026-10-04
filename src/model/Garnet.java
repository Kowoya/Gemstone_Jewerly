package model;

import enums.ClarityGrade;
import enums.GemColor;
import enums.Origin;

/**
 * Гранат - група мінералів, різні кольори яких є різними
 * різновидами. Ціна визначається переважно кольором: червоні
 * гранати поширені, а зелені (демантоїд, цаворит) - дуже рідкісні.
 */

public class Garnet extends SemiPreciousStone{
    public Garnet(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                  double transparencyIndex, GemColor color, Origin origin, String treatmentType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, treatmentType);
    }

    @Override
    public double calculateValue(){
        double rawValue = getPricePerCarat() * getWeightCarats()
                * clarityMultiplierFor(getClarity())
                * colorMultiplierFor(getColor());
        return Math.round(rawValue * 100.0) / 100.0;
    }
    /**
     * Множник чистоти. Гранати зазвичай чисті на око, тому
     * помітні включення знижують ціну сильніше.
     */
    private double clarityMultiplierFor(ClarityGrade grade) {
        return switch (grade) {
            case FL, IF -> 1.2;
            case VVS1, VVS2 -> 1.1;
            case VS1, VS2 -> 1.0;
            case SI1, SI2 -> 0.85;
            case I1 -> 0.6;
        };
    }
    /**
     * Множник кольору, що відповідає різновиду граната.
     */
    private double colorMultiplierFor(GemColor color) {
        return switch (color) {
            case GREEN -> 2.5;
            case ORANGE -> 1.5;
            case PURPLE -> 1.3;
            case PINK -> 1.2;
            case BROWN -> 0.8;
            default -> 1.0;
        };
    }
}
