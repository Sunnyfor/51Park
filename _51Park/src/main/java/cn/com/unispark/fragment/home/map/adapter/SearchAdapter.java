package cn.com.unispark.fragment.home.map.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.home.map.entity.SearchEntity;

/**
 * <pre>
 * 功能说明：展示搜索结果列表的适配器
 * 日期：	2015年6月4日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月4日
 * </pre>
 */
public class SearchAdapter extends BaseAdapter {
	public Context context;
	private List<SearchEntity> list;
	private ViewHolder holder;

	public SearchAdapter(Context context, List<SearchEntity> list) {
		this.context = context;
		this.list = list;
	}

	class ViewHolder {
		TextView nameText;
		TextView districtText;
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

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_result_item, null);
			
			holder = new ViewHolder();
			
			holder.nameText = (TextView) convertView
					.findViewById(R.id.search_name_text);
			holder.districtText = (TextView) convertView
					.findViewById(R.id.search_district_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.nameText.setText(list.get(position).getName());
		holder.districtText.setText(list.get(position).getDistrict());

		return convertView;

	}

}
