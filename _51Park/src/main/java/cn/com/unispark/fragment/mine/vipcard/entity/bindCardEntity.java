package cn.com.unispark.fragment.mine.vipcard.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

public class bindCardEntity extends BaseEntity {
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
		private static final long serialVersionUID = 1L;
		private String card_no_qr;
		public String getCard_no_qr() {
			return card_no_qr;
		}
		public void setCard_no_qr(String card_no_qr) {
			this.card_no_qr = card_no_qr;
		}
		public String getBinddate() {
			return binddate;
		}
		public void setBinddate(String binddate) {
			this.binddate = binddate;
		}
		private String binddate;
	}

}
