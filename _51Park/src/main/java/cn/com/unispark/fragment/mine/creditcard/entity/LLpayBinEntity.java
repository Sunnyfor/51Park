package cn.com.unispark.fragment.mine.creditcard.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 连连银行卡卡BIN查询的实体类
 * 日期：	2015年11月12日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月12日
 * </pre>
 */
public class LLpayBinEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CardInfo data;

	public CardInfo getData() {
		return data;
	}

	public void setData(CardInfo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GetOrderEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class CardInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String bank_code;// 所属银行编号
		private String bank_name;// 所属银行名称
		private int card_type;// 银行卡类型 （2-储蓄卡 3-信用卡）

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

		public int getCard_type() {
			return card_type;
		}

		public void setCard_type(int card_type) {
			this.card_type = card_type;
		}

		@Override
		public String toString() {
			return "DataObject [bank_code=" + bank_code + ", bank_name="
					+ bank_name + ", card_type=" + card_type + "]";
		}

	}

}
