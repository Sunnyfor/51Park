package cn.com.unispark.fragment.home.map.entity;

/**
 * <pre>
 * 功能说明： 【地图搜索】页搜索结果实体类
 * 日期：	2015年6月4日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月4日
 * </pre>
 */
public class SearchEntity {
	private String name;// 地方名称
	private String district;// 所属区域

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public SearchEntity() {

	}

	public SearchEntity(String name, String district) {
		super();
		this.name = name;
		this.district = district;
	}

	@Override
	public String toString() {
		return "SearchEntity [name=" + name + ", district=" + district + "]";
	}

}
