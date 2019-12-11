package cn.com.unispark.define;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.com.unispark.R;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 地图中心点自定义组件
 * 日期：	2016年1月15日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年1月15日
 * </pre>
 */
public class MapMarkLayout extends LinearLayout {
	private RelativeLayout dialogLayout;
	private ImageView loading_iv;
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			dialogLayout.setVisibility(View.INVISIBLE);
			return false;
		}
	});

	public MapMarkLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public MapMarkLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MapMarkLayout(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		/*
		 * 对话框布局
		 */
		dialogLayout = new RelativeLayout(context);
		dialogLayout.setVisibility(View.INVISIBLE);
		dialogLayout.setBackgroundResource(R.drawable.bg_marker);

		/*
		 * 加载进度球
		 */
		loading_iv = new ImageView(context);
		loading_iv.setId(R.id.text_1);
		loading_iv.setBackgroundResource(R.drawable.anim_map_mark_loading);
		AnimationDrawable anim = (AnimationDrawable) loading_iv.getBackground();
		RelativeLayout.LayoutParams loadingParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		loadingParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		loadingParams.topMargin = ViewUtil.getHeight(19);
		anim.start();
		dialogLayout.addView(loading_iv, loadingParams);

		addView(dialogLayout);
		ViewUtil.setViewSize(dialogLayout, 64, 110);

		/**
		 * 中心点图标
		 */
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.icon_marker);
		addView(imageView);
		

	}

	/**
	 * <pre>
	 * 功能说明：展示加载框
	 * 日期：	2016年1月15日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	public void showDialog() {
		
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -20);
		anim.setDuration(200);
		
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				dialogLayout.setVisibility(View.VISIBLE);
			}
		});
		
		startAnimation(anim);
		

	}

	/**
	 * <pre>
	 * 功能说明：关闭加载框
	 * 日期：	2016年1月15日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	public void closeDialog() {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(500);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
	}

}
