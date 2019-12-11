package cn.com.unispark.task;

import java.util.HashMap;

public class TaskParams {

	private HashMap<String, Object> params = null;

	public TaskParams() {
		params = new HashMap<String, Object>();
	}

	public TaskParams(String key, Object value) {
		this();
		put(key, value);
	}

	public void put(String key, Object value) {
		params.put(key, value);
	}

	public Object get(String key) {
		return params.get(key);
	}

	/**
	 * Determine if the JSONObject contains a specific key.
	 * 
	 * @param key
	 *            A key string.
	 * @return true if the key exists in the JSONObject.
	 */
	public boolean has(String key) {
		return this.params.containsKey(key);
	}

}
