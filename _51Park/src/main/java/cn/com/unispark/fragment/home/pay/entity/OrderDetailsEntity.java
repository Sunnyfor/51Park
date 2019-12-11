package cn.com.unispark.fragment.home.pay.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 获取订单详情的实体类
 * 日期：	2015年10月27日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月27日
 * </pre>
 */
public class OrderDetailsEntity extends BaseEntity {
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
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String shparkfee;// 实付费用
		private String orderno;// 订单号
		private String parkname;// 停车场名称
		private String carno;// 车牌号
		private String starttime;// 停车时间（开始）
		private String endtime;// 停车时间（结束）
		private String parklength;// 停车时长
		private String parkfee;// 停车费用
		private String coupons;// 优惠券描述（例如：2.00元，交1元抵3元）
		private String couponstype;// 优惠券类别1.现金券2.限定券3.抵扣券
		private String cost_finance;// 余额交费的金额
		private String paytime;// 支付时间
		private String paymethod;// 支付方式
		private int status; // 交易状态

		public String getShparkfee() {
			return shparkfee;
		}

		public void setShparkfee(String shparkfee) {
			this.shparkfee = shparkfee;
		}

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getParkname() {
			return parkname;
		}

		public void setParkname(String parkname) {
			this.parkname = parkname;
		}

		public String getCarno() {
			return carno;
		}

		public void setCarno(String carno) {
			this.carno = carno;
		}

		public String getStarttime() {
			return starttime;
		}

		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}

		public String getEndtime() {
			return endtime;
		}

		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}

		public String getParklength() {
			return parklength;
		}

		public void setParklength(String parklength) {
			this.parklength = parklength;
		}

		public String getParkfee() {
			return parkfee;
		}

		public void setParkfee(String parkfee) {
			this.parkfee = parkfee;
		}

		public String getCoupons() {
			return coupons;
		}

		public void setCoupons(String coupons) {
			this.coupons = coupons;
		}

		public String getCouponstype() {
			return couponstype;
		}

		public void setCouponstype(String couponstype) {
			this.couponstype = couponstype;
		}

		public String getCost_finance() {
			return cost_finance;
		}

		public void setCost_finance(String cost_finance) {
			this.cost_finance = cost_finance;
		}

		public String getPaytime() {
			return paytime;
		}

		public void setPaytime(String paytime) {
			this.paytime = paytime;
		}

		public String getPaymethod() {
			return paymethod;
		}

		public void setPaymethod(String paymethod) {
			this.paymethod = paymethod;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "DataObject [shparkfee=" + shparkfee + ", orderno="
					+ orderno + ", parkname=" + parkname + ", carno=" + carno
					+ ", starttime=" + starttime + ", endtime=" + endtime
					+ ", parklength=" + parklength + ", parkfee=" + parkfee
					+ ", coupons=" + coupons + ", couponstype=" + couponstype
					+ ", cost_finance=" + cost_finance + ", paytime=" + paytime
					+ ", paymethod=" + paymethod + ", status=" + status + "]";
		}

	}

}
