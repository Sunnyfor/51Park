
package com.uubee.prepay.model;

import java.io.Serializable;

public class MsgVerifyOrder implements Serializable {
    public String user_login;
    public String oid_userno;
    public String mob_user;
    public String action = "sms";
    public String sms_type = "1";
    public String money_order;

    public MsgVerifyOrder() {
    }
}