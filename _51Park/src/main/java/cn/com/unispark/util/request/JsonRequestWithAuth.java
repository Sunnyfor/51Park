package cn.com.unispark.util.request;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import cn.com.unispark.application.Constant;
import cn.com.unispark.util.upload.MultiPartJson;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

public class JsonRequestWithAuth extends JsonRequest<JSONObject> implements
		MultiPartJson {
	private Map<String, File> fileUploads = new HashMap<String, File>();
	private static Map<String, String> mHeader = new HashMap<String, String>();
	private Map<String, String> stringUploads = new HashMap<String, String>();
	private String url;
	private String Token;
	private String appId;

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
		Log.e("slx", "成功上传-------Token3" + Token);
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
			mHeader.put("Timestamp", timestampStr);
			mHeader.put("Authorization", encryptContent); // 加密串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mHeader;
	}

	/**
	 * 
	 * @param method
	 *            请求方式POST
	 * @param url
	 *            请求服务器地址
	 * @param jsonRequest
	 *            post给服务器的json数据
	 * @param appId
	 *            APPID不需要传时则传null
	 * @param Token
	 *            Token为空则默认使用APPKEY，不为空则Token
	 * @param listener
	 *            请求成功服务器的回调接口
	 * @param errorListener
	 *            请求失败服务器的回调接口
	 */
	public JsonRequestWithAuth(int method, String url, JSONObject jsonRequest,
			String appId, String Token, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener, errorListener);
		this.url = url;
		this.appId = appId;
		this.Token = Token;
		Log.e("slx", "成功上传-------Token" + Token);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	public void addFileUpload(String param, File file) {
		fileUploads.put(param, file);
	}

	/**
	 * 要上传的文件
	 */
	public Map<String, File> getFileUploads() {
		return fileUploads;
	}

	@Override
	public void addStringUpload(String param, String content) {
		stringUploads.put(param, content);
		}

	@Override
	public Map<String, String> getStringUploads() {
		return stringUploads;
	}
}
