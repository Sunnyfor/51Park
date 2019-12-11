package cn.com.unispark.fragment.mine.parkrecord;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.parkrecord.ParkRecodeEntity.DataObject.ParkRecodeInfo;
import cn.com.unispark.fragment.mine.setting.offlinemap.view.PinnedHeaderListView;
import cn.com.unispark.fragment.mine.setting.offlinemap.view.PinnedHeaderListView.PinnedHeaderAdapter;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【停车记录】数据适配器
 * 日期：	2015年3月19日
 * 开发者：	任建飞
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class ParkRecordAdapter extends BaseAdapter implements OnScrollListener,
		PinnedHeaderAdapter {

	private Context context;
	private List<ParkRecodeInfo> list;
	private ViewHolder holder;

	public ParkRecordAdapter(Context context, List<ParkRecodeInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	private class ViewHolder {
		RelativeLayout recode_rl;
		TextView tv_rmb;
		LinearLayout time_ll;
		TextView plateCardTextView;// 车牌号
		TextView parkNameTextView;// 停车场名称
		TextView priceTextView;// 金额
		ImageView ispay_iv;// 订单状态
		ImageView record_timer;// 箭头
		TextView timeIn;// 入场时间
		TextView timeOut;// 出场时间
		TextView title_tv;
		public TextView title_price;
		public LinearLayout layout_header;
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
			convertView = View
					.inflate(context, R.layout.park_record_item, null);
			holder = new ViewHolder();

			// 子条目布局
			holder.recode_rl = (RelativeLayout) convertView
					.findViewById(R.id.recode_rl);
			ViewUtil.setViewSize(holder.recode_rl, 179, 608);

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
			
			/**
			 *  可动布局(列表头部)
			 *  判断如果是当前订单：则不显示，如果是历史订单，显示。
			 */
			/*====================Start=======================*/
			
			holder.layout_header = (LinearLayout) convertView
					.findViewById(R.id.layout_header);
			ViewUtil.setViewSize(holder.layout_header, 50, 0);
//			时间 如：2016年08月
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			ViewUtil.setTextSize(holder.title_tv, 24);
			ViewUtil.setPaddingLeft(holder.title_tv, 20);
//			消费 如：￥130.00
			holder.title_price = (TextView) convertView
					.findViewById(R.id.title_price);
			ViewUtil.setTextSize(holder.title_price, 24);
			ViewUtil.setPaddingRight(holder.title_price, 20);
			
			/*=====================End======================*/
			
			
			
			holder.tv_rmb = (TextView) convertView.findViewById(R.id.tv_rmb);
			ViewUtil.setTextSize(holder.tv_rmb, 24);
			// 支付状态图标
			holder.ispay_iv = (ImageView) convertView
					.findViewById(R.id.iv_ispay);
			ViewUtil.setMarginRight(holder.ispay_iv, 10,
					ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginTop(holder.ispay_iv, 10, ViewUtil.RELATIVELAYOUT);

			// 时间布局
			holder.time_ll = (LinearLayout) convertView
					.findViewById(R.id.time_ll);
			ViewUtil.setViewSize(holder.time_ll, 35, 0);
			ViewUtil.setMarginTop(holder.time_ll, 15, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginLeft(holder.time_ll, 20, ViewUtil.RELATIVELAYOUT);

			holder.timeIn = (TextView) convertView
					.findViewById(R.id.tv_in_time);
			ViewUtil.setViewSize(holder.timeIn, 35, 0);
			ViewUtil.setTextSize(holder.timeIn, 20);

			holder.timeOut = (TextView) convertView
					.findViewById(R.id.tv_out_time);
			ViewUtil.setViewSize(holder.timeOut, 35, 0);
			ViewUtil.setTextSize(holder.timeOut, 20);

			convertView.setTag(holder);

		}

		ParkRecodeInfo entity = list.get(position);

		switch (entity.getIspay()) {
		case 1:
			holder.ispay_iv.setImageResource(R.drawable.label_finish);
			break;
		case 2:
			holder.ispay_iv.setImageResource(R.drawable.label_nopay);
			break;
		case 3:
			holder.ispay_iv.setImageResource(R.drawable.label_nofinish);
			break;
		default:
			holder.ispay_iv.setImageResource(R.drawable.label_nofinish);
			break;
		}

		if (!TextUtils.isEmpty(entity.getLicense())) {
			holder.plateCardTextView.setVisibility(View.VISIBLE);
			holder.plateCardTextView.setText(entity.getLicense());
		} else {
			holder.plateCardTextView.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(entity.getPark_name())) {
			if (entity.getPark_name().length() > 10) {
				holder.parkNameTextView.setText(entity.getPark_name()
						.subSequence(0, 7) + "...");
			} else {
				holder.parkNameTextView.setText(entity.getPark_name());
			}
		}
		if (needTitle(position)) {
			holder.title_tv.setText(entity.getGroup());
			holder.title_price.setText("消费：￥" + entity.getTotalfee());
			holder.layout_header.setVisibility(View.VISIBLE);
		} else {
			holder.layout_header.setVisibility(View.GONE);
		}
		// holderView.parkNameTextView.setText(recodeEntity.getPark_name());
		holder.priceTextView.setText(entity.getMoney());
		holder.timeIn.setText(entity.getEnter_time());
		if (!TextUtils.isEmpty(entity.getExit_time())) {
			holder.timeOut.setVisibility(View.VISIBLE);
			holder.timeOut.setText(entity.getExit_time());
		} else {
			holder.timeOut.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		if (getCount() == 0 || position < 0) {
			return PinnedHeaderAdapter.PINNED_HEADER_GONE;
		}

		if (isMove(position) == true) {
			return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
		}

		return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View headerView, int position, int alpaha) {
		ParkRecodeInfo itemEntity = (ParkRecodeInfo) getItem(position);
		String headerValue = itemEntity.getGroup();
		String headerPriceValue = itemEntity.getTotalfee();
		// 设置浮条的样式
		if (!TextUtils.isEmpty(headerValue)) {
			ViewUtil.setViewSize(headerView, 50, 0);
			TextView headerText = (TextView) headerView
					.findViewById(R.id.header);
			ViewUtil.setTextSize(headerText, 24);
			ViewUtil.setPaddingLeft(headerText, 20);
			headerText.setText(headerValue);

			TextView headerPriceText = (TextView) headerView
					.findViewById(R.id.header_price);
			ViewUtil.setTextSize(headerPriceText, 24);
			ViewUtil.setPaddingRight(headerPriceText, 20);
			headerPriceText.setText("消费：￥" + headerPriceValue);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	private boolean needTitle(int position) {
		if (position == 0) {
			return true;
		}

		if (position < 0) {
			return false;
		}

		ParkRecodeInfo currentEntity = (ParkRecodeInfo) getItem(position);
		ParkRecodeInfo previousEntity = (ParkRecodeInfo) getItem(position - 1);
		if (null == currentEntity || null == previousEntity) {
			return false;
		}

		String currentTitle = currentEntity.getGroup();
		String previousTitle = previousEntity.getGroup();
		if (null == previousTitle || null == currentTitle) {
			return false;
		}
		if (currentTitle.equals(previousTitle)) {
			return false;
		}

		return true;
	}

	private boolean isMove(int position) {
		ParkRecodeInfo currentEntity = (ParkRecodeInfo) getItem(position);
		ParkRecodeInfo nextEntity = null;
		if (position>0) {
			nextEntity = (ParkRecodeInfo) getItem(position + 1);
		}
		
		if (null == currentEntity || null == nextEntity) {
			return false;
		}
		String currentTitle = currentEntity.getGroup();
		String nextTitle = nextEntity.getGroup();
		if (null == currentTitle || null == nextTitle) {
			return false;
		}
		if (!currentTitle.equals(nextTitle)) {
			return true;
		}

		return false;
	}

	public int getSectionForPosition(int position) {
		// return list.get(position).getmTitle().charAt(0);
		return 1;
	}

	public int getPositionForSection(int section) {
		// for (int i = 0; i < getCount(); i++) {
		// String sortStr = list.get(i).getTitle();
		// char firstChar = sortStr.toUpperCase().charAt(0);
		// if (firstChar == section) {
		// return i;
		// }
		// }

		return -1;
	}
}
