package model;

import enums.ClarityGrade;
import enums.CutType;
import enums.GemColor;
import enums.Origin;

public abstract class PreciousStone extends Gemstone{
    private final String certificateNumber;
    private final CutType cutType;

    protected PreciousStone(String name, double weightCarats, double pricePerCarat, ClarityGrade clarity,
                            double transparencyIndex, GemColor color, Origin origin, String certificateNumber, CutType cutType){
        super(name, weightCarats, pricePerCarat, clarity, transparencyIndex, color, origin);
        if (cutType == null){
            throw new IllegalArgumentException("Cut type must not be null");
        }
        this.certificateNumber = certificateNumber;
        this.cutType = cutType;
    }

    public String getCertificateNumber(){
        return certificateNumber;
    }

    public CutType getCutType(){
        return cutType;
    }

    public boolean isCertified() {
        return certificateNumber != null && !certificateNumber.isBlank();
    }
}
