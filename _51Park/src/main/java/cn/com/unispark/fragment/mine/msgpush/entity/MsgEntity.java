package cn.com.unispark.fragment.mine.msgpush.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 无忧公告实体类
 * 日期：	2015年11月27日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月27日
 * </pre>
 */
public class MsgEntity extends BaseEntity {
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
		return "GetOrderEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private List<MsgPushInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<MsgPushInfo> getList() {
			return list;
		}

		public void setList(List<MsgPushInfo> list) {
			this.list = list;
		}

		/** 2. ****************************************************************************************/
		public class MsgPushInfo implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String id;
			private String time;
			private String title;
			private String content;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getTime() {
				return time;
			}

			public void setTime(String time) {
				this.time = time;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			@Override
			public String toString() {
				return "MsgPushInfo [id=" + id + ", time=" + time + ", title="
						+ title + ", content=" + content + "]";
			}

		}

	}

}
