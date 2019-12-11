package cn.com.unispark.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.unispark.MainActivity;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.login.entity.MsgEntity;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;

/**
 * <pre>
 * 功能说明： 【一键登录】界面
 * 日期：	2015年3月14日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月10日
 * </pre>
 */
public class LoginActivity extends BaseActivity {

	// 导航栏标题// 返回按钮,点击进入主页
	private TextView titleText;
	private ImageView backImgView;
	private LinearLayout backLLayout;

	// 手机号输入框// 验证码输入框
	private cn.com.unispark.define.ClearEditText phone_et, vertify_et;

	// 倒计时显示
	private TextView time_down_tv;

	// 无忧停车服务协议
	private TextView agreenmentText;

	// 登录按钮
	private Button login_btn;

	// 判断获取验证码的线程是否执行
	private boolean isHandlerExecute;
	// 判断是否为第一次获取验证码
	private boolean isFristGetCode = true;
	// /////////////////////////////////未知
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;

	// 电话语音提示对话框
	private DialogUtil dialog;

	private InputMethodManager inputManager;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			findViewById(R.id.app_image_iv).setVisibility(View.VISIBLE);
			return false;
		}
	});

	@Override
	public void setContentLayout() {
		setContentView(R.layout.login_main);

		registerSMSReceiver();

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("一键登录");

		// 返回按钮,点击进入主页
		backImgView = (ImageView) findViewById(R.id.backImgView);
		backImgView.setImageResource(R.drawable.btn_close_red);
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 无忧停车服务协议
		agreenmentText = (TextView) findViewById(R.id.agreenment_tv);
		agreenmentText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		agreenmentText.setOnClickListener(this);

		// 倒计时显示
		time_down_tv = (TextView) findViewById(R.id.time_down_tv);
		time_down_tv.setOnClickListener(this);
		time_down_tv.setEnabled(false);

		// 手机号输入框
		phone_et = (cn.com.unispark.define.ClearEditText) findViewById(R.id.phone_et);
		phone_et.setOnClickListener(this);
		phone_et.addTextChangedListener(watcher);
		phone_et.setFocusableInTouchMode(true);
		phone_et.requestFocus();

		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// 验证码输入框
		vertify_et = (cn.com.unispark.define.ClearEditText) findViewById(R.id.vertify_et);
		vertify_et.addTextChangedListener(watcher);
		vertify_et.setOnClickListener(this);

		// 登录按钮
		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);
		login_btn.setEnabled(false);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			if(getIntent().getBooleanExtra("imlogin", false)){
				ParkApplication.quitActivity();
				ToolUtil.IntentClass(BaseActivity.activity,MainActivity.class, true);
			}else{
				finish();
			}
			MobclickAgent.onEvent(context, "login_CloseBtn_click");
			break;
		case R.id.phone_et:
			findViewById(R.id.app_image_iv).setVisibility(View.GONE);
			new Thread() {
				public void run() {
					boolean flag = true;
					while (flag) {
						if (!inputManager.isActive()) {
							handler.sendEmptyMessage(0);
						}
					}
				};
			}.start();
			MobclickAgent.onEvent(context, "login_input_click");
			break;
		case R.id.vertify_et:
			findViewById(R.id.app_image_iv).setVisibility(View.GONE);
			MobclickAgent.onEvent(context, "login_captchaInput_click");
			break;
		case R.id.time_down_tv:
			parseGetVertifyCode();
			MobclickAgent.onEvent(context, "login_captchaBtn_click");
			break;
		case R.id.login_btn:
			parseDoLogin();
			MobclickAgent.onEvent(context, "login_LoginBtn_click");
			break;
		case R.id.agreenment_tv:
			// ToolUtil.IntentClass(activity, ReadAgreeActivity.class, false);
			Intent intent = new Intent(context, WebActiveActivity.class);
			intent.putExtra("url", Constant.PARK_SERVER_URL);
			intent.putExtra("title","阅读协议");
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}

	// 监听文本变化
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// 交互：当手机号和验证码均不为空时，登录按钮高亮显示
			if (phone_et.getText().length() == 0
					|| vertify_et.getText().length() == 0) {
				login_btn.setBackgroundResource(R.drawable.btn_common_noselect);
				login_btn.setEnabled(false);

				MobclickAgent.onEvent(context, "login_input_click");
				MobclickAgent.onEvent(context, "login_captchaInput_click");

			} else {
				login_btn.setBackgroundResource(R.drawable.btn_common_selected);
				login_btn.setEnabled(true);
			}

			// 交互：当手机号输入完成时，获取验证码高亮显示
			if (phone_et.getText().length() == 11) {
				time_down_tv.setTextColor(getResources().getColor(
						R.color.red_font));
				time_down_tv.setEnabled(true);
			} else {
				time_down_tv.setTextColor(getResources().getColor(
						R.color.gray_fontbb));
				time_down_tv.setEnabled(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	/**
	 * 用于处理获取按钮的可用性的handler
	 */
	private Handler timetexthandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			int timer = msg.what;
			if (timer == 0) {
				// 重新获取验证码的线程结束
				isHandlerExecute = false;
				time_down_tv.setEnabled(true);
				time_down_tv.setText("重新获取验证码");
			} else {
				if (timer > 0) {
					time_down_tv.setEnabled(false);
					time_down_tv.setText(timer + "s");
				} else {
					time_down_tv.setEnabled(true);
					time_down_tv.setText("重新获取验证码");
				}
			}

			return false;
		}
	});

	/**
	 * <pre>
	 * 功能说明：公用对话框,标题为“提示”,弹窗显示，没有点击按钮
	 * 日期：	2015年6月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param content	主体的内容
	 * </pre>
	 */
	public void showTimeDownDialog(String content) {

		dialog = new DialogUtil(context);
		dialog.setTitle("提示");
		dialog.setMessage(content);

		// View view = View.inflate(this, R.layout.common_dialog, null);
		// TextView titleText = (TextView) view.findViewById(R.id.titleText);
		// titleText.setText("提示");
		// TextView contentText = (TextView)
		// view.findViewById(R.id.contentText);
		// if (content != null) {
		// contentText.setText(content);
		// }
		// View wLineView = (View) view.findViewById(R.id.line_w_view);
		// Button sureBtn = (Button) view.findViewById(R.id.sureBtn);
		// Button cancleBtn = (Button) view.findViewById(R.id.cancleBtn);
		// View hLineView = (View) view.findViewById(R.id.line_h_view);
		// sureBtn.setVisibility(View.GONE);
		// cancleBtn.setVisibility(View.GONE);
		// wLineView.setVisibility(View.GONE);
		// hLineView.setVisibility(View.GONE);
		//
		// dialog.setContentView(view);
		// dialog.show();

	}

	/**
	 * <pre>
	 * 功能说明：【广播】接收短信，并自动填写，需要读取短信的权限
	 * 日期：	2015年6月12日
	 * 
	 * </pre>
	 */
	private void registerSMSReceiver() {
		filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					String message = sms.getMessageBody();
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					if (!TextUtils.isEmpty(from)) {
						if (!TextUtils.isEmpty(patternCode(message))) {
							vertify_et.setText(patternCode(message));
						}
					}
				}
			}

			private String patternCode(String patternContent) {
				if (TextUtils.isEmpty(patternContent)) {
					return null;
				}
				Pattern p = Pattern.compile("(?<!\\d)\\d{4}(?!\\d)");
				Matcher matcher = p.matcher(patternContent);
				if (matcher.find()) {
					return matcher.group();
				}
				return null;
			}
		};
		registerReceiver(smsReceiver, filter);
	}

	/**
	 * <pre>
	 * 功能说明：  隔60s重新发送验证码
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 版本信息：V
	 * 版权声明：版权所有@北京百会易泊科技有限公司
	 * 
	 * 历史记录
	 *    修改内容：
	 *    修改人员：
	 *    修改日期： 2015年10月26日
	 * </pre>
	 */
	private class MyThread implements Runnable {
		private int timerCount = isFristGetCode ? 60 : 120;

		@Override
		public void run() {
			// 是否开始倒计时
			while (isHandlerExecute) {
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
	 * <pre>
	 * 功能说明：【解析】用户登录
	 * 日期：	2015年6月8日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseDoLogin() {

		if (!isMobileNum(phone_et.getText().toString())) {
			phone_et.setShakeAnimation();
			ToastUtil.showToast("手机号格式错误！");
			return;
		}

//		loadingProgress.show();
		Map<String, String> params = new HashMap<>();
		params.put("request", String.valueOf(1));// 请求来源(1:android，2:IOS)
		params.put("phone", phone_et.getText().toString());// 手机号
		params.put("code", vertify_et.getText().toString());// 验证码

		LogUtil.d("【用户登录URL】" + Constant.LOGIN_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.LOGIN_URL, UserEntity.class,
				params, new onResult<UserEntity>() {
					@Override
					public void onSuccess(UserEntity result) {
						loadingProgress.dismiss();

						ParkApplication.setmUserInfo(result.getData());

						setSharedBoolean("islogin", true);
						setSharedString("token", result.getData().getToken());
						setSharedString("uid", result.getData().getUid() + "");
						setSharedString("phone", result.getData().getUsername());
						if(getIntent().getBooleanExtra("imlogin", false)){
							ParkApplication.quitActivity();
							ToolUtil.IntentClass(BaseActivity.activity,MainActivity.class, true);
						}else{
							finish();
						}
						ToastUtil.showToast("登录成功");
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取验证码
	 * 日期：	2015年6月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetVertifyCode() {

		if (!isMobileNum(phone_et.getText().toString())) {
			// 注册的提示信息显示
			phone_et.setShakeAnimation();
			showToast("手机号格式错误！");
			return;
		} else {
			vertify_et.setFocusable(true);
			vertify_et.setFocusableInTouchMode(true);
			vertify_et.requestFocus();
			vertify_et.findFocus();
		}

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone_et.getText().toString());

		LogUtil.showLog(
				3,
				"【短信获取验证码URL】" + Constant.SENDSMS_URL
						+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.SENDSMS_URL, MsgEntity.class,
				params, new onResult<MsgEntity>() {

					@Override
					public void onSuccess(MsgEntity result) {
						loadingProgress.dismiss();
						if ("".equals(result.getData().getNote())) {
							// 发送短信验证码
							ToastUtil.showToast(getResources().getString(
									R.string.msg_already_send));
						} else {
							// 发送语音验证码
							showTimeDownDialog(result.getData().getNote());
							// 设置弹框3秒后消失
							new Thread(new Runnable() {

								@Override
								public void run() {
									SystemClock.sleep(3 * 1000);
									dialog.dismiss();
								}
							}).start();
						}

						// 开启线程
						isHandlerExecute = true;
						new Thread(new MyThread()).start();
						// 获取按钮变为不可用
						if (time_down_tv.isEnabled()) {
							time_down_tv.setEnabled(false);
						} else {
							time_down_tv.setEnabled(true);
						}
						isFristGetCode = false;// 60秒，120秒

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
						if (errCode == httpUtil.SERVER_AUTH_NO) {

						}
					}
				});

	}
}
