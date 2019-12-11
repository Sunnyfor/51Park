package cn.com.unispark.define;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 首页订单状态自定义布局
 * 日期：	2016年1月11日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年1月11日
 * </pre>
 */
public class HomeStateLayout extends RelativeLayout {
	private View convertView;

	// 订单状态背景
	private RelativeLayout relayout_bg;
	// 小车图标
	private ImageView icon_iv;
	// 订单状态
	private TextView name_tv;

	public HomeStateLayout(Context context) {
		super(context);
		initView(context);
	}

	public HomeStateLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public HomeStateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.home_state, this);

		relayout_bg = (RelativeLayout) findViewById(R.id.relayout_bg);
		ViewUtil.setViewSize(relayout_bg, 35, 112);

		icon_iv = (ImageView) convertView.findViewById(R.id.icon_iv);
		name_tv = (TextView) convertView.findViewById(R.id.name_tv);
		ViewUtil.setTextSize(name_tv, 20);

	}

	public void setChecked(boolean isChecked) {
		if (isChecked) {
			
			relayout_bg.setBackgroundResource(R.drawable.bg_order_selected);

			icon_iv.setVisibility(View.VISIBLE);
			ViewUtil.setMarginRight(icon_iv, 5, ViewUtil.RELATIVELAYOUT);

			name_tv.setTextColor(getResources().getColor(R.color.orange_font));
			ViewUtil.setTextSize(name_tv, 24);
			ViewUtil.setViewSize(relayout_bg, 45, 142);
		} else {
			
			relayout_bg.setBackgroundResource(R.drawable.bg_order_noselect);
			ViewUtil.setViewSize(relayout_bg, 35, 112);

			icon_iv.setVisibility(View.GONE);

			name_tv.setTextColor(getResources().getColor(R.color.gray));
			ViewUtil.setTextSize(name_tv, 20);
		}
	}

	public void setText(String text) {
		name_tv.setText(text);
	}

	public void setViewSize(int height, int width) {
		int mHeight = height == 0 ? LayoutParams.WRAP_CONTENT : height;
		int mWidth = width == 0 ? LayoutParams.WRAP_CONTENT : width;
		LayoutParams layoutParams = new LayoutParams(mWidth, mHeight);
		convertView.setLayoutParams(layoutParams);
	}
}
