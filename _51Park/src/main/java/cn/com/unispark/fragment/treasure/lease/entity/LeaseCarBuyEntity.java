package cn.com.unispark.fragment.treasure.lease.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 车位租凭购买实体类
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
public class LeaseCarBuyEntity extends BaseEntity {
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

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String orderno;// 订单号
		private int result;// 1成功 2失败

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public int getResult() {
			return result;
		}

		public void setResult(int result) {
			this.result = result;
		}
	}

}
