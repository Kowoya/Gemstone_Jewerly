package model;

import enums.ClarityGrade;
import enums.GemColor;
import enums.Origin;

/**
 * Топаз. Ціна визначається переважно кольором: найцінніший -
 * імперський (золотисто-помаранчевий) топаз, а поширений блакитний
 * і безбарвний коштують дешево.
 */

public class Topaz extends SemiPreciousStone{
    public Topaz(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
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
     * Множник чистоти. Топази зазвичай дуже чисті, тому помітні
     * включення сильно знижують ціну.
     */
    private double clarityMultiplierFor(ClarityGrade grade) {
        return switch (grade) {
            case FL, IF -> 1.1;
            case VVS1, VVS2 -> 1.05;
            case VS1, VS2 -> 1.0;
            case SI1, SI2 -> 0.75;
            case I1 -> 0.5;
        };
    }
    /**
     * Множник кольору. Імперський і рожевий топази рідкісні,
     * блакитний і безбарвний - поширені та дешеві.
     */
    private double colorMultiplierFor(GemColor color) {
        return switch (color) {
            case ORANGE -> 3.0;
            case PINK -> 2.5;
            case YELLOW -> 1.0;
            case BLUE -> 0.8;
            case BROWN -> 0.6;
            case WHITE -> 0.5;
            default -> 1.0;
        };
    }
}
