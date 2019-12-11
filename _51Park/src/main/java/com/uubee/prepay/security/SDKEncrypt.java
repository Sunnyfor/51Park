
package com.uubee.prepay.security;

import com.uubee.prepay.net.BaseRequest;
import com.uubee.prepay.security.BaseCipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public class SDKEncrypt extends BaseCipher {
	public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqrZWXtOdrK3FOmXwQ5Dx3+nP7WAWsOu6lwDCBg6mJPKh1Dnju8cKuFyhRFgk6OtD/5tRaqRa7xhYkjQFIT2cAPVLzW6/V2unUF+zrHKfr0Vm7z8UtjOUxvGtTR/9ZUa2hhxGLEQUOG5n31ZGzXCgZ9qx4Qupj4vPY3Fr4lFAn/ewRWg1eEu9Ktx+wImy7BJOZWsjh6O2I90+fEGskIBV7Ryqmslf3GTF6Wt5SmQFhHUR61JiD3/ZJRccGkdH8jUTTayZf3dICqzO0vFdbH/agHzsfXYbJmhHY2sq0qI9kSTMUh0OegWFm67yMcGXhYs0cjZuDgVhxXnVHmAPe3PX2wIDAQAB";
	public static final String SDK_VERSION = "1.1";

	public SDKEncrypt() {
	}

	public static byte[] rsaEncrypt(byte[] data) throws Exception {
		byte[] keyByte = base64Decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqrZWXtOdrK3FOmXwQ5Dx3+nP7WAWsOu6lwDCBg6mJPKh1Dnju8cKuFyhRFgk6OtD/5tRaqRa7xhYkjQFIT2cAPVLzW6/V2unUF+zrHKfr0Vm7z8UtjOUxvGtTR/9ZUa2hhxGLEQUOG5n31ZGzXCgZ9qx4Qupj4vPY3Fr4lFAn/ewRWg1eEu9Ktx+wImy7BJOZWsjh6O2I90+fEGskIBV7Ryqmslf3GTF6Wt5SmQFhHUR61JiD3/ZJRccGkdH8jUTTayZf3dICqzO0vFdbH/agHzsfXYbJmhHY2sq0qI9kSTMUh0OegWFm67yMcGXhYs0cjZuDgVhxXnVHmAPe3PX2wIDAQAB");
		X509EncodedKeySpec x509ek = new X509EncodedKeySpec(keyByte);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509ek);
		Cipher cipher = Cipher
				.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		cipher.init(1, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] aesEncrypt(byte[] data, byte[] aesKey, byte[] nonce)
			throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		IvParameterSpec ips = createCtrIv(nonce);
		cipher.init(1, secretKeySpec, ips);
		return cipher.doFinal(data);
	}

	public static String encrypt(String source) throws Exception {
		byte[] data = source.getBytes("utf-8");
		SecureRandom random = new SecureRandom();
		byte[] hmacKey = new byte[16];
		random.nextBytes(hmacKey);
		byte[] aesKey = new byte[16];
		random.nextBytes(aesKey);
		byte[] nonce = new byte[8];
		random.nextBytes(nonce);
		String signData = encrypt(data, byte2hex(hmacKey), byte2hex(aesKey),
				byte2hex(nonce));
		JSONObject signBody = new JSONObject();
		signBody.put("payload", signData);
		signBody.put("ver_sdk", "1.1");
		return signBody.toString();
	}

	public static String encrypt(BaseRequest request) throws Exception {
		byte[] data = request.getBody().getBytes("utf-8");
		SecureRandom random = new SecureRandom();
		byte[] hmacKey = new byte[16];
		random.nextBytes(hmacKey);
		request.hmacKey = byte2hex(hmacKey);
		byte[] aesKey = new byte[16];
		random.nextBytes(aesKey);
		request.aesKey = byte2hex(aesKey);
		byte[] nonce = new byte[8];
		random.nextBytes(nonce);
		request.nonce = byte2hex(nonce);
		String signData = encrypt(data, request.hmacKey, request.aesKey,
				request.nonce);
		JSONObject signBody = new JSONObject();
		signBody.put("payload", signData);
		signBody.put("ver_sdk", "1.1");
		return signBody.toString();
	}

	private static String encrypt(byte[] data, byte[] hmacKey, byte[] aesKey,
			byte[] nonce) throws Exception {
		byte[] encHmacKey = rsaEncrypt(hmacKey);
		byte[] encAesKey = rsaEncrypt(aesKey);
		byte[] cipherText = aesEncrypt(data, aesKey, nonce);
		String message = createMessage(nonce, cipherText);
		byte[] signData = signature(message.getBytes(), hmacKey);
		return "uubee_android_1.1$" + base64Encode(encHmacKey) + "$"
				+ base64Encode(encAesKey) + "$" + base64Encode(nonce) + "$"
				+ base64Encode(cipherText) + "$" + base64Encode(signData);
	}

	private static String encryptSimple(String source, byte[] hmacKey,
			byte[] aesKey, byte[] nonce) throws Exception {
		byte[] data = source.getBytes("utf-8");
		byte[] cipherText = aesEncrypt(data, aesKey, nonce);
		String base64CipherText = base64Encode(cipherText);
		String message = base64Encode(nonce) + "$" + base64CipherText;
		byte[] signData = signature(message.getBytes(), hmacKey);
		return base64Encode(signData) + "$" + base64CipherText;
	}

	private static byte[] byte2hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		byte[] var3 = bytes;
		int var4 = bytes.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			byte aByte = var3[var5];
			int v = aByte & 255;
			if (v < 16) {
				sb.append('0');
			}

			sb.append(Integer.toHexString(v));
		}

		return sb.toString().toUpperCase().getBytes();
	}
}
