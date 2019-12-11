package cn.com.unispark.util;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;

/**
 * <pre>
 * 功能说明： Dialog工具类
 * 日期：	2014年12月1日
 * 开发者：	陈丶泳佐
 * </pre>
 */
public class DialogUtil {
	private AlertDialog dialog;

	// 对话框布局
	private RelativeLayout dialog_rl;

	// 标题// 内容
	private TextView title_tv, content_tv;

	// 确认按钮// 取消按钮
	private Button sureBtn, cancleBtn;
	private String sureStr, cancleStr;

	// 横线//竖线
	private View lineWview, lineHview;

	public DialogUtil(Context context) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = dialog.getWindow();
		window.setContentView(R.layout.common_dialog);

		// 对话框布局
		dialog_rl = (RelativeLayout) window.findViewById(R.id.dialog_rl);
		ViewUtil.setViewSize(dialog_rl, 304, 540);

		// 标题和内容所在的布局
		View test0_rl = window.findViewById(R.id.test0_rl);
		ViewUtil.setViewSize(test0_rl, 216, 540);

		// 标题
		title_tv = (TextView) window.findViewById(R.id.title_tv);
		ViewUtil.setTextSize(title_tv, 32);

		// 内容
		content_tv = (TextView) window.findViewById(R.id.content_tv);
		ViewUtil.setTextSize(content_tv, 32);
		ViewUtil.setMarginTop(content_tv, 10, ViewUtil.RELATIVELAYOUT);

		// 确认按钮
		sureBtn = (Button) window.findViewById(R.id.sure_btn);
		ViewUtil.setTextSize(sureBtn, 32);
		ViewUtil.setViewSize(sureBtn, 88, 0);
		ViewUtil.setPaddingBottom(sureBtn, 5);

		// 取消按钮
		cancleBtn = (Button) window.findViewById(R.id.close_btn);
		ViewUtil.setTextSize(cancleBtn, 32);
		ViewUtil.setViewSize(cancleBtn, 88, 0);
		ViewUtil.setPaddingBottom(cancleBtn, 5);

		// 横线
		lineWview = (View) window.findViewById(R.id.line_view);
		ViewUtil.setViewSize(lineWview, 2, 540);

		// 竖线
		lineHview = (View) window.findViewById(R.id.line0_view);
		ViewUtil.setViewSize(lineHview, 88, 2);

	}

	/**
	 * <pre>
	 * 功能说明：设置主体标题
	 * 日期：	2015年11月29日
	 * 开发者：	任建飞
	 * 
	 * @param content
	 * </pre>
	 */
	public void setTitle(int resId) {
		title_tv.setText(resId);
	}

	/**
	 * <pre>
	 * 功能说明：设置主体标题
	 * 日期：	2015年11月29日
	 * 开发者：	任建飞
	 * 
	 * @param content
	 * </pre>
	 */
	public void setTitle(String title) {
		if (TextUtils.isEmpty(title)) {
			title_tv.setVisibility(View.GONE);
		} else {
			title_tv.setVisibility(View.VISIBLE);
			title_tv.setText(title);
		}
	}

	/**
	 * <pre>
	 * 功能说明：设置主体内容
	 * 日期：	2015年11月29日
	 * 开发者：	任建飞
	 * 
	 * @param content
	 * </pre>
	 */
	public void setMessage(int resId) {
		content_tv.setText(resId);
	}

	/**
	 * <pre>
	 * 功能说明：设置主体内容
	 * 日期：	2015年11月29日
	 * 开发者：	任建飞
	 * 
	 * @param content
	 * </pre>
	 */
	public void setMessage(String content) {
		content_tv.setText(content);
	}

	/**
	 * <pre>
	 * 功能说明：确认按钮
	 * 日期：	2015年11月29日
	 * 开发者：	任建飞
	 * 
	 * @param text	按钮上显示的字
	 * @param listener 点击事件
	 * </pre>
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		sureStr = text;
		if (!TextUtils.isEmpty(text)) {
			sureBtn.setVisibility(View.VISIBLE);
			sureBtn.setText(sureStr);
			sureBtn.setOnClickListener(listener);
			lineWview.setVisibility(View.VISIBLE);

			if (!TextUtils.isEmpty(sureStr) && !TextUtils.isEmpty(cancleStr)) {
				lineHview.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * <pre>
	 * 功能说明：取消按钮
	 * 日期：	2015年11月29日
	 * 开发者：	任建飞
	 * 
	 * @param text	按钮上显示的字
	 * @param listener 点击事件
	 * </pre>
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		cancleStr = text;
		if (!TextUtils.isEmpty(text)) {
			cancleBtn.setVisibility(View.VISIBLE);
			cancleBtn.setText(cancleStr);
			cancleBtn.setOnClickListener(listener);
			lineWview.setVisibility(View.VISIBLE);

			if (!TextUtils.isEmpty(sureStr) && !TextUtils.isEmpty(cancleStr)) {
				lineHview.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}

}
