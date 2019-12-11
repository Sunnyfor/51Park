package cn.com.unispark.fragment.unknown.parkcar;

/**
 * <pre>
 * 功能说明： 收藏的停车场实体类
 * 日期：	2014年12月24日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月24日
 * </pre>
 */
public class ParkCarEntity {
	String id;
	String title;
	String address;
	double my_lat;

	public double getMy_lat() {
		return my_lat;
	}

	public void setMy_lat(double my_lat) {
		this.my_lat = my_lat;
	}

	public double getMy_lon() {
		return my_lon;
	}

	public void setMy_lon(double my_lon) {
		this.my_lon = my_lon;
	}

	double my_lon;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getpCount() {
		return pCount;
	}

	public void setpCount(int pCount) {
		this.pCount = pCount;
	}

	public int gettCount() {
		return tCount;
	}

	public void settCount(int tCount) {
		this.tCount = tCount;
	}

	public String gettType() {
		return tType;
	}

	public void settType(String tType) {
		this.tType = tType;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public double getdPrice() {
		return dPrice;
	}

	public void setdPrice(double dPrice) {
		this.dPrice = dPrice;
	}

	public String getdPriceDay() {
		return dPriceDay;
	}

	public void setdPriceDay(String dPriceDay) {
		this.dPriceDay = dPriceDay;
	}

	public String getdPriceNight() {
		return dPriceNight;
	}

	public void setdPriceNight(String dPriceNight) {
		this.dPriceNight = dPriceNight;
	}

	public String getdOpenTime() {
		return dOpenTime;
	}

	public void setdOpenTime(String dOpenTime) {
		this.dOpenTime = dOpenTime;
	}

	public String getdCloseTime() {
		return dCloseTime;
	}

	public void setdCloseTime(String dCloseTime) {
		this.dCloseTime = dCloseTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getState_falg() {
		return state_falg;
	}

	public void setState_falg(int state_falg) {
		this.state_falg = state_falg;
	}

	int pCount;
	int tCount;
	String tType;
	String imgUrl;
	String TP;
	public String getTP() {
		return TP;
	}

	public void setTP(String tP) {
		TP = tP;
	}

	public String getDhh() {
		return Dhh;
	}

	public void setDhh(String dhh) {
		Dhh = dhh;
	}

	String Dhh;
	double dPrice;
	String dPriceDay;
	String dPriceNight;
	String dOpenTime;
	String dCloseTime;
	String state;
	int state_falg;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	double lat;
	double lon;

	public String toString() {
		return "title " + title + "id" + id + "pCount" + pCount + "tCount"
				+ tCount + "......";
	}
}
