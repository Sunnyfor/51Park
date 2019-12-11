package com.uubee.prepay.model;

import com.uubee.prepay.model.ResultBase;

public class ResultPay extends ResultBase {
    public String partner_sign;
    public String sign_type;
    public String oid_partner;
    public String dt_order;
    public String no_order;
    public String oid_paybill;
    public String result_pay;
    public String money_order;
    public String settle_date;
    public String info_order;

    public ResultPay() {
    }
}