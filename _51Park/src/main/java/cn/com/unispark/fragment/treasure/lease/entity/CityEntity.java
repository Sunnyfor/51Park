package cn.com.unispark.fragment.treasure.lease.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 租赁：获取城市列表
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
public class CityEntity extends BaseEntity {

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
		return "CityEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private List<CityItem> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<CityItem> getList() {
			return list;
		}

		public void setList(List<CityItem> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "DataObject [count=" + count + ", list=" + list + "]";
		}

		/** 2. ****************************************************************************************/

		public class CityItem implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String city_id;// 地区自增id
			private String areacode;// 区号
			private String name;// 区名称（例如：房山区）
			private double longitude;// 经度
			private double latitude;// 纬度

			public String getCity_id() {
				return city_id;
			}

			public void setCity_id(String city_id) {
				this.city_id = city_id;
			}

			public String getAreacode() {
				return areacode;
			}

			public void setAreacode(String areacode) {
				this.areacode = areacode;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
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

			@Override
			public String toString() {
				return "CityItem [city_id=" + city_id + ", areacode="
						+ areacode + ", name=" + name + ", longitude="
						+ longitude + ", latitude=" + latitude + "]";
			}

		}
	}

}
