package cn.com.unispark.fragment.treasure.lease.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.treasure.lease.entity.DistrictEntity.DataObject.DistrictItem;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 区域列表适配器
 * 日期：	2015年11月30日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月30日
 * </pre>
 */
public class ChildAdapter extends BaseAdapter {

	private Context context;
	private List<DistrictItem> list = new ArrayList<DistrictItem>();

	public ChildAdapter(Context context, List<DistrictItem> list) {
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		TextView childText;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.lease_city_child_item,
					null);
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.gray_item_bg));

			holder.childText = (TextView) convertView
					.findViewById(R.id.child_textView);

			ViewUtil.setTextSize(holder.childText, 28);
			ViewUtil.setViewSize(holder.childText, 80, ViewUtil.WEIGHT/2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.childText.setText(list.get(position).getName());
		return convertView;
	}

}
