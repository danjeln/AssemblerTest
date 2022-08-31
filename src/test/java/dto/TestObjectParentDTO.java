package dto;

public class TestObjectParentDTO extends TestDTO{

	private String parent;
	private TestObjectChildDTO child;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public TestObjectChildDTO getChild() {
		return child;
	}

	public void setChild(TestObjectChildDTO child) {
		this.child = child;
	}
	
}
