package datalog;

import java.util.HashMap;
import java.util.Map;

public enum ChangedData {
	INSTANCE;

	private ThreadLocal<Map<ChangedDataType, Object>> data;

	public ThreadLocal<Map<ChangedDataType, Object>> getRequestData() {
		if (data == null) {
			data = new ThreadLocal<Map<ChangedDataType, Object>>();
			data.set(new HashMap<>());
		}
		if (data.get() == null) {
			data.set(new HashMap<>());
		}
		return data;
	}

	public void setRequestData(ThreadLocal<Map<ChangedDataType, Object>> data) {
		this.data = data;
	}

	public void addData(ChangedDataType key, Object val) {
		getRequestData().get().put(key, val);
	}

	public Object getData(ChangedDataType key) {
		return getRequestData().get().get(key);
	}

	public void reset() {
		if (data != null && data.get() != null) {
			data.get().clear();
		}

	}

}
