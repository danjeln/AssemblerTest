package dto;

import assembler.annotation.AssemblerProperty;

public class CustomEntityDTO extends MasterDTO{

    @AssemblerProperty(ignore = false)
    private String customValue;

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }
}
