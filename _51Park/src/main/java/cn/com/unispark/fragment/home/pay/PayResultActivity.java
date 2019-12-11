package cn.com.unispark.fragment.home.pay;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.entity.OrderEntity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【停车费支付结果】界面
 * 日期：	2015年11月6日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月6日
 * </pre>
 */
public class PayResultActivity extends BaseActivity {

	// 导航栏标题// 导航栏返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 硬件、软件返回按钮
	private Button back_btn;

	// 软件查看订单信息// 硬件离场提示时间
	private TextView prompt_tv, see_order_tv, leave_time_tv;

	// 硬件出场提示语
	private String exitNote;
	// 停车场类型
	private int parkType;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.payfee_result_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("交费结果");

		// 导航栏左侧返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 软件:停车交费成功（文字）---硬件：支付成功（文字）
		prompt_tv = (TextView) findViewById(R.id.prompt_tv);

		// 感谢您的支持，欢迎下次光临（文字）
		TextView test1_tv = (TextView) findViewById(R.id.test1_tv);
		ViewUtil.setTextSize(test1_tv, 24);
		ViewUtil.setMarginTop(test1_tv, 30, ViewUtil.LINEARLAYOUT);

		// 底部返回按钮
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		// 判断如果是微信支付就将交停车费界面关闭
		if (ParkApplication.mPayParkType == 2) {
			PayFeeActivity.activity.finish();
		}

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.back_btn:
			finish();
			break;
		case R.id.see_order_tv:
			// 停车记录详情，点击也需要进入订单详情界面，所以这里需要将订单号传过去
			onIntentClass(activity, OrderDetailsActivity.class, "order_num",
					ParkApplication.mOrderNum, true);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		parseGetOrderInfo();
	}

	/**
	 * <pre>
	 * 功能说明：展示软件停车场交费结果
	 * 日期：	2015年12月8日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showPaySoftResult() {

		// 停车交费成功（文字）
		prompt_tv.setText(getResources().getString(
				R.string.ting_che_jiao_fei_zhi_fu_cheng_gong));
		ViewUtil.setTextSize(prompt_tv, 36);
		ViewUtil.setMarginTop(prompt_tv, 148, ViewUtil.LINEARLAYOUT);

		// 底部返回按钮
		ViewUtil.setMarginTop(back_btn, 70, ViewUtil.LINEARLAYOUT);

		// 查看订单信息(V5.3去掉，由于后台数据出错，修改比较复杂)
		see_order_tv = (TextView) findViewById(R.id.see_order_tv);
		see_order_tv.setVisibility(View.GONE);
		see_order_tv.setOnClickListener(this);
		ViewUtil.setTextSize(see_order_tv, 30);
		ViewUtil.setMarginTop(see_order_tv, 340, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginBottom(see_order_tv, 34, ViewUtil.LINEARLAYOUT);

	}

	/**
	 * <pre>
	 * 功能说明：展示硬件停车场交费结果
	 * 日期：	2015年12月8日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showPayHardResult() {

		// 支付成功（文字）
		prompt_tv.setText(getResources().getString(R.string.zhi_fu_cheng_gong));
		ViewUtil.setTextSize(prompt_tv, 36);
		ViewUtil.setMarginTop(prompt_tv, 176, ViewUtil.LINEARLAYOUT);

		// 底部完成按钮
		ViewUtil.setMarginTop(back_btn, 300, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginBottom(back_btn, 70, ViewUtil.LINEARLAYOUT);

		// 离场提示时间
		leave_time_tv = (TextView) findViewById(R.id.leave_time_tv);
		leave_time_tv.setVisibility(View.VISIBLE);
		ViewUtil.setTextSize(leave_time_tv, 24);
		ViewUtil.setMarginTop(leave_time_tv, 50, ViewUtil.LINEARLAYOUT);

		// 设置离场提示时间字体颜色和大小
		if (!TextUtils.isEmpty(exitNote)) {
			SpannableStringBuilder style = new SpannableStringBuilder(exitNote);
			style.setSpan(new ForegroundColorSpan(Color.parseColor("#ec4f39")),
					6, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new AbsoluteSizeSpan(ViewUtil.getWidth(30), false),
					6, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			leave_time_tv.setText(style);
		}

	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取出场提示语,来判断是否为硬件交费
	 * 日期：	2015年6月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetOrderInfo() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("orderno", ParkApplication.mOrderNum);

		LogUtil.d("【获取账单来判断是否为硬件交费URL】" + Constant.GET_ORDER_URL, params);

		httpUtil.parse(httpUtil.POST, Constant.GET_ORDER_URL,
				OrderEntity.class, params, new onResult<OrderEntity>() {

					@Override
					public void onSuccess(OrderEntity result) {

						exitNote = "提示："
								+ result.getData().getInfo().getExitnote();

						parkType = result.getData().getInfo().getOrdertype();

						// 判断布局显示位置
						switch (parkType) {
						case 1: // 软件停车场
							showPaySoftResult();
							break;
						case 2: // 硬件停车场
							if (!TextUtils.isEmpty(exitNote)) {
								showPayHardResult();
							} else {
								showPaySoftResult();
							}
							break;
						default:
							showPaySoftResult();
							break;
						}

						parsePayfeeResult();

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
					}
				});
	}

	/**
	 * 
	 * <pre>
	 * 功能说明：【解析】支付成功后的回调
	 * 日期：	2015年11月6日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parsePayfeeResult() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("order_num", ParkApplication.mOrderNum);// 订单编号
		params.put("coupon_id", ParkApplication.mCouponId);// 优惠劵id
		params.put("cost_after", ParkApplication.mCostAfter);// 实付费
		params.put("balance", ParkApplication.mBalance);// 余额交的金额

		LogUtil.d("【 支付成功后回调的URL】" + Constant.RETURN_URL, params);

		httpUtil.parseno(httpUtil.POST, Constant.RETURN_URL, params,
				new onResultTo() {

					@Override
					public void onResult(int code, String msg, String json) {

						loadingProgress.dismiss();

						if (code == httpUtil.SERVER_REQ_OK) {

							// 回调成功，显示交费成功布局
							findViewById(R.id.test0_ll).setVisibility(
									View.VISIBLE);

							// 更新余额
							float allRemain = Float.parseFloat(ParkApplication
									.getmUserInfo().getUserscore());
							float payRemain = Float
									.parseFloat(ParkApplication.mBalance);

							String remain = ReckonUtil.getMoneyFormat(allRemain
									- payRemain);

							// String remain = String.valueOf(ReckonUtil.cut(
							// allRemain, payRemain)).toString();
							ParkApplication.getmUserInfo().setUserscore(remain);

							// 数据清空
							ParkApplication.mCouponId = "";
							ParkApplication.mCostAfter = "0";
							ParkApplication.mBalance = "0";

						} else {
							final DialogUtil mDialog = new DialogUtil(context);
							mDialog.setMessage("未获取到支付信息！");
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