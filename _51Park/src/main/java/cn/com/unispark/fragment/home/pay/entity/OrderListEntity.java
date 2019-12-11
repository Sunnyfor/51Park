package cn.com.unispark.fragment.home.pay.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 获取当前账单列表的实体类
 * 日期：	2016年1月7日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年1月7日
 * </pre>
 */
public class OrderListEntity extends BaseEntity {
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
		private int count;
		private List<OrderList> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<OrderList> getList() {
			return list;
		}

		public void setList(List<OrderList> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "DataObject [count=" + count + ", list=" + list + "]";
		}

		/** 2. ****************************************************************************************/

		public class OrderList implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private int status;// 订单状态：1 待支付 2 停车中 3 待出场
			private String statusnote;// 列表状态提示语（停车中、待支付、待出场）
			private String parkname;// 停车场名称
			private String carno;// 车牌号
			private String parklength;// 停车时长
			private String shparkfee;// 停车费用
			private String orderno;// 订单号
			private String exitnote;// 出场提示语（注:硬件交费或有日月卡时有效，例如：请您在10时15分前离场，超时需补交停车费。）
			private int iscardmonth;// 0：日卡1：是月卡；2：不是日卡月卡
			private int ordertype;// 1:软件；2：硬件
			private String cardnote;// 日月卡过期提醒（例如：您的包月或计次服务已经过期系统将为您计时收费，如有疑问请联系客服！如果为空时无需提醒。）
			private String entertime;//入场时间

			
			public String getEntertime() {
				return entertime;
			}

			public void setEntertime(String entertime) {
				this.entertime = entertime;
			}

			public int getStatus() {
				return status;
			}

			public void setStatus(int status) {
				this.status = status;
			}

			public String getStatusnote() {
				return statusnote;
			}

			public void setStatusnote(String statusnote) {
				this.statusnote = statusnote;
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

			public String getParklength() {
				return parklength;
			}

			public void setParklength(String parklength) {
				this.parklength = parklength;
			}

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
				return "OrderList [status=" + status + ", statusnote="
						+ statusnote + ", parkname=" + parkname + ", carno="
						+ carno + ", parklength=" + parklength + ", shparkfee="
						+ shparkfee + ", orderno=" + orderno + ", exitnote="
						+ exitnote + ", iscardmonth=" + iscardmonth
						+ ", ordertype=" + ordertype + ", cardnote=" + cardnote
						+ "]";
			}

		}

	}

}
