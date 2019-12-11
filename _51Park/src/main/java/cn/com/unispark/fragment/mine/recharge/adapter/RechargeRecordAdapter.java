package cn.com.unispark.fragment.mine.recharge.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.recharge.entity.RechargeRecordEntity;
import cn.com.unispark.fragment.mine.recharge.entity.RechargeRecordEntity.DataObject.RechargeRecordInfo;
import cn.com.unispark.fragment.mine.recharge.entity.RemainConsumeEntity.DataObject.RemainConsumeInfo;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明：【充值记录】列表的适配器
 * 日期：	2015年7月30日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：  陈丶泳佐
 *    修改日期： 2015年8月12日
 * </pre>
 */
public class RechargeRecordAdapter extends BaseAdapter {

	private List<RechargeRecordInfo> mBalanceRecodeList;
	private Context context;

	public RechargeRecordAdapter(List<RechargeRecordInfo> mBalanceRecodeList,
			Context context) {
		this.mBalanceRecodeList = mBalanceRecodeList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mBalanceRecodeList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBalanceRecodeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.recharge_item_new, null);
			holder.rl_recharge_new = (RelativeLayout) convertView
					.findViewById(R.id.rl_recharge_new);
			ViewUtil.setViewSize(holder.rl_recharge_new, 150, 0);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_item_name);
			ViewUtil.setTextSize(holder.name, 30);
			ViewUtil.setMarginLeft(holder.name, 20, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginTop(holder.name, 27, ViewUtil.RELATIVELAYOUT);
			holder.time = (TextView) convertView
					.findViewById(R.id.tv_item_time);
			ViewUtil.setMarginLeft(holder.time, 20, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginTop(holder.time, 20, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginBottom(holder.time, 27, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setTextSize(holder.time, 24);

			holder.price = (TextView) convertView
					.findViewById(R.id.tv_item_jine);
			ViewUtil.setTextSize(holder.price, 30);
			ViewUtil.setPaddingRight(holder.price, 10);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RechargeRecordInfo entity = mBalanceRecodeList.get(position);
		// 余额类型（1:消费;2:充值）
		if ("1".equals(entity.getBalancetype())) {// 消费
			holder.price.setText("-" + entity.getMoney());
			holder.price.setTextColor(context.getResources().getColor(
					R.color.gray_font));
			holder.name.setText(entity.getParkname());
		} else if ("2".equals(entity.getBalancetype())) {// 充值

			holder.name.setText(entity.getRechargewag());
			holder.price.setText("+" + entity.getMoney());
			holder.price.setTextColor(context.getResources().getColor(
					R.color.red_font));
		}
		holder.time.setText(entity.getTime());
		return convertView;
	}

	class ViewHolder {
		RelativeLayout rl_recharge_new;
		TextView name;// 金额
		TextView time;// 时间
		TextView price;// 时间
	}
}
