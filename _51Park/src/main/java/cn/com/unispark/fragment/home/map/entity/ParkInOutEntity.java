package cn.com.unispark.fragment.home.map.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 停车场出入口
 * 日期：	2015年12月28日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月28日
 * </pre>
 */
public class ParkInOutEntity extends BaseEntity {

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
		return "ParkInOutEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private String name;
		private List<ParkInOutInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<ParkInOutInfo> getList() {
			return list;
		}

		public void setList(List<ParkInOutInfo> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "DataObject [count=" + count + ", name=" + name + ", list="
					+ list + "]";
		}

		/** 2. ****************************************************************************************/

		public class ParkInOutInfo implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String img;// 出入口图片（路径：http://www.51park.com.cn/mapabc/images/icon/EE/+
								// TP）
			private double longitude;// 出入口坐标-经度
			private double latitude;// 出入口坐标-纬度
			private int iseneixt;// 出入口类型(0:入口坐标; 1:出口坐标; 2:出入口坐标)
			private int direction;// 出入口方向(1:北; 2:南; 3:西; 4:东)

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
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

			public int getIseneixt() {
				return iseneixt;
			}

			public void setIseneixt(int iseneixt) {
				this.iseneixt = iseneixt;
			}

			public int getDirection() {
				return direction;
			}

			public void setDirection(int direction) {
				this.direction = direction;
			}

			@Override
			public String toString() {
				return "ParkInOutInfo [img=" + img + ", longitude=" + longitude
						+ ", latitude=" + latitude + ", iseneixt=" + iseneixt
						+ ", direction=" + direction + "]";
			}

		}
	}

	public void onResult(List<InOutEntity> inOutList) {
		// TODO Auto-generated method stub
		
	}
}
