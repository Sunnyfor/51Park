package cn.com.unispark.fragment.treasure.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 汽车专家（车秘）获取url的实体类
 * 日期：	2015年11月3日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月3日
 * </pre>
 */
public class CheMiEntity extends BaseEntity {
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
		return "CheMiEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			return "DataObject [url=" + url + "]";
		}

	}

}
