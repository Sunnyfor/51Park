package cn.com.unispark.fragment.treasure.lease;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.util.ViewUtil;

public class PeriodView extends FrameLayout {
	/**
	 * 添加默认的选中和不选背景
	 */
	private TextView mTopTextView;
	private TextView mDetailTextView;
	private RelativeLayout period_item_ll;
	private View line_w_view;

	public void setmTopStr(String mTopStr) {
		if (!TextUtils.isEmpty(mTopStr)) {
			this.mTopTextView.setText(mTopStr);
		}
	}

	public void setLineVisibility(boolean isVisibility) {
		this.line_w_view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
	}

	@SuppressWarnings("deprecation")
	public void setmTopBg(int resid) {
		this.mTopTextView.setBackgroundDrawable(getResources().getDrawable(
				resid));
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setIsChecked(boolean isSelected) {
		if (isSelected) {
			period_item_ll.setBackground(getResources().getDrawable(
					R.drawable.line_tab_selected));
		} else {
			period_item_ll.setBackground(new BitmapDrawable());
		}
	}

	public void setmDetailStr(String mDetailStr) {

		if (!TextUtils.isEmpty(mDetailStr)) {
			this.mDetailTextView.setText(mDetailStr);
		}
	}

	public PeriodView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.period_item, this);
		this.period_item_ll = (RelativeLayout) this
				.findViewById(R.id.period_item_ll);
		ViewUtil.setViewSize(period_item_ll, 148, 159);
		this.line_w_view = (View) this.findViewById(R.id.line_w_view);
		ViewUtil.setViewSize(line_w_view, 120, 0);
		this.mTopTextView = (TextView) this.findViewById(R.id.tv_top);
		ViewUtil.setMarginTop(mTopTextView, 25, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setPaddingBottom(mTopTextView, 16);
		ViewUtil.setTextSize(mTopTextView, 24);
		this.mDetailTextView = (TextView) this.findViewById(R.id.tv_detail);
		// ViewUtil.setPaddingBottom(mDetailTextView, 30);
		ViewUtil.setMarginTop(mDetailTextView, -15, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setTextSize(mDetailTextView, 22);
	}
}
