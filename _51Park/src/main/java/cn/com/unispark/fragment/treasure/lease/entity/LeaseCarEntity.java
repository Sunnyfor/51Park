package cn.com.unispark.fragment.treasure.lease.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 【车位租赁】停车场实体类
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
public class LeaseCarEntity extends BaseEntity {

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
		private List<LeaseCarInfo> datalist;

		public List<LeaseCarInfo> getDatalist() {
			return datalist;
		}

		public void setDatalist(List<LeaseCarInfo> datalist) {
			this.datalist = datalist;
		}

		@Override
		public String toString() {
			return "DataObject [datalist=" + datalist + "]";
		}

		/** 2. ****************************************************************************************/
		public class LeaseCarInfo implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String parkid;// 停车场ID
			private String parkname;// 停车场名称
			private String parkaddr;// 停车场地址
			private String type1;// 包月卡类型1 描述（可空）
			private String type2;// 计次卡类型2 描述（可空）
			private String coordlong; // 经度
			private String coordlat; // 纬度
			private int defaluttype;// 默认类型 : 0-月卡日卡 1-月卡 2-日卡
			private int isbuy;// 是否购买过;1：是；2：否
			private int iscanrebuy;// 是否可续费; 0是不可以1是可以
			private String reason;// 不能续费的原因
			private String img;// 停车场图片
			private int issoldout;

			public int getIssoldout() {
				return issoldout;
			}

			public void setIssoldout(int issoldout) {
				this.issoldout = issoldout;
			}

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

			public String getParkaddr() {
				return parkaddr;
			}

			public void setParkaddr(String parkaddr) {
				this.parkaddr = parkaddr;
			}

			public String getType1() {
				return type1;
			}

			public void setType1(String type1) {
				this.type1 = type1;
			}

			public String getType2() {
				return type2;
			}

			public void setType2(String type2) {
				this.type2 = type2;
			}

			public String getCoordlong() {
				return coordlong;
			}

			public void setCoordlong(String coordlong) {
				this.coordlong = coordlong;
			}

			public String getCoordlat() {
				return coordlat;
			}

			public void setCoordlat(String coordlat) {
				this.coordlat = coordlat;
			}

			public int getDefaluttype() {
				return defaluttype;
			}

			public void setDefaluttype(int defaluttype) {
				this.defaluttype = defaluttype;
			}

			public int getIsbuy() {
				return isbuy;
			}

			public void setIsbuy(int isbuy) {
				this.isbuy = isbuy;
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
				return "LeaseCarInfo [parkid=" + parkid + ", parkname="
						+ parkname + ", parkaddr=" + parkaddr + ", type1="
						+ type1 + ", type2=" + type2 + ", coordlong="
						+ coordlong + ", coordlat=" + coordlat
						+ ", defaluttype=" + defaluttype + ", isbuy=" + isbuy
						+ ", iscanrebuy=" + iscanrebuy + ", reason=" + reason
						+ ", img=" + img + "]";
			}

		}
	}
}
