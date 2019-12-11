package cn.com.unispark.application;

import java.io.Serializable;

/**
 * <pre>
 * 功能说明： 实体类Entity的父类
 * 日期：	2015年10月25日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月25日
 * </pre>
 */
public class BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;// 服务器返回的参数
	private String msg;// 服务器返回的信息

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "BaseEntity [msg=" + msg + ", code=" + code + "]";
	}

}
