package cn.com.unispark.fragment.mine.creditcard.entity;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class UserCreditCardEntity extends BaseEntity {
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
		private int count;
		// "count": 0,
		// "agreement_list": [
		//
		// ]
		private List<CreditCardInfo> agreement_list;

		public List<CreditCardInfo> getAgreement_list() {
			return agreement_list;
		}

		public void setAgreement_list(List<CreditCardInfo> agreement_list) {
			this.agreement_list = agreement_list;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		/** 2. ****************************************************************************************/
		public class CreditCardInfo implements Serializable {
			// no_agree string 签约协议号
			// card_no string 银行卡号后4位
			// bank_code string 所属银行编号
			// bank_name string 所属银行名称
			// card_type string 银行卡类型 2-储蓄卡 3-信用卡
			// bind_mobile string 手机号码
			private String no_agree;
			private String card_no;
			private String bank_code;
			private String bank_name;
			private String card_type;
			private String bind_mobile;

			public String getNo_agree() {
				return no_agree;
			}

			public void setNo_agree(String no_agree) {
				this.no_agree = no_agree;
			}

			public String getCard_no() {
				return card_no;
			}

			public void setCard_no(String card_no) {
				this.card_no = card_no;
			}

			public String getBank_code() {
				return bank_code;
			}

			public void setBank_code(String bank_code) {
				this.bank_code = bank_code;
			}

			public String getBank_name() {
				return bank_name;
			}

			public void setBank_name(String bank_name) {
				this.bank_name = bank_name;
			}

			public String getCard_type() {
				return card_type;
			}

			public void setCard_type(String card_type) {
				this.card_type = card_type;
			}

			public String getBind_mobile() {
				return bind_mobile;
			}

			public void setBind_mobile(String bind_mobile) {
				this.bind_mobile = bind_mobile;
			}

		}

	}

}
