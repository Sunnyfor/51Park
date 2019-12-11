package cn.com.unispark.fragment.mine.coupons.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.unispark.application.BaseEntity;
import cn.com.unispark.fragment.mine.vipcard.entity.UnbindCardEntity.DataObject;

/**
 * <pre>
 * 功能说明： 【兑换码类】
 * 日期：	2015年11月3日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月3日
 * </pre>
 */
public class CouponsExChangeEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private DataObject data;

	public DataObject getData() {
		return data;
	}

	public void setData(DataObject data) {
		this.data = data;
	}

	/** 1. ****************************************************************************************/

	public class DataObject implements Serializable {

	}

}
