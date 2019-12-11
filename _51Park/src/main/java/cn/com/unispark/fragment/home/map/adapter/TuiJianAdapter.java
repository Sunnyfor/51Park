package cn.com.unispark.fragment.home.map.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.ParkInfoActivity;
import cn.com.unispark.fragment.home.map.entity.SearchItemEntity.SearchData.SearchDataChild;
import cn.com.unispark.fragment.home.map.navigation.NavVoiceActivity;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 智能推荐停车场展示 适配器
 * 日期：	2015年12月14日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月14日
 * </pre>
 */
public class TuiJianAdapter extends PagerAdapter {
	private Context context;
	private Activity activity;
	public List<SearchDataChild> list;

	private ImageView state_iv;
	private TextView name_tv, address_tv, count_tv, empty_count_tv,
			distance_tv;
	private ImageView month_iv;
	// private ImageView meter_iv, join_iv, free_iv;
	private TextView route_tv, detail_tv;
	private LinearLayout route_ll, detail_ll;

	public TuiJianAdapter(Context context, Activity activity,
			List<SearchDataChild> list) {
		this.context = context;
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {

		View contentView = View.inflate(context, R.layout.tuijian_item, null);

		// 状态图标
		state_iv = (ImageView) contentView.findViewById(R.id.state_iv);
		if (position == 0) {
			state_iv.setImageResource(R.drawable.label_map_zuijin);
		} else if (position == 1) {
			state_iv.setImageResource(R.drawable.label_map_zuikong);
		}

		// 停车场名称
		name_tv = (TextView) contentView.findViewById(R.id.name_tv);
		name_tv.setText(position + 1 + "." + list.get(position).getName());
		ViewUtil.setTextSize(name_tv, 30);
		ViewUtil.setMargin(name_tv, 24, 0, 16, 30, ViewUtil.RELATIVELAYOUT);

		// 停车场地址
		address_tv = (TextView) contentView.findViewById(R.id.address_tv);
		address_tv.setText(list.get(position).getAddress());
		ViewUtil.setTextSize(address_tv, 24);
		ViewUtil.setMarginRight(address_tv, 20, ViewUtil.RELATIVELAYOUT);

		// 停车场距离
		distance_tv = (TextView) contentView.findViewById(R.id.distance_tv);
		distance_tv.setText(list.get(position).getFar() + "km");
		ViewUtil.setTextSize(distance_tv, 24);
		ViewUtil.setMarginRight(distance_tv, 40, ViewUtil.RELATIVELAYOUT);

		// 总车位数量
		count_tv = (TextView) contentView.findViewById(R.id.count_tv);
		count_tv.setText("总车位: " + list.get(position).getTotalcount());
		ViewUtil.setTextSize(count_tv, 24);
		ViewUtil.setMarginTop(count_tv, 16, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginBottom(count_tv, 24, ViewUtil.RELATIVELAYOUT);

		/*
		 * 空车位数量
		 */
		if (!TextUtils.isEmpty(list.get(position).getFreecount())) {
			empty_count_tv = (TextView) contentView
					.findViewById(R.id.empty_count_tv);
			ViewUtil.setTextSize(empty_count_tv, 24);
			ViewUtil.setMarginLeft(empty_count_tv, 20, ViewUtil.RELATIVELAYOUT);
			
			// 空车位数量用红色标识
			String str = "空车位: " + list.get(position).getFreecount() + " (仅供参考)";
			SpannableStringBuilder style = new SpannableStringBuilder(str);
			style.setSpan(new ForegroundColorSpan(Color.parseColor("#ec4f39")), 5,
					str.length() - 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// (仅供参考)颜色改为灰色，字号18
			style.setSpan(new ForegroundColorSpan(Color.parseColor("#bbbbbb")),
					str.length() - 6, str.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new AbsoluteSizeSpan(ViewUtil.getWidth(18), false),
					str.length() - 6, str.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			empty_count_tv.setText(style);
		}

		/*
		 * 停车场类型:包月(包租车位)
		 */
		month_iv = (ImageView) contentView.findViewById(R.id.month_iv);
		ViewUtil.setMarginLeft(month_iv, 10, ViewUtil.RELATIVELAYOUT);

		if (list.get(position).getIsmonth() == 0
				&& list.get(position).getIstimes() == 0) {
			month_iv.setVisibility(View.GONE);
		} else {
			month_iv.setVisibility(View.VISIBLE);
		}

		// // 停车场类型:计次
		// meter_iv = (ImageView) contentView.findViewById(R.id.meter_iv);
		// ViewUtil.setMarginLeft(meter_iv, 10, ViewUtil.RELATIVELAYOUT);
		// if (list.get(position).getIstimes() == 0) {
		// meter_iv.setVisibility(View.GONE);
		// } else if (list.get(position).getIsmonth() == 1) {
		// meter_iv.setVisibility(View.VISIBLE);
		// }
		//
		// // 停车场类型:无忧
		// join_iv = (ImageView) contentView.findViewById(R.id.join_iv);
		// ViewUtil.setMarginLeft(join_iv, 10, ViewUtil.RELATIVELAYOUT);
		// if (list.get(position).getIsjoin() == 0) {
		// join_iv.setVisibility(View.GONE);
		// } else if (list.get(position).getIsjoin() == 1) {
		// join_iv.setVisibility(View.VISIBLE);
		// }
		//
		// // 停车场类型:免费
		// free_iv = (ImageView) contentView.findViewById(R.id.free_iv);
		// ViewUtil.setMarginLeft(free_iv, 10, ViewUtil.RELATIVELAYOUT);
		// if (list.get(position).getIsfree() == 0) {
		// free_iv.setVisibility(View.GONE);
		// } else if (list.get(position).getIsfree() == 1) {
		// free_iv.setVisibility(View.VISIBLE);
		// }

		// 路线
		route_tv = (TextView) contentView.findViewById(R.id.route_tv);
		route_ll = (LinearLayout) contentView.findViewById(R.id.route_ll);
		route_ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				ToolUtil.IntentClass(activity, NavVoiceActivity.class, false);
				ParkApplication.mLatEnd = list.get(position).getLatitude();
				ParkApplication.mLonEnd = list.get(position).getLongitude();

			}
		});
		ViewUtil.setTextSize(route_tv, 30);
		ViewUtil.setViewSize(route_ll, 88, 0);

		// 详情
		detail_tv = (TextView) contentView.findViewById(R.id.detail_tv);
		detail_ll = (LinearLayout) contentView.findViewById(R.id.detail_ll);
		detail_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToolUtil.IntentClass(activity, ParkInfoActivity.class, false);
				ParkApplication.mParkId = list.get(position).getParkid();

			}
		});
		ViewUtil.setTextSize(detail_tv, 30);
		ViewUtil.setViewSize(detail_ll, 88, 0);

		view.addView(contentView);

		return contentView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
