package cn.com.unispark.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.BaseEntity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.login.LoginActivity;
import cn.com.unispark.util.request.HMAC;

/**
 * <pre>
 * 功能说明： 封装网络请求工具类
 * 日期：	2015年10月23日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月23日
 * </pre>
 */
public class HttpUtil {
	private LoadingProgress loading;
	/**
	 * POST请求
	 */
	public final int POST = 0;
	/**
	 * GET请求
	 */
	public final int GET = 1;
	/**
	 * 服务器成功返回用户请求的数据=200
	 */
	public final int SERVER_REQ_OK = 200;

	/**
	 * 语义或请求参数有误=400
	 */
	public final int SERVER_REQ_NO = 400;

	/**
	 * 认证失败=401
	 */
	public final int SERVER_AUTH_NO = 401;

	/**
	 * 没有该模块权限=403
	 */
	public final int SERVER_PEMS_NO = 403;

	/**
	 * 请求失败，资源不存在=404
	 */
	public final int SERVER_RES_NO = 404;

	/**
	 * 内部服务器错误=500
	 */
	public final int SERVER_INTE_NO = 500;

	public Context context;

	/**
	 * 网络状态不好时展示
	 */
	// public Dialog dialog;

	public int method = POST;

	public List<JsonObjectRequest> requestList;

	public HttpUtil(Context context) {
		requestList = new ArrayList<>();
		this.context = context;
		loading = new LoadingProgress(context);
	}

	/**
	 * 
	 * <pre>
	 * 功能说明：带解析的网络请求
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param method	解析方式：get or post
	 * @param url		解析的接口
	 * @param clazz		实体类
	 * @param params	请求参数
	 * @param result	访问结果
	 * </pre>
	 */
	public <T extends BaseEntity> void parse(int method, final String url,
			final Class<T> clazz, final Map<String, String> params,
			final onResult<T> result) {

		this.method = method;

		/**
		 * 转换参数
		 */
		JSONObject jsonObject = new JSONObject();

		if (params != null && !params.isEmpty()) {
			for (Entry<String, String> entry : params.entrySet()) {
				try {
					jsonObject.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if (method == GET) {
			method = Method.GET;
		}

		if (method == POST) {
			method = Method.POST;
		}

		JsonObjectRequest request = new JsonObjectRequest(method, url,
				jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject json) {
						/**
						 * 打印服务器JSON结果
						 */
						LogUtil.i("【" + url + "】\n【服务器解析返回的Json串】"
								+ json.toString());
						requestList.remove(this);
						parseResult(clazz, json, result);

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						// ToastUtil.showToastNetError();
						// // showNetErrorDialog();
						requestList.remove(this);
						LogUtil.i("【解析异常信息】" + error.getMessage());
						result.onFailed(0, "网络异常，请检查网络设置!");

						// NetErrorDialog.show(context, new onReload() {
						//
						// @Override
						// public void onOption() {
						// parse(HttpUtil.this.method, url, clazz, params,
						// result);
						// }
						// });

					}

				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				return getHeader(url);
			}
		};

		requestList.add(request);

		ParkApplication.getInstance().addToRequestQueue(request, "My Tag");
	}

	/**
	 * 
	 * <pre>
	 * 功能说明：非解析的网络请求
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param method	解析方式：get or post
	 * @param url		解析的接口
	 * @param params	请求参数
	 * @param result	访问结果
	 * </pre>
	 */
	public <T> void parseno(int method, final String url,
			final Map<String, String> params, final onResultTo result) {

		/**
		 * 转换参数
		 */
		JSONObject jsonObject = new JSONObject();

		if (params != null && !params.isEmpty()) {
			for (Entry<String, String> entry : params.entrySet()) {
				try {
					jsonObject.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if (method == GET) {
			method = Method.GET;
		}

		if (method == POST) {
			method = Method.POST;
		}

		JsonObjectRequest request = new JsonObjectRequest(method, url,
				jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject json) {

						/**
						 * 打印服务器JSON结果
						 */
						LogUtil.i("【" + url + "】\n【服务器非解析返回的Json串】"
								+ json.toString());
						requestList.remove(this);
						result.onResult(json.optInt("code"),
								json.optString("msg"), json.toString());
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						ToastUtil.showToastJsonError();
						requestList.remove(this);
						LogUtil.i("【非解析异常信息】" + error.getMessage());
						result.onResult(0, "网络异常，请检查网络设置!", "{}");

						// NetErrorDialog.show(context, new onReload() {
						//
						// @Override
						// public void onOption() {
						// parseno(HttpUtil.this.method, url, params,
						// result);
						// }
						// });

					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				return getHeader(url);
			}
		};

		requestList.add(request);

		request.setRetryPolicy(new DefaultRetryPolicy(500000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		ParkApplication.getInstance().addToRequestQueue(request, "My Tag");
	}

	/**
	 * <pre>
	 * 功能说明：获取头信息
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param url	访问的url
	 * @return
	 * </pre>
	 */
	private Map<String, String> getHeader(String url) {
		Map<String, String> header = new HashMap<>();

		String token = ShareUtil.getSharedString("token");

		LogUtil.showLog(1, "【token】" + token);

		/*
		 * 要加密数据
		 */
		StringBuilder builder = new StringBuilder();
		// 时间戳
		final String timestampStr = String.valueOf(System.currentTimeMillis() / 1000);

		builder.append(timestampStr).append(url)
				.append("".equals(token) ? Constant.APP_KEY : token);
		String builderAppend = builder.toString().trim();

		try {
			/*
			 * 加密
			 */
			byte[] data = HMAC.encodeHmacSHA256(
					builderAppend.getBytes(),
					"".equals(token) ? Constant.APP_KEY.getBytes() : token
							.getBytes());
			final String encryptContent = new String(Hex.encode(data));

			/*
			 * <pre>
			 * 创建加解密对象，放入头信息
			 * token为空时，需要传appid
			 * </pre>
			 */
			if ("".equals(token)) {
				header.put("Appid", Constant.APP_ID);
			}
			header.put("Timestamp", timestampStr);
			header.put("Authorization", encryptContent); // 加密串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}

	/**
	 * <pre>
	 * 功能说明：解析结果
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param clazz	实体类
	 * @param json		JSON串
	 * @param result	解析结果
	 * </pre>
	 */
	private <T extends BaseEntity> void parseResult(Class<T> clazz,
			JSONObject json, onResult<T> result) {

		int code = json.optInt("code");
		if (code == SERVER_REQ_OK) {

			T resultObj = JSON.parseObject(json.toString(), clazz);

			if (clazz == null) {
				ToastUtil.showToast(json.optString("code"));
			} else {
				if (resultObj != null) {
					result.onSuccess(resultObj);
				}
			}

		} else if (code == SERVER_AUTH_NO) {
			// if(loading.isShowing()){
			// loading.hide();
			// }
			// ExitLoginDialog.show(context);

			ShareUtil.setSharedString("token", "");
			ShareUtil.setSharedString("uid", "");
			ShareUtil.setSharedBoolean("islogin", false);
			ParkApplication.setmUserInfo(null);

			ToastUtil.show("您当前登录的账号已下线");
			ParkApplication.quitActivity();
			ToolUtil.IntentClass(BaseActivity.activity, LoginActivity.class,
					"imlogin", true, true);

		} else {
			result.onFailed(code, json.optString("msg"));
		}

	}

	/**
	 * <pre>
	 * 功能说明： 成功结果回调
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * 历史记录
	 *    修改内容：
	 *    修改人员：
	 *    修改日期： 2015年10月25日
	 * </pre>
	 */
	public interface onResult<T> {

		void onSuccess(T result);

		void onFailed(int code, String msg);

	}

	/**
	 * <pre>
	 * 功能说明： 非解析的请求回调
	 * 日期：	2015年10月29日
	 * 开发者：	陈丶泳佐
	 * 版本信息：V5.0.0
	 * 版权声明：版权所有@北京百会易泊科技有限公司
	 * 
	 * 历史记录
	 *    修改内容：
	 *    修改人员：
	 *    修改日期： 2015年10月29日
	 * </pre>
	 */
	public interface onResultTo {
		void onResult(int code, String msg, String json);
	}

	public void close() {
		for (int i = 0, size = requestList.size(); i < size; i++) {
			requestList.get(i).cancel();
		}
	}

	// /**
	// * <pre>
	// * 功能说明：网络状态不好时展示的对话框
	// * 日期： 2015年11月23日
	// * 开发者： 陈丶泳佐
	// *
	// * </pre>
	// */
	// private void showNetErrorDialog() {
	//
	// dialog = new Dialog(context);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
	// dialog.getWindow().setGravity(Gravity.CENTER);// 全屏展示
	// dialog.setCanceledOnTouchOutside(false);
	//
	// View view = View.inflate(context, R.layout.net_error_dialog, null);
	//
	// ImageView clear_iv = (ImageView) view.findViewById(R.id.clear_iv);
	// clear_iv.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// }
	// });
	//
	// dialog.setContentView(view);
	// dialog.show();
	//
	// }

}
