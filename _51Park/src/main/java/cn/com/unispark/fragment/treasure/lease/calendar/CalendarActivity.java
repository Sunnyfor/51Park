package cn.com.unispark.fragment.treasure.lease.calendar;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 车位租赁包月用户选择日期
 * 日期：	2015年10月13日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 *
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月13日
 * </pre>
 */
public class CalendarActivity extends BaseActivity {

	// private static final String TAG = CalendarActivity.class.getSimpleName();
	private ScrollView mScrollView;
	private DatepickerParam mDatepickerParam;
	private Context context = CalendarActivity.this;
	private int scrollHeight = 0;
	private LinearLayout mLinearLayoutSelected;



	// 获取对应的属性值 Android框架自带的属性 attr
	int pressed = android.R.attr.state_pressed;
	int enabled = android.R.attr.state_enabled;
	int selected = android.R.attr.state_selected;
	int metricWidth; // 屏幕宽度（像素）
	int metricHeight; // 屏幕高度（像素）
	private Intent dataIntent;
	private String dateStr;
	private String longStr;
	private String dateStartStr;

	private TextView titleText;
	private LinearLayout backLLayout;
	private int lasttempCalendar2;


	private Handler mHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			// 点击直接跳转到选择日的对应月份
			if (msg.what == 11) {
				mScrollView.scrollTo(0, scrollHeight);
				mScrollView.setVisibility(View.VISIBLE);
			}

			return false;
		}
	});

	@Override
	public void setContentLayout() {

		setContentView(R.layout.calender_main);

	}

	@Override
	public void initView() {
		dataIntent = getIntent();
		dateStr = dataIntent.getExtras().getString("startDareStr");
		dateStartStr = dataIntent.getExtras().getString("dateStartStr");
		longStr = dataIntent.getExtras().getString("longStr");
		// Log.e("slx", "longStr" + longStr);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		metricWidth = metric.widthPixels; // 屏幕宽度（像素）
		metricHeight = metric.heightPixels; // 屏幕高度（像素）

		// 主布局ScrollView
		mScrollView = (ScrollView) findViewById(R.id.sv_calender);
		mScrollView.setVisibility(View.INVISIBLE);

		/*
		 * 标题栏布局
		 */
		View title_ic = findViewById(R.id.title_ic);
		ViewUtil.setViewSize(title_ic, 88, 0);

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("日历");
		ViewUtil.setTextSize(titleText, 34);

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		mDatepickerParam = new DatepickerParam();
		// 开始时间
		mDatepickerParam.startDate = DateTimeUtil.getCalendar(dateStartStr);
		// 设置日期时长longStr
		mDatepickerParam.dateRange = Integer.valueOf(longStr).intValue();
		// 选中时间
		mDatepickerParam.selectedDay = DateTimeUtil.getCalendar(dateStr);

		LinearLayout localLinearLayout1 = new LinearLayout(this);
		localLinearLayout1.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		localLinearLayout1.setOrientation(LinearLayout.VERTICAL);
		mScrollView.addView(localLinearLayout1);
		localLinearLayout1.setPadding(ScreenUtil.dip2px(context, 5f),
				ScreenUtil.dip2px(context, 5f), ScreenUtil.dip2px(context, 5f),
				0);
		// 今天
		Calendar localCalendar1 = (Calendar) DateTimeUtil.getCurrentDateTime()
				.clone();
		Calendar calendarToday = (Calendar) localCalendar1.clone();
		Calendar calendarTomorrow = (Calendar) localCalendar1.clone();

		calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1);
		Calendar calendarTwoMore = (Calendar) localCalendar1.clone();
		calendarTwoMore.add(Calendar.DAY_OF_MONTH, 2);

		Calendar selectedCalendar = (Calendar) mDatepickerParam.selectedDay
				.clone();
		int yearOfLocalCalendar1 = localCalendar1.get(Calendar.YEAR);
		int monthOfLocalCalendar1 = localCalendar1.get(Calendar.MONTH);

		Calendar localCalendarEnd = (Calendar) mDatepickerParam.startDate
				.clone();
		localCalendarEnd.add(Calendar.DAY_OF_MONTH,
				mDatepickerParam.dateRange - 1);

		// 2016nian
		int yearOfLocalCalendar2 = localCalendarEnd.get(Calendar.YEAR);
		Log.e("slx", "yearOfLocalCalendar2" + yearOfLocalCalendar2);
		int monthOfLocalCalendar2 = localCalendarEnd.get(Calendar.MONTH);

		Log.e("slx", "monthOfLocalCalendar2" + monthOfLocalCalendar2);
		int differOfYear = yearOfLocalCalendar2 - yearOfLocalCalendar1;

		Log.e("slx", "differOfYear" + differOfYear);
		int differOfMonth = monthOfLocalCalendar2 - monthOfLocalCalendar1;

		Log.e("slx", "differOfMonth" + differOfMonth);

		// 涉及到的月份数
		int totalDiffer = differOfYear * 12 + differOfMonth + 1;

		Log.e("slx", "totalDiffer" + totalDiffer);
		for (int i = 0; i < totalDiffer; i++) {
			LinearLayout localLinearLayout2 = (LinearLayout) View.inflate(
					context, R.layout.calendar_head, null);
			localLinearLayout1.addView(localLinearLayout2,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			TextView localTextView1 = (TextView) localLinearLayout2
					.findViewById(R.id.tv_cal_year);
			TextView localTextView2 = (TextView) localLinearLayout2
					.findViewById(R.id.tv_cal_month);
			Calendar tempCalendar = (Calendar) localCalendar1.clone();
			tempCalendar.add(Calendar.YEAR, i / 11);
			Calendar tempCalendar2 = (Calendar) localCalendar1.clone();
			tempCalendar2.add(Calendar.MONTH, i);
			localTextView2.setText(tempCalendar2.get(Calendar.MONTH) + 1 + "月");
			// 判断月份设置年
			if (i == 0) {
				lasttempCalendar2 = tempCalendar2.get(Calendar.MONTH);
				localTextView1.setText(tempCalendar.get(Calendar.YEAR) + "年");
			} else {
				if (tempCalendar2.get(Calendar.MONTH) - lasttempCalendar2 < 0) {
					localTextView1.setText(tempCalendar.get(Calendar.YEAR) + 1
							+ "年");
				} else {
					localTextView1.setText(tempCalendar.get(Calendar.YEAR)
							+ "年");
				}
			}
			tempCalendar2.set(Calendar.DAY_OF_MONTH, 1);

			// 星期天-星期六 Calendar.DAY_OF_WEEK = 1-7
			int weekOfDay = tempCalendar2.get(Calendar.DAY_OF_WEEK) - 1;
			int maxOfMonth = tempCalendar2
					.getActualMaximum(Calendar.DAY_OF_MONTH);
			int lines = (int) Math.ceil((weekOfDay + maxOfMonth) / 7.0f);
			// 开始日期之前和结束日期之后的变灰
			// int startDay = localCalendar1.get(Calendar.DAY_OF_MONTH);

			for (int j = 0; j < lines; j++) {
				LinearLayout oneLineLinearLayout = getOneLineDayLinearLayout();
				if (j == 0) {// 第一行
					for (int k = 0; k < 7; k++) {
						TextView localTextView = (TextView) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(0));
						RelativeLayout localSelectedRela = (RelativeLayout) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(1));
						TextView localTextViewSelected = (TextView) localSelectedRela
								.getChildAt(0);
						if (k >= weekOfDay) {
							int index = k - weekOfDay + 1;
							localTextView.setText(index + "");
							localTextViewSelected.setText(index + "");
							Calendar tempCalendar3 = (Calendar) tempCalendar2
									.clone();
							tempCalendar3.set(Calendar.DAY_OF_MONTH, index);
							String date = tempCalendar3.get(Calendar.YEAR)
									+ "-"
									+ (tempCalendar3.get(Calendar.MONTH) + 1)
									+ "-"
									+ tempCalendar3.get(Calendar.DAY_OF_MONTH);

							localTextView.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							localSelectedRela.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));

							if (compareCal(tempCalendar3, calendarToday) == -1) {// 小于当天
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_gray_font));
								localTextView.setEnabled(false);
							}

							if (DataParams.HOLIDAYS.get(date) != null) {
								localTextView.setText(DataParams.HOLIDAYS
										.get(date));
								localTextViewSelected
										.setText(DataParams.HOLIDAYS.get(date));
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextViewSelected.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextView
										.setTextColor(getTextColorOrange());
							}

							if (compareCal(tempCalendar3, calendarToday) == 0) {// 今天
								localTextView
										.setTextColor(getTextColorOrange());
								localTextView.setText("今天");
								localTextViewSelected.setText("今天");
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 16.0f);
							}

							if (compareCal(tempCalendar3, selectedCalendar) == 0) {// 选择日
								localTextView.setVisibility(View.INVISIBLE);
								localSelectedRela.setVisibility(View.VISIBLE);
								localSelectedRela.setSelected(true);
								mLinearLayoutSelected = localLinearLayout2;
							}

							if (compareCal(tempCalendar3, localCalendarEnd) == 1) {// 大于截止日
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_gray_font));
								localTextView.setEnabled(false);
							}

						} else {
							localTextView.setVisibility(View.INVISIBLE);
						}
					}
				} else if (j == lines - 1) {// 最后一行
					int temp = maxOfMonth - (lines - 2) * 7 - (7 - weekOfDay);
					for (int k = 0; k < 7; k++) {
						TextView localTextView = (TextView) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(0));
						RelativeLayout localSelectedRela = (RelativeLayout) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(1));
						TextView localTextViewSelected = (TextView) localSelectedRela
								.getChildAt(0);
						if (k < temp) {
							int index = (7 - weekOfDay) + (j - 1) * 7 + k + 1;
							localTextView.setText(index + "");
							localTextViewSelected.setText(index + "");
							Calendar tempCalendar3 = (Calendar) tempCalendar2
									.clone();
							tempCalendar3.set(Calendar.DAY_OF_MONTH, index);
							String date = tempCalendar3.get(Calendar.YEAR)
									+ "-"
									+ (tempCalendar3.get(Calendar.MONTH) + 1)
									+ "-"
									+ tempCalendar3.get(Calendar.DAY_OF_MONTH);
							localTextView.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							localSelectedRela.setTag(Long.valueOf(tempCalendar3
									.getTimeInMillis()));
							if (compareCal(tempCalendar3, calendarToday) == -1) {// 小于当天
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_gray_font));
								localTextView.setEnabled(false);
							}
							if (DataParams.HOLIDAYS.get(date) != null) {
								localTextView.setText(DataParams.HOLIDAYS
										.get(date));
								localTextViewSelected
										.setText(DataParams.HOLIDAYS.get(date));
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextViewSelected.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14.0f);
								localTextView
										.setTextColor(getTextColorOrange());
							}

							if (compareCal(tempCalendar3, calendarToday) == 0) {// 今天
								localTextView
										.setTextColor(getTextColorOrange());
								localTextView.setText("今天");
								localTextViewSelected.setText("今天");
								localTextView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 16.0f);
							}

							if (compareCal(tempCalendar3, selectedCalendar) == 0) {// 选择日
								localTextView.setVisibility(View.INVISIBLE);
								localSelectedRela.setVisibility(View.VISIBLE);
								localSelectedRela.setSelected(true);
								mLinearLayoutSelected = localLinearLayout2;

							}
							if (compareCal(tempCalendar3, localCalendarEnd) == 1) {// 大于截止日
								localTextView.setTextColor(getResources()
										.getColor(R.color.calendar_gray_font));
								localTextView.setEnabled(false);
							}

						} else {
							localTextView.setVisibility(View.INVISIBLE);
						}
					}

				} else {// 中间（有效期部分）
					for (int k = 0; k < 7; k++) {
						TextView localTextView = (TextView) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(0));
						RelativeLayout localSelectedRela = (RelativeLayout) (((RelativeLayout) oneLineLinearLayout
								.getChildAt(k)).getChildAt(1));
						TextView localTextViewSelected = (TextView) localSelectedRela
								.getChildAt(0);
						int index = (7 - weekOfDay) + (j - 1) * 7 + k + 1;
						localTextView.setText(index + "");
						localTextViewSelected.setText(index + "");
						Calendar tempCalendar3 = (Calendar) tempCalendar2
								.clone();
						tempCalendar3.set(Calendar.DAY_OF_MONTH, index);
						String date = tempCalendar3.get(Calendar.YEAR) + "-"
								+ (tempCalendar3.get(Calendar.MONTH) + 1) + "-"
								+ tempCalendar3.get(Calendar.DAY_OF_MONTH);
						localTextView.setTag(Long.valueOf(tempCalendar3
								.getTimeInMillis()));
						localSelectedRela.setTag(Long.valueOf(tempCalendar3
								.getTimeInMillis()));
						if (compareCal(tempCalendar3, calendarToday) == -1) {// 小于当天
							localTextView.setTextColor(getResources().getColor(
									R.color.calendar_gray_font));
							localTextView.setEnabled(false);
						}

						// 特殊节日
						if (DataParams.HOLIDAYS.get(date) != null) {
							localTextView
									.setText(DataParams.HOLIDAYS.get(date));
							localTextViewSelected.setText(DataParams.HOLIDAYS
									.get(date));
							localTextView.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 14.0f);
							localTextViewSelected.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 14.0f);
							localTextView.setTextColor(getTextColorOrange());
						}

						// 今天
						if (compareCal(tempCalendar3, calendarToday) == 0) {
							localTextView.setTextColor(getTextColorOrange());
							localTextView.setText("今天");
							localTextViewSelected.setText("今天");
							localTextView.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 16.0f);
						}

						// 选择日
						if (compareCal(tempCalendar3, selectedCalendar) == 0) {
							localTextView.setVisibility(View.INVISIBLE);
							localSelectedRela.setVisibility(View.VISIBLE);
							localSelectedRela.setSelected(true);
							mLinearLayoutSelected = localLinearLayout2;
						}

						// 大于截止日
						if (compareCal(tempCalendar3, localCalendarEnd) == 1) {
							localTextView.setTextColor(getResources().getColor(
									R.color.calendar_gray_font));
							localTextView.setEnabled(false);
						}

					}
				}
				localLinearLayout1.addView(oneLineLinearLayout);
			}
		}

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}

		if (v.getTag() != null) {
			Calendar localCalendar = Calendar.getInstance();
			localCalendar.setTimeInMillis(((Long) v.getTag()).longValue());
			String dateStr = localCalendar.get(Calendar.YEAR) + "-"
					+ (localCalendar.get(Calendar.MONTH) + 1) + "-"
					+ localCalendar.get(Calendar.DAY_OF_MONTH);

			SimpleDateFormat simple = new SimpleDateFormat("yyyy-M-d",
					Locale.getDefault());

			Date localDate = new Date();
			String date = simple.format(localDate);
			if (date.equals(dateStr)) {
				v.setClickable(false);
				Toast.makeText(context, "不能选择当前这一天", Toast.LENGTH_LONG).show();
			} else {
				Intent intent = getIntent();
				intent.putExtra("dateStr", dateStr);
				setResult(30, intent);
				finish();
			}
		}

	}

	/**
	 * 获取一行 七天的LinearLayout
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private LinearLayout getOneLineDayLinearLayout() {
		LinearLayout localLinearLayout = new LinearLayout(this);
		localLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		for (int i = 0; i < 7; i++) {
			float height = (metricWidth - ScreenUtil.dip2px(context, 10f) - ScreenUtil
					.dip2px(context, 1.5f * 6)) / 7;
			// Log.i(TAG, "height:" + height);
			LinearLayout.LayoutParams localLayoutParams4 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, (int) height, 1.0F);
			RelativeLayout localRelativeLayout = new RelativeLayout(context);
			localRelativeLayout.setLayoutParams(localLayoutParams4);
			localLayoutParams4.setMargins(ScreenUtil.dip2px(this, 1.5F),
					ScreenUtil.dip2px(this, 1.5F),
					ScreenUtil.dip2px(this, 1.5F),
					ScreenUtil.dip2px(this, 1.5F));
			TextView localTextView3 = new TextView(this);
			localTextView3.setLayoutParams(localLayoutParams4);
			localTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F);
			localTextView3.setBackgroundDrawable(getBackGroundDrawable());
			localTextView3.setTextColor(getTextColorBlue());
			localTextView3.setGravity(Gravity.CENTER);
			localTextView3.setOnClickListener(this);
			localTextView3.setVisibility(View.VISIBLE);
			localRelativeLayout.addView(localTextView3, 0);

			RelativeLayout localRelativeLayout2 = new RelativeLayout(this);
			localRelativeLayout2.setLayoutParams(localLayoutParams4);
			localRelativeLayout2.setOnClickListener(this);
			localRelativeLayout2.setBackgroundDrawable(getBackGroundDrawable());
			TextView localTextView1 = new TextView(this);
			localTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F);
			localTextView1.setId(R.id.text_1);
			localTextView1.setTextColor(getTextColorBlue());
			TextView localTextView2 = new TextView(this);
			localTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0F);
			localTextView2.setTextColor(context.getResources().getColor(
					R.color.white));
			localTextView2.setText("选中");
			/**
			 * localTextView2
			 *
			 */
			RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			localLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
			localLayoutParams2.topMargin = ScreenUtil.dip2px(context, 4f);
			localRelativeLayout2.addView(localTextView1, 0, localLayoutParams2);
			RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			localLayoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL);
			localLayoutParams3.addRule(RelativeLayout.BELOW, R.id.text_1);
			localRelativeLayout2.addView(localTextView2, R.id.text_1, localLayoutParams3);
			localRelativeLayout2.setVisibility(View.INVISIBLE);
			localRelativeLayout.addView(localRelativeLayout2, R.id.text_1);

			localLinearLayout.addView(localRelativeLayout, i);

		}
		return localLinearLayout;
	}

	/**
	 * 比较两个日期的大小
	 *
	 * @param paramCalendar1
	 * @param paramCalendar2
	 * @return
	 */
	private int compareCal(Calendar paramCalendar1, Calendar paramCalendar2) {
		if (paramCalendar1.get(Calendar.YEAR) > paramCalendar2
				.get(Calendar.YEAR)) {
			return 1;
		} else if (paramCalendar1.get(Calendar.YEAR) < paramCalendar2
				.get(Calendar.YEAR)) {
			return -1;
		} else {
			if (paramCalendar1.get(Calendar.MONTH) > paramCalendar2
					.get(Calendar.MONTH)) {
				return 1;
			} else if (paramCalendar1.get(Calendar.MONTH) < paramCalendar2
					.get(Calendar.MONTH)) {
				return -1;
			} else {
				if (paramCalendar1.get(Calendar.DAY_OF_MONTH) > paramCalendar2
						.get(Calendar.DAY_OF_MONTH)) {
					return 1;
				} else if (paramCalendar1.get(Calendar.DAY_OF_MONTH) < paramCalendar2
						.get(Calendar.DAY_OF_MONTH)) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 点击背景切换
	 *
	 * @return
	 */
	private StateListDrawable getBackGroundDrawable() {
		// 获取对应的属性值 Android框架自带的属性 attr
		int pressed = android.R.attr.state_pressed;
		int enabled = android.R.attr.state_enabled;
		int selected = android.R.attr.state_selected;

		StateListDrawable localStateListDrawable = new StateListDrawable();
		ColorDrawable localColorDrawable1 = new ColorDrawable(context
				.getResources().getColor(android.R.color.transparent));
		// ColorDrawable localColorDrawable2 = new ColorDrawable(context
		// .getResources().getColor(R.color.blue));
		Drawable localColorDrawable2 = context.getResources().getDrawable(
				R.drawable.bg_calendar_seleced);
		ColorDrawable localColorDrawable3 = new ColorDrawable(context
				.getResources().getColor(android.R.color.transparent));
		localStateListDrawable.addState(new int[] { pressed, enabled },
				localColorDrawable2);
		localStateListDrawable.addState(new int[] { selected, enabled },
				localColorDrawable2);
		localStateListDrawable.addState(new int[] { enabled },
				localColorDrawable1);
		localStateListDrawable.addState(new int[0], localColorDrawable3);
		return localStateListDrawable;
	}

	private ColorStateList getTextColorBlue() {
		return new ColorStateList(new int[][] { { pressed, enabled },
				{ selected, enabled }, { enabled }, new int[0] }, new int[] {
				-1, -1,
				context.getResources().getColor(R.color.calendar_blue_font),
				context.getResources().getColor(R.color.white) });
	}

	private ColorStateList getTextColorOrange() {
		return new ColorStateList(new int[][] { { pressed, enabled },
				{ selected, enabled }, { enabled }, new int[0] }, new int[] {
				-1, -1,
				context.getResources().getColor(R.color.calendar_orange_font),
				context.getResources().getColor(R.color.calendar_gray_font) });
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		scrollHeight = mLinearLayoutSelected.getTop();
		mHandler.sendEmptyMessageDelayed(11, 100l);
		// Log.i(TAG, "scrollHeight:" + scrollHeight);
	}

}
