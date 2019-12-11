package cn.com.unispark.fragment.treasure.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明：TreasureEntity
 * 日期：	2016年1月8日
 * 开发者：	rjf
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class TreasureEntity extends BaseEntity {
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

	public class DataObject implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<TreasureDataList> list;

		public List<TreasureDataList> getList() {
			return list;
		}

		public void setList(List<TreasureDataList> list) {
			this.list = list;
		}

		public class TreasureDataList implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			// "img":
			// "http:\/\/alpha_backend.51park.cn\/\/Uploads\/Icon\/\/568ce045363f6.png",
			// "type": "1",
			// "describe": "更便宜，更优惠"
			private String img;
			private int type;
			private String describe;

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			public int getType() {
				return type;
			}

			public void setType(int type) {
				this.type = type;
			}

			public String getDescribe() {
				return describe;
			}

			public void setDescribe(String describe) {
				this.describe = describe;
			}
		}
	}

}
