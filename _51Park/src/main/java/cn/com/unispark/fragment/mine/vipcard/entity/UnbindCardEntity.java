package cn.com.unispark.fragment.mine.vipcard.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

public class UnbindCardEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private DataObject data;

	public DataObject getData() {
		return data;
	}

	public void setData(DataObject data) {
		this.data = data;
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {

	}

}
