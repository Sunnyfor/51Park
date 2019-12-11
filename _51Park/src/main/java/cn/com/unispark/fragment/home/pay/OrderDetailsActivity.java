package cn.com.unispark.fragment.home.pay;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.entity.OrderDetailsEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明：【订单详情】界面
 * 日期：	2015年3月19日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月17日
 * </pre>
 */
public class OrderDetailsActivity extends BaseActivity {

	// 导航栏标题// 导航栏返回按钮
	private TextView titleText;
	private LinearLayout backRLayout;

	// 实交金额
	private TextView actual_pay_tv;

	// 订单编号
	private TextView order_num_tv;

	// 停车场名称
	private TextView name_tv;

	// 车牌号码
	private TextView plate_tv;

	// 停车时间
	private TextView time_tv;

	// 停车时长
	private TextView long_tv;

	// 停车费用
	private TextView should_pay_tv;

	// 优惠券
	private TextView coupons_tv;

	// 余额支付
	private TextView remain_pay_tv;

	// 支付时间
	private TextView pay_time_tv;

	// 支付方式
	private TextView pay_way_tv;

	// 交易状态
	private TextView state_tv;
	private RelativeLayout test0_rl;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.order_detail_main);

		parseOrderInfo();
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("停车记录详情");

		// 导航栏返回按钮
		backRLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backRLayout.setOnClickListener(this);

		/*
		 * 底部交易状态显示
		 */
		test0_rl = (RelativeLayout) findViewById(R.id.test0_rl);
		test0_rl.setOnClickListener(this);

		// 交易状态
		state_tv = (TextView) findViewById(R.id.state_tv);
		ViewUtil.setViewSize(state_tv, 98, 0);
		ViewUtil.setTextSize(state_tv, 30);

		/*
		 * 停车记录订单详情字段
		 */
		actual_pay_tv = (TextView) findViewById(R.id.actual_pay_tv);
		order_num_tv = (TextView) findViewById(R.id.order_num_tv);
		name_tv = (TextView) findViewById(R.id.name_tv);
		plate_tv = (TextView) findViewById(R.id.plate_tv);
		time_tv = (TextView) findViewById(R.id.time_tv);
		long_tv = (TextView) findViewById(R.id.long_tv);
		should_pay_tv = (TextView) findViewById(R.id.should_pay_tv);
		coupons_tv = (TextView) findViewById(R.id.coupons_tv);
		remain_pay_tv = (TextView) findViewById(R.id.remain_pay_tv);
		pay_time_tv = (TextView) findViewById(R.id.pay_time_tv);
		pay_way_tv = (TextView) findViewById(R.id.pay_way_tv);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.test0_rl:
			finish();
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取订单详情(即停车记录详情)
	 * 日期：	2015年10月27日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseOrderInfo() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("orderno", getIntent().getStringExtra("order_num"));

		LogUtil.showLog(
				3,
				"【 获取订单详情URL】" + Constant.INFO_ORDER_URL
						+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.INFO_ORDER_URL,
				OrderDetailsEntity.class, params,
				new onResult<OrderDetailsEntity>() {

					@Override
					public void onSuccess(OrderDetailsEntity result) {

						// 解析成功，布局和底部交易状态展示
						findViewById(R.id.test0_rl).setVisibility(View.VISIBLE);
						findViewById(R.id.scrollview).setVisibility(
								View.VISIBLE);

						// 实付金额
						actual_pay_tv.setText("￥"
								+ result.getData().getShparkfee());

						// 订单编号
						order_num_tv.setText(result.getData().getOrderno());

						// 停车场名称
						name_tv.setText(result.getData().getParkname());

						// 车牌号码
						plate_tv.setText(result.getData().getCarno()
								.isEmpty() ? "未绑定车牌" : result.getData()
								.getCarno());

						// 停车时间
						time_tv.setText(result.getData().getStarttime()
								+ "-" + result.getData().getEndtime());

						// 停车时长
						long_tv.setText(result.getData().getParklength());

						// 停车费用
						should_pay_tv.setText(result.getData().getParkfee()
								+ "元");

						// 优惠
						coupons_tv.setText(result.getData().getCoupons());

						// 余额支付
						remain_pay_tv.setText(result.getData()
								.getCost_finance());

						// 支付时间
						pay_time_tv.setText(result.getData().getPaytime());

						// 支付方式
						pay_way_tv.setText(result.getData().getPaymethod());

						// 交易状态
						state_tv.setText("1".equals(result.getData()
								.getStatus() + "") ? "支付成功" : "未完成");

						loadingProgress.dismiss();
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
						loadingProgress.dismiss();
					}

				});

	}

	public static String formatParkTime(int parkTime) {
		if (parkTime < 60) {
			return parkTime + "分钟";
		} else if (parkTime < 1440) {
			return parkTime / 60 + "小时" + parkTime % 60 + "分钟";
		} else {
			return parkTime / 1440 + "天" + (parkTime % 1440) / 60 + "小时"
					+ (parkTime % 1440) % 60 + "分钟";
		}
	}

	public static String getPayType(String payCode) {
		if (payCode.equals("0")) {
			return "余额支付";
		} else if (payCode.equals("1")) {
			return "支付宝支付";
		} else if (payCode.equals("2")) {
			return "余额和支付宝支付";
		} else if (payCode.equals("3")) {
			return "微信支付";
		} else {
			return "未知";
		}
	}

}
