package cn.com.unispark.fragment.home.pay.uubeepay;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * RSA工具类
 */
public class Rsa {
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(base64Decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes("utf-8"));
            byte[] signed = signature.sign();
            return base64Encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean doCheck(String content, String sign, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(content.getBytes("utf-8"));

            return signature.verify(base64Decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    protected static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    protected static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
