package entities;

public class TestObjectParent extends TestEntity {

	private String parent;
	private TestObjectChild child;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public TestObjectChild getChild() {
		return child;
	}

	public void setChild(TestObjectChild child) {
		this.child = child;
	}

}
