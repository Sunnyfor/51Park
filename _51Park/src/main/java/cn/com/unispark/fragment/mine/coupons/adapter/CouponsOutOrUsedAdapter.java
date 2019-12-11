package cn.com.unispark.fragment.mine.coupons.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity.DataObject.CouponsInfo;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 优惠券列表显示的适配器
 * 日期：	2015年7月31日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年7月31日
 * </pre>
 */
public class CouponsOutOrUsedAdapter extends BaseAdapter {
	public Context mcontext;
	private LayoutInflater inflater = null;
	private ViewHolder holder;
	private List<CouponsInfo> list;
	private Map<String, Boolean> index = new HashMap<String, Boolean>();
	public final int ITEM = 0;// 抵扣券
	public final int ITEM_OTHER = 1;// 其他

	private class ViewHolder {
		/**
		 * 优惠券面额
		 */
		TextView tv_youhuijine;
		/**
		 * 优惠券类型
		 */
		TextView coupon_type_tv;
		/**
		 * 优惠券类型解释
		 */
		TextView coupon_prompt_tv;
		TextView coupon_desc_tv;// 优惠券描述
		TextView tv_date;// 优惠券使用截至日期
		CheckBox is_show_cb;// 是否展示优惠券详情
		public LinearLayout detail_ll;
		public TextView tv_rmb;

	}

	public CouponsOutOrUsedAdapter(Context context, List<CouponsInfo> dataList) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.list = dataList;
		this.mcontext = context;
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
		if (3 == list.get(position).getCoupons_type()) {
			return ITEM;
		} else {
			return ITEM_OTHER;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type;
		if (3 == list.get(position).getCoupons_type()) {
			type = ITEM;
		} else {
			type = ITEM_OTHER;
		}
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.coupons_item_use, null);
			holder = new ViewHolder();
			switch (type) {
			case ITEM:// 抵扣券
				convertView = View.inflate(mcontext,
						R.layout.coupons_item_out_discount, null);
				break;
			case ITEM_OTHER:// 其他
				convertView = View.inflate(mcontext, R.layout.coupons_item_out,
						null);
				break;
			}

			// detail布局
			holder.detail_ll = (LinearLayout) convertView
					.findViewById(R.id.detail_ll);
			ViewUtil.setMarginLeft(holder.detail_ll, 80,
					ViewUtil.RELATIVELAYOUT);
			// 金额
			holder.tv_youhuijine = (TextView) convertView
					.findViewById(R.id.tv_youhuijine);

			ViewUtil.setTextSize(holder.tv_youhuijine, 72);
			holder.tv_rmb = (TextView) convertView.findViewById(R.id.tv_rmb);

			ViewUtil.setTextSize(holder.tv_rmb, 36);
			// 优惠券名称
			holder.coupon_type_tv = (TextView) convertView
					.findViewById(R.id.coupon_type_tv);
			ViewUtil.setTextSize(holder.coupon_type_tv, 30);
			ViewUtil.setMarginLeft(holder.coupon_type_tv, 48,
					ViewUtil.RELATIVELAYOUT);
			// 优惠券描述
			holder.coupon_prompt_tv = (TextView) convertView
					.findViewById(R.id.coupon_prompt_tv);
			ViewUtil.setTextSize(holder.coupon_prompt_tv, 28);

			switch (type) {
			case ITEM:// 抵扣券
				ViewUtil.setMarginLeft(holder.tv_youhuijine, 15,
						ViewUtil.LINEARLAYOUT);
				break;
			case ITEM_OTHER:// 其他
				ViewUtil.setMarginLeft(holder.coupon_prompt_tv, 15,
						ViewUtil.LINEARLAYOUT);
				break;
			}
			// 有效期
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			ViewUtil.setTextSize(holder.tv_date, 25);
			ViewUtil.setMarginTop(holder.tv_date, 15, ViewUtil.LINEARLAYOUT);
			// 按钮checkbox
			holder.is_show_cb = (CheckBox) convertView
					.findViewById(R.id.is_show_cb);
			ViewUtil.setMarginRight(holder.is_show_cb, 25,
					ViewUtil.RELATIVELAYOUT);
			holder.coupon_desc_tv = (TextView) convertView
					.findViewById(R.id.coupon_desc_tv);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// int s = list.get(position).getType_money().lastIndexOf(".");
		// Log.e("slx", "s" + map.get("type_money").subSequence(0, s));
		holder.tv_youhuijine.setText(list.get(position).getType_money());
		// 优惠券类型描述
		holder.coupon_prompt_tv.setText("("
				+ list.get(position).getCoupon_prompt() + ")");
		holder.coupon_desc_tv.setText(list.get(position).getDesc());
		holder.tv_date.setText("有效期: " + list.get(position).getUse_end_date());
		holder.is_show_cb.setTag(String.valueOf(position));
		if (index.containsKey(String.valueOf(position))) {
			holder.coupon_desc_tv.setVisibility(View.VISIBLE);
			holder.is_show_cb.setChecked(true);
		} else {
			holder.coupon_desc_tv.setVisibility(View.GONE);
			holder.is_show_cb.setChecked(false);
		}
		// 优惠券类别1.现金券2.限定券3.抵扣券
		switch (list.get(position).getCoupons_type()) {
		case 1:
			holder.coupon_type_tv.setText("现金券");
			holder.coupon_prompt_tv.setVisibility(View.GONE);
			holder.is_show_cb.setVisibility(View.GONE);
			break;
		case 2:
			holder.coupon_type_tv.setText("限定券");
			holder.coupon_prompt_tv.setVisibility(View.VISIBLE);
			holder.is_show_cb.setVisibility(View.GONE);
			break;
		case 3:
			holder.coupon_type_tv.setText("抵扣券");
			holder.coupon_prompt_tv.setVisibility(View.VISIBLE);
			holder.is_show_cb.setVisibility(View.GONE);
			break;
		default:
			holder.coupon_type_tv.setText("停车券");
			holder.coupon_prompt_tv.setVisibility(View.GONE);
			break;
		}
		holder.is_show_cb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.e("miao", "CheckBox:" + isChecked);
						if (isChecked) {
							index.put((String) buttonView.getTag(), isChecked);
							Log.e("miao", "CheckBox:hah");
						} else {
							index.remove((String) buttonView.getTag());
						}
						notifyDataSetChanged();
					}
				});

		return convertView;
	}

}
