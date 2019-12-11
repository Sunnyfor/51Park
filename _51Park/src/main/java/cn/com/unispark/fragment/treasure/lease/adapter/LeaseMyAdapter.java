package cn.com.unispark.fragment.treasure.lease.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseMyEntity.DataObject.LeaseMyInfo;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【我的租赁】停车场适配器
 * 日期：	2015年9月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月22日
 * </pre>
 */
public class LeaseMyAdapter extends BaseAdapter {

	public Context context;
	private List<LeaseMyInfo> list;
	private ViewHolder holder;
	private boolean isNoDate;

	public LeaseMyAdapter(Context context, List<LeaseMyInfo> list,
			boolean isCanUser) {
		super();
		this.context = context;
		this.list = list;
		this.isNoDate = isCanUser;
	}

	class ViewHolder {
		TextView type_tv;// 租赁类型
		ImageView desc_iv;// 停车场详情图
		TextView name_tv;// 停车场名称
		TextView address_tv;// 停车场地址
		TextView detail_tv;// 停车场租赁服务详情介绍
		TextView renew_tv;// 续费按钮
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
			holder = new ViewHolder();
			if (isNoDate) {//未过期
				convertView = View.inflate(context, R.layout.lease_my_item,
						null);

				// 续费按钮
				holder.renew_tv = (TextView) convertView
						.findViewById(R.id.renew_tv);
				ViewUtil.setPadding(holder.renew_tv, 20);
			} else {//已过期
				convertView = View.inflate(context, R.layout.lease_mydate_item,
						null);
			}

			// 租赁类型
			holder.type_tv = (TextView) convertView
					.findViewById(R.id.type_tv);
			ViewUtil.setTextSize(holder.type_tv, 20);
			ViewUtil.setPaddingLeft(holder.type_tv, 5);
			ViewUtil.setMargin(holder.type_tv, 10,
					ViewUtil.RELATIVELAYOUT);

			// 停车场描述图片
			holder.desc_iv = (ImageView) convertView
					.findViewById(R.id.desc_iv);
			ViewUtil.setViewSize(holder.desc_iv, 143, 190);
			ViewUtil.setMargin(holder.desc_iv, 20, ViewUtil.RELATIVELAYOUT);

			// 停车场名称
			holder.name_tv = (TextView) convertView
					.findViewById(R.id.name_tv);
			ViewUtil.setTextSize(holder.name_tv, 30);
			ViewUtil.setMarginTop(holder.name_tv, 22,
					ViewUtil.RELATIVELAYOUT);

			// 停车场地址
			holder.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			ViewUtil.setTextSize(holder.address_tv, 24);
			ViewUtil.setMarginTop(holder.address_tv, 16,
					ViewUtil.RELATIVELAYOUT);

			// 租赁描述
			holder.detail_tv = (TextView) convertView
					.findViewById(R.id.detail_tv);
			ViewUtil.setTextSize(holder.detail_tv, 28);
			ViewUtil.setMarginTop(holder.detail_tv, 16,
					ViewUtil.RELATIVELAYOUT);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LeaseMyInfo entity = list.get(position);

		ImageUtil.loadImage(context, holder.desc_iv, entity.getImg());
		holder.type_tv.setText(entity.getType() == 1 ? "包月" : "计次");
		holder.name_tv.setText(entity.getParkname());
		holder.address_tv.setText(entity.getAddress());
		holder.detail_tv.setText(entity.getDesc());

		return convertView;
	}

}
