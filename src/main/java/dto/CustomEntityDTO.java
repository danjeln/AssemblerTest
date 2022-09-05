package dto;

import assembler.annotation.AssemblerProperty;

import java.util.Date;

public class CustomEntityDTO extends MasterDTO {

    @AssemblerProperty(ignore = false)
    private String customValue;

    private Date datum1;

    private Date datum2;

    @AssemblerProperty(ignore = true)
    private String yesOrNo;

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

    public Date getDatum2() {
        return datum2;
    }

    public void setDatum2(Date datum2) {
        this.datum2 = datum2;
    }

    public String isYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(String yesOrNo) {
        this.yesOrNo = yesOrNo;
    }
}
