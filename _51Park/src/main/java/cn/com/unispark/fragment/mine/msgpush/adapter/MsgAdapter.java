package cn.com.unispark.fragment.mine.msgpush.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.msgpush.entity.MsgEntity.DataObject.MsgPushInfo;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 消息中心列表的适配器
 * 日期：	2015年6月23日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月23日
 * </pre>
 */
public class MsgAdapter extends BaseAdapter {

	private Context context;
	private List<MsgPushInfo> list;

	public MsgAdapter(Context context, List<MsgPushInfo> list) {
		this.list = list;
		this.context = context;
	}

	class ViewHolder {
		String id;// 消息id
		ImageView icon_iv;// 子条目图标
		TextView title_tv;// 标题
		TextView content_tv;// 内容
		TextView time_tv;// 发布时间
		ImageView redpoint_iv;// 红点
		ImageView more_iv;// 更多按钮
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.msg_item, null);
			holder = new ViewHolder();

			// 子条目图标
			holder.icon_iv = (ImageView) convertView.findViewById(R.id.icon_iv);
			ViewUtil.setMarginLeft(holder.icon_iv, 20, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginRight(holder.icon_iv, 20, ViewUtil.RELATIVELAYOUT);

			// 标题
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			ViewUtil.setTextSize(holder.title_tv, 30);
			ViewUtil.setMarginTop(holder.title_tv, 26, ViewUtil.LINEARLAYOUT);

			// 内容
			holder.content_tv = (TextView) convertView
					.findViewById(R.id.content_tv);
			ViewUtil.setTextSize(holder.content_tv, 24);
			ViewUtil.setMarginTop(holder.content_tv, 16, ViewUtil.LINEARLAYOUT);

			// 发布时间
			holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
			ViewUtil.setTextSize(holder.time_tv, 20);
			ViewUtil.setMarginTop(holder.time_tv, 16, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginBottom(holder.time_tv, 26, ViewUtil.LINEARLAYOUT);

			// 更多按钮
			holder.more_iv = (ImageView) convertView.findViewById(R.id.more_iv);
			ViewUtil.setMarginLeft(holder.more_iv, 20, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginRight(holder.more_iv, 20, ViewUtil.RELATIVELAYOUT);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MsgPushInfo entity = list.get(position);
		holder.id = entity.getId();
		holder.title_tv.setText(entity.getTitle());
		holder.content_tv.setText(entity.getContent());
		holder.time_tv.setText(entity.getTime() + " 发布");

		if (!ShareUtil.getSharedString(holder.id).equals(holder.id)) {
			// 消息未读过
			holder.title_tv.setTextColor(context.getResources().getColor(
					R.color.black_font));
			holder.content_tv.setTextColor(context.getResources().getColor(
					R.color.gray_font));
			holder.time_tv.setTextColor(context.getResources().getColor(
					R.color.gray_font));
		} else {
			// 消息已读过
			holder.title_tv.setTextColor(context.getResources().getColor(
					R.color.gray_fontbb));
			holder.content_tv.setTextColor(context.getResources().getColor(
					R.color.gray_fontbb));
			holder.time_tv.setTextColor(context.getResources().getColor(
					R.color.gray_fontbb));

		}

		return convertView;
	}

}
