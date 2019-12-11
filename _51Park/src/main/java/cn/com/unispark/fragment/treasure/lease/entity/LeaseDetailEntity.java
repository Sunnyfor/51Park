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
public class LeaseDetailEntity extends BaseEntity {
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

	// bought string 己购买情况

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String parkid;// 停车场ID
		private String parkname;// 停车场名称
		private String parkaddr;// 停车场地址
		private String pic;// 图片地址
		private int type;// 卡类型（1：按月 2：按次）
		private String num;// 购买个数 默认为1（type 2有用）
		private String times;// 一次购买的停车次数（type 2有用）
		private String timeprice;// 次数购买单价（type 2有用）
		private String start;// "start": "2015-11-10",
		private String monthdesc;// 包月购买描述
		private String meterdesc;// 计次购买描述
		private String desc;// 购买描述
		private int isrebuy;// 续费（0:否 1：是）
		private String longtime;// 生效日期（具体天数）
		private String coordlong;// 经度
		private String coordlat;// 纬度
		private int istimessoldout;//
		private String ismonthsoldout;//
		private String bought;
		private DetailsnoteObject detailsnote;
		private String totalcount;// 总车位
		private String freecount;// 空车位
		private String buyrule;// 购买须知url

		public String getBuyrule() {
			return buyrule;
		}

		public void setBuyrule(String buyrule) {
			this.buyrule = buyrule;
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

		public DetailsnoteObject getDetailsnote() {
			return detailsnote;
		}

		public void setDetailsnote(DetailsnoteObject detailsnote) {
			this.detailsnote = detailsnote;
		}

		public class DetailsnoteObject implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private NightObject night;
			private DayObject day;
			private String[] desc;

			public String[] getDesc() {
				return desc;
			}

			public void setDesc(String[] desc) {
				this.desc = desc;
			}

			public NightObject getNight() {
				return night;
			}

			public void setNight(NightObject night) {
				this.night = night;
			}

			public DayObject getDay() {
				return day;
			}

			public void setDay(DayObject day) {
				this.day = day;
			}

			public class NightObject implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private String subtitle;//
				private String title;
				private List<NightDataObject> data;// 多个list 7个字段

				public String getSubtitle() {
					return subtitle;
				}

				public void setSubtitle(String subtitle) {
					this.subtitle = subtitle;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public List<NightDataObject> getData() {
					return data;
				}

				public void setData(List<NightDataObject> data) {
					this.data = data;
				}

				public class NightDataObject implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					private String value;//
					private String label;

					public String getValue() {
						return value;
					}

					public void setValue(String value) {
						this.value = value;
					}

					public String getLabel() {
						return label;
					}

					public void setLabel(String label) {
						this.label = label;
					}
				}
			}

			public class DayObject implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private String subtitle;//
				private String title;
				private List<DayDataObject> data;// 多个list 7个字段

				public String getSubtitle() {
					return subtitle;
				}

				public void setSubtitle(String subtitle) {
					this.subtitle = subtitle;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public List<DayDataObject> getData() {
					return data;
				}

				public void setData(List<DayDataObject> data) {
					this.data = data;
				}

				public class DayDataObject implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					private String value;//
					private String label;

					public String getValue() {
						return value;
					}

					public void setValue(String value) {
						this.value = value;
					}

					public String getLabel() {
						return label;
					}

					public void setLabel(String label) {
						this.label = label;
					}
				}
			}
		}

		public int getIstimessoldout() {
			return istimessoldout;
		}

		public void setIstimessoldout(int istimessoldout) {
			this.istimessoldout = istimessoldout;
		}

		public String getIsmonthsoldout() {
			return ismonthsoldout;
		}

		public void setIsmonthsoldout(String ismonthsoldout) {
			this.ismonthsoldout = ismonthsoldout;
		}

		public String getBought() {
			return bought;
		}

		public void setBought(String bought) {
			this.bought = bought;
		}

		private List<LeaseDetailInfo> month;// 多个list 7个字段

		private List<TimeslistDetailInfo> timeslist;// 多个list 7个字段

		public List<TimeslistDetailInfo> getTimeslist() {
			return timeslist;
		}

		public void setTimeslist(List<TimeslistDetailInfo> timeslist) {
			this.timeslist = timeslist;
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

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getTimes() {
			return times;
		}

		public void setTimes(String times) {
			this.times = times;
		}

		public String getTimeprice() {
			return timeprice;
		}

		public void setTimeprice(String timeprice) {
			this.timeprice = timeprice;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getMonthdesc() {
			return monthdesc;
		}

		public void setMonthdesc(String monthdesc) {
			this.monthdesc = monthdesc;
		}

		public String getMeterdesc() {
			return meterdesc;
		}

		public void setMeterdesc(String meterdesc) {
			this.meterdesc = meterdesc;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getIsrebuy() {
			return isrebuy;
		}

		public void setIsrebuy(int isrebuy) {
			this.isrebuy = isrebuy;
		}

		public String getLongtime() {
			return longtime;
		}

		public void setLongtime(String longtime) {
			this.longtime = longtime;
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

		public List<LeaseDetailInfo> getMonth() {
			return month;
		}

		public void setMonth(List<LeaseDetailInfo> month) {
			this.month = month;
		}

		public class LeaseDetailInfo implements Serializable {

			private static final long serialVersionUID = 1L;
			private int isbuy;//
			private String pricename;// 类型（月分的类型）
			private String price;// 原价
			private String realprice;// 实际价格
			private String start;// 生效时间开始
			private int discounttype;// 优惠 （0：无1： 有 ）
			private String name;// 名称（如： 1个月）
			private int longtime;// 生效日期（具体天数）
			private String type;// 类型（月分的类型）
			private int show_old_price;// 是否显示原价（0不显示1显示）

			public int getIsbuy() {
				return isbuy;
			}

			public void setIsbuy(int isbuy) {
				this.isbuy = isbuy;
			}

			public String getPricename() {
				return pricename;
			}

			public void setPricename(String pricename) {
				this.pricename = pricename;
			}

			public String getPrice() {
				return price;
			}

			public void setPrice(String price) {
				this.price = price;
			}

			public String getRealprice() {
				return realprice;
			}

			public void setRealprice(String realprice) {
				this.realprice = realprice;
			}

			public String getStart() {
				return start;
			}

			public void setStart(String start) {
				this.start = start;
			}

			public int getDiscounttype() {
				return discounttype;
			}

			public void setDiscounttype(int discounttype) {
				this.discounttype = discounttype;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getLongtime() {
				return longtime;
			}

			public void setLongtime(int longtime) {
				this.longtime = longtime;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public int getShow_old_price() {
				return show_old_price;
			}

			public void setShow_old_price(int show_old_price) {
				this.show_old_price = show_old_price;
			}

		}

		/*
		 * 价格
		 */
		public class TimeslistDetailInfo implements Serializable {

			private static final long serialVersionUID = 1L;
			private int isbuy;//
			private String pricename;// 类型（月分的类型）
			private String price;// 原价
			private String realprice;// 实际价格
			private String start;// 生效时间开始
			private String name;// 名称（如： 1个月）
			private int num;// 类型（月分的类型）

			public int getNum() {
				return num;
			}

			public void setNum(int num) {
				this.num = num;
			}

			private String type;// 类型（月分的类型）

			public int getIsbuy() {
				return isbuy;
			}

			public void setIsbuy(int isbuy) {
				this.isbuy = isbuy;
			}

			public String getPricename() {
				return pricename;
			}

			public void setPricename(String pricename) {
				this.pricename = pricename;
			}

			public String getPrice() {
				return price;
			}

			public void setPrice(String price) {
				this.price = price;
			}

			public String getRealprice() {
				return realprice;
			}

			public void setRealprice(String realprice) {
				this.realprice = realprice;
			}

			public String getStart() {
				return start;
			}

			public void setStart(String start) {
				this.start = start;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

		}

		@Override
		public String toString() {
			return "DataObject [parkid=" + parkid + ", parkname=" + parkname
					+ ", parkaddr=" + parkaddr + ", pic=" + pic + ", type="
					+ type + ", num=" + num + ", times=" + times
					+ ", timeprice=" + timeprice + ", start=" + start
					+ ", monthdesc=" + monthdesc + ", meterdesc=" + meterdesc
					+ ", desc=" + desc + ", isrebuy=" + isrebuy + ", longtime="
					+ longtime + ", coordlong=" + coordlong + ", coordlat="
					+ coordlat + ", istimessoldout=" + istimessoldout
					+ ", ismonthsoldout=" + ismonthsoldout + ", bought="
					+ bought + ", detailsnote=" + detailsnote + ", totalcount="
					+ totalcount + ", freecount=" + freecount + ", buyrule="
					+ buyrule + ", month=" + month + ", timeslist=" + timeslist
					+ "]";
		}

	}
}
