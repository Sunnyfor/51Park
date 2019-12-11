package com.uubee.prepay.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import cn.fraudmetrix.android.FMAgent;

import com.google.gson.Gson;
import com.uubee.prepay.api.OnResultListener;
import com.uubee.prepay.core.PrepayApi;
import com.uubee.prepay.model.PayResult;
import com.uubee.prepay.model.ResultCredit;
import com.uubee.prepay.util.Utils;

public class PrepayAgent {
	private static final String TAG = "PrepayAgent";
	public static OnResultListener mResultListener;
	private static Context mContext;

	public PrepayAgent() {
	}

	public static void init(Context context, boolean isRelease) {
		FMAgent.init(context, "uubee", isRelease);
		mContext = context;
	}

	public static void creditApply(final Context context, final String params,
			final OnResultListener listener) {
		if (isInit(context)) {
			(new AsyncTask<Void, Void, String>() {
				protected String doInBackground(Void... p) {
					PayResult result = PrepayApi.creditApply(context, params);
					Gson gson = new Gson();
					ResultCredit ra = (ResultCredit) gson.fromJson(
							result.toJson(), ResultCredit.class);
					return gson.toJson(ra);
				}

				protected void onPostExecute(String s) {
					super.onPostExecute(s);
					listener.onResult(s);
				}
			}).execute(new Void[0]);
		}
	}

	public static void creditPay(Context context, String params,
			OnResultListener listener) {
		creditPay(context, params, listener, false);
	}

	public static void creditPay(Context context, String params,
			OnResultListener listener, boolean useFloatMode) {
		creditPay(context, params, listener, useFloatMode, false);
	}

	public static void creditPay(Context context, String params,
			OnResultListener listener, boolean useFloatMode,
			boolean showSuccessPage) {
		if (isInit(context)) {
			PrepayApi.creditPay(context, params, listener, useFloatMode,
					showSuccessPage);
		}
	}

	public static void creditQuery(final Context context, final String params,
			final OnResultListener listener) {
		if (isInit(context)) {
			(new AsyncTask<Void, Void, String>() {
				protected String doInBackground(Void... p) {
//					 PayResult result = PrepayApi.creditQuery(context, params);
//					 Gson gson = new Gson();
//					 // ResultCredit resultCredit = (ResultCredit)gson.fromJson(result.toJson(), ResultCredit.class);
//					 Log.d("zy","result:"+result);
//					 Log.d("zy","toJson:"+result.toJson());
//					 return result.toJson();

					PayResult result = PrepayApi.creditQuery(context, params);
					Gson gson = new Gson();
					ResultCredit resultCredit = (ResultCredit) gson.fromJson(
							result.toJson(), ResultCredit.class);
					return gson.toJson(resultCredit);

				}

				protected void onPostExecute(String s) {
					super.onPostExecute(s);
					listener.onResult(s);
				}
			}).execute(new Void[0]);
		}
	}

	public static void billQuery(final Context context, final String params,
			final OnResultListener listener) {
		if (isInit(context)) {
			(new AsyncTask<Void, Void, String>() {
				protected String doInBackground(Void... p) {
					PayResult result = PrepayApi.billQuery(context, params);
					return result.toJson();
				}

				protected void onPostExecute(String s) {
					super.onPostExecute(s);
					listener.onResult(s);
				}
			}).execute(new Void[0]);
		}
	}

	public static void setEnvMode(int mode) {
		Utils.setEnvMode(mode);
		if (isRelease()) {
			init(mContext, true);
		} else {
			init(mContext, false);
		}

	}

	public static boolean isRelease() {
		return Utils.ENV_MODE == 3;
	}

	private static boolean isInit(Context context) {
		if (mContext == null) {
			Toast.makeText(context, "PrepayAgent没有初始化！", 0).show();
			Log.e("PrepayAgent", "PrepayAgent.init is not called.");
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 功能说明：
	 * 日期：	2015年9月17日
	 * 开发者：	陈丶泳佐
	 *
	 * @param context
	 * @param params 签名
	 * @param listener 回调
	 * @param useFloatMode
	 * @param showSuccessPage
	 * </pre>
	 */
	public static void createOrder(Context context, String params,
			OnResultListener listener, boolean useFloatMode,
			boolean showSuccessPage) {
		if (isInit(context)) {
			PrepayApi.createOrder(context, params, listener, useFloatMode,
					showSuccessPage);
		}
	}
}