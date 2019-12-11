package cn.com.unispark.login.entity;

import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * 功能说明： 用户信息实体类
 * 日期：	2015年10月26日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月17日
 * </pre>
 */
public class UserEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInfo data;

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UserEntity [data=" + data + "]";
	}

}
