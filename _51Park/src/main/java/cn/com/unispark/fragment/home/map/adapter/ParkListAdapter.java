package cn.com.unispark.fragment.home.map.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity.DataObject.ParkItemInfo;
import cn.com.unispark.fragment.home.map.navigation.NavVoiceActivity;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明：展示【车位列表】的适配器
 * 日期：	2015年6月4日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月4日
 * </pre>
 */
public class ParkListAdapter extends BaseAdapter {

	public Context context;
	private List<ParkItemInfo> list;
	private ViewHolder holder;

	public ParkListAdapter(Context context, List<ParkItemInfo> list) {
		this.context = context;
		this.list = list;
	}

	class ViewHolder {
		TextView name_tv;
		TextView address_tv;
		TextView total_count_tv;
		TextView empty_count_tv;
		TextView prompt_tv;
		TextView distance_tv;
		View line_view;
		ImageView state_iv;
		ImageView lease_iv;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = View.inflate(context, R.layout.park_list_item, null);

			holder = new ViewHolder();

			// 停车场类型：免费/无忧
			holder.state_iv = (ImageView) convertView
					.findViewById(R.id.state_iv);

			// 停车场名称
			holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			ViewUtil.setTextSize(holder.name_tv, 30);
			ViewUtil.setMarginTop(holder.name_tv, 22, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginLeft(holder.name_tv, 22, ViewUtil.RELATIVELAYOUT);

			// 停车场总车位数
			holder.total_count_tv = (TextView) convertView
					.findViewById(R.id.total_count_tv);
			ViewUtil.setTextSize(holder.total_count_tv, 26);
			ViewUtil.setMarginTop(holder.total_count_tv, 12,
					ViewUtil.RELATIVELAYOUT);

			// 停车场空车位数
			holder.empty_count_tv = (TextView) convertView
					.findViewById(R.id.empty_count_tv);
			ViewUtil.setTextSize(holder.empty_count_tv, 26);

			// 空车位提示语
			TextView prompt_tv = (TextView) convertView
					.findViewById(R.id.prompt_tv);
			ViewUtil.setTextSize(prompt_tv, 18);
			ViewUtil.setMarginRight(prompt_tv, 10, ViewUtil.RELATIVELAYOUT);

			// 停车场地址
			holder.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			ViewUtil.setTextSize(holder.address_tv, 26);
			ViewUtil.setMargin(holder.address_tv, 12, 0, 20, 20,
					ViewUtil.RELATIVELAYOUT);

			// 停车场距离
			holder.distance_tv = (TextView) convertView
					.findViewById(R.id.distance_tv);
			ViewUtil.setTextSize(holder.distance_tv, 28);
			ViewUtil.setViewSize(holder.distance_tv, 108, 138);

			// 分割线
			holder.line_view = convertView.findViewById(R.id.line_view);
			ViewUtil.setViewSize(holder.line_view, 118, 1);
			ViewUtil.setMargin(holder.line_view, 0, 20, ViewUtil.RELATIVELAYOUT);

			// 包租车位
			holder.lease_iv = (ImageView) convertView
					.findViewById(R.id.lease_iv);
			ViewUtil.setMarginLeft(holder.lease_iv, 10, ViewUtil.RELATIVELAYOUT);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name_tv.setText(list.get(position).getName());
		holder.address_tv.setText(list.get(position).getAddress());
		holder.total_count_tv.setText("总车位: "
				+ list.get(position).getAllcount() + "\t空车位: ");
		holder.empty_count_tv.setText(list.get(position).getAllcount());
		holder.distance_tv.setText(ReckonUtil.getDistanceFormat(list.get(
				position).getFar()));
		holder.distance_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ParkApplication.mLatEnd = list.get(position).getLatitude();
				ParkApplication.mLonEnd = list.get(position).getLongitude();
				ToolUtil.IntentClass(BaseActivity.activity,
						NavVoiceActivity.class, false);
			}
		});

		// 是否为包租车位
		if (list.get(position).getIsmonth() == 0
				&& list.get(position).getIstimes() == 0) {
			holder.lease_iv.setVisibility(View.GONE);
		} else if (list.get(position).getIsmonth() == 1
				|| list.get(position).getIstimes() == 1) {
			holder.lease_iv.setVisibility(View.VISIBLE);
		}

		// 无忧停车场
		if (list.get(position).getIsjoin() == 0) {
			holder.state_iv.setBackgroundResource(0);
		} else if (list.get(position).getIsjoin() == 1) {
			holder.state_iv.setBackgroundResource(R.drawable.label_park_join);
		}

		// 免费停车场
		if (list.get(position).getIsfree() == 0) {
			holder.state_iv.setBackgroundResource(0);
		} else if (list.get(position).getIsfree() == 1) {
			holder.state_iv.setBackgroundResource(R.drawable.label_park_free);
		}

		return convertView;

	}
}
