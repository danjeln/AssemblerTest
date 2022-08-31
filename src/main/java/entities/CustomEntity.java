package entities;

import assembler.annotation.AssemblerProperty;

public class CustomEntity extends MasterEntity{

    private String customValue;

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }
}
