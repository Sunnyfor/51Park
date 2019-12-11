package com.uubee.prepay.model;

import com.uubee.prepay.model.BaseInfo;

public class ApplyInfo extends BaseInfo {
    private static final long serialVersionUID = 1L;
    private String sign_type;
    private String sign;

    public ApplyInfo() {
    }

    public String getSign_type() {
        return this.sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
