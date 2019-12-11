package cn.com.unispark.fragment.mine.setting.aboutinfo;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 【关于我们】界面
 * 日期：	2014年12月5日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月5日
 * </pre>
 */
public class AboutInfoActivity extends BaseActivity {

	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 无忧停车图标
	private ImageView app_image_iv;

	// 无忧停车版本
	private TextView vesion_tv;

	// 联系客服
	private RelativeLayout contact_rl;
	
	// 服务须知//推荐给小伙伴
	private TextView server_tv, tuijian_tv;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.about_info_main);

	}

	@Override
	public void initView() {

		// 导航栏标题“关于我们”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.guan_yu_wo_men));

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 无忧停车图标
		app_image_iv = (ImageView) findViewById(R.id.app_image_iv);
		ViewUtil.setMarginTop(app_image_iv, 60, ViewUtil.LINEARLAYOUT);

		// 无忧停车版本
		try {
			vesion_tv = (TextView) findViewById(R.id.vesion_tv);
			vesion_tv
					.setText("无忧停车v"
							+ getPackageManager().getPackageInfo(
									getPackageName(), 0).versionName);
			ViewUtil.setMarginTop(vesion_tv, 10, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginBottom(vesion_tv, 40, ViewUtil.LINEARLAYOUT);
		} catch (NameNotFoundException e) {
			vesion_tv.setText("无忧停车");

		}

		// 服务须知
		server_tv = (TextView) findViewById(R.id.server_tv);
		server_tv.setOnClickListener(this);

		// 联系客服
		contact_rl = (RelativeLayout) findViewById(R.id.contact_rl);
		contact_rl.setOnClickListener(this);

		// 推荐给小伙伴
		tuijian_tv = (TextView) findViewById(R.id.tuijian_tv);
		tuijian_tv.setOnClickListener(this);

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.server_tv://服务须知
//			ToolUtil.IntentClass(this, ServiceAgreeActivity.class, false);
			Intent intent = new Intent(context, WebActiveActivity.class);
			intent.putExtra("url", Constant.PARK_SERVER_URL);
			intent.putExtra("title","阅读协议");
			startActivity(intent);
			MobclickAgent.onEvent(context, "About_serviceBtn_click");
			break;
		case R.id.contact_rl://联系客服
			ToolUtil.IntentPhone(activity, Constant.CONTACT_PHONE);
			MobclickAgent.onEvent(context, "About_contactBtn_click");
			break;
		case R.id.tuijian_tv://推荐给小伙伴
			String msg = "随时随地寻找身边停车位，还支持手机支付停车费！无忧停车，解决您的停车难题！点击下载："
					+ Constant.ABOUT_US_URL;
			Intent shareintent = new Intent(Intent.ACTION_SEND);
			shareintent.setType("text/plain");
			shareintent.putExtra("android.intent.extra.TEXT", msg);
			startActivity(Intent.createChooser(shareintent, "好东西要分享"));
			MobclickAgent.onEvent(context, "About_recommendBtn_click");
			break;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
