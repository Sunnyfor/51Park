package com.uubee.prepay.net;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    public int http_code;
    public String err_desc;
    public String body;

    public BaseResponse() {
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}