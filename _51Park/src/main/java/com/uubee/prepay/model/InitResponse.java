package com.uubee.prepay.model;

import com.uubee.prepay.model.CashGift;
import java.io.Serializable;
import java.util.ArrayList;

public class InitResponse implements Serializable {
    public String ret_code;
    public String ret_msg;
    public String transcode;
    public String user_login;
    public String mob_user;
    public String oid_userno;
    public String name_user;
    public String acct_balance;
    public String need_auth;
    public String need_paypasswd;
    public String oid_traderno;
    public String name_trader;
    public ArrayList<CashGift> cashgiftlist;
    public String partner_sign_type;
    public String partner_sign;
    public String money_order;
    public String oid_paybill;
    public String date_repay;
    public String url_webview;
    public String id_type;
    public String id_no;
    public String type_passwd;
    public String flag_paypasswd;
    public String flag_active;
    public String illegal_url;
    public String flag_youbei;
    public String flag_real;
    public String need_real;
    public String word_active_above;
    public String word_active_top;
    public String word_contact_us;
    public String word_marketing;
    public String word_marketing_before;

    public InitResponse() {
    }
}