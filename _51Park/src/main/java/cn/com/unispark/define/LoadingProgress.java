package cn.com.unispark.define;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.unispark.R;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 自定义无忧停车动画加载进度的对话框
 * 日期：	2015年2月2日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月10日
 * </pre>
 */
public class LoadingProgress extends ProgressDialog {

	private String loadingTip;// 加载文字（传值）

	// 加载框
	private LinearLayout loading_ll;
	private TextView loading_tv;
	private ImageView loading_iv;

	// 加载动画
	private AnimationDrawable animation;

	/**
	 * 自定义无忧停车动画加载进度的对话框
	 * 
	 * @param context
	 *            上下文对象
	 */
	public LoadingProgress(Context context) {
		super(context);
		this.loadingTip = "正在加载...";
		setCanceledOnTouchOutside(true);
	}

	/**
	 * 自定义无忧停车动画加载进度的对话框
	 * 
	 * @param context
	 *            上下文对象
	 * @param content
	 *            提示加载的内容
	 */
	public LoadingProgress(Context context, String content) {
		super(context);
		this.loadingTip = content;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		setContentView(R.layout.loading_progress_main);

		/*
		 * 加载框布局
		 */
		loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
		ViewUtil.setViewSize(loading_ll, 208, 290);

		// 加载提示文字
		loading_tv = (TextView) findViewById(R.id.loading_tv);
		ViewUtil.setTextSize(loading_tv, 30);
		ViewUtil.setMarginTop(loading_tv, 15, ViewUtil.LINEARLAYOUT);

		// 加载动画展示
		loading_iv = (ImageView) findViewById(R.id.loading_iv);
		loading_iv.setFocusable(false);
		ViewUtil.setViewSize(loading_iv, 93, 290);
		ViewUtil.setMarginTop(loading_iv, 40, ViewUtil.LINEARLAYOUT);

	}

	private void initData() {
		loading_iv.setBackgroundResource(R.drawable.anim_loading_51park);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		animation = (AnimationDrawable) loading_iv.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		loading_iv.post(new Runnable() {
			@Override
			public void run() {
				animation.start();
			}
		});
		loading_tv.setText(loadingTip);
	}

	public void setContent(String str) {
		loading_tv.setText(str);
	}

}
