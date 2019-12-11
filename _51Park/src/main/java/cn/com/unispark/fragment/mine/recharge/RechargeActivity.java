package cn.com.unispark.fragment.mine.recharge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.alipay.AliPay;
import cn.com.unispark.fragment.home.pay.wechatpay.Wxpay;
import cn.com.unispark.fragment.mine.recharge.entity.RechargeConfigEntity;
import cn.com.unispark.fragment.mine.recharge.entity.RechargeConfirmEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【充值】界面---【余额】界面点击充值按钮进入
 * 日期：	2015年1月8日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.4.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年8月12日
 * </pre>
 */
public class RechargeActivity extends BaseActivity {
	public static Activity activity;

	/**
	 * 微信支付的配置参数
	 */
	private Wxpay wxpay;
	/**
	 * 充值金额
	 */
	private String recharge = "0.01";
	private String recodeOrderStr;
	private RelativeLayout layout;
	private int viewCount;
	private List<String> moneyList = new ArrayList<String>();
	private TextView lastBtn;
	@SuppressLint("HandlerLeak")
	private Handler configHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 动态布局
			getRechargeView(viewCount);
		}
	};
	private AliPay aliPay;
	private TextView titleText;
	private LinearLayout backLLayout;
	private Button finish_btn;
	private RadioButton wechat_rbtn;

	private TextView rechageInfoTextView;

	private LinearLayout rechargeLinearLayout;

	private TextView rechargeTypeTextView;

	private RadioButton alipay_rbtn;

	private TextView tishiTextView;

	private RadioGroup radiogroup;

	private LinearLayout rechargeTypeLinearLayout;

	private int lineCount;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.recharge_main);
		activity = this;

		wxpay = new Wxpay(this);
		aliPay = new AliPay(this);

		parseRechargeConfig();
	}

	@Override
	public void initView() {

		// 导航栏标题 “充值”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.chong_zhi));

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 充值金额布局
		rechargeLinearLayout = (LinearLayout) findViewById(R.id.recharge_ll);

		// 请选择充值金额
		rechageInfoTextView = (TextView) findViewById(R.id.tv_rechage_info);
		ViewUtil.setMarginLeft(rechageInfoTextView, 30, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginTop(rechageInfoTextView, 30, ViewUtil.LINEARLAYOUT);
		ViewUtil.setTextSize(rechageInfoTextView, 28);

		// 请选择充值方式
		rechargeTypeLinearLayout = (LinearLayout) findViewById(R.id.recharge_type_ll);
		ViewUtil.setViewSize(rechargeTypeLinearLayout, 70, 0);

		rechargeTypeTextView = (TextView) findViewById(R.id.recharge_type_tv);
		ViewUtil.setViewSize(rechargeTypeTextView, 70, 0);
		ViewUtil.setTextSize(rechargeTypeTextView, 28);

		// 温馨提示
		tishiTextView = (TextView) findViewById(R.id.tishi);
		ViewUtil.setTextSize(tishiTextView, 24);
		ViewUtil.setViewSize(tishiTextView, 80, 0);
		ViewUtil.setMarginLeft(tishiTextView, 30, ViewUtil.LINEARLAYOUT);

		// 选择支付方式
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		ViewUtil.setMarginLeft(radiogroup, 30, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(radiogroup, 20, ViewUtil.LINEARLAYOUT);

		// 微信支付
		wechat_rbtn = (RadioButton) findViewById(R.id.wechat_rbtn);
		ViewUtil.setTextSize(wechat_rbtn, 30);
		ViewUtil.setViewSize(wechat_rbtn, 88, 0);

		// 支付宝支付
		alipay_rbtn = (RadioButton) findViewById(R.id.alipay_rbtn);
		ViewUtil.setTextSize(alipay_rbtn, 30);
		ViewUtil.setViewSize(alipay_rbtn, 88, 0);

		// 立即充值按钮
		finish_btn = (Button) findViewById(R.id.finish_btn);
		finish_btn.setOnClickListener(this);
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.finish_btn: // 立即充值按钮
			parseRechargeConfirm();
			MobclickAgent.onEvent(context, "recharge_successBtn_click");
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：判断是什么支付方式
	 * 日期：	2015年11月18日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void doChoiceRechargeMethod() {
		ParkApplication.isRecharge = true;
		if (wechat_rbtn.isChecked()) {
			// 微信支付
			ParkApplication.mNotifyUrlPage = Constant.WEIXIN_RECHARGE_URL;
			wxpay.pay(ParkApplication.rechargeOrderNum,
					String.valueOf((int) (Float.valueOf(recharge) * 100)),
					2);
			MobclickAgent.onEvent(context, "recharge_WeChatPay_click");
		} else {
			// 支付宝支付
			aliPay.pay(ParkApplication.rechargeOrderNum, Constant.SUBJECT,
					Constant.BODY_RECHARGE, recharge,
					Constant.ALIPAY_RECHARGE_URL);
			MobclickAgent.onEvent(context, "recharge_AlipayPay_click");
		}
	}

	/**
	 * 动态日期布局
	 *
	 */
	private void getRechargeView(int viewCount) {
		layout = (RelativeLayout) findViewById(R.id.regist_mid);
		if (viewCount % 4 == 0) {
			lineCount = viewCount / 4;
		} else {
			lineCount = viewCount / 4 + 1;
		}
		ViewUtil.setViewSize(rechargeLinearLayout,
				285 + (64 * (lineCount - 2)), 0);
		ViewUtil.setViewSize(layout, 64 + (94 * lineCount), 0);
		ViewUtil.setMarginBottom(layout, 42, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginLeft(layout, 28, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(layout, 28, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginTop(layout, 36, ViewUtil.LINEARLAYOUT);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int width = metric.widthPixels; // 屏幕宽度（像素）
		for (int i = 0; i < viewCount; i++) {
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			LinearLayout lLayout = new LinearLayout(context);
			lLayout.setId(i + 10);
			lLayout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lLayout.setLayoutParams(lLayoutlayoutParams);
			final TextView bt = new TextView(context);
			bt.setGravity(Gravity.CENTER);
			bt.setText(moneyList.get(i) + "元");
			if (i == 0) {
				recharge = moneyList.get(i);
				lastBtn = bt;
				bt.setTextColor(ContextCompat.getColor(context,R.color.red_font));
				bt.setBackgroundResource(R.drawable.bg_recharge_selected);
			} else {
				bt.setTextColor(ContextCompat.getColor(context,R.color.black_font66));
				bt.setBackgroundResource(R.drawable.bg_recharge_noselect);
			}
			final int index = i;
			bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					recharge = moneyList.get(index);
					lastBtn.setBackgroundResource(R.drawable.bg_recharge_noselect);
					lastBtn.setTextColor(ContextCompat.getColor(context,R.color.black_font66));
					lastBtn = bt;
					bt.setTextColor(ContextCompat.getColor(context,R.color.red_font));
					bt.setBackgroundResource(R.drawable.bg_recharge_selected);
					Log.e("slx", "recharge" + recharge);
				}
			});
			// 为Button添加长高设置
			LinearLayout.LayoutParams layoutParams_bt = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			bt.setLayoutParams(layoutParams_bt);
			ViewUtil.setViewSize(bt, 64, 120);
			ViewUtil.setTextSize(bt, 30);
			if (i % 4 != 0) {
				ViewUtil.setMarginLeft(bt, 31, ViewUtil.LINEARLAYOUT);
			} else {
				ViewUtil.setMarginLeft(bt, 5, ViewUtil.LINEARLAYOUT);
			}
			lLayout.addView(bt);
			// 每个linearlayout都在前一个的下面，第一个在顶,不处理
			if (i > 0) {
				if (i == 0 || i == 1 || i == 2 || i == 3) {
					if (i % 4 == 0) {
						relativeParams
								.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					} else {
						relativeParams.addRule(RelativeLayout.RIGHT_OF,
								i + 10 - 1);
					}
				} else {
					ViewUtil.setMarginBottom(bt, 42, ViewUtil.LINEARLAYOUT);
					ViewUtil.setMarginTop(bt, 20, ViewUtil.LINEARLAYOUT);
					if (i % 4 == 0) {
						relativeParams
								.addRule(RelativeLayout.BELOW, i + 10 - 4);
						relativeParams
								.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					} else {
						relativeParams
								.addRule(RelativeLayout.BELOW, i + 10 - 4);
						relativeParams.addRule(RelativeLayout.RIGHT_OF,
								i + 10 - 1);
					}
				}
			}
			// 把每个linearlayout加到relativelayout中
			// 添加到主布局
			layout.addView(lLayout, relativeParams);
		}
	}

	/**
	 * <pre>
	 * 功能说明：【解析】①拉取充值配置
	 * 日期：	2015年11月18日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseRechargeConfig() {
		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.showLog(
				3,
				"【充值配置URL】" + Constant.RECHARGE_CONFIG_URL
						+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.RECHARGE_CONFIG_URL,
				RechargeConfigEntity.class, params,
				new onResult<RechargeConfigEntity>() {
					@Override
					public void onSuccess(RechargeConfigEntity result) {
						loadingProgress.dismiss();
						for (int i = 0; i < result.getData().getList().size(); i++) {
							moneyList.add(result.getData().getList().get(i)
									.getMoney());
						}
						viewCount = result.getData().getList().size();
						configHandler.sendEmptyMessage(0);
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
						loadingProgress.dismiss();
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】充值订单确定:②提交给51账单，51返回订单号
	 * 日期：	2015年11月18日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseRechargeConfirm() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("money", recharge);

		LogUtil.d("【充值订单确定URL】" + Constant.RECHARGE_CONFIRM_URL, params);

		httpUtil.parse(httpUtil.POST, Constant.RECHARGE_CONFIRM_URL,
				RechargeConfirmEntity.class, params,
				new onResult<RechargeConfirmEntity>() {
					@Override
					public void onSuccess(RechargeConfirmEntity result) {
						loadingProgress.dismiss();

						recodeOrderStr = result.getData().getOrderno();
						ParkApplication.rechargeOrderNum = recodeOrderStr;
						doChoiceRechargeMethod();
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});
	}

}
