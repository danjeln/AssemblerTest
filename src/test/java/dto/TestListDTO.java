package dto;

import java.util.ArrayList;
import java.util.List;

public class TestListDTO<T> extends TestDTO{

	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = (List<T>) list;
	}

	public void addToList(T o) {
		if (this.list == null) {
			this.list = new ArrayList<>();
		}
		this.list.add((T) o);
	}
}
