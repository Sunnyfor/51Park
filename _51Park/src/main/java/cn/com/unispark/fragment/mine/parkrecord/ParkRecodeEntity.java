package cn.com.unispark.fragment.mine.parkrecord;

import java.io.Serializable;
import java.util.List;
import cn.com.unispark.application.BaseEntity;

@SuppressWarnings("serial")
public class ParkRecodeEntity extends BaseEntity {
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

	public class DataObject implements Serializable {
		private static final long serialVersionUID = 1L;
		// "count": 8,
		// "page": "1",

		private int count;
		private String page;
		private List<ParkRecodeInfo> list;

		public List<ParkRecodeInfo> getList() {
			return list;
		}

		public void setList(List<ParkRecodeInfo> list) {
			this.list = list;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public class ParkRecodeInfo implements Serializable {

			private String park_name;
			private String money;
			private String orderno;
			private String totalfee;
			private int ispay;
			private String group;
			private String enter_time;
			private String exit_time;
			private String license;

			public String getTotalfee() {
				return totalfee;
			}

			public void setTotalfee(String totalfee) {
				this.totalfee = totalfee;
			}

			public String getGroup() {
				return group;
			}

			public void setGroup(String group) {
				this.group = group;
			}

			public String getPark_name() {
				return park_name;
			}

			public void setPark_name(String park_name) {
				this.park_name = park_name;
			}

			public String getLicense() {
				return license;
			}

			public void setLicense(String license) {
				this.license = license;
			}

			public String getMoney() {
				return money;
			}

			public void setMoney(String money) {
				this.money = money;
			}

			public int getIspay() {
				return ispay;
			}

			public void setIspay(int ispay) {
				this.ispay = ispay;
			}

			public String getOrderno() {
				return orderno;
			}

			public void setOrderno(String orderno) {
				this.orderno = orderno;
			}

			public String getEnter_time() {
				return enter_time;
			}

			public void setEnter_time(String enter_time) {
				this.enter_time = enter_time;
			}

			public String getExit_time() {
				return exit_time;
			}

			public void setExit_time(String exit_time) {
				this.exit_time = exit_time;
			}

		}

	}

}
