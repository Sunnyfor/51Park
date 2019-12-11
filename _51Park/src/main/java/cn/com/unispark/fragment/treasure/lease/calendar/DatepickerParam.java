package cn.com.unispark.fragment.treasure.lease.calendar;

import java.io.Serializable;
import java.util.Calendar;

public class DatepickerParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Calendar selectedDay = null;
	public Calendar startDate = null;
	public int dateRange = 0;
	public String title = "出发日期";
}
