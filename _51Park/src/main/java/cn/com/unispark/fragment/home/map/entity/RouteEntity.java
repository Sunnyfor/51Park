package cn.com.unispark.fragment.home.map.entity;

/**
 * <pre>
 * 功能说明： 驾车路线子条目的实体类
 * 日期：	2015年11月16日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月16日
 * </pre>
 */
public class RouteEntity {

	private int icon;
	private String title;

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "RouteEntity [icon=" + icon + ", title=" + title + "]";
	}

}
