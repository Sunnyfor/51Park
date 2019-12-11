package cn.com.unispark.util.upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import cn.com.unispark.application.Constant;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.request.HMAC;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * MultipartRequest - To handle the large file uploads. Extended from
 * JSONRequest. You might want to change to StringRequest based on your response
 * type.
 * 
 */
public class MultiPartStringRequest extends Request<JSONObject> implements
		MultiPartJson {

	private String appId;
	private String url;
	private String Token = ShareUtil.getSharedString("token");
	private final Listener<JSONObject> mListener;
	private static Map<String, String> mHeader = new HashMap<String, String>();
	/* To hold the parameter name and the File to upload */
	private Map<String, File> fileUploads = new HashMap<String, File>();
	/* To hold the parameter name and the string content to upload */
	private Map<String, String> stringUploads = new HashMap<String, String>();

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		/**
		 * 要加密数据
		 */
		final String timestampStr = String.valueOf(System.currentTimeMillis())
				.toString();
		StringBuilder builder = new StringBuilder();
		builder.append(timestampStr).append(url)
				.append(Token.isEmpty() ? Constant.APP_KEY : Token);
		String builderAppend = builder.toString().trim();
		try {
			/**
			 * 加密
			 */
			byte[] data2 = HMAC.encodeHmacSHA256(builderAppend.getBytes(),
					Token.getBytes());
			final String encryptContent = new String(Hex.encode(data2));
			/**
			 * 创建加解密对象，放入头信息
			 */
			if (!appId.isEmpty()) {
				mHeader.put("Appid", appId);
			}
			// mHeader.put("Content-Type", "application/octet-stream");
			mHeader.put("Timestamp", timestampStr);
			mHeader.put("Authorization", encryptContent); // 加密串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mHeader;
	}

	/**
	 * Creates a new request with the given method.
	 * 
	 * @param method
	 *            the request {@link Method} to use
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public MultiPartStringRequest(int method, String url, String appId,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		this.url = url;
		this.appId = appId;
	}

	public void addFileUpload(String param, File file) {
		fileUploads.put(param, file);
	}

	public void addStringUpload(String param, String content) {
		stringUploads.put(param, content);
	}

	/**
	 * 要上传的文件
	 */
	public Map<String, File> getFileUploads() {
		return fileUploads;
	}

	/**
	 * 要上传的参数
	 */
	public Map<String, String> getStringUploads() {

		return stringUploads;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		try {
			return Response.success(new JSONObject(parsed),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

	/**
	 * 空表示不上传
	 */
	public String getBodyContentType() {
		return null;
	}
}