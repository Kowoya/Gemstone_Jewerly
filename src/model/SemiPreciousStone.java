package model;

import enums.ClarityGrade;
import enums.CutType;

public abstract class SemiPreciousStone extends Gemstone{
    private final String treatmentType;

    protected SemiPreciousStone(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                                double transparencyIndex, String color, String origin, String treatmentType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin);
        this.treatmentType = treatmentType;
    }

    public String getTreatmentType(){
        return treatmentType;
    }
}
