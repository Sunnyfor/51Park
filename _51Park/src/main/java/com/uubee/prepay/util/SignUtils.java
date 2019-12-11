package com.uubee.prepay.util;

import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONObject;

public class SignUtils {
	private static final String SIGN_TYPE = "RSA";
	private static final String PARAM_SIGN_TYPE = "sign_type";

	private SignUtils() {
		throw new AssertionError();
	}

	public static JSONObject rsaSignature(String jsonStr) {
		if (jsonStr == null) {
			return null;
		} else {
			try {
				JSONObject e = new JSONObject(jsonStr);
				e.put("sign_type", "RSA");
				Iterator it = e.keys();

				while (it.hasNext()) {
					String key = (String) it.next();
					e.put(key, URLEncoder.encode(e.optString(key, ""), "utf-8"));
				}

				return e;
			} catch (Exception var4) {
				return null;
			}
		}
	}
}
