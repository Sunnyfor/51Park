package cn.com.unispark.util;

import android.text.TextUtils;
import android.widget.Toast;
import cn.com.unispark.application.ParkApplication;

/**
 * <pre>
 * 功能说明： Toast工具类
 * 日期：	2014年12月1日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月1日
 * </pre>
 */
public class ToastUtil {

	/**
	 * <pre>
	 * 功能说明：单例模式Toast
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param content
	 * </pre>
	 */
	private static Toast toast;

	public static void showToast(String content) {
		if (!TextUtils.isEmpty(content)) {
			if (toast == null) {
				toast = Toast.makeText(ParkApplication.applicationContext,
						content, Toast.LENGTH_SHORT);
			} else {
				toast.setText(content);
			}
			toast.show();
		}
	}

	/**
	 * <pre>
	 * 功能说明：提供给外部调用的方法
	 * 日期：	2015年12月1日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param content
	 * </pre>
	 */
	public static void show(String content) {

		if (!TextUtils.isEmpty(content)) {
			if (toast == null) {
				toast = Toast.makeText(ParkApplication.applicationContext,
						content, Toast.LENGTH_SHORT);
			} else {
				toast.setText(content);
			}
			toast.show();
		}
	}

	/**
	 * <pre>
	 * 功能说明：网络不可用时返回的提示语:
	 * 		"网络异常，请检查网络设置!"
	 * 日期：	2015年1月20日
	 * 开发者：陈丶泳佐
	 * 
	 * </pre>
	 */
	public static void showToastNetError() {
		showToast("网络异常，请检查网络设置!");
	}

	/**
	 * <pre>
	 * 功能说明：JSON获取为空时返回的提示语:
	 * 		"数据获取失败,请检查网络设置!"
	 * 日期：	2015年3月3日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public static void showToastJsonError() {
		showToast("数据获取失败,请检查网络设置!");
	}

}
