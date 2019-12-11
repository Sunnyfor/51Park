package cn.com.unispark.fragment.mine.creditcard.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 连连信用卡签约验证的实体类[绑卡验证]
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
public class CreditCardFinishEntity extends BaseEntity {
	/**
	 * 
	 */
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
		String sign_type;// 签名方式
		String sign;// 签名
		String card_no;// 银行卡号

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

		public String getCard_no() {
			return card_no;
		}

		public void setCard_no(String card_no) {
			this.card_no = card_no;
		}

		@Override
		public String toString() {
			return "DataObject [sign_type=" + sign_type + ", sign=" + sign
					+ ", card_no=" + card_no + "]";
		}

	}

}
