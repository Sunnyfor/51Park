package cn.com.unispark.fragment.mine.recharge.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 【充值记录】实体类
 * 日期：	2015年7月30日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class RechargeConfigEntity extends BaseEntity {
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
		private int count;
		private List<RechargeConfigInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<RechargeConfigInfo> getList() {
			return list;
		}

		public void setList(List<RechargeConfigInfo> list) {
			this.list = list;
		}

		/** 2. ****************************************************************************************/
		public class RechargeConfigInfo implements Serializable {
			private static final long serialVersionUID = 1L;
			// "money": "0.04"
			private String money;

			public String getMoney() {
				return money;
			}

			public void setMoney(String money) {
				this.money = money;
			}
		}

	}

}
