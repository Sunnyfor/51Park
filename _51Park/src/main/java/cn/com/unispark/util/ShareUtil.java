package cn.com.unispark.util;

import android.content.Context;
import android.content.SharedPreferences;
import cn.com.unispark.application.ParkApplication;

/**
 * <pre>  
 * 功能说明： SharedPreferences保存数据工具类
 * 日期：	2015年1月14日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年1月14日
 * </pre>
 */
public class ShareUtil {

	public static String orderId = "orderId";
	
	
	public static SharedPreferences getSharedObject() {
		SharedPreferences shp = ParkApplication.applicationContext
				.getSharedPreferences("51Park", Context.MODE_PRIVATE);
		return shp;
	}

	/**
	 * <pre>
	 * 功能说明：保存Integer信息
	 * 日期：	2015年1月28日
	 * 开发者：陈丶泳佐
	 * 
	 * </pre>
	 */
	public static void setSharedInteger(String key, int content) {
		getSharedObject().edit().putInt(key, content).commit();
	}

	public static int getSharedInteger(String key) {
		return getSharedObject().getInt(key, 0);
	}

	/**
	 * <pre>
	 * 功能说明：保存String信息
	 * 日期：	2015年1月28日
	 * 开发者：陈丶泳佐
	 * 
	 * </pre>
	 */
	public static void setSharedString(String key, String content) {
		getSharedObject().edit().putString(key, content).commit();
	}

	public static String getSharedString(String key) {
		return getSharedObject().getString(key, "");
	}

	/**
	 * <pre>
	 * 功能说明：保存Boolean类型的信息
	 * 日期：	2015年1月28日
	 * 开发者：陈丶泳佐
	 * 
	 * </pre>
	 */
	public static void setSharedBoolean(String key, boolean isOK) {
		getSharedObject().edit().putBoolean(key, isOK).commit();
	}

	public static boolean getSharedBoolean(String key) {
		return getSharedObject().getBoolean(key, false);
	}

}
