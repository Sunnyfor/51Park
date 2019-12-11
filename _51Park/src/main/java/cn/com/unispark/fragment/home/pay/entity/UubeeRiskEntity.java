package cn.com.unispark.fragment.home.pay.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 获取有贝的风控参数
 * 日期：	2015年10月29日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月29日
 * </pre>
 */
public class UubeeRiskEntity extends BaseEntity {
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
		return "UubeeRiskEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String ubstr;

		public String getUbstr() {
			return ubstr;
		}

		public void setUbstr(String ubstr) {
			this.ubstr = ubstr;
		}

		@Override
		public String toString() {
			return "DataObject [ubstr=" + ubstr + "]";
		}

	}

}
