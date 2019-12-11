package com.uubee.prepay.core;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.uubee.prepay.activity.FastPayActivity;
import com.uubee.prepay.activity.FirstPayActivity;
import com.uubee.prepay.api.OnResultListener;
import com.uubee.prepay.api.PrepayAgent;
import com.uubee.prepay.core.PrePayImpl;
import com.uubee.prepay.model.ApplyInfo;
import com.uubee.prepay.model.BaseInfo;
import com.uubee.prepay.model.CreateOrderResponse;
import com.uubee.prepay.model.PayInfo;
import com.uubee.prepay.model.PayResult;
import com.uubee.prepay.model.QueryInfo;
import com.uubee.prepay.util.DebugLog;

import java.lang.reflect.Method;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class PrepayApi {
	public PrepayApi() {
	}

	static PayResult orderInit(Context context, String reqJson) {
		PayResult checkResult = checkPayInfo(reqJson, QueryInfo.class);
		return !"000000".equals(checkResult.getRet_code()) ? checkResult
				: PrePayImpl.INSTANCE.orderInit(context, reqJson);
	}

	static PayResult createOrder(Context context, String reqJson) {
		PayResult checkResult = checkPayInfo(reqJson, QueryInfo.class);
		return !"000000".equals(checkResult.getRet_code()) ? checkResult
				: PrePayImpl.INSTANCE.createOrder(context, reqJson);
	}

	public static PayResult creditApply(Context context, String reqJson) {
		PayResult checkResult = checkPayInfo(reqJson, ApplyInfo.class);
		return !"000000".equals(checkResult.getRet_code()) ? checkResult
				: PrePayImpl.INSTANCE.creditApply(context, reqJson);
	}

	public static void creditPay(final Context context, final String payReq,
			final OnResultListener listener, final boolean useFloatMode,
			final boolean showSuccessPage) {
		PayResult checkResult = checkPayInfo(payReq, PayInfo.class);
		if (!checkResult.isSuccess()) {
			listener.onResult(checkResult.toJson());
		} else {
			DebugLog.e("payReq : " + payReq);
			(new AsyncTask<Void,Void,PayResult>() {
				protected PayResult doInBackground(Void... params) {
					return PrepayApi.orderInit(context, payReq);
				}

				protected void onPostExecute(PayResult initResult) {
					super.onPostExecute(initResult);
					DebugLog.e("initResult : " + initResult.toJson());
					if (!initResult.isSuccess()) {
						listener.onResult(initResult.toJson());
					} else {
						PrepayAgent.mResultListener = listener;
						PrepayApi.startPayActivity(context, payReq,
								initResult.toJson(), useFloatMode,
								showSuccessPage);
					}
				}
			}).execute(new Void[0]);
		}
	}

	public static void createOrder(final Context context, final String payReq,
			final OnResultListener listener, final boolean useFloatMode,
			final boolean showSuccessPage) {
		PayResult checkResult = checkPayInfo(payReq, PayInfo.class);
		if (!checkResult.isSuccess()) {
			listener.onResult(checkResult.toJson());
		} else {
			DebugLog.e("payReq : " + payReq);
			(new AsyncTask<Void,Void,PayResult>() {
				private boolean isNoPass = false;

				protected PayResult doInBackground(Void... params) {
					try {
						JSONObject e = new JSONObject(payReq);
						e.put("action", "init");
						PayResult result = PrepayApi.createOrder(context,
								e.toString());
						Log.d("zy", "支付："+result);
						if (!result.isSuccess()) {
							return result;
						} else {
							Gson gson = new Gson();
							CreateOrderResponse mResponse = (CreateOrderResponse) gson
									.fromJson(result.toJson(),
											CreateOrderResponse.class);
							
							Log.d("zy", "支付成功："+mResponse.toString());
							
							if (!"1".equals(mResponse.type_passwd)) {
								return result;
							} else {
								this.isNoPass = true;
								JSONObject payOrder = new JSONObject(payReq);
								payOrder.put("token", mResponse.token);
								if (TextUtils.isEmpty(payOrder
										.optString("mob_user"))) {
									payOrder.put("mob_user", mResponse.mob_user);
								}
								
								Log.d("zy", "支付订单："+payOrder.toString());
								return PrepayApi.sendPayReq(context,
										payOrder.toString());
							}
						}
					} catch (JSONException var7) {
						return PayResult
								.createPayResult(
										"{\'ret_code\':\'700001\',\'ret_msg\':\'商户请求参数校验错误[%s]\'}",
										var7.getMessage());
					}
				}

				protected void onPostExecute(PayResult result) {
					super.onPostExecute(result);
					DebugLog.e("createOrder result : " + result.toJson());
					if (result.isSuccess() && !this.isNoPass) {
						PrepayAgent.mResultListener = listener;
						PrepayApi.startPayActivity(context, payReq,
								result.toJson(), useFloatMode, showSuccessPage);
					} else {
						listener.onResult(result.toJson());
					}
				}
			}).execute(new Void[0]);
		}
	}

	private static void startPayActivity(Context context, String payReq,
			String initResult, boolean useFloatMode, boolean showSuccessPage) {
		Intent intent = new Intent();
		intent.putExtra("pay_req", payReq);
		intent.putExtra("query_res", initResult);
		intent.putExtra("show_success_page", showSuccessPage);
		intent.setFlags(335544320);
		intent.setClass(context, FirstPayActivity.class);

		try {
			JSONObject e = new JSONObject(initResult);
			boolean isActive = "0".equals(e.getString("flag_active"));
			if (useFloatMode && isActive) {
				intent.setClass(context, FastPayActivity.class);
			}
		} catch (JSONException var8) {
			var8.printStackTrace();
		}

		context.startActivity(intent);
	}

	public static PayResult sendPayReq(Context context, String payReq) {
		try {
			JSONObject e = new JSONObject(payReq);
			if (e.has("illegal_url")) {
				String illegal_url = e.getString("illegal_url");
				e.put("flag_illegalmob", getFlagIllegal(context, illegal_url));
				payReq = e.toString();
			}
		} catch (JSONException var4) {
			var4.printStackTrace();
		}

		return PrePayImpl.INSTANCE.creditPay(context, payReq);
	}

	public static PayResult billQuery(Context context, String reqJson) {
		PayResult checkResult = checkPayInfo(reqJson, BaseInfo.class);
		return !"000000".equals(checkResult.getRet_code()) ? checkResult
				: PrePayImpl.INSTANCE.billQuery(context, reqJson);
	}

	public static PayResult creditQuery(Context context, String reqJson) {
		PayResult checkResult = checkPayInfo(reqJson, BaseInfo.class);
		return !"000000".equals(checkResult.getRet_code()) ? checkResult
				: PrePayImpl.INSTANCE.creditQuery(context, reqJson);
	}

	public static PayResult userActive(Context context, String reqJson) {
		return PrePayImpl.INSTANCE.userActive(context, reqJson);
	}

	public static PayResult getMsgVerifyCode(Context context, String reqJson) {
		return PrePayImpl.INSTANCE.getMsgVerifyCode(context, reqJson);
	}

	private static <T extends BaseInfo> PayResult checkPayInfo(String info,
			Class<T> objClass) {
		if (info == null) {
			return PayResult.createPayResult(
					"{\'ret_code\':\'700001\',\'ret_msg\':\'商户请求参数校验错误[%s]\'}",
					"请求为空");
		} else {
			try {
				BaseInfo e = (BaseInfo) (new Gson()).fromJson(info, objClass);
				Method[] methods = e.getClass().getMethods();
				Method[] var6 = methods;
				int var7 = methods.length;

				for (int var8 = 0; var8 < var7; ++var8) {
					Method method = var6[var8];
					if (method != null && method.getName().startsWith("get")
							&& !method.getName().startsWith("getClass")) {
						Object objParams = null;

						try {
							objParams = method.invoke(e, new Object[0]);
						} catch (Exception var11) {
							var11.printStackTrace();
						}

						if (objParams == null) {
							String name = method.getName()
									.replaceFirst("get", "").toLowerCase();
							return PayResult
									.createPayResult(
											"{\'ret_code\':\'700001\',\'ret_msg\':\'商户请求参数校验错误[%s]\'}",
											name + "=null");
						}
					}
				}
			} catch (JsonSyntaxException var12) {
				return PayResult
						.createPayResult(
								"{\'ret_code\':\'700001\',\'ret_msg\':\'商户请求参数校验错误[%s]\'}",
								var12.getMessage());
			}

			return new PayResult("000000", (String) null);
		}
	}

	private static String getFlagIllegal(Context context, String illegal_url) {
		if (TextUtils.isEmpty(illegal_url)) {
			return "0";
		} else {
			List packages = context.getPackageManager().getInstalledPackages(0);

			for (int i = 0; i < packages.size(); ++i) {
				PackageInfo pInfo = (PackageInfo) packages.get(i);
				if (illegal_url.contains(pInfo.packageName)) {
					return "1";
				}
			}

			return "0";
		}
	}
}
