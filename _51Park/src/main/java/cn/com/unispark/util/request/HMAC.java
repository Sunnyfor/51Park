package cn.com.unispark.util.request;

import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.annotation.SuppressLint;

/**
 * <pre>
 * 功能说明： HmacSHA256
 * 日期：	2015年10月22日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月22日
 * </pre>
 */
public class HMAC {
	@SuppressLint("TrulyRandom")
	public static byte[] initHmacSHA256Key() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}

	public static byte[] encodeHmacSHA256(byte[] data, byte[] key)
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}
}
