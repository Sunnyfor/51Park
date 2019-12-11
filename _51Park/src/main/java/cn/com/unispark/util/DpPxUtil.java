package cn.com.unispark.util;

import android.content.Context;

/**
 * <pre>
 * 功能说明：dp和px转换工具类
 * 日期：	2014年12月1日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月1日
 * </pre>
 */
public class DpPxUtil {

	/**
	 * <pre>
	 * 功能说明：dip转为px
	 * 日期：	2015年6月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 * </pre>
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * <pre>
	 * 功能说明：px转为dip
	 * 日期：	2015年6月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 * </pre>
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
