package cn.com.unispark.fragment.mine.msgpush.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MsgStatus implements Serializable {
	private String key;
	private String value;
	
	public MsgStatus() {
		super();
	}
	public MsgStatus(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
