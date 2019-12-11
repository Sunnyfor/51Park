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
public class RecordDetailEntity extends BaseEntity {
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
		 * "waynum": "支付宝充值", 
		 * "money": "0.01", 
		 * "orderno": "2016011816030114537",
		 * "remarks": "余额充值", 
		 * "time": "2016-01-18 16:03:15", 
		 * "balancetype": "2",
		 * "typenote": "收入"
		 */
//		"money": "0.11",
//        "orderno": "16011813344171022",
//        "time": "2016-01-18 13:54:29",
//        "remarks": "交停车费",
//        "balancetype": "1",
//        "typenote": "支出"
		private static final long serialVersionUID = 1L;
		private String typenote;
		private String balancetype;
		private String waynum;
		private String orderno;
		private String money;
		private String time;
		private String remarks;

		public String getTypenote() {
			return typenote;
		}

		public void setTypenote(String typenote) {
			this.typenote = typenote;
		}

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getBalancetype() {
			return balancetype;
		}

		public void setBalancetype(String balancetype) {
			this.balancetype = balancetype;
		}

		public String getWaynum() {
			return waynum;
		}

		public void setWaynum(String waynum) {
			this.waynum = waynum;
		}

	}

}
