package cn.com.unispark.fragment.treasure.lease.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 【我的租赁】停车场实体类
 * 日期：	2015年9月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月22日
 * </pre>
 */
public class LeaseMyEntity extends BaseEntity {

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
		return "LeaseCarEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private List<LeaseMyInfo> datalist;

		public List<LeaseMyInfo> getDatalist() {
			return datalist;
		}

		public void setDatalist(List<LeaseMyInfo> datalist) {
			this.datalist = datalist;
		}

		@Override
		public String toString() {
			return "DataObject [datalist=" + datalist + "]";
		}

		/** 2. ****************************************************************************************/

		public class LeaseMyInfo implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String parkid;// 停车场ID
			private String parkname;// 停车场名称
			private String address;// 停车场地址
			private int type;// 卡类型？1月卡 ： 2日卡
			private String desc;// 卡购买情况描述
			private int iscanrebuy;// 是否可续费1是可以0是不可以
			private String reason;// 不能续费的原因
			private String img;// 停车场图片

			public String getParkid() {
				return parkid;
			}

			public void setParkid(String parkid) {
				this.parkid = parkid;
			}

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

			public int getType() {
				return type;
			}

			public void setType(int type) {
				this.type = type;
			}

			public String getDesc() {
				return desc;
			}

			public void setDesc(String desc) {
				this.desc = desc;
			}

			public int getIscanrebuy() {
				return iscanrebuy;
			}

			public void setIscanrebuy(int iscanrebuy) {
				this.iscanrebuy = iscanrebuy;
			}

			public String getReason() {
				return reason;
			}

			public void setReason(String reason) {
				this.reason = reason;
			}

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			@Override
			public String toString() {
				return "LeaseMyInfo [parkid=" + parkid + ", parkname="
						+ parkname + ", address=" + address + ", type=" + type
						+ ", desc=" + desc + ", iscanrebuy=" + iscanrebuy
						+ ", reason=" + reason + ", img=" + img + "]";
			}

		}
	}
}
