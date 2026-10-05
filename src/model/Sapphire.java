package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;

/**
 * Сапфір - корунд будь-якого кольору, крім червоного (червоний
 * корунд - це рубін). Класичний сапфір синій, а найцінніші камені
 * походять з Кашміру.
 * Ціна залежить від чистоти, кольору, форми, походження
 */

public class Sapphire extends PreciousStone{
    public Sapphire(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                    double transparencyIndex, GemColor color, Origin origin, String certificateNumber, CutType cutType) {
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin, certificateNumber, cutType);
        if (color == GemColor.RED) {
            throw new IllegalArgumentException("It's a ruby");
        }
    }

    @Override
    public double calculateValue(){
        double rawValue = getPricePerCarat() * getWeightCarats()
                * clarityMultiplierFor(getClarity())
                * colorMultiplierFor(getColor())
                * cutTypeMultiplierFor(getCutType())
                * originMultiplierFor(getOrigin());
        return Math.round(rawValue * 100.0) / 100.0;
    }
    /**
     * Множник чистоти. Сапфіри зазвичай чисті,
     * тому включення впливають на ціну помітніше.
     */
    private double clarityMultiplierFor(ClarityGrade grade) {
        return switch (grade) {
            case FL, IF -> 1.4;
            case VVS1, VVS2 -> 1.25;
            case VS1, VS2 -> 1.1;
            case SI1, SI2 -> 0.95;
            case I1 -> 0.7;
        };
    }
    /**
     * Множник кольору. Класичний синій - базовий, рожево-помаранчевий
     * "падпараджа" - найрідкісніший, інші кольори дешевші.
     */
    private double colorMultiplierFor(GemColor color) {
        return switch (color) {
            case ORANGE -> 1.3;
            case BLUE -> 1.0;
            case PINK -> 0.9;
            case PURPLE -> 0.7;
            case YELLOW -> 0.6;
            case GREEN -> 0.5;
            case WHITE -> 0.4;
            default -> 0.5;
        };
    }
    /**
     * Множник форми огранювання. Овал і кушон найкраще зберігають
     * вагу та колір сапфіра.
     */
    private double cutTypeMultiplierFor(CutType cut) {
        return switch (cut) {
            case OVAL, CUSHION -> 1.1;
            case ROUND -> 1.05;
            case EMERALD, RADIANT, PEAR -> 1.0;
            case ASSCHER, HEART -> 0.95;
            case PRINCESS, MARQUISE -> 0.9;
        };
    }
    /**
     * Множник походження. Кашмірські сапфіри - найцінніші.
     */
    private double originMultiplierFor(Origin origin) {
        return switch (origin) {
            case KASHMIR -> 1.8;
            case MYANMAR -> 1.4;
            case SRI_LANKA -> 1.3;
            case MADAGASCAR -> 1.1;
            case THAILAND, AUSTRALIA -> 0.9;
            default -> 1.0;
        };
    }
}
