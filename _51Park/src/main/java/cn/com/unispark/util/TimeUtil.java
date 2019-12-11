package cn.com.unispark.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 * 功能说明： Time工具类
 * 日期：	2014年12月1日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月1日
 * </pre>
 */
public class TimeUtil {

	/**
	 * <pre>
	 * 功能说明：获取系统当前时间
	 * 日期：	2015年6月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return yyyy.MM.dd HH:mm:ss
	 * </pre>
	 */
	public static String getSystemTime() {
		String currentTime = null;
		// 获取系统时间
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy.MM.dd HH:mm:ss", Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		currentTime = formatter.format(curDate);

		return currentTime;
	}

	/**
	 * <pre>
	 * 功能说明：获取当前年、月、日、时、分、字符串
	 * 日期：	2015年6月24日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return	yyyy-MM-dd HH:mm
	 * </pre>
	 */
	public static String getDateTime() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm", Locale.getDefault());
		Date localDate = new Date();
		return localSimpleDateFormat.format(localDate);
	}

	/**
	 * <pre>
	 * 功能说明：获取当前时、分、字符串
	 * 日期：	2015年6月24日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return	HH:mm
	 * </pre>
	 */
	public static String getHourAndMinuteTime() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH:mm",
				Locale.getDefault());
		Date localDate = new Date();
		return localSimpleDateFormat.format(localDate);
	}

	/**
	 * <pre>
	 * 功能说明：获取当前时、字符串
	 * 日期：	2015年6月24日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return	HH
	 * </pre>
	 */
	public static int getHourTime() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH",
				Locale.getDefault());
		Date localDate = new Date();
		return Integer.parseInt(localSimpleDateFormat.format(localDate));
	}

	/**
	 * <pre>
	 * 功能说明：Date日期转为String类型
	 * 日期：	2015年6月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param dateStr	yyyy-MM-dd
	 * @return	yyyy年MM月dd日
	 * @throws Exception
	 * </pre>
	 */
	public static String getDate2str(String dateStr) throws Exception {
		Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
				.parse(dateStr);
		String now = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
				.format(date);
		return now;
	}

	/**
	 * <pre>
	 * 功能说明：String类型转为Date日期
	 * 日期：	2015年6月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param dateStr	yyyy年MM月dd日
	 * @return	yyyy-MM-dd
	 * @throws Exception
	 * </pre>
	 */
	public static String getStr2date(String dateStr) throws Exception {
		Date date = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
				.parse(dateStr);
		String now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
				.format(date);
		return now;
	}

}
