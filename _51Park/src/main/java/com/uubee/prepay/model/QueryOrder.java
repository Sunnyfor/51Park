package com.uubee.prepay.model;

import com.uubee.prepay.model.BaseOrder;

public class QueryOrder extends BaseOrder {
    private static final long serialVersionUID = 1L;
    private String money_order;

    public QueryOrder() {
    }

    public String getMoney_order() {
        return this.money_order;
    }

    public void setMoney_order(String money_order) {
        this.money_order = money_order;
    }
}