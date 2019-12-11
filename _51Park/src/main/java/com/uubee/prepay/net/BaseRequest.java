package com.uubee.prepay.net;

import java.io.Serializable;

public abstract class BaseRequest implements Serializable {
	private BaseRequest.Method method;
	private String url;
	private String body;
	private String contentType;
	public byte[] hmacKey;
	public byte[] aesKey;
	public byte[] nonce;

	public BaseRequest() {
		this.method = BaseRequest.Method.POST;
	}

	public BaseRequest.Method getMethod() {
		return this.method;
	}

	public void setMethod(BaseRequest.Method method) {
		this.method = method;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public abstract boolean sign(String var1);

	public abstract static class BaseBuilder {
		public BaseBuilder() {
		}

		public abstract BaseRequest build();
	}

	public static enum Method {
		GET, POST;

		private Method() {
		}
	}
}
