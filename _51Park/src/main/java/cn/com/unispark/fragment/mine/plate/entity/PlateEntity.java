package cn.com.unispark.fragment.mine.plate.entity;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class PlateEntity extends BaseEntity {
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
		private List<PlateInfo> list;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<PlateInfo> getList() {
			return list;
		}

		public void setList(List<PlateInfo> list) {
			this.list = list;
		}

		/** 2. ****************************************************************************************/
		public class PlateInfo implements Serializable {
			// isdefault int 是否为默认车牌 0.非默认 1.默认
			// isexamine int 是否为审核状态 0：否 1：是

			private String plate;
			private String isdefault;
			private int isexamine;

			public int getIsexamine() {
				return isexamine;
			}

			public void setIsexamine(int isexamine) {
				this.isexamine = isexamine;
			}

			public String getIsdefault() {
				return isdefault;
			}

			public void setIsdefault(String isdefault) {
				this.isdefault = isdefault;
			}

			public String getPlate() {
				return plate;
			}

			public void setPlate(String plate) {
				this.plate = plate;
			}

		}

	}

}
