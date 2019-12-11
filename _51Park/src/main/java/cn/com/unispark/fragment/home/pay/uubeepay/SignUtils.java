package cn.com.unispark.fragment.home.pay.uubeepay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.unispark.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SignUtils {
    public static final String SIGN_TYPE_RSA = "RSA";
    private static final String PARAM_EQUAL = "=";
    private static final String PARAM_AND = "&";
    private static final String PARAM_SIGN = "sign";
    private static final String PARAM_SIGN_TYPE = "sign_type";

    /**
     * 防止工具类实例化
     *
     * @throws AssertionError
     */
    private SignUtils() {
        throw new AssertionError();
    }

    public static JSONObject addRSASignature(String jsonStr, String privateKey) {
        if (jsonStr == null) {
            return null;
        }

        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            json.put(PARAM_SIGN_TYPE, SIGN_TYPE_RSA);
        } catch (JSONException e) {
            return null;
        }

        StringBuilder sbSign = new StringBuilder();
        StringBuilder sbField = new StringBuilder();
        @SuppressWarnings("unchecked")
		Iterator<String> it = json.keys();
        while (it.hasNext()) {
            String key = it.next();
            String value = json.optString(key);
            if (!isNull(value) && !key.equals("id_no") && !key.equals("name_user")) {
                sbField.append(key).append("|");
                sbSign.append(key);
                sbSign.append(PARAM_EQUAL);
                sbSign.append(value);
                sbSign.append(PARAM_AND);
            }
        }
        sbSign.deleteCharAt(sbSign.length() - 1);

        DebugLog.e("sbSign : " + sbSign.toString());
        DebugLog.e("sbField : " + sbField.toString());
        try {
            json.put(PARAM_SIGN, Rsa.sign(sbSign.toString(), privateKey));
            json.put("sign_field", sbField.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static boolean verifyRSASignature(String jsonStr, Context context) {
        DebugLog.e("jsonStr verify: " + jsonStr);
        if (jsonStr == null) {
            return false;
        }

        String signature;
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
            signature = json.getString("partner_sign");
            json.remove("partner_sign");
            json.remove("ret_code");
            json.remove("ret_msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        List<NameValuePair> dataList = new ArrayList<NameValuePair>();
        @SuppressWarnings("unchecked")
		Iterator<String> it = json.keys();
        while (it.hasNext()) {
            String key = it.next();
            dataList.add(new NameValuePair(key, json.optString(key)));
        }
        Collections.sort(dataList);

        StringBuilder builder = new StringBuilder();
        int size = dataList.size();
        int count = 0;
        for (NameValuePair pair : dataList) {
            count++;
            if (!isNull(pair.getName())) {
                builder.append(pair.getName());
                builder.append(PARAM_EQUAL);
                builder.append(pair.getValue());
                if (count < size) {
                    builder.append(PARAM_AND);
                }
            }
        }

        try {
            String content = builder.toString();
            DebugLog.e("builder sign : " + content);
            PublicKey publicKey = SignUtils.readPublicKey(R.raw.uubee_public_key, context);
            return Rsa.doCheck(content, signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNull(String str) {
        return TextUtils.isEmpty(str) || str.equalsIgnoreCase("NULL");
    }

    public static PublicKey readPublicKey(int cert_id, Context context) throws Exception {
        InputStream in = context.getResources().openRawResource(cert_id);
        byte[] buff = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((bytesRead = in.read(buff)) != -1) {
            out.write(buff, 0, bytesRead);
        }

        byte[] publicKeyBytes = out.toByteArray();

        CertificateFactory cf = CertificateFactory.getInstance("X509");
        Certificate cert = cf.generateCertificate(new ByteArrayInputStream(publicKeyBytes));
        return cert.getPublicKey();
    }

    private static class NameValuePair implements Comparable<NameValuePair> {
        private String key;
        private String value;

        public NameValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getName() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int compareTo(@NonNull NameValuePair another) {
            return key.compareToIgnoreCase(another.getName());
        }
    }
}
