package com.uubee.prepay.security;

import com.uubee.prepay.security.BaseCipher;
import com.uubee.prepay.util.DebugLog;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SDKDecrypt extends BaseCipher {
	public SDKDecrypt() {
	}

	public static String aesDecrypt(byte[] cipherText, byte[] aesKey,
			byte[] nonce) throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		IvParameterSpec ips = createCtrIv(nonce);
		cipher.init(2, secretKeySpec, ips);
		byte[] data = cipher.doFinal(cipherText);
		return new String(data, "utf-8");
	}

	public static byte[] rsaDecrypt(byte[] rsaCipher) throws Exception {
		byte[] keyBytes = base64Decode(TRADER_PRI_KEY);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(keySpec);
		Cipher cipher = Cipher
				.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		cipher.init(2, priKey);
		return cipher.doFinal(rsaCipher);
	}

	public static String decrypt(String source) {
		String[] str = source.split("\\$");
		String base64HmacKey = str[1];
		String base64AesKey = str[2];
		String base64Nonce = str[3];
		String base64CipherText = str[4];
		String signData = str[5];
		if (!isValidate(base64Nonce, base64HmacKey, base64CipherText, signData)) {
			DebugLog.e("校验失败！");
		}

		return decrypt(base64CipherText, base64AesKey, base64Nonce);
	}

	public static boolean isValidate(String base64Nonce, String base64HmacKey,
			String base64CipherText, String signData) {
		try {
			byte[] e = rsaDecrypt(base64Decode(base64HmacKey));
			String message = base64Nonce + "$" + base64CipherText;
			byte[] sign = signature(message.getBytes(), e);
			String new_signature = base64Encode(sign);
			if (signData.equals(new_signature)) {
				return true;
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		return false;
	}

	public static String decrypt(String base64CipherText, String base64AesKey,
			String base64Nonce) {
		try {
			byte[] e = rsaDecrypt(base64Decode(base64AesKey));
			byte[] nonce = base64Decode(base64Nonce);
			return aesDecrypt(base64Decode(base64CipherText), e, nonce);
		} catch (Exception var5) {
			var5.printStackTrace();
			return "";
		}
	}

	public static String decryptSimple(String source, byte[] hmacKey,
			byte[] aesKey, byte[] nonce) {
		if (!source.contains("$")) {
			return "";
		} else {
			String[] str = source.split("\\$");
			String signature = str[0];
			String cipherText = str[1];
			String message = base64Encode(nonce) + "$" + cipherText;

			try {
				byte[] e = signature(message.getBytes(), hmacKey);
				if (!signature.equals(base64Encode(e))) {
					DebugLog.e("signature not same...");
					return "";
				} else {
					return aesDecrypt(base64Decode(cipherText), aesKey, nonce);
				}
			} catch (Exception var9) {
				var9.printStackTrace();
				return "";
			}
		}
	}
}
