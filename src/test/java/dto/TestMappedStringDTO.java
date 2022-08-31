package dto;

import assembler.annotation.AssemblerProperty;

public class TestMappedStringDTO extends TestDTO {

	@AssemblerProperty(mapsTo="string")
	private String brandNewValue;

	public String getBrandNewValue() {
		return brandNewValue;
	}

	public void setBrandNewValue(String brandNewValue) {
		this.brandNewValue = brandNewValue;
	}
	
	
}
