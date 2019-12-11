package cn.com.unispark.fragment.home.viewpager.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 保存轮播图地址和链接的实体类
 * 日期：	2015年10月20日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月23日
 * </pre>
 */
public class AdPagerEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AdPagerEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class Data implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String count;
		private List<ImageInfo> list;

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public List<ImageInfo> getList() {
			return list;
		}

		public void setList(List<ImageInfo> list) {
			this.list = list;
		}

		@Override
		public String toString() {
			return "DataObject [count=" + count + ", list=" + list + "]";
		}

		/** 2. ****************************************************************************************/

		public class ImageInfo implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String img;
			private String src;

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			public String getSrc() {
				return src;
			}

			public void setSrc(String src) {
				this.src = src;
			}

			@Override
			public String toString() {
				return "ImageInfo [img=" + img + ", src=" + src + "]";
			}

		}
	}

}
