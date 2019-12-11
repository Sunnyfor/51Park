package cn.com.unispark.fragment.mine.setting.offlinemap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.util.ViewUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 离线地图下载管理
 * 日期：	2016年3月25日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年3月25日
 * </pre>
 */
public class DownLoadManagerActivity extends FragmentActivity implements
		OnClickListener {

	protected static final int TAB_COUNT = 1;
	private ViewPager mViewPager;
	private Context context;
	private LinearLayout backLLayout;
	private TextView titleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.download_main);
		initView();
	}

	private void initView() {
		context = this;
		View title_ic = findViewById(R.id.title_ic);
		ViewUtil.setViewSize(title_ic, 88, 0);
		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.li_xian_di_tu));
//		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
//		// ViewUtil.setViewSize(radiogroup, 60, 250);
//		left_rb = (RadioButton) findViewById(R.id.left_rb);
//		ViewUtil.setViewSize(left_rb, 60, 150);
//		left_rb.setText("下载管理");
//		right_rb = (RadioButton) findViewById(R.id.right_rb);
//		ViewUtil.setViewSize(right_rb, 60, 150);
//		right_rb.setText("城市列表");
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return TAB_COUNT;
			}

			@Override
			public Fragment getItem(int index) {
				switch (index) {
//				 case 0:
//				 MobclickAgent.onEvent(context, "DownLoadFragment_click");
//				 return new CityFragment();
				case 0:
					MobclickAgent.onEvent(context, "DownLoadFragment_click");
					return new DownLoadFragment();
				}
				return null;
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
//				if (arg0 == 0) {
//					left_rb.setChecked(true);
//				} else {
//					right_rb.setChecked(true);
//				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 切换viewpagerItem
//		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				if (left_rb.getId() == checkedId) {
//					mViewPager.setCurrentItem(0);
//					MobclickAgent.onEvent(context, "DownLoadFragment_click");
//				}
//				if (right_rb.getId() == checkedId) {
//					mViewPager.setCurrentItem(1);
//					MobclickAgent.onEvent(context, "DownLoadFragment_click");
//				}
//			}
//		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}

}
