package cn.com.unispark.fragment.mine.recharge.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.recharge.entity.RemainConsumeEntity.DataObject.RemainConsumeInfo;

/**
 * <pre>
 * 功能说明：【余额消费】列表的适配器
 * 日期：	2015年7月30日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */

public class RemainConsumeAdapter extends BaseAdapter {

	private List<RemainConsumeInfo> mBalanceConsumptionList;
	private Context context;

	public RemainConsumeAdapter(
			List<RemainConsumeInfo> mBalanceConsumptionList, Context context) {
		this.mBalanceConsumptionList = mBalanceConsumptionList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mBalanceConsumptionList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBalanceConsumptionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		RemainConsumeInfo entity = mBalanceConsumptionList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = View.inflate(context, R.layout.recharge_item,
					null);
			holder.dingdanhaoma_record = (TextView) convertView
					.findViewById(R.id.dingdanhaoma_record);
			holder.money_num_record = (TextView) convertView
					.findViewById(R.id.money_num_record);
			holder.time_record = (TextView) convertView
					.findViewById(R.id.time_record);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.dingdanhaoma_record.setText(entity.getOrderno());
		holder.money_num_record.setText("￥" + entity.getMoney());
		holder.time_record.setText(entity.getTime());
		return convertView;
	}

	class ViewHolder {
		TextView dingdanhaoma_record;// 订单号
		TextView money_num_record;// 金额
		TextView time_record;// 时间
	}
}
