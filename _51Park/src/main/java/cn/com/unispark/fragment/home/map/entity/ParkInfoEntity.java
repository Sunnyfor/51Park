package cn.com.unispark.fragment.home.map.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 停车场详情实体类
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
public class ParkInfoEntity extends BaseEntity {

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
		private String parkid;// 停车场id
		private String name;// 停车场名称
		private String address;// 地址
		private String totalcount;// 总车位
		private String freecount;// 空车位
		private String type;// 停车场分类(例如：占道停车场)
		private double longitude;// 经度
		private double latitude;// 纬度
		private String img;// 图片地址，例如：http://www.51park.com.cn/upload/home/upload/cppark/06.jpg
		private String state;// 车位状态（例如：P1001.jpg）
		private String dprice;// 临时停车价格
		private String dpriceday;// 白天停车价格( 2元/15分钟(07:00-21:00) )
		private String dpricenight;// 晚上停车价格( 5元/2小时(21:00-07:00) )
		private String dopentime;// 开始营业时间
		private String dclosetime;// 结束营业时间
		private List<PriceInfo> dpricenightlist;// 晚上价格信息List
		private List<PriceInfo> dpricedaylist;// 白天价格信息List
		private List<TimeState> timesstate;// 列表[停车场空车位预测]
		private String dpricenighttime;// 夜间时间范围
		private String dpricedaytime;// 白天时间范围
		private int istimes;// 是否为计次停车场？0不显示：1显示
		private int ismonth;// 是否为包月停车场？0不显示：1显示
		private int defaluttype;// 默认类型 ? 0-月卡日卡 : 1-月卡 : 2-日卡 : 9-非日卡月卡
		private int is_interior;// 1.无忧2.非无忧

		public String getParkid() {
			return parkid;
		}

		public void setParkid(String parkid) {
			this.parkid = parkid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTotalcount() {
			return totalcount;
		}

		public void setTotalcount(String totalcount) {
			this.totalcount = totalcount;
		}

		public String getFreecount() {
			return freecount;
		}

		public void setFreecount(String freecount) {
			this.freecount = freecount;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
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

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getDprice() {
			return dprice;
		}

		public void setDprice(String dprice) {
			this.dprice = dprice;
		}

		public String getDpriceday() {
			return dpriceday;
		}

		public void setDpriceday(String dpriceday) {
			this.dpriceday = dpriceday;
		}

		public String getDpricenight() {
			return dpricenight;
		}

		public void setDpricenight(String dpricenight) {
			this.dpricenight = dpricenight;
		}

		public String getDopentime() {
			return dopentime;
		}

		public void setDopentime(String dopentime) {
			this.dopentime = dopentime;
		}

		public String getDclosetime() {
			return dclosetime;
		}

		public void setDclosetime(String dclosetime) {
			this.dclosetime = dclosetime;
		}

		public List<PriceInfo> getDpricenightlist() {
			return dpricenightlist;
		}

		public void setDpricenightlist(List<PriceInfo> dpricenightlist) {
			this.dpricenightlist = dpricenightlist;
		}

		public List<PriceInfo> getDpricedaylist() {
			return dpricedaylist;
		}

		public void setDpricedaylist(List<PriceInfo> dpricedaylist) {
			this.dpricedaylist = dpricedaylist;
		}

		public List<TimeState> getTimesstate() {
			return timesstate;
		}

		public void setTimesstate(List<TimeState> timesstate) {
			this.timesstate = timesstate;
		}

		public String getDpricenighttime() {
			return dpricenighttime;
		}

		public void setDpricenighttime(String dpricenighttime) {
			this.dpricenighttime = dpricenighttime;
		}

		public String getDpricedaytime() {
			return dpricedaytime;
		}

		public void setDpricedaytime(String dpricedaytime) {
			this.dpricedaytime = dpricedaytime;
		}

		public int getIstimes() {
			return istimes;
		}

		public void setIstimes(int istimes) {
			this.istimes = istimes;
		}

		public int getIsmonth() {
			return ismonth;
		}

		public void setIsmonth(int ismonth) {
			this.ismonth = ismonth;
		}

		public int getDefaluttype() {
			return defaluttype;
		}

		public void setDefaluttype(int defaluttype) {
			this.defaluttype = defaluttype;
		}

		public int getIs_interior() {
			return is_interior;
		}

		public void setIs_interior(int is_interior) {
			this.is_interior = is_interior;
		}

		@Override
		public String toString() {
			return "DataObject [parkid=" + parkid + ", name=" + name
					+ ", address=" + address + ", totalcount=" + totalcount
					+ ", freecount=" + freecount + ", type=" + type
					+ ", longitude=" + longitude + ", latitude=" + latitude
					+ ", img=" + img + ", state=" + state + ", dprice="
					+ dprice + ", dpriceday=" + dpriceday + ", dpricenight="
					+ dpricenight + ", dopentime=" + dopentime
					+ ", dclosetime=" + dclosetime + ", dpricenightlist="
					+ dpricenightlist + ", dpricedaylist=" + dpricedaylist
					+ ", timesstate=" + timesstate + ", dpricenighttime="
					+ dpricenighttime + ", dpricedaytime=" + dpricedaytime
					+ ", istimes=" + istimes + ", ismonth=" + ismonth
					+ ", defaluttype=" + defaluttype + ", is_interior="
					+ is_interior + "]";
		}

		/** 2. ****************************************************************************************/

		public class PriceInfo implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String info;// 描述 （xx分钟）
			private String price;// 价格（xxx元）

			public String getInfo() {
				return info;
			}

			public void setInfo(String info) {
				this.info = info;
			}

			public String getPrice() {
				return price;
			}

			public void setPrice(String price) {
				this.price = price;
			}

			@Override
			public String toString() {
				return "PriceInfo [info=" + info + ", price=" + price + "]";
			}

		}

		/** 2. ****************************************************************************************/
		public class TimeState implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String hour;// 时间
			private String state;// 图片

			public String getHour() {
				return hour;
			}

			public void setHour(String hour) {
				this.hour = hour;
			}

			public String getState() {
				return state;
			}

			public void setState(String state) {
				this.state = state;
			}

			@Override
			public String toString() {
				return "TimeState [hour=" + hour + ", state=" + state + "]";
			}

		}
	}
}
