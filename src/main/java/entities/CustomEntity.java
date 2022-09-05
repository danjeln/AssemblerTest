package entities;

import assembler.annotation.AssemblerProperty;

import java.util.Date;

public class CustomEntity extends MasterEntity{

    private String customValue;
    private Date datum1 = new Date();
    private boolean yesOrNo;

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public Date getDatum1() {
        return datum1;
    }

    public void setDatum1(Date datum1) {
        this.datum1 = datum1;
    }

    public boolean isYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }
}
