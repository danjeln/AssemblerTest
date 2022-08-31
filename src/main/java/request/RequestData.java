package request;

import java.util.HashMap;
import java.util.Map;

public enum RequestData {
	INSTANCE;

	private ThreadLocal<Map<RequestDataType, Object>> data;

	public ThreadLocal<Map<RequestDataType, Object>> getRequestData() {
		if (data == null) {
			data = new ThreadLocal<Map<RequestDataType, Object>>();
			data.set(new HashMap<>());
		}
		if (data.get() == null) {
			data.set(new HashMap<>());
		}
		return data;
	}

	public void setRequestData(ThreadLocal<Map<RequestDataType, Object>> data) {
		this.data = data;
	}

	public void addData(RequestDataType key, Object val) {
		getRequestData().get().put(key, val);
	}

	public Object getData(RequestDataType key) {
		return getRequestData().get().get(key);
	}

	public void reset() {
		if (data != null && data.get() != null) {
			data.get().clear();
		}

	}

}
