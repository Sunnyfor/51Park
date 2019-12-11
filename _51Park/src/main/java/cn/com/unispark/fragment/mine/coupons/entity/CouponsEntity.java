package cn.com.unispark.fragment.mine.coupons.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 优惠券实体类
 * 日期：	2015年11月3日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：数据结构改变，添加count字段
 *    修改人员：陈泳佐
 *    修改日期： 2015年11月3日
 * </pre>
 */
public class CouponsEntity extends BaseEntity {
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
		return "CouponsEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/
	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int count;
		private List<CouponsInfo> list;
		private String couponrule;// 优惠券使用规则url

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public List<CouponsInfo> getList() {
			return list;
		}

		public void setList(List<CouponsInfo> list) {
			this.list = list;
		}

		public String getCouponrule() {
			return couponrule;
		}

		public void setCouponrule(String couponrule) {
			this.couponrule = couponrule;
		}

		@Override
		public String toString() {
			return "DataObject [count=" + count + ", list=" + list
					+ ", couponrule=" + couponrule + "]";
		}

		/** 2. ****************************************************************************************/

		public class CouponsInfo implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String coupons_id;
			private String use_end_date;
			private String desc;
			private int coupons_type;
			private String type_money;
			private String coupon_prompt;
			private String other_amount;

			public String getCoupons_id() {
				return coupons_id;
			}

			public void setCoupons_id(String coupons_id) {
				this.coupons_id = coupons_id;
			}

			public String getUse_end_date() {
				return use_end_date;
			}

			public void setUse_end_date(String use_end_date) {
				this.use_end_date = use_end_date;
			}

			public String getDesc() {
				return desc;
			}

			public void setDesc(String desc) {
				this.desc = desc;
			}

			public int getCoupons_type() {
				return coupons_type;
			}

			public void setCoupons_type(int coupons_type) {
				this.coupons_type = coupons_type;
			}

			public String getType_money() {
				return type_money;
			}

			public void setType_money(String type_money) {
				this.type_money = type_money;
			}

			public String getCoupon_prompt() {
				return coupon_prompt;
			}

			public void setCoupon_prompt(String coupon_prompt) {
				this.coupon_prompt = coupon_prompt;
			}

			public String getOther_amount() {
				return other_amount;
			}

			public void setOther_amount(String other_amount) {
				this.other_amount = other_amount;
			}
		}
	}
}
