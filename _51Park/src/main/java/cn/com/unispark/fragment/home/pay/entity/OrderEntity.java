package cn.com.unispark.fragment.home.pay.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 获取账单的实体类
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
public class OrderEntity extends BaseEntity {
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
		private int msgcode;
		private InfoObject info;

		public int getMsgcode() {
			return msgcode;
		}

		public void setMsgcode(int msgcode) {
			this.msgcode = msgcode;
		}

		public InfoObject getInfo() {
			return info;
		}

		public void setInfo(InfoObject info) {
			this.info = info;
		}

		@Override
		public String toString() {
			return "DataObject [msgcode=" + msgcode + ", info=" + info + "]";
		}

		/** 2. ****************************************************************************************/

		public class InfoObject implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private String parkname;// 停车场名称
			private String address;// 停车场地址
			private String parkid;// 停车场ID号
			private String cardno;// 卡号
			private String username;// 用户名
			private String carno;// 车牌号
			private String starttime;// 停车开始时间(例如：08-10 08:10am)
			private String endtime;// 停车结束时间(例如：08-10 13:10pm)
			private String parklength;// 停车时长
			private float shparkfee;// 停车费用
			private int isdiscount;// 是否有本地优惠（0:无;1:有）
			private String localdiscount;// 本地优惠（减免两小时或者优惠X元）
			private float aftershparkfee;// 减去本地优惠后的停车费用（如无本地优惠则值与shaparkfee字段相同）

			private String feecouponafter;// 实交金额（去掉优惠券后的金额Msg＝5时有效）
			private String coupons;// 优惠券描述（例如：2.00元，交1元抵3元Msg＝5时有效）
			private String couponstype;// 优惠券类别（例如：现金券Msg＝5时有效）
			private String orderno;// 订单号
			private String userdcoupon;// 已使用优惠券描述，例如：15元优惠券，[注：①Msg＝4时有效，②该值存在时，订单为超时，补交]
			private String prepayment;// 已预付金额，Msg＝4时有效
			private String afterpay;// 需补交金额，Msg＝4时有效
			private String exitnote;// 出场提示语（注:硬件交费或有日月卡时有效，例如：请您在10时15分前离场，超时需补交停车费。）
			private int iscardmonth;// 0：日卡1：是月卡；2：不是日卡月卡
			private int ordertype;// 停车场类型--1:软件 ； 2：硬件
			private String cardnote;// 日月卡过期提醒

			public String getParkname() {
				return parkname;
			}

			public void setParkname(String parkname) {
				this.parkname = parkname;
			}

			public String getAddress() {
				return address;
			}

			public void setAddress(String address) {
				this.address = address;
			}

			public String getParkid() {
				return parkid;
			}

			public void setParkid(String parkid) {
				this.parkid = parkid;
			}

			public String getCardno() {
				return cardno;
			}

			public void setCardno(String cardno) {
				this.cardno = cardno;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
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

			public float getShparkfee() {
				return shparkfee;
			}

			public void setShparkfee(float shparkfee) {
				this.shparkfee = shparkfee;
			}

			public int getIsdiscount() {
				return isdiscount;
			}

			public void setIsdiscount(int isdiscount) {
				this.isdiscount = isdiscount;
			}

			public String getLocaldiscount() {
				return localdiscount;
			}

			public void setLocaldiscount(String localdiscount) {
				this.localdiscount = localdiscount;
			}

			public float getAftershparkfee() {
				return aftershparkfee;
			}

			public void setAftershparkfee(float aftershparkfee) {
				this.aftershparkfee = aftershparkfee;
			}

			public String getFeecouponafter() {
				return feecouponafter;
			}

			public void setFeecouponafter(String feecouponafter) {
				this.feecouponafter = feecouponafter;
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

			public String getOrderno() {
				return orderno;
			}

			public void setOrderno(String orderno) {
				this.orderno = orderno;
			}

			public String getUserdcoupon() {
				return userdcoupon;
			}

			public void setUserdcoupon(String userdcoupon) {
				this.userdcoupon = userdcoupon;
			}

			public String getPrepayment() {
				return prepayment;
			}

			public void setPrepayment(String prepayment) {
				this.prepayment = prepayment;
			}

			public String getAfterpay() {
				return afterpay;
			}

			public void setAfterpay(String afterpay) {
				this.afterpay = afterpay;
			}

			public String getExitnote() {
				return exitnote;
			}

			public void setExitnote(String exitnote) {
				this.exitnote = exitnote;
			}

			public int getIscardmonth() {
				return iscardmonth;
			}

			public void setIscardmonth(int iscardmonth) {
				this.iscardmonth = iscardmonth;
			}

			public int getOrdertype() {
				return ordertype;
			}

			public void setOrdertype(int ordertype) {
				this.ordertype = ordertype;
			}

			public String getCardnote() {
				return cardnote;
			}

			public void setCardnote(String cardnote) {
				this.cardnote = cardnote;
			}

			@Override
			public String toString() {
				return "InfoObject [parkname=" + parkname + ", address="
						+ address + ", parkid=" + parkid + ", cardno=" + cardno
						+ ", username=" + username + ", carno=" + carno
						+ ", starttime=" + starttime + ", endtime=" + endtime
						+ ", parklength=" + parklength + ", shparkfee="
						+ shparkfee + ", isdiscount=" + isdiscount
						+ ", localdiscount=" + localdiscount
						+ ", aftershparkfee=" + aftershparkfee
						+ ", feecouponafter=" + feecouponafter + ", coupons="
						+ coupons + ", couponstype=" + couponstype
						+ ", orderno=" + orderno + ", userdcoupon="
						+ userdcoupon + ", prepayment=" + prepayment
						+ ", afterpay=" + afterpay + ", exitnote=" + exitnote
						+ ", iscardmonth=" + iscardmonth + ", ordertype="
						+ ordertype + ", cardnote=" + cardnote + "]";
			}

		}

	}

}
