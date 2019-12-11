package cn.com.unispark.fragment.treasure.lease.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseCarEntity.DataObject.LeaseCarInfo;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【车位租赁】停车场适配器
 * 日期：	2015年9月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月22日
 * </pre>
 */
public class LeaseCarAdapter extends BaseAdapter {

	public Context context;
	private ViewHolder holder;
	private List<LeaseCarInfo> list;

	public final int ITEM = 0;// 未售完
	public final int ITEM_SOLDOUT = 1;// 已售完

	public LeaseCarAdapter(Context context, List<LeaseCarInfo> list) {
		this.context = context;
		this.list = list;
	}

	class ViewHolder {
		ImageView already_buy_iv;
		ImageView desc_iv;
		TextView name_tv;
		TextView address_tv;
		TextView month_tv;
		TextView meter_tv;
		LinearLayout sold_ll;
		TextView sold_tv;
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
	public int getItemViewType(int position) {

		// 定义不同位置的convertView类型
		LeaseCarInfo entity = list.get(position);
		if (1 == entity.getIssoldout()) {
			return ITEM;
		} else {
			return ITEM_SOLDOUT;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LeaseCarInfo entity = list.get(position);

		int type;
		if (1 == entity.getIssoldout()) {
			type = ITEM;
		} else {
			type = ITEM_SOLDOUT;
		}

		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case ITEM:
				convertView = View.inflate(context, R.layout.lease_car_item,
						null);
				break;
			case ITEM_SOLDOUT:
				convertView = View.inflate(context,
						R.layout.lease_car_item_sold, null);

				// 已售罄布局
				holder.sold_ll = (LinearLayout) convertView
						.findViewById(R.id.sold_ll);
				ViewUtil.setViewSize(holder.sold_ll, 143, 190);
				ViewUtil.setMargin(holder.sold_ll, 20, ViewUtil.RELATIVELAYOUT);

				// 已售罄提示
				holder.sold_tv = (TextView) convertView
						.findViewById(R.id.sold_tv);
				ViewUtil.setTextSize(holder.sold_tv, 20);

				break;
			}

			// 已购买图标
			holder.already_buy_iv = (ImageView) convertView
					.findViewById(R.id.already_buy_iv);

			// 停车场描述图片
			holder.desc_iv = (ImageView) convertView.findViewById(R.id.desc_iv);
			ViewUtil.setViewSize(holder.desc_iv, 143, 190);
			ViewUtil.setMargin(holder.desc_iv, 20, ViewUtil.RELATIVELAYOUT);

			// 停车场名称
			holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			ViewUtil.setTextSize(holder.name_tv, 30);
			ViewUtil.setMarginTop(holder.name_tv, 22, ViewUtil.RELATIVELAYOUT);

			// 停车场地址
			holder.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			ViewUtil.setTextSize(holder.address_tv, 24);
			ViewUtil.setMarginTop(holder.address_tv, 16,
					ViewUtil.RELATIVELAYOUT);

			// 包月
			holder.month_tv = (TextView) convertView
					.findViewById(R.id.month_tv);
			ViewUtil.setTextSize(holder.month_tv, 28);
			ViewUtil.setMarginTop(holder.month_tv, 16, ViewUtil.LINEARLAYOUT);

			// 计次
			holder.meter_tv = (TextView) convertView
					.findViewById(R.id.meter_tv);
			ViewUtil.setTextSize(holder.meter_tv, 28);
			ViewUtil.setMarginTop(holder.meter_tv, 16, ViewUtil.LINEARLAYOUT);

			convertView.setTag(holder);

		} else {
			switch (type) {
			case ITEM:
				holder = (ViewHolder) convertView.getTag();
				break;
			case ITEM_SOLDOUT:
				holder = (ViewHolder) convertView.getTag();
				break;
			}
		}

		// 不同停车场模式显示？0-月卡日卡 1-月卡 2-日卡
		switch (entity.getDefaluttype()) {
		case 0:// 包月+计次
			holder.month_tv.setText(entity.getType1());
			holder.meter_tv.setText(entity.getType2());
			holder.month_tv.setVisibility(View.VISIBLE);
			holder.meter_tv.setVisibility(View.VISIBLE);
			break;
		case 1:// 包月
			holder.month_tv.setText(entity.getType1());
			holder.month_tv.setVisibility(View.VISIBLE);
			holder.meter_tv.setVisibility(View.GONE);
			break;
		case 2:// 计次
			holder.meter_tv.setText(entity.getType2());
			holder.meter_tv.setVisibility(View.VISIBLE);
			holder.month_tv.setVisibility(View.GONE);
			break;
		}

		// 停车场描述图片、名称、地址
		ImageUtil.loadImage(context, holder.desc_iv, entity.getImg());
		holder.name_tv.setText(entity.getParkname());
		holder.address_tv.setText(entity.getParkaddr());

		// 是否已购买停车位
		if (entity.getIsbuy() == 1) {
			holder.already_buy_iv.setVisibility(View.VISIBLE);
		} else {
			holder.already_buy_iv.setVisibility(View.GONE);
		}

		return convertView;
	}
}
