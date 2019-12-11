package cn.com.unispark.fragment.mine.recharge.entity;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class RemainConsumeEntity extends BaseEntity {
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
		private String page;
		private List<RemainConsumeInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public List<RemainConsumeInfo> getList() {
			return list;
		}

		public void setList(List<RemainConsumeInfo> list) {
			this.list = list;
		}

		/** 2. ****************************************************************************************/
		public class RemainConsumeInfo implements Serializable {
			// orderno string 订单号
			// money float 金额
			// time string 时间
			private String money;
			private String orderno;
			private String time;

			public String getMoney() {
				return money;
			}

			public void setMoney(String money) {
				this.money = money;
			}

			public String getOrderno() {
				return orderno;
			}

			public void setOrderno(String orderno) {
				this.orderno = orderno;
			}

			public String getTime() {
				return time;
			}

			public void setTime(String time) {
				this.time = time;
			}

		}

	}

}
