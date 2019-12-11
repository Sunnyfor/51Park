package cn.com.unispark.fragment.treasure.lease.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.treasure.lease.entity.CityEntity.DataObject.CityItem;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 城市列表适配器
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
public class GroupAdapter extends BaseAdapter {

	private Context context;
	// private String[] mGroupNameArr;// 父item标题数组
	private int mPosition = 0;// 选中的位置
	private List<CityItem> list = new ArrayList<CityItem>();

	public GroupAdapter(Context context, List<CityItem> list) {
		this.context = context;
		this.list = list;
	}

	public void setSelectedPosition(int position) {
		this.mPosition = position;
	}

	static class ViewHolder {
		/** 父Item名称 **/
		TextView groupText;
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
		// 初始化布局控件
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.lease_city_group_item, null);
			
			holder.groupText = (TextView) convertView
					.findViewById(R.id.group_textView);
			ViewUtil.setTextSize(holder.groupText, 28);
			ViewUtil.setViewSize(holder.groupText, 80, 0);
			ViewUtil.setMarginLeft(holder.groupText, 40, ViewUtil.LINEARLAYOUT);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 设置控件内容
		holder.groupText.setText(list.get(position).getName());

		if (mPosition == position) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.gray_item_bg));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.white));
		}
		
		return convertView;
	}

}
