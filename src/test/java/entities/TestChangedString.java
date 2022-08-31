package entities;

import assembler.annotation.LoggingDescriptor;

public class TestChangedString extends TestEntity {

	@LoggingDescriptor(description="En string har ändrats")
	private String string;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
}
