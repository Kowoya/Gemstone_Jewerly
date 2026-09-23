package model;

import enums.ClarityGrade;
import enums.CutType;

public abstract class PreciousStone extends Gemstone{
    private final String certificateNumber;
    private final CutType cutType;

    protected PreciousStone(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                            double transparencyIndex, String color, String origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin);
        this.certificateNumber = certificateNumber;
        this.cutType = cutType;
    }

    public String getCertificateNumber(){
        return certificateNumber;
    }

    public CutType getCutType(){
        return cutType;
    }
}
