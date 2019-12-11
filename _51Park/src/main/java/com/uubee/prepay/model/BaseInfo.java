package com.uubee.prepay.model;

import java.io.Serializable;

public class BaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String oid_partner;
	private String user_id;
	private String mob_user;

	public BaseInfo() {
	}

	public String getOid_partner() {
		return this.oid_partner;
	}

	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMob_user() {
		return this.mob_user;
	}

	public void setMob_user(String mob_user) {
		this.mob_user = mob_user;
	}

	public String toString() {
		return "PayInfo : [oid_partner=" + this.oid_partner + ",user_id="
				+ this.user_id + ",mob_user=" + this.mob_user + "]";
	}
}
