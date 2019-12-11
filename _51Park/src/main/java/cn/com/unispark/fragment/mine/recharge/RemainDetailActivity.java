package cn.com.unispark.fragment.mine.recharge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.recharge.childfrag.RechargeRecordFragment;
import cn.com.unispark.util.ViewUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 【余额明细】界面
 * 日期：	2015年7月29日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年8月12日
 * </pre>
 */
public class RemainDetailActivity extends FragmentActivity implements
		OnClickListener {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;
	// 余额明细// 充值记录
	private Context context;
	private ViewPager mViewPager;

	private static final int TAB_COUNT = 1;// 选项卡总数
	private static final String[] TITLE = new String[] { "余额消费" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.remain_detail_main);
		context = this;
		initView();
	}
	public void initView() {
		/*
		 * 标题栏布局
		 */
		View title_ic = findViewById(R.id.title_ic);
		ViewUtil.setViewSize(title_ic, 88, 0);
		// 导航栏标题 “余额明细”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.yu_e_ming_xi));
		ViewUtil.setTextSize(titleText, 34);

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return TAB_COUNT;
			}

//			@Override
//			public CharSequence getPageTitle(int position) {
//				return TITLE[position % TITLE.length];
//			}

			@Override
			public Fragment getItem(int index) {
				switch (index) {
				case 0:
					// 未使用
					MobclickAgent.onEvent(context,
							"balance_BalanceConsume_click");
					return new RechargeRecordFragment();
//				case 1:
//					// 已过期
//					MobclickAgent.onEvent(context,
//							"Balance_rechargeRecord_click");
//					return new RechargeRecordFragment();
				}
				return null;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}

}