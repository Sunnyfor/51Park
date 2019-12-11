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
public class RechargeRecordEntity extends BaseEntity {
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
		private List<RechargeRecordInfo> list;

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

		public List<RechargeRecordInfo> getList() {
			return list;
		}

		public void setList(List<RechargeRecordInfo> list) {
			this.list = list;
		}

		/** 2. ****************************************************************************************/
		public class RechargeRecordInfo implements Serializable {
			/**
			 *  "statue": "1",
                "rechargewag": "支付宝充值",
                "waynum": "1",
			 */
			private static final long serialVersionUID = 1L;
//            "parkname": "无忧停车场",
			private String statue;
			private String rechargewag;
			private String waynum;
			
			private String money;
			private String orderno;
			private String time;
			private String parkname;
			
			private String ordertime;
			private String balancetype;
			
			public String getStatue() {
				return statue;
			}
			public void setStatue(String statue) {
				this.statue = statue;
			}
			public String getRechargewag() {
				return rechargewag;
			}
			public void setRechargewag(String rechargewag) {
				this.rechargewag = rechargewag;
			}
			public String getWaynum() {
				return waynum;
			}
			public void setWaynum(String waynum) {
				this.waynum = waynum;
			}
			public String getParkname() {
				return parkname;
			}
			public void setParkname(String parkname) {
				this.parkname = parkname;
			}
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
			public String getOrdertime() {
				return ordertime;
			}
			public void setOrdertime(String ordertime) {
				this.ordertime = ordertime;
			}
			public String getBalancetype() {
				return balancetype;
			}
			public void setBalancetype(String balancetype) {
				this.balancetype = balancetype;
			}

			
		}

	}

}
