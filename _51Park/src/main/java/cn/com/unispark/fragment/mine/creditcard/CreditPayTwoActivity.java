package cn.com.unispark.fragment.mine.creditcard;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 无密支付第二步
 * 日期：	2015年11月12日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月12日
 * </pre>
 */
public class CreditPayTwoActivity extends BaseActivity {

	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 当前点击的View的ID,防止多次重复点击使用
	private int currentViewId;

	private ClearEditText et_realname;
	private ClearEditText et_identity_card;
	private TextView et_valid_date;
	private ClearEditText et_cnv2;
	private Button bt_finish;
	private TextView tv_bank_name;
	private TextView tv_card_type;
	private TextView tv_card_number;
	private TextView timeText;
	private ClearEditText et_phone;

	// 银行卡信息
	private String cardNumberStr;
	private boolean isIdentifyingCodeTrue;
	private String validdateStr;
	private String bank_name, no_order, money_order;
	int card_type;
	private TextView ordernumber;
	private TextView payfee;
	private LinearLayout iv_cnv2;
	private LinearLayout iv_valid_date;
	private Dialog creditdialog;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.credit_pay_two_main);

		// 银行卡信息
		bank_name = getIntent().getStringExtra("bank_name");
		card_type = getIntent().getIntExtra("card_type", 0);
		cardNumberStr = getIntent().getExtras().getString("card_number");

		// 交费金额
		money_order = getIntent().getStringExtra("money");
	}

	@Override
	public void initView() {

		// 导航栏标题以及返回按钮
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("绑定信用卡");
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		tv_bank_name = (TextView) findViewById(R.id.bank_name_tv);
		tv_bank_name.setText(bank_name);

		tv_card_type = (TextView) findViewById(R.id.card_type_tv);
		tv_card_type.setText(card_type == 3 ? "(信用卡)" : "(未知)");

		tv_card_number = (TextView) findViewById(R.id.credit_card_tv);
		tv_card_number.setText(cardNumberStr);

		et_realname = (ClearEditText) findViewById(R.id.et_realname);
		et_identity_card = (ClearEditText) findViewById(R.id.et_identity_card);
		et_valid_date = (TextView) findViewById(R.id.et_valid_date);
		et_cnv2 = (ClearEditText) findViewById(R.id.et_cnv2);
		et_phone = (ClearEditText) findViewById(R.id.et_phone);
		// vertifycodeEdit = (ClearEditText) findViewById(R.id.vertifycodeEdit);
		timeText = (TextView) findViewById(R.id.timeText);
		bt_finish = (Button) findViewById(R.id.bt_finish);
		// bt_finish.setEnabled(false);
		ordernumber = (TextView) findViewById(R.id.ordernumber);
		payfee = (TextView) findViewById(R.id.payfee);
		// no_order = getIntent().getStringExtra("order_num");
		ordernumber.setText(ParkApplication.mOrderNum);
		// money_order = getIntent().getStringExtra("actualpayStr");
		payfee.setText("¥ " + money_order);
		iv_cnv2 = (LinearLayout) findViewById(R.id.iv_cnv2);
		iv_valid_date = (LinearLayout) findViewById(R.id.iv_valid_date);

		iv_cnv2.setOnClickListener(this);
		iv_valid_date.setOnClickListener(this);
		et_valid_date.setOnClickListener(this);
		bt_finish.setOnClickListener(this);
		timeText.setOnClickListener(this);

	}

	@Override
	public void onClickEvent(View v) {
		// 防止按钮多次点击
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.iv_valid_date:

			Bitmap bm1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_credit_valid_date);
			showCreditDialog("有效期提示", "卡正面的月/年，如下图的02/19", bm1);
			break;
		case R.id.iv_cnv2:

			Bitmap bm2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_cnv2_pic);
			showCreditDialog("CVN2提示", "卡背后的三位数，如下图的332", bm2);
			break;
		case R.id.et_valid_date: // 选择有效期
			doSelectValiddate();
			break;
		// case R.id.timeText: // 获取验证码
		// if (!isFastDoubleClick()) {
		// parseLLpayfee();
		// }
		// break;
		case R.id.bt_finish: // 绑定支付操作

			if (!isFastDoubleClick()) {
			parseLLpayfee();
			}
			break;
		}

	}
	private void showCreditDialog(String titleStr, String contentStr,
			Bitmap mBitmap) {
		creditdialog = new Dialog(this, R.style.Dialog_Fullscreen);
		creditdialog.setContentView(R.layout.credit_dialog_image);
		creditdialog.show();

		RelativeLayout dialog_fl = (RelativeLayout) creditdialog
				.findViewById(R.id.dialog_fl);
		ViewUtil.setViewSize(dialog_fl, 416, 540);

		TextView title = (TextView) creditdialog.findViewById(R.id.title);
		ViewUtil.setViewSize(title, 70, 0);
		ViewUtil.setTextSize(title, 30);
		title.setText(titleStr);
		ImageView line = (ImageView) creditdialog.findViewById(R.id.line);

		ViewUtil.setMarginLeft(line, 20, ViewUtil.RELATIVELAYOUT);

		ViewUtil.setMarginRight(line, 20, ViewUtil.RELATIVELAYOUT);
		TextView content = (TextView) creditdialog.findViewById(R.id.content);
		ViewUtil.setTextSize(content, 30);
		ViewUtil.setMarginTop(content, 10, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(content, 8, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(content, 8, ViewUtil.RELATIVELAYOUT);
		content.setText(contentStr);
		ImageView pic = (ImageView) creditdialog.findViewById(R.id.pic);
		pic.setImageBitmap(mBitmap);
		ViewUtil.setMarginTop(pic, 15, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(pic, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(pic, 20, ViewUtil.RELATIVELAYOUT);
		ImageView iv = (ImageView) creditdialog.findViewById(R.id.iv_close);
		ViewUtil.setMarginTop(iv, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(iv, 20, ViewUtil.RELATIVELAYOUT);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (creditdialog.isShowing()) {
					creditdialog.dismiss();
				}
			}
		});
		RelativeLayout rl = (RelativeLayout) creditdialog.findViewById(R.id.rl);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (creditdialog.isShowing()) {
					creditdialog.dismiss();
				}
			}
		});
	}
	/**
	 * <pre>
	 * 功能说明：【解析】连连银行卡支付申请
	 * 日期：	2015年11月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseLLpayfee() {

		loadingProgress.show();

		String name = et_realname.getText().toString();
		String idcard = et_identity_card.getText().toString();
		String cnv2 = et_cnv2.getText().toString();
		String phone = et_phone.getText().toString();

		if (TextUtils.isEmpty(name)) {
			et_realname.setShakeAnimation();
			ToastUtil.showToast("请填写您的真实姓名！");
			return;
		} else if (TextUtils.isEmpty(idcard)) {
			et_identity_card.setShakeAnimation();
			ToastUtil.showToast("请填写您的身份证号！");
			return;
		} else if (TextUtils.isEmpty(validdateStr)) {
			et_valid_date.startAnimation(shakeAnimation(5));
			ToastUtil.showToast("请填写信用卡的有效期！");
			return;
		} else if (TextUtils.isEmpty(cnv2)) {
			et_cnv2.setShakeAnimation();
			ToastUtil.showToast("请填写信用卡背面最后3位数！");
			return;
		} else if (TextUtils.isEmpty(phone)) {
			et_phone.setShakeAnimation();
			ToastUtil.showToast("请填写您的手机号码！");
			return;
		} else if (!ToolUtil.isMobileNumber(phone)) {
			// 注册的提示信息显示
			et_phone.setShakeAnimation();
			ToastUtil.showToast("手机号格式错误！");
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("no_order", ParkApplication.mOrderNum);// 商户系统唯一订单号
		params.put("money_order", money_order);// 交易金额
		params.put("card_no", cardNumberStr.replace(" ", ""));// 银行卡号
		params.put("acct_name", name);// 姓名
		params.put("bind_mob", phone);// 绑定手机号
		Log.e("slx", "validdateStr" + validdateStr);
		params.put("vali_date", validdateStr);// 信用卡有效期.格式 YYMM 如:1412
		params.put("cvv2", cnv2);// 信用卡时必填
		params.put("id_no", idcard);// 身份证号
		// LogUtil.showLog(3, "【连连银行卡支付申请URL】" + Constant.LLPAY_PREPAY_URL
		// + LogUtil.buildUrlParams(params));
		httpUtil.parseno(httpUtil.POST, Constant.LLPAY_PREPAY_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						loadingProgress.dismiss();

						if (code == httpUtil.SERVER_REQ_OK) {
							ToastUtil.showToast("支付成功！");
							ToolUtil.IntentClass(activity,
									PayResultActivity.class, true);
							CreditPayOneActivity.activity.finish();
							PayFeeActivity.activity.finish();
						} else {
							ToastUtil.showToast("支付失败！");
						}
					}
				});

	}

	/**
	 * 隔60s重新发送验证码
	 */
	public class MyThread implements Runnable {
		private int timerCount = 60;

		@Override
		public void run() {
			while (isIdentifyingCodeTrue) {
				try {
					Thread.sleep(1000);// 线程暂停60s
					timerCount--;
					Message message = new Message();
					message.what = timerCount;
					timetexthandler.sendMessage(message);// 發送消息
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 用于处理获取按钮的可用性的handler
	 */
	Handler timetexthandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			// 要做的事情
			int timer = msg.what;
			if (timer == 0) {
				// 重新获取验证码的线程结束
				isIdentifyingCodeTrue = false;
				// 获取验证码的按钮变为可用
				timeText.setEnabled(true);
				// 并且设置获取按钮上的文字
				timeText.setText("重新获取验证码");
			} else {
				if (timer > 0) {
					timeText.setEnabled(false);
					timeText.setText("  重新发送(" + timer + ")");
				} else {
					// 获取验证码的按钮变为可用
					timeText.setEnabled(true);
					// 并且设置获取按钮上的文字
					timeText.setText("重新获取验证码");
				}
			}

			return false;
		}
	});

	private Dialog valid_date_dialog;
	private Dialog cnv2_dialog;

	/**
	 * <pre>
	 * 功能说明：选择有效期
	 * 日期：	2015年11月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void doSelectValiddate() {
		final DatePicker datePicker = new DatePicker(context);
		datePicker.setCalendarViewShown(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4以上
			((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
					.getChildAt(2).setVisibility(View.GONE);
		} else {
			// 通过反射机制，访问private的mDaySpinner成员，并隐藏它。
			try {
				Field daySpinner = datePicker.getClass().getDeclaredField(
						"mDaySpinner");
				daySpinner.setAccessible(true);
				((View) daySpinner.get(datePicker)).setVisibility(View.GONE);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Calendar minCalendar = Calendar.getInstance();
		minCalendar.set(Calendar.HOUR_OF_DAY, 0);
		minCalendar.set(Calendar.MINUTE, 0);
		minCalendar.set(Calendar.SECOND, 0);
		datePicker.setMinDate(minCalendar.getTimeInMillis());
		Calendar maxCalendar = Calendar.getInstance();
		maxCalendar.add(Calendar.YEAR, 100);
		datePicker.setMaxDate(maxCalendar.getTimeInMillis());
		Calendar curCalendar = Calendar.getInstance();
		datePicker.init(curCalendar.get(Calendar.YEAR),
				curCalendar.get(Calendar.MONTH),
				curCalendar.get(Calendar.DAY_OF_MONTH), null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(datePicker);
		builder.setTitle("请选择有效期");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d/%02d", datePicker.getYear(),
						datePicker.getMonth() + 1));
				String datestr = sb.subSequence(2, sb.length()).toString();
				validdateStr = datestr.replace("/", "").trim();
				et_valid_date.setText(datestr);
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
