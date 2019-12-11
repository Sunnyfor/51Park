package cn.com.unispark.fragment.home.map.entity;

/**
 * <pre>
 * 功能说明： 停车场状态实体类
 * 日期：	2015年6月24日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月24日
 * </pre>
 */
public class ParkStateEntity {

	private String time;
	private String state;

	public ParkStateEntity(String time, String state) {
		this.time = time;
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
