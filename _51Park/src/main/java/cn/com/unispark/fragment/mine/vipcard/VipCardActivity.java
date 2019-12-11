package cn.com.unispark.fragment.mine.vipcard;

import java.util.HashMap;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 无忧会员卡管理类（绑卡、解绑）
 * 日期：	2015年6月9日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月9日
 * </pre>
 */
public class VipCardActivity extends BaseActivity {

	// 导航栏标题以及返回按钮//右侧解除绑定按钮
	private TextView titleText, moreText;
	private LinearLayout backLLayout, moreLLayout;

	// 未绑卡布局//已绑卡布局
	private RelativeLayout unbind_card_rl, bind_card_rl;
	private TextView card_number_tv;

	// 提示文字
	private TextView prompt_tv;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.vipcard_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("停车卡");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 添加会员卡
		moreText = (TextView) findViewById(R.id.moreText);
		moreText.setText("解除绑定");
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);

		/*
		 * 未绑定会员卡布局
		 */
		unbind_card_rl = (RelativeLayout) findViewById(R.id.unbind_card_rl);
		unbind_card_rl.setOnClickListener(this);
		ViewUtil.setViewSize(unbind_card_rl, 342, 600);
		ViewUtil.setMargin(unbind_card_rl, 20, ViewUtil.LINEARLAYOUT);

		// 添加会员卡文字、图标
		TextView add_card_tv = (TextView) findViewById(R.id.add_card_tv);
		ViewUtil.setTextSize(add_card_tv, 36);

		/*
		 * 绑定会员卡布局
		 */
		bind_card_rl = (RelativeLayout) findViewById(R.id.bind_card_rl);
		ViewUtil.setViewSize(bind_card_rl, 342, 600);
		ViewUtil.setMargin(bind_card_rl, 20, ViewUtil.LINEARLAYOUT);

		// 会员卡号
		card_number_tv = (TextView) findViewById(R.id.card_number_tv);
		ViewUtil.setTextSize(card_number_tv, 26);
		ViewUtil.setMargin(card_number_tv, 20, ViewUtil.RELATIVELAYOUT);

		/*
		 * 提示语句
		 */
		prompt_tv = (TextView) findViewById(R.id.prompt_tv);
		ViewUtil.setTextSize(prompt_tv, 24);
		ViewUtil.setMarginLeft(prompt_tv, 20, ViewUtil.LINEARLAYOUT);

		/*
		 * 判断展示绑卡布局还是未绑卡布局、提示语句显示内容
		 */
		if (TextUtils.isEmpty(ParkApplication.getmUserInfo().getCard_no_qr())) {
			initUnbindInfo();
		} else {
			initBindInfo();
		}

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout:// 解绑
			final DialogUtil dialog = new DialogUtil(context);
			dialog.setTitle("确定是否解绑？");
			dialog.setMessage("解绑后该卡将被注销");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					parseUnbindCard();
					MobclickAgent.onEvent(context,
							"wallet_unbindCardConfirm_click");
				}
			});
			dialog.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					MobclickAgent.onEvent(context,
							"wallet_unbindCardCancel_click");
				}
			});
			MobclickAgent.onEvent(context, "wallet_unbindParkingCard_click");
			break;
		case R.id.unbind_card_rl:
			ParkApplication.cardManager = true;
			startActivityForResult(new Intent(context, QrScanActivity.class), 0);
			MobclickAgent.onEvent(context, "wallet_bindingParkingCard_click");
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：初始化未绑卡布局
	 * 日期：	2015年11月29日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void initUnbindInfo() {

		unbind_card_rl.setVisibility(View.VISIBLE);
		bind_card_rl.setVisibility(View.GONE);
		moreText.setVisibility(View.GONE);
		prompt_tv.setText(getResources().getString(
				R.string.hui_yuan_wei_bang_ka));

		// // 将"添加会员卡"由灰色字体替换为蓝色字体
		// String tishiStr = "未绑定会员卡的用户,请单击“添加会员卡”完成会员卡上二维码扫描码自动添加";
		// SpannableStringBuilder style = new SpannableStringBuilder(tishiStr);
		// style.setSpan(new ForegroundColorSpan(Color.parseColor("#089DDF")),
		// 13,
		// 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// prompt_tv.setText(style);
	}

	/**
	 * <pre>
	 * 功能说明：初始化绑卡状态布局
	 * 日期：	2015年11月29日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void initBindInfo() {

		unbind_card_rl.setVisibility(View.GONE);
		bind_card_rl.setVisibility(View.VISIBLE);
		moreText.setVisibility(View.VISIBLE);
		prompt_tv.setText(getResources()
				.getString(R.string.hui_yuan_yi_bang_ka));

		// 卡号
		card_number_tv.setText(ParkApplication.getmUserInfo().getCard_no_qr());

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 20) {
			initBindInfo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		String phone = ShareUtil.getSharedString("phone");
		/**
		 * 获取用户绑卡信息
		 */
		parseGetBindCardInfo(phone);
		super.onResume();
	}

	/**
	 * <pre>
	 * 功能说明： 【解析】获取用户绑卡信息
	 * 日期：	2015年8月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param phone
	 * </pre>
	 */
	private void parseGetBindCardInfo(String phone) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("phone", phone);

		LogUtil.d("【获取用户绑卡信息URL】" + Constant.USER_INFO_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.USER_INFO_URL, UserEntity.class,
				params, new onResult<UserEntity>() {
					@Override
					public void onSuccess(UserEntity result) {
						ParkApplication.getmUserInfo().setCard_no_qr(
								result.getData().getCard_no_qr());
						ParkApplication.getmUserInfo().setBinddate(
								result.getData().getBinddate());
						if (TextUtils.isEmpty(ParkApplication.getmUserInfo()
								.getCard_no_qr())) {
							initUnbindInfo();
						} else {
							initBindInfo();
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.show(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】解绑会员卡
	 * 日期：	2015年8月13日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseUnbindCard() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.d("【解绑会员卡URL】" + Constant.UNBIND_URL + params);

		httpUtil.parseno(httpUtil.POST, Constant.UNBIND_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						loadingProgress.dismiss();

						if (code == httpUtil.SERVER_REQ_OK) {
							initUnbindInfo();
							final DialogUtil mDialog = new DialogUtil(context);
							mDialog.setMessage("解除绑定成功！");
							mDialog.setPositiveButton("确定",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mDialog.dismiss();
										}
									});

						} else {
							final DialogUtil mDialog = new DialogUtil(context);
							mDialog.setMessage("解除绑定失败！");
							mDialog.setPositiveButton("确定",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mDialog.dismiss();
										}
									});
						}
					}
				});
	}

}
