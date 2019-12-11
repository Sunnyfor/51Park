package com.uubee.prepay.model;

import java.io.Serializable;

public class CashGift implements Serializable, Comparable<CashGift> {
    public String oid_cashgift;
    public String amt_cashgift;
    public int type_cashgift;
    public String oid_userno;
    public int sta_cashgift;
    public String dt_start;
    public String dt_end;

    public CashGift() {
    }

    public int compareTo(CashGift another) {
        if(another == null) {
            return -1;
        } else if(this.sta_cashgift == 0 && another.sta_cashgift != 0) {
            return -1;
        } else if(this.sta_cashgift != 0 && another.sta_cashgift == 0) {
            return 1;
        } else {
            try {
                float e = Float.valueOf(this.amt_cashgift).floatValue();
                float moneyOther = Float.valueOf(another.amt_cashgift).floatValue();
                return e > moneyOther?-1:(e == moneyOther?another.dt_end.compareTo(this.dt_end):1);
            } catch (Exception var4) {
                var4.printStackTrace();
                return 0;
            }
        }
    }
}
