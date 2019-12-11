package cn.com.unispark.fragment.mine.wallet;

import java.util.HashMap;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.coupons.CouponsFragmentActivity;
import cn.com.unispark.fragment.mine.creditcard.CreditBindOneActivity;
import cn.com.unispark.fragment.mine.recharge.RemainActivity;
import cn.com.unispark.fragment.mine.vipcard.VipCardActivity;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToolUtil;

/**
 * <pre>
 * 功能说明： 【我的钱包】界面
 * 日期：	2015年11月24日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月24日
 * </pre>
 */
public class WalletActivity extends BaseActivity {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	private RelativeLayout remain_rl;
	private RelativeLayout coupons_rl;
	private RelativeLayout vipcard_rl;
	private RelativeLayout credit_rl;
	private TextView remain_tv;
	private TextView credit_tv;
	private TextView vipcard_tv;
	private TextView coupons_tv;
	private ImageView iv_red;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.wallet_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("我的钱包");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 账户余额
		remain_rl = (RelativeLayout) findViewById(R.id.remain_rl);
		remain_rl.setOnClickListener(this);
		remain_tv = (TextView) findViewById(R.id.remain_tv);

		// 停车券
		coupons_rl = (RelativeLayout) findViewById(R.id.coupons_rl);
		coupons_rl.setOnClickListener(this);
		coupons_tv = (TextView) findViewById(R.id.coupons_tv);

		// 停车卡
		vipcard_rl = (RelativeLayout) findViewById(R.id.vipcard_rl);
		vipcard_rl.setOnClickListener(this);
		vipcard_tv = (TextView) findViewById(R.id.vipcard_tv);

		// 信用卡
		credit_rl = (RelativeLayout) findViewById(R.id.credit_rl);
		credit_rl.setOnClickListener(this);
		credit_tv = (TextView) findViewById(R.id.credit_tv);

		iv_red = (ImageView) findViewById(R.id.iv_red);
		iv_red.setVisibility(View.GONE);

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.remain_rl:
			ToolUtil.IntentClass(activity, RemainActivity.class, false);
			MobclickAgent.onEvent(context, "wallet_balanceBtn_click");
			break;
		case R.id.coupons_rl:
			ShareUtil.setSharedBoolean("couponRed", false);
			iv_red.setVisibility(View.GONE);
			ToolUtil.IntentClass(activity, CouponsFragmentActivity.class, false);
			MobclickAgent.onEvent(context, "wallet_rechargeTicket_click");
			break;
		case R.id.vipcard_rl:
			ToolUtil.IntentClass(activity, VipCardActivity.class, false);
			MobclickAgent.onEvent(context, "wallet_rechargeCard_click");
			break;
		case R.id.credit_rl:
			ToolUtil.IntentClass(activity, CreditBindOneActivity.class, false);
			MobclickAgent.onEvent(context, "wallet_credit_click");
			break;
		}

	}

	/**
	 * <pre>
	 * 功能说明：【解析】初始化用户信息
	 * 日期：	2015年8月21日
	 * 开发者：	陈丶泳佐
	 * @param phone
	 * </pre>
	 */
	public void parseInitUserInfo(String phone, String uid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", uid);
		params.put("phone", phone);
		httpUtil.parse(httpUtil.POST, Constant.USER_INFO_URL, UserEntity.class,
				params, new onResult<UserEntity>() {
					@Override
					public void onSuccess(UserEntity result) {
						ParkApplication.setmUserInfo(result.getData());
						// 账户余额
						if (!TextUtils.isEmpty(ParkApplication.getmUserInfo()
								.getUserscore())) {
							remain_tv.setText(ParkApplication.getmUserInfo()
									.getUserscore());
						}
						// 停车卡绑定
						if (!TextUtils.isEmpty(ParkApplication.getmUserInfo()
								.getBinddate())) {
							vipcard_tv.setText("已绑定");
						} else {
							vipcard_tv.setText("未绑定");
						}
						// 信用卡绑定
						if (!TextUtils.isEmpty(ParkApplication.getmUserInfo()
								.getNoagree())) {
							credit_tv.setText("已绑定");
						} else {
							credit_tv.setText("未绑定");
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		// 优惠券数量
		if (!TextUtils.isEmpty(getIntent().getExtras()
				.getString("couponsCount"))) {
			coupons_tv.setText(getIntent().getExtras()
					.getString("couponsCount"));
		}
		if (ShareUtil.getSharedBoolean("couponRed")) {
			iv_red.setVisibility(View.VISIBLE);
		} else {
			iv_red.setVisibility(View.GONE);
		}
		parseInitUserInfo(ParkApplication.getmUserInfo().getUsername(),
				ParkApplication.getmUserInfo().getUid());

	}

}
