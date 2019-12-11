package cn.com.unispark.fragment.mine.plate.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.plate.entity.PlateEntity.DataObject.PlateInfo;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenu;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenuCreator;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenuItem;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 车牌管理实体类
 * 日期：	2015年11月19日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月3日
 * </pre>
 */
public class PlateAdapter extends BaseAdapter {

	private static final int IS_DEFAULT = 0;

	private static final int NO_DEFAULT = 1;

	private static final int IS_EXAMINE = 2;
	private ArrayList<PlateInfo> list;
	private ViewHolder holder;
	private Context context;

	public PlateAdapter(Context context, ArrayList<PlateInfo> list) {
		this.context = context;
		this.list = list;
	}

	private class ViewHolder {
		TextView test0_tv;// 座驾牌号
		RelativeLayout province_rl;// 车牌号布局
		TextView province_tv;// 省份
		TextView plate_tv;// 车牌号
		TextView tv_default;
	}

	@Override
	public int getViewTypeCount() {
		// menu type count
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		// current menu type
		if ("0".equals(list.get(position).getIsdefault())
				&& list.get(position).getIsexamine() == 0) {
			return IS_DEFAULT;
		} else if ("0".equals(list.get(position).getIsdefault())
				&& list.get(position).getIsexamine() == 1) {
			return IS_EXAMINE;
		}
		return NO_DEFAULT;
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

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.plate_item, null);
			holder = new ViewHolder();

			// 座驾牌号
			holder.test0_tv = (TextView) convertView
					.findViewById(R.id.test0_tv);
			ViewUtil.setTextSize(holder.test0_tv, 30);
			ViewUtil.setMarginTop(holder.test0_tv, 20, ViewUtil.RELATIVELAYOUT);
			
			// 默认
			/*holder.tv_default = (TextView) convertView
					.findViewById(R.id.tv_defaultcard);
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.icon_red_point);
			drawable.setBounds(0, 0, ViewUtil.getWidth(10),
					ViewUtil.getWidth(10));
			holder.tv_default.setCompoundDrawables(drawable, null, null, null);
			ViewUtil.setTextSize(holder.tv_default, 24);
			ViewUtil.setMarginTop(holder.tv_default, 20,
					ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginRight(holder.tv_default, 20,
					ViewUtil.RELATIVELAYOUT);*/
			// 车牌号布局
			holder.province_rl = (RelativeLayout) convertView
					.findViewById(R.id.province_rl);
			ViewUtil.setViewSize(holder.province_rl, 130, 412);
			ViewUtil.setMarginTop(holder.province_rl, 10, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginBottom(holder.province_rl, 20,
					ViewUtil.LINEARLAYOUT);

			// 省份
			holder.province_tv = (TextView) convertView
					.findViewById(R.id.province_tv);
			ViewUtil.setTextSize(holder.province_tv, 60);

			// 车牌号
			holder.plate_tv = (TextView) convertView
					.findViewById(R.id.plate_tv);
			ViewUtil.setTextSize(holder.plate_tv, 60);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(list.get(position).getPlate())) {
			String province = list.get(position).getPlate().substring(0, 2);
			String plate = list.get(position).getPlate()
					.substring(2, list.get(position).getPlate().length());
			holder.province_tv.setText(province);
			holder.plate_tv.setText(plate);
		}

		/*if ("0".equals(list.get(position).getIsdefault())) {
			holder.tv_default.setVisibility(View.INVISIBLE);
		} else {
			holder.tv_default.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getIsexamine() == 0) {
			holder.tv_default.setText("默认");
			holder.province_rl.setBackground(context.getResources()
					.getDrawable(R.drawable.bg_plate_card));
		} 
		if (list.get(position).getIsexamine() != 0) {
			holder.tv_default.setText("申诉中");
			holder.tv_default.setVisibility(View.VISIBLE);
			holder.province_rl.setBackground(context.getResources()
					.getDrawable(R.drawable.bg_plate_card_default));
		}*/
		return convertView;
	}
}
