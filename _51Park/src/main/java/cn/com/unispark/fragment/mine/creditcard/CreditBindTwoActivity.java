package cn.com.unispark.fragment.mine.creditcard;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.Toast;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.fragment.mine.creditcard.entity.CreditCardFinishEntity;
import cn.com.unispark.fragment.mine.creditcard.entity.CreditCardMessageEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 信用卡绑定第二步
 * 日期：	2015年05月06日
 * 开发者：任建飞
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月25日
 * </pre>
 */
public class CreditBindTwoActivity extends BaseActivity {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 完成绑定按钮
	private Button finish_btn;

	private ClearEditText et_realname;
	private ClearEditText et_identity_card;
	private TextView et_valid_date;
	private ClearEditText et_cnv2;

	private TextView bank_name_tv;
	private TextView card_type_tv;
	private TextView credit_card_tv;
	private TextView timeText;
	private Context mContext;
	private ClearEditText et_phone;
	private ClearEditText vertifycodeEdit;
	// private Dialog CodeDialog;
	private String cardNumberStr;
	private String token, verify_code;
	private boolean isIdentifyingCodeTrue;
	private Intent data;
	private String realnameStr;
	private String identitycardStr;
	private String validdateStr;
	private String cnv2Str;
	private String phoneStr, bank_name;
	private LinearLayout iv_cnv2;
	private LinearLayout iv_valid_date;
	private RelativeLayout credit_card_rl;
	private ImageView credit_bank_iv;
	private ImageView xin_pian_iv;
	private Dialog creditdialog;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.credit_bind_two_main);
	}

	@Override
	public void initView() {
		mContext = CreditBindTwoActivity.this;
		data = new Intent();

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("绑定信用卡");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		/*
		 * 信用卡整体布局
		 */
		credit_card_rl = (RelativeLayout) findViewById(R.id.credit_card_rl);
		ViewUtil.setViewSize(credit_card_rl, 236, 580);
		ViewUtil.setMarginTop(credit_card_rl, 20, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginLeft(credit_card_rl, 30, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(credit_card_rl, 30, ViewUtil.LINEARLAYOUT);

		// 银行图标
		credit_bank_iv = (ImageView) findViewById(R.id.credit_bank_iv);
		ViewUtil.setViewSize(credit_bank_iv, 72, 72);
		ViewUtil.setMarginTop(credit_bank_iv, 22, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(credit_bank_iv, 34, ViewUtil.RELATIVELAYOUT);

		// 银行名称
		bank_name_tv = (TextView) findViewById(R.id.bank_name_tv);
		bank_name = getIntent().getExtras().getString("bank_name");
		bank_name_tv.setText(bank_name);
		ViewUtil.setTextSize(bank_name_tv, 30);
		ViewUtil.setMarginTop(bank_name_tv, 44, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(bank_name_tv, 25, ViewUtil.RELATIVELAYOUT);

		/*
		 * 卡类型标识
		 */
		card_type_tv = (TextView) findViewById(R.id.card_type_tv);
		card_type_tv.setText("信用卡");
		// tv_card_type.setText(getIntent().getExtras().getString("card_type")
		// .equals("3") ? "信用卡" : "未知");
		ViewUtil.setTextSize(card_type_tv, 24);
		ViewUtil.setMarginTop(card_type_tv, 80, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(card_type_tv, 62, ViewUtil.RELATIVELAYOUT);

		// 卡的芯片图标
		xin_pian_iv = (ImageView) findViewById(R.id.xin_pian_iv);
		ViewUtil.setViewSize(xin_pian_iv, 55, 69);
		ViewUtil.setMarginTop(xin_pian_iv, 22, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(xin_pian_iv, 38, ViewUtil.RELATIVELAYOUT);

		/*
		 * 信用卡卡号布局
		 */

		// 卡号第一部分
		ImageView test1_iv = (ImageView) findViewById(R.id.test1_iv);
		ViewUtil.setMarginTop(test1_iv, 19, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(test1_iv, 36, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginBottom(test1_iv, 27, ViewUtil.RELATIVELAYOUT);

		// 卡号第二部分
		ImageView test2_iv = (ImageView) findViewById(R.id.test2_iv);
		ViewUtil.setMarginLeft(test2_iv, 18, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(test2_iv, 18, ViewUtil.RELATIVELAYOUT);

		// 卡号后四位
		credit_card_tv = (TextView) findViewById(R.id.credit_card_tv);
		cardNumberStr = getIntent().getExtras().getString("cardNumber");
		credit_card_tv.setText(cardNumberStr.subSequence(
				cardNumberStr.length() - 4, cardNumberStr.length()));
		ViewUtil.setTextSize(credit_card_tv, 42);
		ViewUtil.setMarginLeft(credit_card_tv, 18, ViewUtil.RELATIVELAYOUT);

		// 、、、、、、、、、、、、、//、、、、、、、、、、、、、、、、、、

		et_realname = (ClearEditText) findViewById(R.id.et_realname);
		et_identity_card = (ClearEditText) findViewById(R.id.et_identity_card);
		et_valid_date = (TextView) findViewById(R.id.et_valid_date);
		et_cnv2 = (ClearEditText) findViewById(R.id.et_cnv2);
		et_phone = (ClearEditText) findViewById(R.id.et_phone);
		vertifycodeEdit = (ClearEditText) findViewById(R.id.vertifycodeEdit);
		timeText = (TextView) findViewById(R.id.timeText);
		finish_btn = (Button) findViewById(R.id.finish_btn);
		finish_btn.setEnabled(false);
		iv_cnv2 = (LinearLayout) findViewById(R.id.iv_cnv2);
		iv_valid_date = (LinearLayout) findViewById(R.id.iv_valid_date);
		initOnClickListener();
	}

	private void initOnClickListener() {
		// backImgView.setOnClickListener(this);
		finish_btn.setOnClickListener(this);
		timeText.setOnClickListener(this);
		et_valid_date.setOnClickListener(this);
		iv_cnv2.setOnClickListener(this);
		iv_valid_date.setOnClickListener(this);

		vertifycodeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (6 == s.length()) {
					finish_btn
							.setBackgroundResource(R.drawable.btn_common_selected);
					finish_btn.setEnabled(true);
				} else {
					finish_btn.setBackgroundResource(R.drawable.btn_common_noselect);
					finish_btn.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
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
		case R.id.timeText:
			// 获取验证码
			if (!isFastDoubleClick()) {
				setObtainButtonEnaled();
			}
			break;
		case R.id.et_valid_date:
			// 选择有效期
			selectValiddate();
			break;
		case R.id.finish_btn:// 完成绑定
			verify_code = vertifycodeEdit.getText().toString().trim();
			parseBindCredit(token, verify_code);
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
		TextView content = (TextView) creditdialog.findViewById(R.id.content);
		ViewUtil.setTextSize(content, 30);
		ViewUtil.setMarginTop(content, 10, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(content, 8, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(content, 8, ViewUtil.RELATIVELAYOUT);
		content.setText(contentStr);
		ImageView line = (ImageView) creditdialog.findViewById(R.id.line);

		ViewUtil.setMarginLeft(line, 20, ViewUtil.RELATIVELAYOUT);

		ViewUtil.setMarginRight(line, 20, ViewUtil.RELATIVELAYOUT);
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

	private void setObtainButtonEnaled() {
		// user_id int yes 该用户在商户系统中的唯一编号,要求是该 编号在商户系统中唯一标识该用户
		// card_no varchar yes 银行卡号
		// acct_name varchar yes 姓名
		// bind_mob varchar yes 绑定手机号
		// vali_date varchar yes 信用卡有效期.格式 YYMM 如:1412
		// cvv2 varchar yes 信用卡时必填
		// id_no varchar yes 身份证号
		realnameStr = et_realname.getText().toString();
		identitycardStr = et_identity_card.getText().toString();
		// validdateStr = et_valid_date.getText().toString();
		cnv2Str = et_cnv2.getText().toString();
		phoneStr = et_phone.getText().toString();
		if (TextUtils.isEmpty(realnameStr)) {
			et_realname.setShakeAnimation();
			Toast.makeText(mContext, "请填写您的真实姓名！", 1).show();
		} else if (TextUtils.isEmpty(identitycardStr)) {
			et_identity_card.setShakeAnimation();
			Toast.makeText(mContext, "请填写您的身份证号！", 1).show();
		} else if (TextUtils.isEmpty(validdateStr)) {
			// et_valid_date.setShakeAnimation();
			et_valid_date.startAnimation(shakeAnimation(5));
			Toast.makeText(mContext, "请填写信用卡的有效期！", 1).show();
		} else if (TextUtils.isEmpty(cnv2Str)) {
			et_cnv2.setShakeAnimation();
			Toast.makeText(mContext, "请填写信用卡背面最后3位数！", 1).show();
		} else if (TextUtils.isEmpty(phoneStr)) {
			et_phone.setShakeAnimation();
			Toast.makeText(mContext, "请填写您的手机号码！", 1).show();
		} else {
			if (ToolUtil.isMobileNumber(phoneStr)) {
				vertifycodeEdit.setFocusable(true);
				vertifycodeEdit.setFocusableInTouchMode(true);
				vertifycodeEdit.requestFocus();
				vertifycodeEdit.findFocus();
				getIdentifyingCode(phoneStr, realnameStr, identitycardStr,
						validdateStr, cnv2Str);
			} else {
				// 注册的提示信息显示
				et_phone.setShakeAnimation();
				Toast.makeText(mContext, "手机号格式错误！", 1).show();
			}
		}
	}

	/**
	 * 用于处理获取按钮的可用性的handler
	 */
	private Handler timetexthandler = new Handler(new Callback() {

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

	private void selectValiddate() {
		final DatePicker datePicker = new DatePicker(mContext);
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
		// 5。0系统以上反射隐藏日view不管用
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
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	/**
	 * <pre>
	 * 功能说明：连连信用卡签约申请[绑卡申请]
	 * 日期：	2015年11月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param phoneStr
	 * @param realnameStr
	 * @param identitycardStr
	 * @param validdateStr
	 * @param cnv2Str
	 * </pre>
	 */
	private void getIdentifyingCode(String phoneStr, String realnameStr,
			String identitycardStr, String validdateStr, String cnv2Str) {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("card_no", cardNumberStr.replace(" ", ""));
		params.put("acct_name", realnameStr);
		params.put("bind_mob", phoneStr);
		params.put("vali_date", validdateStr);
		params.put("cvv2", cnv2Str);
		params.put("id_no", identitycardStr);

		LogUtil.showLog(3, "【连连信用卡签约申请[绑卡申请]URL】" + Constant.LLPAY_BIND_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.LLPAY_BIND_URL,
				CreditCardMessageEntity.class, params,
				new onResult<CreditCardMessageEntity>() {
					@Override
					public void onSuccess(CreditCardMessageEntity result) {
						loadingProgress.dismiss();
						token = result.getData().getToken();
						isIdentifyingCodeTrue = true;
						new Thread(new MyThread()).start();
						// 获取按钮变为不可用
						if (timeText.isEnabled()) {
							timeText.setEnabled(false);
						} else {
							timeText.setEnabled(true);
						}

						ToastUtil.showToast("验证码已发送至您的手机，请查收短信");

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						isIdentifyingCodeTrue = false;
						// 获取验证码的按钮变为可用,并且设置获取按钮上的文字
						timeText.setEnabled(true);
						timeText.setText("重新获取验证码");
						ToastUtil.showToast("验证码获取失败，请重新获取！");
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】连连银行卡签约验证[绑卡操作]
	 * 日期：	2015年11月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param token
	 * @param verify_code
	 * </pre>
	 */
	private void parseBindCredit(String token, String verify_code) {

		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("token", token);
		params.put("verify_code", verify_code);

		httpUtil.parse(httpUtil.POST, Constant.LLPAY_VERIFY_URL,
				CreditCardFinishEntity.class, params,
				new onResult<CreditCardFinishEntity>() {
					@Override
					public void onSuccess(CreditCardFinishEntity result) {
						loadingProgress.dismiss();
						if ("200".equals(result.getCode())) {
							data.putExtra("realnameStr", realnameStr);
							data.putExtra("bank_name", bank_name);
							data.putExtra("cardNumberStr", cardNumberStr);
							CreditBindTwoActivity.this.setResult(20, data);
							CreditBindTwoActivity.this.finish();
							Toast.makeText(mContext, "绑定成功！", Toast.LENGTH_LONG)
									.show();
						} else {
							// isIdentifyingCodeTrue = false;
							// // 获取验证码的按钮变为可用
							// timeText.setEnabled(true);
							// // 并且设置获取按钮上的文字
							// timeText.setText("重新获取验证码");
							Toast.makeText(mContext, "绑定失败！", Toast.LENGTH_LONG)
									.show();
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						// isIdentifyingCodeTrue = false;
						// // 获取验证码的按钮变为可用
						// timeText.setEnabled(true);
						// // 并且设置获取按钮上的文字
						// timeText.setText("重新获取验证码");
						Toast.makeText(mContext, "绑定失败！", Toast.LENGTH_LONG)
								.show();
					}
				});
	}

}
