package cn.com.unispark.fragment.mine.coupons;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.mine.coupons.fragment.CanBeUsedFragment;
import cn.com.unispark.fragment.mine.coupons.fragment.OutDateFragment;
import cn.com.unispark.fragment.mine.coupons.fragment.UsedFragment;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

/**
 * <pre>
 * 功能说明： 优惠券界面（选择、查看）
 * 日期：	2015年10月29日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月29日
 * </pre>
 */
public class CouponsFragmentActivity extends FragmentActivity implements
		OnClickListener {

	// 导航栏标题// 导航栏返回按钮//右侧兑换码按钮
	private View title_ic;
	private TextView titleText, moreText;
	private LinearLayout backLLayout, moreLLayout;

	/**
	 * 停车券切换
	 */
	private ViewPager mViewPager;
	private static final int TAB_COUNT = 3;// 选项卡总数
	private static final String[] TITLE = new String[] { "未使用", "已过期", "已使用" };
	private TabPageIndicator indicator;

	private Context context;
	private HttpUtil httpUtil;
	private LoadingProgress loadingProgress;

	/**
	 * 兑换码对话框
	 */
	private Dialog dialog;
	private ImageView clear_iv;
	private Button finish_btn;
	private ClearEditText code_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 未继承父类，所以需要去掉头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置默认为竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.coupons_main);
		context = this;
		httpUtil = new HttpUtil(context);
		loadingProgress = new LoadingProgress(context);
		initView();
	}

	private void initView() {
		/*
		 * 标题栏布局
		 */
		title_ic = findViewById(R.id.title_ic);
		ViewUtil.setViewSize(title_ic, 88, 0);

		// 导航栏标题“停车券”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.ting_che_quan));
		ViewUtil.setTextSize(titleText, 34);

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 右侧兑换码按钮
		moreText = (TextView) findViewById(R.id.moreText);
		moreText.setVisibility(View.VISIBLE);
		moreText.setText("兑换码");
		moreText.setTextColor(getResources().getColor(R.color.black_font));
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);
		ViewUtil.setTextSize(moreText, 28);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return TAB_COUNT;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return TITLE[position % TITLE.length];
			}

			@Override
			public Fragment getItem(int index) {
				switch (index) {
				case 0:
					// 未使用
					MobclickAgent.onEvent(context, "parkTicket_unusedBtn_click");
					return new CanBeUsedFragment();
				case 1:
					// 已过期
					MobclickAgent.onEvent(context, "parkTicket_ExpiredBtn_click");
					return new OutDateFragment();
				case 2:
					// 已使用
					MobclickAgent.onEvent(context, "parkTicket_useBtn_click");
					return new UsedFragment();
				}
				return null;
			}
		});
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout:// 兑换码
			showCodeExchangeDialog();
			MobclickAgent.onEvent(context, "parkTicket_JYABtn_click");
			break;
		case R.id.clear_iv://兑换码关闭按钮
			dialog.dismiss();
			MobclickAgent.onEvent(context, "parkTicket_JYACloseBtn_click");
			break;
		case R.id.finish_btn://兑换码确定按钮
			String code = code_et.getText().toString();
			exchange(code, dialog);
			MobclickAgent.onEvent(context, "parkTicket_JYAConfirmBtn_click");
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (ParkApplication.isPay) {
				ParkApplication.isPay = false;
				ParkApplication.isComePayFeeActivityPage = false;
				Intent intent = new Intent(CouponsFragmentActivity.this,
						PayFeeActivity.class);
				startActivity(intent);
				finish();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		loadingProgress.dismiss();
		super.onDestroy();
	}

	/**
	 * <pre>
	 * 功能说明：展示兑换码对话框
	 * 日期：	2015年11月8日
	 * 开发者：	任建飞
	 * 
	 * </pre>
	 */
	private void showCodeExchangeDialog() {

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		final View view = View.inflate(context, R.layout.coupons_dialog, null);
		dialog.setContentView(view);

		/*
		 * 对话框布局
		 */
		LinearLayout test0_ll = (LinearLayout) view.findViewById(R.id.test0_ll);
		ViewUtil.setViewSize(test0_ll, 360, 540);

		// 关闭按钮
		clear_iv = (ImageView) view.findViewById(R.id.clear_iv);
		clear_iv.setOnClickListener(this);
		ViewUtil.setPadding(view, 20);

		// 兑换码（文字）
		TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
		ViewUtil.setTextSize(title_tv, 36);
		// ViewUtil.setMarginTop(title_tv, 40, ViewUtil.LINEARLAYOUT);

		// 兑换码输入框
		code_et = (ClearEditText) view.findViewById(R.id.code_et);
		ViewUtil.setTextSize(code_et, 28);
		ViewUtil.setViewSize(code_et, 80, 460);
		ViewUtil.setPaddingLeft(code_et, 20);
		ViewUtil.setPaddingRight(code_et, 20);
		ViewUtil.setMarginTop(code_et, 30, ViewUtil.LINEARLAYOUT);

		// 兑换按钮
		finish_btn = (Button) view.findViewById(R.id.finish_btn);
		finish_btn.setOnClickListener(this);
		ViewUtil.setTextSize(finish_btn, 32);
		ViewUtil.setViewSize(finish_btn, 70, 264);
		ViewUtil.setMarginTop(finish_btn, 58, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginBottom(finish_btn, 20, ViewUtil.LINEARLAYOUT);

		dialog.show();

	}

	/**
	 * 兑换码
	 * 
	 * @param code
	 *            兑换码
	 * @param dialog
	 *            对话框
	 */
	/**
	 * <pre>
	 * 功能说明：【解析】用户登录
	 * 日期：	2015年6月8日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	protected void exchange(String code, final Dialog dialog) {
		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());// 手机号
		params.put("code", code);// 兑换码
		httpUtil.parseno(httpUtil.POST, Constant.COUPONCODE_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						loadingProgress.hide();
						if (code == httpUtil.SERVER_REQ_OK) {
							ToastUtil.showToast("兑换成功");
							dialog.dismiss();
						} else {
							ToastUtil.showToast(msg);
						}
					}
				});
	}

}