package cn.com.unispark.fragment.treasure.lease;

import com.umeng.analytics.MobclickAgent;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.util.LogUtil;

/**
 * <pre>
 * 功能说明： 【购买完成】界面
 * 日期：	2015年9月28日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class LeaseResultActivity extends BaseActivity {

	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 支付完成后的提示语以及按钮
	private TextView successText;
	private Button finish_btn;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.lease_result_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("购买完成");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 支付完成后的提示语以及按钮
		successText = (TextView) findViewById(R.id.success_tv);
		finish_btn = (Button) findViewById(R.id.finish_btn);
		finish_btn.setOnClickListener(this);

		LogUtil.showLog(2, "【租赁停车场类型】" + ParkApplication.mLeaseType);
		switch (Integer.valueOf(ParkApplication.mLeaseType).intValue()) {
		case 1:// 包月
			successText.setText("购买完成，您购买的包月服务\n将于"
					+ ParkApplication.monthStartDate + "开始");
			break;
		case 2:// 计次
			successText.setText("购买完成，您购买的计次服务\n将于明日生效");
			break;
		default:
			successText.setText("购买完成");
			break;
		}

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.finish_btn:
			finish();
			MobclickAgent.onEvent(context, "lease_buySuccessBtn_click");
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
