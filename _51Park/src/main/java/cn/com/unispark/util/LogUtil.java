package cn.com.unispark.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;
import cn.com.unispark.application.ParkApplication;

/**
 * <pre>
 * 功能说明： Log工具类
 * 日期：	2014年12月1日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月1日
 * </pre>
 */
public class LogUtil {

	/**
	 * Log类别：i == 1 ：info 绿色
	 */
	public static final int i = 1;

	/**
	 * Log类别：e == 2 ：error 红色
	 */
	public static final int e = 2;

	/**
	 * Log类别：d == 3 ：debug 紫色
	 */
	public static final int d = 3;

	/**
	 * 获取当前类所在的路径
	 */
	public static String TAG = ParkApplication.applicationContext.getClass()
			.getCanonicalName();

	/**
	 * <pre>
	 * 功能说明：打印log日志
	 * 日期：	2015年6月29日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param content	打印的内容
	 * @param tag： 1--i
	 * @param tag： 2--e
	 * @param tag： 3--d
	 * </pre>
	 */

	public static void showLog(int tag, String content) {
		switch (tag) {
		case 1:
			Log.i("miao", "【左郁】（" + TAG + "）\n" + content);
			break;
		case 2:
			Log.e("miao", "【左郁】（" + TAG + "）\n" + content);
			break;
		case 3:
			try {
				Log.d("miao", "【左郁】（" + TAG + "）\n" + content);
			} catch (Exception e) {
				LogUtil.e("有参数为空");
			}

			break;
		}
	}

	/**
	 * 
	 * <pre>
	 * 功能说明：打印log日志
	 * 日期：	2015年11月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param content 打印的内容
	 * </pre>
	 */

	public static void i(String content) {
		Log.i("miao", "【左郁】（" + TAG + "）\n" + content);
	}

	public static void d(String content) {
		try {
			Log.d("miao", "【左郁】（" + TAG + "）\n" + content);
		} catch (Exception e) {
			LogUtil.e("有参数为空");
		}

	}

	public static void e(String content) {
		Log.e("miao", "【左郁】（" + TAG + "）\n" + content);
	}

	/**
	 * <pre>
	 * 功能说明：打印log日志,和请求的URL
	 * 日期：	2015年11月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param content 打印内容
	 * @param params 拼接参数
	 * </pre>
	 */
	public static void d(String content, Map<String, String> params) {

		try {
			Log.i("miao",
					"【左郁】（" + TAG + "）\n" + content
							+ LogUtil.buildUrlParams(params));
		} catch (Exception e) {
			LogUtil.e("有参数为空");
		}
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
	public static String buildUrlParams(Map<String, String> params) {
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

}
