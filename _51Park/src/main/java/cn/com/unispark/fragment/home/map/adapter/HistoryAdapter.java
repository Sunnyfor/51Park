package cn.com.unispark.fragment.home.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.home.map.entity.HistoryEntity;

/**
 * <pre>
 * 功能说明： 历史搜索记录，数据保存的适配器
 * 日期：	2015年11月5日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月5日
 * </pre>
 */
public class HistoryAdapter extends BaseAdapter {

	public Context context;
	private HistoryEntity[] entity;
	@SuppressWarnings("unused")
	private ViewHolder holder;

	public HistoryAdapter(Context context, HistoryEntity[] entity) {
		this.context = context;
		this.entity = entity;
	}

	class ViewHolder {
		TextView groupItem;
	}

	@Override
	public int getCount() {
		return entity.length;
	}

	@Override
	public Object getItem(int position) {
		return entity[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_history_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.groupItem = (TextView) convertView
					.findViewById(R.id.search_address_tv);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.groupItem.setText(entity[position].getKey());

		return convertView;
	}

}
