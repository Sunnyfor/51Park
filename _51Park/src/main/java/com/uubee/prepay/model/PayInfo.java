package com.uubee.prepay.model;

import com.uubee.prepay.model.BaseInfo;

public class PayInfo extends BaseInfo {
    private String sign_type;
    private String sign;
    private String busi_partner;
    private String no_order;
    private String dt_order;
    private String money_order;
    private String notify_url;
    private String valid_order;

    public PayInfo() {
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

    public String getBusi_partner() {
        return this.busi_partner;
    }

    public void setBusi_partner(String busi_partner) {
        this.busi_partner = busi_partner;
    }

    public String getNo_order() {
        return this.no_order;
    }

    public void setNo_order(String no_order) {
        this.no_order = no_order;
    }

    public String getDt_order() {
        return this.dt_order;
    }

    public void setDt_order(String dt_order) {
        this.dt_order = dt_order;
    }

    public String getMoney_order() {
        return this.money_order;
    }

    public void setMoney_order(String money_order) {
        this.money_order = money_order;
    }

    public String getNotify_url() {
        return this.notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getValid_order() {
        return this.valid_order;
    }

    public void setValid_order(String valid_order) {
        this.valid_order = valid_order;
    }
}