package cn.com.unispark.login.entity;

import java.io.Serializable;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 获取验证码状态校验的实体类
 * 日期：	2015年10月26日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月26日
 * </pre>
 */
public class MsgEntity extends BaseEntity {
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
		return "MsgEntity [data=" + data + "]";
	}

	/** 1. ****************************************************************************************/

	public class Data implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String note;

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		@Override
		public String toString() {
			return "Data [note=" + note + "]";
		}

	}
}
