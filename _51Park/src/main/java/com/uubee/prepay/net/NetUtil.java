package com.uubee.prepay.net;

import com.uubee.prepay.net.BaseRequest;
import com.uubee.prepay.net.BaseResponse;
import com.uubee.prepay.security.SDKDecrypt;
import com.uubee.prepay.security.SDKEncrypt;
import com.uubee.prepay.util.DebugLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public enum NetUtil {
    INSTANCE;

    private NetUtil() {
    }

    public BaseResponse submit(BaseRequest req) throws IOException {
        URL url = new URL(req.getUrl());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        OutputStream outputStream = connection.getOutputStream();
        String signBody = req.getBody();

        try {
            DebugLog.e("source : " + signBody);
            signBody = SDKEncrypt.encrypt(req);
            DebugLog.e("signBody : " + signBody);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        outputStream.write(signBody.getBytes());
        int responseCode = connection.getResponseCode();
        if(responseCode == 200) {
            InputStream badResponse1 = connection.getInputStream();
            return this.dealResponseResult(badResponse1, req);
        } else {
            BaseResponse badResponse = new BaseResponse();
            badResponse.http_code = responseCode;
            badResponse.err_desc = connection.getResponseMessage();
            DebugLog.e("http_code : " + responseCode + " -- " + badResponse.err_desc);
            return badResponse;
        }
    }

    public BaseResponse dealResponseResult(InputStream inputStream, BaseRequest req) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];

        int len;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        }

        BaseResponse response = new BaseResponse();
        String resultData = new String(byteArrayOutputStream.toByteArray());

        try {
            JSONObject e = new JSONObject(resultData);
            DebugLog.e("resultData:" + resultData);
            if(e.has("payload")) {
                resultData = e.optString("payload");
                String decrypt = SDKDecrypt.decryptSimple(resultData, req.hmacKey, req.aesKey, req.nonce);
                DebugLog.e("decryptData:" + decrypt);
                response.setBody(decrypt);
            } else {
                response.setBody(resultData);
            }
        } catch (JSONException var10) {
            var10.printStackTrace();
            response.setBody("");
        }

        return response;
    }
}
