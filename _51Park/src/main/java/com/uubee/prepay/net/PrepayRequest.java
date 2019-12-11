package com.uubee.prepay.net;

import com.uubee.prepay.net.BaseRequest;
import com.uubee.prepay.net.BaseRequest.BaseBuilder;
import com.uubee.prepay.net.BaseRequest.Method;
import com.uubee.prepay.util.DebugLog;
import com.uubee.prepay.util.SignUtils;
import org.json.JSONObject;

public class PrepayRequest extends BaseRequest {
	private PrepayRequest(PrepayRequest.Builder builder) {
		this.setBody(builder.body);
		this.setMethod(builder.method);
		this.setUrl(builder.url);
		this.setContentType(builder.contentType);
		DebugLog.d("PrePayRequest:\nMethod = " + this.getMethod() + ",url = "
				+ this.getUrl() + ",contentType = " + this.getContentType()
				+ ",\nbody = " + this.getBody());
	}

	public boolean sign(String signKey) {
		if (signKey != null && !signKey.isEmpty()) {
			JSONObject json = SignUtils.rsaSignature(this.getBody());
			if (json != null) {
				this.setBody(json.toString());
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static class Builder extends BaseBuilder {
		private Method method;
		private String url;
		private String body;
		private String contentType;
		private String signKey;

		public Builder(String url, String body) {
			this.method = Method.POST;
			this.contentType = "application/json;charset=utf-8";
			this.url = url;
			this.body = body;
		}

		public PrepayRequest.Builder setSignKey(String signKey) {
			this.signKey = signKey;
			return this;
		}

		public PrepayRequest build() {
			PrepayRequest req = new PrepayRequest(this);
			return req.sign(this.signKey) ? req : null;
		}
	}
}
