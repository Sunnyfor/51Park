package cn.com.unispark.fragment.home.map.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 搜索推荐停车场的子条目
 * 日期：	2015年11月17日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月17日
 * </pre>
 */
public class SearchItemEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SearchData data;

	public SearchData getData() {
		return data;
	}

	public void setData(SearchData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "SearchItem [data=" + data + "]";
	}

	public class SearchData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private List<SearchDataChild> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<SearchDataChild> getList() {
			return list;
		}

		public void setList(List<SearchDataChild> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "SearchData [count=" + count + ", list=" + list + "]";
		}

		public class SearchDataChild implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String address;// 停车场地址
			private String name;// 停车场名称
			private String totalcount;// 总车位数
			private String freecount;// 空车位数
			private String img;
			private String far;// 距离[单位：米]
			private String parkid;// 停车场自增ID
			private double longitude;// 经度
			private double latitude;// 纬度
			private String serial;
			private String dpriceday;// 白天价格
			private String dpricenight;// 晚上价格
			private int ismonth;// 是否有月卡？ 0不是月卡： 1是月卡
			private int istimes;// 是否有日卡？0 不是日卡 ：1 是日卡
			private int isfree;// 是否免费？ 0 不免费 ：1 免费
			private int isjoin;// 是否加盟？ 0未加盟： 1己加盟

			public String getTotalcount() {
				return totalcount;
			}

			public void setTotalcount(String totalcount) {
				this.totalcount = totalcount;
			}

			public String getDpricenight() {
				return dpricenight;
			}

			public void setDpricenight(String dpricenight) {
				this.dpricenight = dpricenight;
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

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			public String getFreecount() {
				return freecount;
			}

			public void setFreecount(String freecount) {
				this.freecount = freecount;
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

			public String getSerial() {
				return serial;
			}

			public void setSerial(String serial) {
				this.serial = serial;
			}

			public String getDpriceday() {
				return dpriceday;
			}

			public void setDpriceday(String dpriceday) {
				this.dpriceday = dpriceday;
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
				return "SearchDataChild [address=" + address + ", name=" + name
						+ ", totalcount=" + totalcount + ", freecount="
						+ freecount + ", img=" + img + ", far=" + far
						+ ", parkid=" + parkid + ", longitude=" + longitude
						+ ", latitude=" + latitude + ", serial=" + serial
						+ ", dpriceday=" + dpriceday + ", dpricenight="
						+ dpricenight + ", ismonth=" + ismonth + ", istimes="
						+ istimes + ", isfree=" + isfree + ", isjoin=" + isjoin
						+ "]";
			}

		}
	}
}
