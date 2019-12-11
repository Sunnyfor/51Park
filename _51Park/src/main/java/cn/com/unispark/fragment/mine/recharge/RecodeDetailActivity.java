package cn.com.unispark.fragment.mine.recharge;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.recharge.entity.RecordDetailEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【消费充值明细】界面
 * 日期：	2016年1月6日
 * 开发者：	任建飞
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * </pre>
 */
public class RecodeDetailActivity extends BaseActivity {

	// 导航栏标题 //返回按钮//右侧余额明细按钮
	private TextView titleText, moreText;
	private LinearLayout backLLayout, moreLLayout;

	private TextView remain;
	private Button finish_btn;
	private ImageView remain_icon;
	private TextView tv_typename;
	private TextView tv_jine;
	private TextView tv_order_type;
	private TextView tv_order_pay_type;
	private TextView tv_order_time;
	private TextView tv_order_num_tv;
	private TextView tv_order_detail;
	private String orderno;
	private String balancetype;
	private RelativeLayout top_rl;
	private LinearLayout first_ll;
	private TextView tv_order_type_left;
	private TextView tv_order_pay_type_left;
	private TextView tv_order_time_left;
	private TextView tv_order_num_tv_left;
	private TextView tv_order_detail_left;
	private CheckBox isread_checkbox;
	private LinearLayout isread_ll;
	private TextView tv_rmb;
	private LinearLayout top_ll;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.recharge_recode_detail_main);
		initView();
	}

	@Override
	public void initView() {
		orderno = getIntent().getStringExtra("orderno");
		balancetype = getIntent().getStringExtra("balancetype");
		Log.e("rjf", "orderno" + orderno);
		Log.e("rjf", "balancetype" + balancetype);

		top_rl = (RelativeLayout) findViewById(R.id.top_rl);
		ViewUtil.setViewSize(top_rl, 160, 0, ViewUtil.LINEARLAYOUT);

		// 标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("余额明细");
		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		tv_typename = (TextView) findViewById(R.id.tv_typename);
		ViewUtil.setMargin(tv_typename, 54, 0, 0, 30, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setTextSize(tv_typename, 30);

		top_ll = (LinearLayout) findViewById(R.id.top_ll);
		ViewUtil.setMargin(top_ll, 66, 40, 0, 0, ViewUtil.RELATIVELAYOUT);
		tv_jine = (TextView) findViewById(R.id.tv_jine);
		ViewUtil.setTextSize(tv_jine, 48);

		tv_rmb = (TextView) findViewById(R.id.tv_rmb);
		ViewUtil.setTextSize(tv_rmb, 30);

		ViewUtil.setMargin(findViewById(R.id.first_ll), 54, 30, 0, 30,
				ViewUtil.LINEARLAYOUT);
//		ViewUtil.setViewSize(findViewById(R.id.first_ll), 50, 0);
		
		ViewUtil.setMargin(findViewById(R.id.ll_order_pay_type), 28, 30, 0, 30,
				ViewUtil.LINEARLAYOUT);
//		ViewUtil.setViewSize(findViewById(R.id.ll_order_pay_type), 50, 0);
		
		ViewUtil.setMargin(findViewById(R.id.three_ll), 28, 30, 0, 30,
				ViewUtil.LINEARLAYOUT);
//		ViewUtil.setViewSize(findViewById(R.id.three_ll), 50, 0);
		
		ViewUtil.setMargin(findViewById(R.id.four_ll), 28, 30, 0, 30,
				ViewUtil.LINEARLAYOUT);
//		ViewUtil.setViewSize(findViewById(R.id.four_ll), 50, 0);
		
		ViewUtil.setMargin(findViewById(R.id.five_ll), 28, 30, 0, 30,
				ViewUtil.LINEARLAYOUT);
//		ViewUtil.setViewSize(findViewById(R.id.five_ll), 50, 0);

		tv_order_type = (TextView) findViewById(R.id.tv_order_type);
		ViewUtil.setTextSize(tv_order_type, 24);
		tv_order_pay_type = (TextView) findViewById(R.id.tv_order_pay_type);
		ViewUtil.setTextSize(tv_order_pay_type, 24);
		tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		ViewUtil.setTextSize(tv_order_time, 24);
		tv_order_num_tv = (TextView) findViewById(R.id.tv_order_num_tv);
		ViewUtil.setTextSize(tv_order_num_tv, 24);
		tv_order_detail = (TextView) findViewById(R.id.tv_order_detail);
		ViewUtil.setTextSize(tv_order_detail, 24);

		tv_order_type_left = (TextView) findViewById(R.id.tv_order_type_left);
		ViewUtil.setTextSize(tv_order_type_left, 30);
		tv_order_pay_type_left = (TextView) findViewById(R.id.tv_order_pay_type_left);
		ViewUtil.setTextSize(tv_order_pay_type_left, 30);
		tv_order_time_left = (TextView) findViewById(R.id.tv_order_time_left);
		ViewUtil.setTextSize(tv_order_time_left, 30);
		tv_order_num_tv_left = (TextView) findViewById(R.id.tv_order_num_tv_left);
		ViewUtil.setTextSize(tv_order_num_tv_left, 30);
		tv_order_detail_left = (TextView) findViewById(R.id.tv_order_detail_left);
		ViewUtil.setTextSize(tv_order_detail_left, 30);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：初始化用户信息
	 * @param phone
	 * </pre>
	 */
	public void loadDetailInfo() {
		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("orderno", orderno);
		params.put("balancetype", balancetype);
		httpUtil.parse(httpUtil.POST, Constant.CONSUM_BCINFO_URL,
				RecordDetailEntity.class, params,
				new onResult<RecordDetailEntity>() {
					@Override
					public void onSuccess(RecordDetailEntity result) {
						loadingProgress.dismiss();
						tv_jine.setText(result.getData().getMoney());
						tv_order_type.setText(result.getData().getTypenote());
						if ("2".equals(balancetype)) {
							tv_typename.setText("入账金额");
							tv_order_pay_type.setText(result.getData()
									.getWaynum());
						} else {
							tv_typename.setText("出账金额");
							findViewById(R.id.ll_order_pay_type).setVisibility(
									View.GONE);
						}
						tv_order_time.setText(result.getData().getTime());
						tv_order_num_tv.setText(result.getData().getOrderno());
						tv_order_detail.setText(result.getData().getRemarks());
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.show("获取失败");
					}
				});
	}

	protected String getType(int waynum) {
		// TODO Auto-generated method stub
		String type = "";
		switch (waynum) {
		// 充值方式（1；支付宝充值，2：微信充值3：活动返现）
		case 1:
			type = "支付宝充值";
			break;
		case 2:
			type = "微信充值";
			break;
		case 3:
			type = "活动返现";
			break;
		}
		return type;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadDetailInfo();
	}

}
