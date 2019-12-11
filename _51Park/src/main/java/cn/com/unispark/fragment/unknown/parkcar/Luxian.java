package cn.com.unispark.fragment.unknown.parkcar;


public class Luxian {
	String sta_point;
	double sta_lat;

	public double getSta_lat() {
		return sta_lat;
	}

	public void setSta_lat(double sta_lat) {
		this.sta_lat = sta_lat;
	}

	public double getSta_lon() {
		return sta_lon;
	}

	public void setSta_lon(double sta_lon) {
		this.sta_lon = sta_lon;
	}

	public double getEnd_lat() {
		return end_lat;
	}

	public void setEnd_lat(double end_lat) {
		this.end_lat = end_lat;
	}

	public double getEnd_lon() {
		return end_lon;
	}

	public void setEnd_lon(double end_lon) {
		this.end_lon = end_lon;
	}

	double sta_lon;
	double end_lat;
	double end_lon;

	public String getSta_point() {
		return sta_point;
	}

	public void setSta_point(String sta_point) {
		this.sta_point = sta_point;
	}

	public String getEnd_point() {
		return end_point;
	}

	public void setEnd_point(String end_point) {
		this.end_point = end_point;
	}

	String end_point;
}
