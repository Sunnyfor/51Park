package cn.com.unispark.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

/**
 * 
 * <pre>
 * 功能说明： 简单的计算工具类
 * 日期：	2015年10月25日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月25日
 * </pre>
 */
public class ReckonUtil {

	/**
	 * <pre>
	 * 功能说明：格式化距离，保留两位小数
	 * 			若距离大于1000米，则已km为单位，反之以m为单位
	 * 日期：	2015年11月3日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param  String类型
	 * @return String类型
	 * </pre>
	 */
	public static String getDistanceFormat(String distance) {
		String result = "";

		Float distanc = Float.parseFloat(distance);
		// 后台返回的是km，判断如果大于1km，就以"千米"为单位，反之以"米"为单位。
		if (distanc >= 1) {
			DecimalFormat formatter = new DecimalFormat("###,##0.00");
			result = formatter.format(distanc) + "千米";
		} else {
			DecimalFormat formatter = new DecimalFormat("###,##0");
			distance = String.valueOf(distanc * 1000);
			result = formatter.format(Float.parseFloat(distance)) + "米";
		}

		return result;

	}

	/**
	 * <pre>
	 * 功能说明：保留两位小数
	 * 日期：	2016年3月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param string
	 * @return
	 * </pre>
	 */
	public static String getTwoPointFormat(String string) {
		Float str = Float.parseFloat(string);
		DecimalFormat formatter = new DecimalFormat("###,##0.00");

		return formatter.format(str);

	}

	// public static String getDistanceKmFormat(String distance) {
	// String result = "";
	// if (1 < Float.parseFloat(distance)) {
	// DecimalFormat formatter = new DecimalFormat("###,##0.00");
	// result = formatter.format(Float.valueOf(distance)) + "km";
	// } else {
	// DecimalFormat formatter = new DecimalFormat("###,##0");
	// result = formatter.format(Float.valueOf(Float.valueOf(distance) * 1000))
	// + "m";
	// }
	//
	// return result;
	//
	// }

	/**
	 * <pre>
	 * 功能说明：格式化金额，保留两位小数
	 * 日期：	2015年11月3日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param money String类型
	 * @return String类型
	 * </pre>
	 */
	public static String getMoneyFormat(String money) {

		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		String result = decimalFormat.format(Float.valueOf(money));// format
		// 返回的是字符串
		return result;

	}

	/**
	 * <pre>
	 * 功能说明：格式化金额，保留两位小数
	 * 日期：	2015年11月3日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param money float类型
	 * @return float类型
	 * </pre>
	 */
	public static String getMoneyFormat(float money) {

		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		String result = decimalFormat.format(money);// format 返回的是字符串
		return result;

	}

	/**
	 * <pre>
	 * 功能说明：计算某两点之间的距离
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param satrtLatLng	起始经纬度
	 * @param endLatLng		终点经纬度
	 * @return
	 * </pre>
	 */
	public static String getDistance(LatLng satrtLatLng, LatLng endLatLng) {
		double distance = AMapUtils.calculateLineDistance(satrtLatLng,
				endLatLng);
		String str = String.format(Locale.getDefault(), "%.2f千米",
				distance / 1000);
		return str;
	}

	/**
	 * <pre>
	 * 功能说明： 进行除法运算
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 * </pre>
	 */
	public static double div(double d1, double d2, int len) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * <pre>
	 * 功能说明： 进行乘法运算
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 * </pre>
	 */
	public static double mul(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(d1));
		BigDecimal b2 = new BigDecimal(String.valueOf(d2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * <pre>
	 * 功能说明：提供精确的减法运算
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param v1	被减数
	 * @param v2	减数
	 * @return	两参数的差
	 * </pre>
	 */
	public static double cut(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		b1.setScale(2);
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		b2.setScale(2);
		return b1.subtract(b2).setScale(2).doubleValue();
	}

	public static float cut(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		b1.setScale(2);
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		b2.setScale(2);
		return b1.subtract(b2).setScale(2).floatValue();
	}

	/**
	 * <pre>
	 * 功能说明：提供精确的减法运算
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param v1	被加数
	 * @param v2	加数
	 * @return	两参数的和
	 * </pre>
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * <pre>
	 * 功能说明：单位转换，将元转换成分
	 * 日期：	2015年10月29日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param str
	 * @return
	 * </pre>
	 */
	public static String StringY2StringF(String str) {
		double d = Double.valueOf(str).doubleValue() * 100f;
		String resultstr = Double.toString(d);
		return resultstr.substring(0, resultstr.indexOf("."));
	}

	/**
	 * <pre>
	 * 功能说明：获取路由器地址的经度
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param routableAddress
	 * @return
	 * </pre>
	 */
	public static double getLon(String routableAddress) {
		if (routableAddress == null) {
			return 0;
		}
		int index = routableAddress.indexOf(",");
		return Double.parseDouble(routableAddress.substring(index + 1));
	}

	/**
	 * <pre>
	 * 功能说明：获取路由器地址的纬度
	 * 日期：	2015年10月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param routableAddress
	 * @return
	 * </pre>
	 */
	public static double getLat(String routableAddress) {
		if (routableAddress == null) {
			return 0;
		}
		int index = routableAddress.indexOf(",");
		return Double.parseDouble(routableAddress.substring(0, index));
	}

	public static boolean getDebuggable(Context context) {
		return (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
	}

	public static JSONObject asJSONObject(String result) throws HttpException {
		try {
			return new JSONObject(result);
		} catch (JSONException jsone) {
			throw new HttpException(jsone.getMessage() + ":" + result, jsone);
		}
	}

	public static JSONArray asJSONArray(String result) throws HttpException {
		try {
			return new JSONArray(result);
		} catch (JSONException jsone) {
			throw new HttpException(jsone.getMessage() + ":" + result, jsone);
		}
	}

}
