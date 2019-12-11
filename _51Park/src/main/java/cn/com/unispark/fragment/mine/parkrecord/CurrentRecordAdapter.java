package cn.com.unispark.fragment.mine.parkrecord;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.home.pay.entity.OrderListEntity.DataObject.OrderList;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【停车记录】当前订单数据适配器
 * 日期：	2015年3月19日
 * 开发者：	任建飞
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class CurrentRecordAdapter extends BaseAdapter {

	private Context context;
	private List<OrderList> list;
	private ViewHolder holder;

	public CurrentRecordAdapter(Context context, List<OrderList> list) {
		super();
		this.context = context;
		this.list = list;
	}

	private class ViewHolder {
		RelativeLayout recode_rl;
		TextView plateCardTextView;// 车牌号
		TextView parkNameTextView;// 停车场名称
		TextView priceTextView;// 金额
		ImageView record_timer;// 箭头
		TextView timeIn;// 入场时间
		ImageView ispay_iv;// 订单状态
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
			convertView = View.inflate(context,
					R.layout.park_record_current_item, null);
			holder = new ViewHolder();

			// 子条目布局
			// holder.recode_rl = (RelativeLayout) convertView
			// .findViewById(R.id.recode_rl);
			// ViewUtil.setViewSize(holder.recode_rl, 179, 608);

			// 支付状态图标
			holder.ispay_iv = (ImageView) convertView
					.findViewById(R.id.iv_ispay);
			ViewUtil.setMarginRight(holder.ispay_iv, 10,
					ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginTop(holder.ispay_iv, 10, ViewUtil.RELATIVELAYOUT);

			// 车牌号
			holder.plateCardTextView = (TextView) convertView
					.findViewById(R.id.tv_platecard);
			ViewUtil.setTextSize(holder.plateCardTextView, 24);

			// 停车场名称
			holder.parkNameTextView = (TextView) convertView
					.findViewById(R.id.tv_parkname);
			ViewUtil.setTextSize(holder.parkNameTextView, 30);
			ViewUtil.setMarginLeft(holder.parkNameTextView, 20,
					ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginTop(holder.parkNameTextView, 62,
					ViewUtil.RELATIVELAYOUT);

			// 箭头
			holder.record_timer = (ImageView) convertView
					.findViewById(R.id.record_timer);
			ViewUtil.setMarginRight(holder.record_timer, 20,
					ViewUtil.RELATIVELAYOUT);
			// 金额
			holder.priceTextView = (TextView) convertView
					.findViewById(R.id.tv_price);
			ViewUtil.setMarginRight(holder.priceTextView, 95,
					ViewUtil.RELATIVELAYOUT);
			ViewUtil.setTextSize(holder.priceTextView, 36);

			// 进场时间
			holder.timeIn = (TextView) convertView
					.findViewById(R.id.tv_in_time);
			ViewUtil.setViewSize(holder.timeIn, 35, 0);
			ViewUtil.setTextSize(holder.timeIn, 20);
			ViewUtil.setMarginTop(holder.timeIn, 30, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginLeft(holder.timeIn, 20, ViewUtil.RELATIVELAYOUT);

			convertView.setTag(holder);

		}

		OrderList entity = list.get(position);

		if (!TextUtils.isEmpty(entity.getCarno())) {
			holder.plateCardTextView.setVisibility(View.VISIBLE);
			holder.plateCardTextView.setText(entity.getCarno());
		} else {
			holder.plateCardTextView.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(entity.getParkname())) {
			if (entity.getParkname().length() > 10) {
				holder.parkNameTextView.setText(entity.getParkname()
						.subSequence(0, 10) + "...");
			} else {
				holder.parkNameTextView.setText(entity.getParkname());
			}
		}

		switch (entity.getStatus()) {
		case 1:
			holder.ispay_iv.setImageResource(R.drawable.label_finish);
			break;
		case 2:
			holder.ispay_iv.setImageResource(R.drawable.label_nopay);
			break;
		case 3:
			holder.ispay_iv.setImageResource(R.drawable.label_nofinish);
			break;
		case 4:
			holder.ispay_iv.setImageResource(R.drawable.label_gudingche);
			break;
		default:
			holder.ispay_iv.setImageResource(R.drawable.label_nofinish);
			break;
		}

		// holderView.parkNameTextView.setText(recodeEntity.getPark_name());
		holder.priceTextView.setText(entity.getShparkfee());

		// 需要添加进场时间
		holder.timeIn.setText(entity.getEntertime());
		/*
		 * if (!TextUtils.isEmpty(entity.getExit_time())) {
		 * holder.timeOut.setVisibility(View.VISIBLE);
		 * holder.timeOut.setText(entity.getExit_time()); } else {
		 * holder.timeOut.setVisibility(View.INVISIBLE); }
		 */
		return convertView;
	}

	/*
	 * @Override public void onScroll(AbsListView view, int firstVisibleItem,
	 * int visibleItemCount, int totalItemCount) { if (view instanceof
	 * PinnedHeaderListView) { ((PinnedHeaderListView)
	 * view).controlPinnedHeader(firstVisibleItem); } }
	 * 
	 * @Override public void onScrollStateChanged(AbsListView view, int
	 * scrollState) {
	 * 
	 * }
	 */

	// private boolean needTitle(int position) {
	// if (position == 0) {
	// return true;
	// }
	//
	// if (position < 0) {
	// return false;
	// }
	//
	// ParkRecodeInfo currentEntity = (ParkRecodeInfo) getItem(position);
	// ParkRecodeInfo previousEntity = (ParkRecodeInfo) getItem(position - 1);
	// if (null == currentEntity || null == previousEntity) {
	// return false;
	// }
	//
	// String currentTitle = currentEntity.getGroup();
	// String previousTitle = previousEntity.getGroup();
	// if (null == previousTitle || null == currentTitle) {
	// return false;
	// }
	// if (currentTitle.equals(previousTitle)) {
	// return false;
	// }
	//
	// return true;
	// }

	// private boolean isMove(int position) {
	// ParkRecodeInfo currentEntity = (ParkRecodeInfo) getItem(position);
	// ParkRecodeInfo nextEntity = (ParkRecodeInfo) getItem(position + 1);
	// if (null == currentEntity || null == nextEntity) {
	// return false;
	// }
	// String currentTitle = currentEntity.getGroup();
	// String nextTitle = nextEntity.getGroup();
	// if (null == currentTitle || null == nextTitle) {
	// return false;
	// }
	// if (!currentTitle.equals(nextTitle)) {
	// return true;
	// }
	//
	// return false;
	// }

	// public int getSectionForPosition(int position) {
	// // return list.get(position).getmTitle().charAt(0);
	// return 1;
	// }

	// public int getPositionForSection(int section) {
	// // for (int i = 0; i < getCount(); i++) {
	// // String sortStr = list.get(i).getTitle();
	// // char firstChar = sortStr.toUpperCase().charAt(0);
	// // if (firstChar == section) {
	// // return i;
	// // }
	// // }
	//
	// return -1;
	// }
}
