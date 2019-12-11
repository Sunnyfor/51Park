package cn.com.unispark.fragment.treasure.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.treasure.activity.ActiveEntity.DataObject.ActionCenterInfo;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【活动中心】数据适配器
 * 日期：	2015年3月19日
 * 开发者：	任建飞
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class ActiveAdapter extends BaseAdapter {
	private ArrayList<ActionCenterInfo> list;
	private Context context;
	private ViewHolder holder;

	public ActiveAdapter(Context context, ArrayList<ActionCenterInfo> list) {
		super();
		this.list = list;
		this.context = context;
	}

	class ViewHolder {
		TextView title_tv;// 标题
		TextView time_tv;// 时间
		ImageView image;// 图片
		ImageView outdate_tv;// 图片
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

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(context, R.layout.action_item, null);

			holder = new ViewHolder();

			// 标题
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			ViewUtil.setTextSize(holder.title_tv, 32);
			ViewUtil.setMarginTop(holder.title_tv, 22, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginLeft(holder.title_tv, 20, ViewUtil.LINEARLAYOUT);

			// 时间
			holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
			ViewUtil.setTextSize(holder.time_tv, 24);
			ViewUtil.setMarginTop(holder.time_tv, 10, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginLeft(holder.time_tv, 20, ViewUtil.LINEARLAYOUT);

			// 图片
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			ViewUtil.setViewSize(holder.image, 280, 560);
			ViewUtil.setMargin(holder.image, 20, ViewUtil.RELATIVELAYOUT);

			// 过期
			holder.outdate_tv = (ImageView) convertView
					.findViewById(R.id.outdate_tv);
			ViewUtil.setMargin(holder.outdate_tv, 10, ViewUtil.RELATIVELAYOUT);

			convertView.setTag(holder);
		}

		ActionCenterInfo entity = list.get(position);

		holder.title_tv.setText(entity.getTitle());
		holder.time_tv.setText("截止时间： " + entity.getEnddata());
		ImageUtil.loadImage(context, holder.image, entity.getImg());

		// 判断过期是否显示
		if (1 == entity.getIsexpire()) {
			// 有效
			holder.outdate_tv.setVisibility(View.GONE);
		} else {
			// 过期
			holder.outdate_tv.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
