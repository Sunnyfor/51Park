package cn.com.unispark.fragment.mine.creditcard.entity;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class CreditCardUnBindEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private DataObject data;

	public DataObject getData() {
		return data;
	}

	public void setData(DataObject data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GetOrderEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// sign_type string 签名方式
		// sign string 签名
		// card_no string 银行卡号
		String sign_type;
		String sign;
		public String getSign_type() {
			return sign_type;
		}
		public void setSign_type(String sign_type) {
			this.sign_type = sign_type;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
	}

}
