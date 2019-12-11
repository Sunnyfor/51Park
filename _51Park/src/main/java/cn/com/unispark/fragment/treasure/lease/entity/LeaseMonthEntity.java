package cn.com.unispark.fragment.treasure.lease.entity;

/**
 * <pre>
 * 功能说明： 包月详情列表实体类
 * 日期：	2015年9月23日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月23日
 * </pre>
 */
public class LeaseMonthEntity {

	private String type;// 类型（序列号，如：1,2,3）
	private String name;// 名称（如：1个月，3个月）
	private String discountname;// 优惠详情
	private int discounttype;// 优惠type
	private String price;// 原价
	private String realprice;// 实际价格
	private String start;// 开始时间
	private String longStr;// 时长
	private int showOldPrice;// 时长
		
	public LeaseMonthEntity() {

	}

	public LeaseMonthEntity(String type, String name, String discountname,
			int discounttype, String price, String realprice, String start,
			String longStr,int showOldPrice) {
		super();
		this.type = type;
		this.name = name;
		this.discountname = discountname;
		this.discounttype = discounttype;
		this.price = price;
		this.realprice = realprice;
		this.start = start;
		this.longStr = longStr;
		this.showOldPrice = showOldPrice;
	}

	public int getShowOldPrice() {
		return showOldPrice;
	}

	public void setShowOldPrice(int showOldPrice) {
		this.showOldPrice = showOldPrice;
	}

	public String getLongStr() {
		return longStr;
	}

	public void setLongStr(String longStr) {
		this.longStr = longStr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscountname() {
		return discountname;
	}

	public void setDiscountname(String discountname) {
		this.discountname = discountname;
	}

	public int getDiscounttype() {
		return discounttype;
	}

	public void setDiscounttype(int discounttype) {
		this.discounttype = discounttype;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRealprice() {
		return realprice;
	}

	public void setRealprice(String realprice) {
		this.realprice = realprice;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "LeaseMonthEntity [type=" + type + ", name=" + name
				+ ", discountname=" + discountname + ", discounttype="
				+ discounttype + ", price=" + price + ", realprice="
				+ realprice + ", start=" + start + ", coordlong=" + "]";
	}

}
