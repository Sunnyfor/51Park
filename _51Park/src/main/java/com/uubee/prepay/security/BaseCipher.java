package com.uubee.prepay.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class BaseCipher {
	protected static final String HMAC_SHA256 = "HmacSHA256";
	protected static final String ALGORITHM_RSA = "RSA";
	protected static final String ALGORITHM_RSA_MODE = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
	protected static final String ALGORITHM_AES = "AES";
	protected static final String ALGORITHM_AES_MODE = "AES/CTR/NoPadding";
	static String TRADER_PRI_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDgHj9TH7R6vl64a1G9wOjmacJfL/tjBqYXR1c4OvSXRKkwRjtZ1TFeF9rIm3Wktj+pBtNmb4uGHtIjvaubphsS9fHywJjMWztJ/6sK+jD2GAI9N1baxKKyLa6aZb9ylUEDEIbH9K3bUaQycR/zsDk5RMWdfjDnBFZGKjtcyH+kqktsEJ7t8UyXQxmNgPrXWudiVtQ9D9w1AdFRgmsb86M+XJvhY1PQ2Bm0TzSj68wq63d4TChjSCT+qQ0ZFPiQ0EJUSGf9dshDfyDt7DsUc9rBPfbSg5P0C+mLJyAhQRD54Vn5Ew8hkTyXTCIxhyaGAAsNR0HK46sGHja1BMoDjXoTAgMBAAECggEAZseYVN+aXhwFxl95KiNY66oeuJaBm+VFsDFIX4Ix/1k+mePrfgQWUsdvz0X0CqiFw1ddBYIsI0LpLfjixUvE6MofvirzJ+zC4Om+IBei8okKJdbGHyfEUSBADZCR3tNx8BzZIsQkIwtq2LMmaxQq+gd+9P4VISzM//qIJMHSP8n5SpMTld69IuKtU7T54iTN1mDGpYFrVQbXmuFpf0+cowpfg5ttXC/IFKQy+hOFf3eOufHjd3NYr/aZdNnejj0d6VCsbiQ63hGLfxS1OJX5VaTbRYahQxInCPEg7NaWHLSEofUNTisDROLV+Pd2RCTCm/yvD367g9KEqkt2lhq8GQKBgQD6N7eSaEBVOqWWFwVE7tcHHTQyG71e+pW9K6/PFA3Ox6GRgsmzcydmL64RRPaNCOQaUZGvw46Ep4azZLyCyPdzKH4EmnhJW0Apt4S0rdHlXul3mOqGR3JbMMwcYn99tGq5B7nom4fqFuFWFcpbl3N+8K2acWVTJwbTwLP/HxyZ7QKBgQDlTCBNcdnKp82CUXRC1Td2ubh34UBbOMBQRKXuTB2AjwgWSTZt6zgsWveHMjxY/ikB2HiMj56kGkFvvpbvH4XG+1qWOwPNXIJg66sPNcGem92ZT76pM6c54cb+r5cbOonOxcY3teCykuZUucevVEr9ETwuDBhET18iuU3C4X/j/wKBgQDR8xpXAJ6qZPE3xM0pD/bhA0IeoWP+Y+hsuFFmQf9r99m7zLANoJv4hB1cZC80G7e51aOWhVH6QM8ni2kZnCL1P46tsicn9icaWeBB8m9iNIdVULvIO/7aQVGip4suIMpDwxXc9VPPua8Nmm8W9zdz1eL06h0I7oAuAU2GzcYj6QKBgEnzEzfmz+ab15t/yY8EVK7llqiS7L5+vwckHJ+NzLX5axJE6ljo02T6sXnYqb3Juk/uEHLEntUt4oy3WtYjxHE3/y8UOVWx0BqR5skw/RC6E+2t2j+XeQch2Zup5YHSjz8waYzuC1mMJtyMTeottUGInW/8VpcxQe1g9cM0FprhAoGBAPcD6AZsymrXoRWrHtS9TgsIVc2KLEsSMbidnQB8xyMtB7GBZAA2Ckg4LL1ZpBA7c/HMAovLXdiK94TPDs7qTncSHkGXU73+gurrKYk/CPRjnooUKNdGg9uwM5X/u0J/uyRYIygq4Wlem2XC52P1n/I85NJ8dzak00soUdtUKOeD";

	public BaseCipher() {
	}

	protected static byte[] signature(byte[] data, byte[] hmacKey)
			throws InvalidKeyException, NoSuchAlgorithmException {
		SecretKeySpec signingKey = new SecretKeySpec(hmacKey, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);
		return mac.doFinal(data);
	}

	protected static IvParameterSpec createCtrIv(byte[] nonce) {
		byte[] counter = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0,
				(byte) 0, (byte) 0, (byte) 0, (byte) 0 };
		if (nonce.length == 8) {
			byte[] output = new byte[nonce.length + counter.length];
			System.arraycopy(nonce, 0, output, 0, nonce.length);
			System.arraycopy(counter, 0, output, nonce.length, counter.length);
			return new IvParameterSpec(output);
		} else if (nonce.length == 16) {
			return new IvParameterSpec(nonce);
		} else {
			throw new RuntimeException("nonce length must be 8 or 16");
		}
	}

	protected static String createMessage(byte[] nonce, byte[] cipherText) {
		return base64Encode(nonce) + "$" + base64Encode(cipherText);
	}

	protected static String base64Encode(byte[] data) {
		return Base64.encodeToString(data, 2);
	}

	protected static byte[] base64Decode(String data) {
		return Base64.decode(data, 2);
	}
}
