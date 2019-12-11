package com.uubee.prepay.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	public static int ENV_MODE = 3;
	private static String server = "https://u.uubee.com/uubee_sdkserver/";

	private Utils() {
		throw new AssertionError();
	}

	public static void setEnvMode(int mode) {
		ENV_MODE = mode;
		setServer(ENV_MODE);
		Log.e("prepay", "server : " + getServer());
	}

	public static String getServer() {
		return server;
	}

	public static void setServer(int mode) {
		switch (mode) {
		case 0:
			server = "http://192.168.1.8:8090/uubee_sdkserver/";
			break;
		case 1:
			server = "http://60.12.6.46:8090/uubee_sdkserver/";
			break;
		case 2:
			server = "http://192.168.1.7:80/uubee_sdkserver/";
			break;
		case 3:
			server = "https://u.uubee.com/uubee_sdkserver/";
			break;
		case 4:
			server = "http://60.12.6.46:8070/uubee_sdkserver/";
			break;
		default:
			server = "http://192.168.1.8:8090/uubee_sdkserver/";
		}

	}

	public static String getRequestUrl(int transcode) {
		String reqType = null;
		switch (transcode) {
		case 6001:
			reqType = "creditquery";
			break;
		case 6002:
			reqType = "creditapply";
			break;
		case 6003:
			reqType = "creditpay";
			break;
		case 6004:
			reqType = "billquery";
			break;
		case 6005:
			reqType = "useractive";
			break;
		case 6006:
			reqType = "smsapply";
			break;
		case 6007:
			reqType = "orderinit";
			break;
		case 6008:
			reqType = "createorder";
		}

		String reqUrl = String
				.format(server + reqType + ".htm?", new Object[0]);
		return reqUrl;
	}

	public static boolean isNull(String str) {
		return TextUtils.isEmpty(str) || str.equalsIgnoreCase("NULL");
	}

	public static boolean isPasswordLength(String password) {
		return password.length() < 6;
	}

	public static boolean changeName(JSONObject json, String oldName,
			String newName) {
		if (json != null && json.has(oldName) && !isNull(newName)) {
			Object value = json.opt(oldName);
			json.remove(oldName);

			try {
				json.put(newName, value);
				return true;
			} catch (JSONException var5) {
				var5.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public static String formatPhoneNumber(String number) {
		return number.length() == 11 ? number.substring(0, 3) + "****"
				+ number.substring(7) : number;
	}

	public static String getShortCardNo(String cardNo) {
		return isNull(cardNo) ? cardNo : (cardNo.length() > 4 ? cardNo
				.substring(cardNo.length() - 4, cardNo.length()) : cardNo);
	}

	public static String formatIDCardNumber(String card) {
		return isNull(card) ? "" : card.subSequence(0, 4) + "****************"
				+ card.substring(card.length() - 4);
	}

	public static String formatBankCardNo(String card) {
		if (isNull(card)) {
			return "";
		} else {
			StringBuffer mBuffer = new StringBuffer();
			if (card.length() > 10) {
				mBuffer.append(card.subSequence(0, 6));

				for (int i = 0; i < card.length() - 10; ++i) {
					mBuffer.append("*");
				}

				mBuffer.append(card.subSequence(card.length() - 4,
						card.length()));
			}

			return mBuffer.toString();
		}
	}

	public static String formatUsername(String src) {
		if (isNull(src)) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder(src);
			if (src.length() > 1) {
				sb.replace(0, 1, "*");
			}

			return sb.toString();
		}
	}

	public static void toggleInput(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService("input_method");
		imm.toggleSoftInput(0, 2);
	}
}
