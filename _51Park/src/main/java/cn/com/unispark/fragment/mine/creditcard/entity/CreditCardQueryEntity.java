package cn.com.unispark.fragment.mine.creditcard.entity;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class CreditCardQueryEntity extends BaseEntity {
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

		// "bank_code": "03080000",
		// "card_type": "3",
		// "bank_name": "招商银行"
		String bank_code;
		String card_type;
		String bank_name;
		public String getBank_code() {
			return bank_code;
		}
		public void setBank_code(String bank_code) {
			this.bank_code = bank_code;
		}
		public String getCard_type() {
			return card_type;
		}
		public void setCard_type(String card_type) {
			this.card_type = card_type;
		}
		public String getBank_name() {
			return bank_name;
		}
		public void setBank_name(String bank_name) {
			this.bank_name = bank_name;
		}
	}

}
