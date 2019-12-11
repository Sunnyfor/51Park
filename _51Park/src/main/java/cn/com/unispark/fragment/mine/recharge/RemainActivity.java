package cn.com.unispark.fragment.mine.recharge;

import java.util.HashMap;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【账户余额】界面
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
public class RemainActivity extends BaseActivity {

	// 导航栏标题 //返回按钮//右侧余额明细按钮
	private TextView titleText, moreText;
	private LinearLayout backLLayout, moreLLayout;

	private TextView remain;
	private Button finish_btn;
	private ImageView remain_icon;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.remain_main);
		initView();
	}

	@Override
	public void initView() {

		// 标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("账户余额");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 余额明细按钮
		moreText = (TextView) findViewById(R.id.moreText);
		moreText.setVisibility(View.VISIBLE);
		moreText.setText("余额明细");
		moreText.setTextColor(getResources().getColor(R.color.black_font));
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);

		remain_icon = (ImageView) findViewById(R.id.remain_icon);
		ViewUtil.setMarginTop(remain_icon, 94, ViewUtil.LINEARLAYOUT);

		remain = (TextView) findViewById(R.id.tv_remain);
		ViewUtil.setTextSize(remain, 72);
		ViewUtil.setMarginTop(remain_icon, 36, ViewUtil.LINEARLAYOUT);

		// 充值按钮
		finish_btn = (Button) findViewById(R.id.finish_btn);
		finish_btn.setOnClickListener(this);
		ViewUtil.setMarginTop(finish_btn, 80, ViewUtil.LINEARLAYOUT);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout:// 余额明细
			ToolUtil.IntentClass(activity, RemainDetailActivity.class, false);
			MobclickAgent.onEvent(context, "Balance_rechargeDetailsBtn_click");
			break;
		case R.id.finish_btn:// 充值按钮
			ToolUtil.IntentClass(activity, RechargeActivity.class, false);
			MobclickAgent.onEvent(context, "Balance_rechargeBtn_click");
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：初始化用户信息
	 * @param phone
	 * </pre>
	 */
	public void loadUserInfo(String phone) {
		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("phone", phone);
		httpUtil.parse(httpUtil.POST, Constant.USER_INFO_URL, UserEntity.class,
				params, new onResult<UserEntity>() {
					@Override
					public void onSuccess(UserEntity result) {
						loadingProgress.dismiss();
						ParkApplication.getmUserInfo().setUserscore(
								result.getData().getUserscore());
						String remainStr = result.getData().getUserscore();
						if (!TextUtils.isEmpty(remainStr)) {
							remain.setText("￥" + remainStr);
						} else {
							remain.setText("￥0.00");
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.show("获取余额失败");
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		loadUserInfo(ParkApplication.getmUserInfo().getUsername());
	}

}
