package dto;

public class TestStringDTO extends TestDTO {

	private String string;

	public TestStringDTO() {
	}

	public TestStringDTO(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}


}
