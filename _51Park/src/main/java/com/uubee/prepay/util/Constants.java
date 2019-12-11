package com.uubee.prepay.util;

public class Constants {
    public static final int TRANSCODE_CREDIT_QUERY = 6001;
    public static final int TRANSCODE_CREDIT_APPLY = 6002;
    public static final int TRANSCODE_CREDIT_PAY = 6003;
    public static final int TRANSCODE_BILL_QUERY = 6004;
    public static final int TRANSCODE_USER_ACTIVE = 6005;
    public static final int TRANSCODE_VERIFY_CODE = 6006;
    public static final int TRANSCODE_ORDER_INIT = 6007;
    public static final int TRANSCODE_CREATE_ORDER = 6008;
    public static final int PASSWORD_TYPE_NOPASS = 1;
    public static final int PASSWORD_TYPE_PAY_PAWD = 2;
    public static final int PASSWORD_TYPE_MSG_VERIFY = 3;
    public static final int PASSWORD_TYPE_BOTH = 4;
    public static final String SIGN_KEY = "uubee1234567890";
    public static final String SERVER_URL_HTTP = "http://u.uubee.com/uubee_sdkserver/";
    public static final String SERVER_URL_HTTPS = "https://u.uubee.com/uubee_sdkserver/";
    public static final String TEST_SERVER_URL_HTTP = "http://192.168.1.8:8090/uubee_sdkserver/";
    public static final String TEST_OUT_SERVER_URL_HTTP = "http://60.12.6.46:8090/uubee_sdkserver/";
    public static final String DEV_SERVER_URL_HTTP = "http://192.168.1.7:80/uubee_sdkserver/";
    public static final String OUT_DEV_SERVER_URL_HTTP = "http://60.12.6.46:8070/uubee_sdkserver/";
    public static final String CREATE_ORDER = "createorder";
    public static final String CREDIT_QUERY = "creditquery";
    public static final String CREDIT_APPLY = "creditapply";
    public static final String CREDIT_PAY = "creditpay";
    public static final String BILL_QUERY = "billquery";
    public static final String ORDER_INIT = "orderinit";
    public static final String USER_ACTIVE = "useractive";
    public static final String SMS_APPLY = "smsapply";
    public static final String TRANSCODE = "transcode";
    public static final String SIGN = "sign";
    public static final String IMEI = "imei_mob";
    public static final String IMSI = "imsi_mob";
    public static final String MACHINE = "id_machine";
    public static final String IPADDRESS = "ip_address";
    public static final String SIGN_TYPE = "sign_type";
    public static final String PARNTER_SIGN = "partner_sign";
    public static final String PARNTER_SIGN_TYPE = "partner_sign_type";
    public static final String BLACK_BOX = "black_box";
    public static final String PAY_REQ = "pay_req";
    public static final String QUERY_RES = "query_res";
    public static final String PAY_RESULT = "pay_result";
    public static final String CASH_LIST = "cash_list";
    public static final String CASH_SELECT_INDEX = "cash_select_index";
    public static final String SHOW_SUCCESS_PAGE = "show_success_page";
    public static final int START_SELECT_CASH = 1;
    public static final int START_INFO_SUPPLY = 2;

    private Constants() {
        throw new AssertionError();
    }
}
