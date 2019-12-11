package cn.com.unispark.fragment.mine.msgpush.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

public class MsgDetailEntity extends BaseEntity implements Serializable {
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
		// title
		// body
		// time
		private static final long serialVersionUID = 1L;
		private String title;
		private String body;
		private String time;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}

	}

}
