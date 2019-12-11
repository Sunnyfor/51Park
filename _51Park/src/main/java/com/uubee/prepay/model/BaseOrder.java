package com.uubee.prepay.model;

import java.io.Serializable;

public class BaseOrder implements Serializable {
    public String oid_partner;
    public String user_id;
    public String mob_user;
    public String transcode;
    public String imei_mob;
    public String imsi_mob;
    public String id_machine;
    public String ip_address;
    public String mac_request;
    public String token;

    public BaseOrder() {
    }
}
