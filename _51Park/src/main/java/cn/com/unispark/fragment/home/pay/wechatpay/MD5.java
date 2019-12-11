package cn.com.unispark.fragment.home.pay.wechatpay;

import java.security.MessageDigest;

/**
 * <pre>
 * 功能说明： 加密操作
 * 日期：	2015年1月6日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年1月6日
 * </pre>
 */
public class MD5 {

	private MD5() {}
	
	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
