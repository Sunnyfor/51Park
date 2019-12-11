package com.uubee.prepay.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class PayResult implements Parcelable {
	public static final String ERROR_SOCKETTIMEOUTECXEPTION = "读取响应超时";
	public static final String PARAM_INVALID = "{\'ret_code\':\'700001\',\'ret_msg\':\'商户请求参数校验错误[%s]\'}";
	public static final String PAY_TIMEOUT = "{\'ret_code\':\'700002\',\'ret_msg\':\'支付服务超时，请重新支付\'}";
	public static final String PAY_SYSTEM_EXCEPTION = "{\'ret_code\':\'709999\',\'ret_msg\':\'交易异常，请联系客服\'}";
	public static final String RES_SUCCESS = "000000";
	public static final String RES_TIEMOUT = "700002";
	public static final String RES_IS_ACTIVE = "0";
	public static final Creator<PayResult> CREATOR = new Creator() {
		public PayResult createFromParcel(Parcel source) {
			return new PayResult(source.readString());
		}

		public PayResult[] newArray(int size) {
			return null;
		}
	};
	private String ret_code;
	private String ret_msg;
	private String jsonString;

	public PayResult(String result) {
		try {
			JSONObject e = new JSONObject(result);
			this.ret_code = e.optString("ret_code", "");
			this.ret_msg = e.optString("ret_msg", "");
			this.jsonString = result;
		} catch (Exception var3) {
			this.jsonString = "{\'ret_code\':\'709999\',\'ret_msg\':\'交易异常，请联系客服\'}";
			this.ret_code = "709999";
			this.ret_msg = "交易异常，请联系客服";
		}

	}

	public PayResult(String ret_code, String ret_msg) {
		this.ret_code = ret_code;
		this.ret_msg = ret_msg;

		try {
			JSONObject e = (new JSONObject()).put("ret_code", ret_code).put(
					"ret_msg", ret_msg);
			this.jsonString = e.toString();
		} catch (JSONException var4) {
			var4.printStackTrace();
			this.jsonString = "";
		}

	}

	public static PayResult createPayResult(String format, String param) {
		return new PayResult(String.format(format, new Object[] { param }));
	}

	public boolean isSuccess() {
		return "000000".equals(this.ret_code);
	}

	public String toJson() {
		return this.jsonString;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.jsonString);
	}

	public String getRet_code() {
		return this.ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return this.ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public String toString() {
		return this.toJson();
	}
}