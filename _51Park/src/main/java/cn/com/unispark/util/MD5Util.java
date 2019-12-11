package cn.com.unispark.util;

import java.security.MessageDigest;

/**
 * <pre>
 * Function：
 *
 * Created by JoannChen on 2017/10/11 13:54
 * E-mail:Q8622268@foxmail.com
 * QQ:411083907
 * </pre>
 */
public class MD5Util {

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String code, String charset) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charset == null || "".equals(charset))
                code = byteArrayToHexString(md.digest(code
                        .getBytes()));
            else
                code = byteArrayToHexString(md.digest(code
                        .getBytes(charset)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
