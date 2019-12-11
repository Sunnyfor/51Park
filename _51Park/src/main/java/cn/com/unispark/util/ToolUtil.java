package cn.com.unispark.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import cn.com.unispark.application.ParkApplication;

/**
 * <pre>
 * 功能说明： 代码片段工具类
 * 日期：	2014年12月1日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月1日
 * </pre>
 */
@SuppressLint("SimpleDateFormat")
public class ToolUtil extends Activity {

	/**
	 * <pre>
	 * 功能说明：判断当前是否有网络连接
	 * 日期：	2015年6月4日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public static boolean isNetConn() {
		ConnectivityManager connManager = (ConnectivityManager) ParkApplication.applicationContext
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 功能说明： 判断手机号码是否正确
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param mobiles
	 * @return
	 * </pre>
	 */
	public static boolean isMobileNumber(String mobile) {
		char[] edit_shoujihao = mobile.toCharArray();
		for (int i = 0; i < edit_shoujihao.length; i++) {
			if (edit_shoujihao[i] < 48 || edit_shoujihao[i] > 57) {
				return false;
			}
		}
		// Pattern p = Pattern
		// .compile("^((13[0-9])|(15[012356789])|(18[0-9])|(14[57]))\\d{8}$");
		Pattern p = Pattern
				.compile("^((17[0-9])|(13[0-9])|(15[012356789])|(18[0-9])|(14[57]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * <pre>
	 * 功能说明：判断是否插入SD卡，SD卡是否可用
	 * 日期：	2015年10月20日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return
	 * </pre>
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)
				&& Environment.getExternalStorageDirectory().canWrite()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 初始化loading条
	 */
	public static ProgressDialog initProgressDialog(Context context,
			String message) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		// 单击对话框以外的区域对话框不消失
		progressDialog.setCanceledOnTouchOutside(false);
		// progressDialog.show();
		return progressDialog;
	}

	/**
	 * <pre>
	 * 功能说明：追加参数到URL,方便调试
	 * 日期：	2015年1月10日
	 * 开发者：   陈丶泳佐
	 * 
	 * @param params
	 * @return	URL
	 * </pre>
	 */
	public static String buildUrlParams(Map<String, Object> params) {
		String urlParams = "?";
		Iterator<?> iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			if (urlParams.length() > 1) {
				urlParams = urlParams + "&";
			}
			try {
				urlParams = urlParams
						+ entry.getKey().toString()
						+ "="
						+ URLEncoder.encode(entry.getValue().toString(),
								"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return urlParams;
	}

	/**
	 * <pre>
	 * 功能说明：跳转到拨号界面
	 * 日期：	2016年1月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param context 当前类
	 * @param phone	要拨打的电话号码
	 * </pre>
	 */
	public static void IntentPhone(Activity context, String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL,
				Uri.parse("tel:" + phone));
		context.startActivity(intent);
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转,不进行登录判断
	 * 日期：	2015年11月9日
	 * 开发者：	陈丶泳佐
	 * </pre>
	 */
	public static void IntentClass(Activity context, Class<?> clazz,
			boolean isFinish) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
		if (isFinish) {
			context.finish();
		}
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转,判断是否登录
	 * 日期：	2015年11月9日
	 * 开发者：	陈丶泳佐
	 * </pre>
	 */
	public static void IntentClassLogin(Activity context, Class<?> clazz,
			boolean isfinish) {

		if (ParkApplication.isLogin(context)) {// 已登录
			Intent intent = new Intent(context, clazz);
			context.startActivity(intent);
			if (isfinish) {
				context.finish();
			}
		}
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转传值,不需要登录
	 * 日期：	2015年1月8日
	 * 开发者：   陈丶泳佐
	 * </pre>
	 */
	public static void IntentClass(Activity context, Class<?> clazz,
			String paramsName, String paramsContent, boolean isFinish) {

		Intent intent = new Intent(context, clazz);
		intent.putExtra(paramsName, paramsContent);
		context.startActivity(intent);
		if (isFinish) {
			context.finish();
		}
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转传值,判断是否登录
	 * 日期：	2015年1月8日
	 * 开发者：   陈丶泳佐
	 * </pre>
	 */
	public static void IntentClassLogin(Activity context, Class<?> clazz,
			String paramsName, String paramsContent, boolean isFinish) {

		if (ParkApplication.isLogin(context)) {// 已登录
			Intent intent = new Intent(context, clazz);
			intent.putExtra(paramsName, paramsContent);
			context.startActivity(intent);
			if (isFinish) {
				context.finish();
			}
		}
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转传值,不需要登录
	 * 日期：	2015年1月8日
	 * 开发者：   陈丶泳佐
	 * </pre>
	 */
	public static void IntentClass(Activity context, Class<?> clazz,
			String paramsName, int paramsContent, boolean isFinish) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(paramsName, paramsContent);
		context.startActivity(intent);
		if (isFinish) {
			context.finish();
		}
	}

	public static void IntentClass(Activity context, Class<?> clazz,
			String paramsName, boolean paramsContent, boolean isFinish) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(paramsName, paramsContent);
		context.startActivity(intent);
		if (isFinish) {
			context.finish();
		}
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转传值,判断是否登录
	 * 日期：	2015年1月8日
	 * 开发者：   陈丶泳佐
	 * </pre>
	 */
	public static void IntentClassLogin(Activity context, Class<?> clazz,
			String paramsName, int paramsContent, boolean isFinish) {

		if (ParkApplication.isLogin(context)) {// 已登录
			Intent intent = new Intent(context, clazz);
			intent.putExtra(paramsName, paramsContent);
			context.startActivity(intent);
			if (isFinish) {
				context.finish();
			}
		}
	}

	/**
	 * <pre>
	 * 功能说明：Intent跳转传值ForResult,判断是否登录
	 * 日期：	2015年1月8日
	 * 开发者：   陈丶泳佐
	 * </pre>
	 */
	public static void IntentClassForResult(Activity context, Class<?> clazz,
			String paramsName, String paramsContent, int requestCode,
			boolean isFinish) {

		Intent intent = new Intent(context, clazz);
		intent.putExtra(paramsName, paramsContent);
		context.startActivityForResult(intent, requestCode);
		if (isFinish) {
			context.finish();
		}

	}

}
