package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;

public abstract class SemiPreciousStone extends Gemstone{
    private final String treatmentType;

    protected SemiPreciousStone(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                                double transparencyIndex, GemColor color, Origin origin, String treatmentType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin);
        this.treatmentType = treatmentType;
    }

    public String getTreatmentType() {
        return treatmentType;
    }
}
