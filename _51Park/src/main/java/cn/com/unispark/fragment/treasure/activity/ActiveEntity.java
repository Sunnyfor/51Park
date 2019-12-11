package cn.com.unispark.fragment.treasure.activity;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class ActiveEntity extends BaseEntity {
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
		return "GetOrderEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		// "count": 104,
		private List<ActionCenterInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<ActionCenterInfo> getList() {
			return list;
		}

		public void setList(List<ActionCenterInfo> list) {
			this.list = list;
		}

		/** 2. ****************************************************************************************/
		public class ActionCenterInfo implements Serializable {
			// title string 活动标题
			// enddata string 活动截至日期(例如：2015-10-10)
			// img string
			// 活动图片链接地址(例如http://api.51park.com.cn/event/lb/images/hongyejie.jpg)
			// isexpire int 1：有效；2：已过期
			// src string 活动详情url
			private String img;// 活动图片链接地址
			private String enddata;// 活动截至日期
			private String title;// 活动标题
			private int isexpire;// 1：有效；2：已过期
			private String src;// 活动详情url
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getEnddata() {
				return enddata;
			}
			public void setEnddata(String enddata) {
				this.enddata = enddata;
			}
			public String getImg() {
				return img;
			}
			public void setImg(String img) {
				this.img = img;
			}
			public int getIsexpire() {
				return isexpire;
			}
			public void setIsexpire(int isexpire) {
				this.isexpire = isexpire;
			}
			public String getSrc() {
				return src;
			}
			public void setSrc(String src) {
				this.src = src;
			}


		}

	}

}
