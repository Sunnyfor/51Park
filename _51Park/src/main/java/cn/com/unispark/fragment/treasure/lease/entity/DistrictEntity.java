package cn.com.unispark.fragment.treasure.lease.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 租赁：获取地区列表
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
public class DistrictEntity extends BaseEntity {

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
		return "DistrictEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private List<DistrictItem> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<DistrictItem> getList() {
			return list;
		}

		public void setList(List<DistrictItem> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "DataObject [count=" + count + ", list=" + list + "]";
		}

		/** 2. ****************************************************************************************/

		public class DistrictItem implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String districtid;// 地区自增id(不可更改为int)
			private String name;// 区名称（例如：房山区）
			private double longitude;// 经度
			private double latitude;// 纬度

			public String getDistrictid() {
				return districtid;
			}

			public void setDistrictid(String districtid) {
				this.districtid = districtid;
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
				return "DistrictItem [districtid=" + districtid + ", name="
						+ name + ", longitude=" + longitude + ", latitude="
						+ latitude + "]";
			}

		}
	}
}
