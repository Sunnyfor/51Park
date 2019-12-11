package cn.com.unispark.fragment.mine.recharge.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 【充值确认】实体类
 * 日期：	2015年7月30日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class RechargeConfirmEntity extends BaseEntity {
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
		private String orderno;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}
	}

}
