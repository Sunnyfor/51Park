package cn.com.unispark.util.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import cn.com.unispark.application.Constant;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.request.HMAC;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

public class upLoadRequest extends JsonRequest<JSONObject> {
	private static Map<String, String> mHeader = new HashMap<String, String>();
	private String url;
	private String appId;
	private MultipartEntity entity = new MultipartEntity();
	private final Response.Listener<JSONObject> mListener;
	private Map<String, File> fileUploads = new HashMap<String, File>();
	private String mFilePartName;
	private Map<String, String> mParams;
	private String Token = ShareUtil.getSharedString("token");

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
			if ("".equals(Token)) {
				mHeader.put("Appid", Constant.APP_ID);
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
	public upLoadRequest(int method, String url, JSONObject jsonRequest,
			String appId, Response.ErrorListener errorListener,
			Response.Listener<JSONObject> listener, Map<String, File> fileUpload) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener, errorListener);
		mListener = listener;
		fileUploads = fileUpload;
		buildMultipartEntity(fileUpload, jsonRequest);
	}

	@Override
	public String getBodyContentType() {
		Log.e("slx", "entity.getContentType().getValue()"
				+ entity.getContentType().getValue());
		return entity.getContentType().getValue();
		// return "application/octet-stream";
	}

	private void buildMultipartEntity(Map<String, File> fileUpload,
			JSONObject jsonRequest) {
		for (Map.Entry<String, File> entry : fileUpload.entrySet()) {
			entity.addPart(((String) entry.getKey()),
					new FileBody((File) entry.getValue()));
			Log.e("slx", "FileFile===entry.getValue()" + entry.getValue());
		}
//		StringEntity se = new StringEntity(jsonRequest.toString(), HTTP.UTF_8);
//		// 设置类型 编码
//		se.setContentType("application/json;charset=UTF-8");
//		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
//				"application/json;charset=UTF-8"));
//		request.setEntity(se);
		// if (mFileParts != null && mFileParts.size() > 0) {
		// for (File file : mFileParts) {
		// entity.addPart(mFilePartName, new FileBody(file));
		// }
		// }
	}

	@Override
	public byte[] getBody() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			Log.e("slx", "jsonString------" + jsonString);
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

}
