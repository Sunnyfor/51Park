package cn.com.unispark.fragment.home.map.entity;

/**
 * <pre>
 * 功能说明： 保存出入口经纬度的实体类
 * 日期：	2016年12月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年12月22日
 * </pre>
 */
public class InOutEntity {

	private double longitude;// 出入口坐标-经度
	private double latitude;// 出入口坐标-纬度

	public InOutEntity() {
		super();
	}

	public InOutEntity(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "InOutEntity [longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}

}
