package cn.com.unispark.fragment.mine.setting.offlinemap;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 【城市列表带索引】实体类
 * 日期：	2015年7月30日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class CityListEntity extends BaseEntity {
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
		private List<CityGroupInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<CityGroupInfo> getList() {
			return list;
		}

		public void setList(List<CityGroupInfo> list) {
			this.list = list;
		}

		public class CityGroupInfo implements Serializable {
			private static final long serialVersionUID = 1L;
			// "group": "B",
			// "city": [
			// {
			// "city_id": "1",
			// "areacode": "010",
			// "name": "\u5317\u4eac\u5e02",
			// "longitude": "116.3665",
			// "latitude": "39.9202"
			// }
			// ]
			private String group;
			private List<CityInfo> city;

			public String getGroup() {
				return group;
			}

			public void setGroup(String group) {
				this.group = group;
			}

			public List<CityInfo> getCity() {
				return city;
			}

			public void setCity(List<CityInfo> city) {
				this.city = city;
			}

			public class CityInfo implements Serializable {
				private static final long serialVersionUID = 1L;
				// "city_id": "113",
				// "areacode": "0411",
				// "longitude": "121.609383",
				// "latitude": "38.906797",
				// "name": "大连市"
				private String city_id;
				private String areacode;
				private String longitude;
				private String latitude;
				private String name;

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

				public String getLongitude() {
					return longitude;
				}

				public void setLongitude(String longitude) {
					this.longitude = longitude;
				}

				public String getLatitude() {
					return latitude;
				}

				public void setLatitude(String latitude) {
					this.latitude = latitude;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

			}
		}

	}

}
