package cn.com.unispark.fragment.home.map.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 停车场图标的实体类(点击图标弹出悬浮框)
 * 日期：	2015年10月26日
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
public class ParkItemEntity extends BaseEntity {

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
		return "ParkItem [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private List<ParkItemInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<ParkItemInfo> getList() {
			return list;
		}

		public void setList(List<ParkItemInfo> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "ParkItemData [count=" + count + ", list=" + list + "]";
		}

		/** 2. ****************************************************************************************/

		public class ParkItemInfo implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String allcount;// 总车位数
			private String freecount;// 空车位数
			private String address;// 停车场地址
			private String name;// 停车场名称
			private String state;// 停车场空车位状态图片
			private String far;// 距离
			private String parkid;// 停车场id
			private String serial;// 停车场序列号
			private double longitude;// 目的地经度
			private double latitude;// 目的地纬度
			private String price;// 价格
			private String money;// 包月计次停车场价格
			private int ismonth;// 是否有月卡？ 0不是月卡： 1是月卡
			private int istimes;// 是否有日卡？0 不是日卡 ：1 是日卡
			private int isfree;// 是否免费？ 0 不免费 ：1 免费
			private int isjoin;// 是否加盟？ 0未加盟： 1己加盟

			public String getAllcount() {
				return allcount;
			}

			public void setAllcount(String allcount) {
				this.allcount = allcount;
			}

			public String getFreecount() {
				return freecount;
			}

			public void setFreecount(String freecount) {
				this.freecount = freecount;
			}

			public String getAddress() {
				return address;
			}

			public void setAddress(String address) {
				this.address = address;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getState() {
				return state;
			}

			public void setState(String state) {
				this.state = state;
			}

			public String getFar() {
				return far;
			}

			public void setFar(String far) {
				this.far = far;
			}

			public String getParkid() {
				return parkid;
			}

			public void setParkid(String parkid) {
				this.parkid = parkid;
			}

			public String getSerial() {
				return serial;
			}

			public void setSerial(String serial) {
				this.serial = serial;
			}

			public double getLongitude() {
				return longitude;
			}

			public void setLongitude(double longitude) {
				this.longitude = longitude;
			}

			public double getLatitude() {
				return latitude;
			}

			public void setLatitude(double latitude) {
				this.latitude = latitude;
			}

			public String getPrice() {
				return price;
			}

			public void setPrice(String price) {
				this.price = price;
			}

			public String getMoney() {
				return money;
			}

			public void setMoney(String money) {
				this.money = money;
			}

			public int getIsmonth() {
				return ismonth;
			}

			public void setIsmonth(int ismonth) {
				this.ismonth = ismonth;
			}

			public int getIstimes() {
				return istimes;
			}

			public void setIstimes(int istimes) {
				this.istimes = istimes;
			}

			public int getIsfree() {
				return isfree;
			}

			public void setIsfree(int isfree) {
				this.isfree = isfree;
			}

			public int getIsjoin() {
				return isjoin;
			}

			public void setIsjoin(int isjoin) {
				this.isjoin = isjoin;
			}

			@Override
			public String toString() {
				return "ParkItemInfo [allcount=" + allcount + ", freecount="
						+ freecount + ", address=" + address + ", name=" + name
						+ ", state=" + state + ", far=" + far + ", parkid="
						+ parkid + ", serial=" + serial + ", longitude="
						+ longitude + ", latitude=" + latitude + ", price="
						+ price + ", money=" + money + ", ismonth=" + ismonth
						+ ", istimes=" + istimes + ", isfree=" + isfree
						+ ", isjoin=" + isjoin + "]";
			}

		}
	}
}
