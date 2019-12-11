package com.uubee.prepay.model;

import com.uubee.prepay.model.CashGift;
import java.io.Serializable;
import java.util.ArrayList;

public class CreateOrderResponse implements Serializable {
    public String ret_code;
    public String ret_msg;
    public int user_status;
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
    public String word_repay_date;
    public String token;
    public int trader_flag_real;

    public CreateOrderResponse() {
    }

	@Override
	public String toString() {
		return "CreateOrderResponse [ret_code=" + ret_code + ", ret_msg="
				+ ret_msg + ", user_status=" + user_status + ", user_login="
				+ user_login + ", mob_user=" + mob_user + ", oid_userno="
				+ oid_userno + ", name_user=" + name_user + ", acct_balance="
				+ acct_balance + ", need_auth=" + need_auth
				+ ", need_paypasswd=" + need_paypasswd + ", oid_traderno="
				+ oid_traderno + ", name_trader=" + name_trader
				+ ", cashgiftlist=" + cashgiftlist + ", partner_sign_type="
				+ partner_sign_type + ", partner_sign=" + partner_sign
				+ ", money_order=" + money_order + ", oid_paybill="
				+ oid_paybill + ", date_repay=" + date_repay + ", url_webview="
				+ url_webview + ", id_type=" + id_type + ", id_no=" + id_no
				+ ", type_passwd=" + type_passwd + ", flag_paypasswd="
				+ flag_paypasswd + ", flag_active=" + flag_active
				+ ", illegal_url=" + illegal_url + ", flag_youbei="
				+ flag_youbei + ", flag_real=" + flag_real + ", need_real="
				+ need_real + ", word_active_above=" + word_active_above
				+ ", word_active_top=" + word_active_top + ", word_contact_us="
				+ word_contact_us + ", word_marketing=" + word_marketing
				+ ", word_marketing_before=" + word_marketing_before
				+ ", word_repay_date=" + word_repay_date + ", token=" + token
				+ ", trader_flag_real=" + trader_flag_real + "]";
	}
    
    
    
}