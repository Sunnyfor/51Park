package cn.com.unispark.fragment.mine.creditcard;

import java.util.HashMap;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.fragment.mine.creditcard.entity.CreditCardQueryEntity;
import cn.com.unispark.fragment.mine.creditcard.entity.CreditCardUnBindEntity;
import cn.com.unispark.fragment.mine.creditcard.entity.UserCreditCardEntity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 信用卡绑定第一步
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
public class CreditBindOneActivity extends BaseActivity {
	public boolean xuanfu_creditcard_flag = true;
	private ClearEditText et_cardno;
	private Button bt_next;
	private TextView tv_tishi2;
	private String tStr = "2.自动交费是指在绑定信用卡后，账单生成之后2小时内，如果没有通过其他支付方式，系统将自动从您的信用卡中扣除未支付的金额。";
	// private Dialog loadingProgress;
	private TextView tv_bankname;
	private TextView tv_credit_no;
	// private TextView tv_realname;
	private RelativeLayout rl_isbind;
	private RelativeLayout rl_is_noband;
	private TextView tv_tishi1;
	private View bindview1;
	private View bindview2;
	private LinearLayout ll_ts;
	private TextView rightBtn;
	// private Dialog unbindloadingProgress;
	// private Dialog dialog;
	private LinearLayout iv_credit;
	private Dialog creditdialog;
	private TextView titleText;
	private LinearLayout backLLayout;
	private TextView tvCnText;
	private LoadingProgress unbindProgress;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.credit_bind_one_main);

		unbindProgress = new LoadingProgress(activity, "正在解绑..");

	}

	@Override
	public void initView() {

		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("绑定信用卡");

		tvCnText = (TextView) findViewById(R.id.tv_cn);
		ViewUtil.setTextSize(tvCnText, 30);
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		et_cardno = (ClearEditText) findViewById(R.id.et_cardno);

		ViewUtil.setTextSize(et_cardno, 30);
		bt_next = (Button) findViewById(R.id.bt_next);
		ViewUtil.setTextSize(bt_next, 36);
		bt_next.setEnabled(false);
		tv_tishi2 = (TextView) findViewById(R.id.tv_tishi2);
		ViewUtil.setTextSize(tv_tishi2, 22);
		SpannableStringBuilder style = new SpannableStringBuilder(tStr);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#70B1DF")), 22,
				26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_tishi2.setText(style);
		rl_isbind = (RelativeLayout) findViewById(R.id.rl_isbind);
		tv_bankname = (TextView) findViewById(R.id.tv_bankname);
		tv_credit_no = (TextView) findViewById(R.id.tv_credit_no);
		// tv_realname = (TextView) findViewById(R.id.tv_realname);
		rl_is_noband = (RelativeLayout) findViewById(R.id.rl_is_noband);
		tv_tishi1 = (TextView) findViewById(R.id.tv_tishi1);
		ViewUtil.setTextSize(tv_tishi1, 22);
		bindview1 = (View) findViewById(R.id.bindview1);
		bindview2 = (View) findViewById(R.id.bindview2);
		ll_ts = (LinearLayout) findViewById(R.id.ll_ts);
		ViewUtil.setMarginTop(ll_ts, 10, ViewUtil.LINEARLAYOUT);
		rightBtn = (TextView) findViewById(R.id.moreText);
		rightBtn.setText("解除绑定");
		iv_credit = (LinearLayout) findViewById(R.id.iv_credit);
		rightBtn.setOnClickListener(this);
		iv_credit.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		initOnClickListener();

		parseQueryBindCard();

	}

	@Override
	public void onClickEvent(View view) {

		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreText: // 解除绑定操作
			final DialogUtil dialog = new DialogUtil(context);
			dialog.setMessage("您是否要解除当前绑定的信用卡？");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					parseUnbindCard();
				}
			});
			dialog.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			MobclickAgent.onEvent(context, "credit_unbindBtn_click");
			break;
		case R.id.iv_credit:
			showCreditDialog("信用卡提示", "请确保您要绑定的信用卡真实有效,以成功完成绑定!");
			MobclickAgent.onEvent(context, "credit_hintBtn_click");
			break;
		case R.id.bt_next:
			// 绑定操作
			String cardNumberStr = et_cardno.getText().toString().trim()
					.replace(" ", "");
			parseDoBindNext(cardNumberStr);
			MobclickAgent.onEvent(context, "credit_nextStepBtn_click");
			break;

		}
	}

	private void showCreditDialog(String titleStr, String contentStr) {
	
		creditdialog = new Dialog(this, R.style.Dialog_Fullscreen);
		creditdialog.setContentView(R.layout.credit_dialog_string);
		creditdialog.show();

		RelativeLayout dialog_fl = (RelativeLayout) creditdialog
				.findViewById(R.id.dialog_fl);
		ViewUtil.setViewSize(dialog_fl, 300, 540);

		TextView title = (TextView) creditdialog.findViewById(R.id.title);
		ViewUtil.setViewSize(title, 70, 0);
		ViewUtil.setTextSize(title, 30);
		title.setText(titleStr);
		TextView content = (TextView) creditdialog.findViewById(R.id.content);
		// ViewUtil.setViewSize(title, 200, 0);
		ViewUtil.setTextSize(content, 30);
		ViewUtil.setMarginTop(content, 80, ViewUtil.RELATIVELAYOUT);

		ViewUtil.setMarginLeft(content, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(content, 20, ViewUtil.RELATIVELAYOUT);
		content.setText(contentStr);

		ImageView line = (ImageView) creditdialog.findViewById(R.id.line);

		ViewUtil.setMarginLeft(line, 20, ViewUtil.RELATIVELAYOUT);

		ViewUtil.setMarginRight(line, 20, ViewUtil.RELATIVELAYOUT);

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

	// private void initFirstShowDialog() {
	// dialog = new Dialog(this, R.style.Dialog_Fullscreen);
	// dialog.setContentView(R.layout.credit_bind_dialog);
	// if (ParkApplication.isFirstIn & xuanfu_creditcard_flag) {
	// dialog.show();
	// xuanfu_creditcard_flag = false;
	// } else {
	// if (dialog.isShowing()) {
	// dialog.dismiss();
	// xuanfu_creditcard_flag = false;
	// }
	// }
	// ImageView iv = (ImageView) dialog.findViewById(R.id.iv_close);
	// iv.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (dialog.isShowing()) {
	// dialog.dismiss();
	// }
	// }
	// });
	// }

	/**
	 * <pre>
	 * 功能说明：【解析】连连用户签约信息查询
	 * 日期：	2015年11月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param userId
	 * </pre>
	 */
	private void parseQueryBindCard() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.showLog(3, "【连连用户签约信息查询 URL】" + Constant.LLPAY_QUERY_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.LLPAY_QUERY_URL,
				UserCreditCardEntity.class, params,
				new onResult<UserCreditCardEntity>() {
					@Override
					public void onSuccess(UserCreditCardEntity result) {
						loadingProgress.dismiss();
						if (result.getData().getCount() == 0) {// 没有绑定
							titleText.setText("绑定信用卡");
							rl_is_noband.setVisibility(View.VISIBLE);
							tv_tishi1.setVisibility(View.VISIBLE);
							tv_tishi2.setVisibility(View.VISIBLE);
							bindview1.setVisibility(View.VISIBLE);
							bindview2.setVisibility(View.VISIBLE);
							bt_next.setVisibility(View.VISIBLE);
						} else {
							for (int i = 0; i < result.getData()
									.getAgreement_list().size(); i++) {
								tv_bankname.setText(result.getData()
										.getAgreement_list().get(i)
										.getBank_name());
								String creditNoStr = result.getData()
										.getAgreement_list().get(i)
										.getCard_no();
								tv_credit_no.setText(creditNoStr);
							}
							isBind();
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});
	}

	private void initOnClickListener() {
		/**
		 * 实现输入四位数字自动加一个空格
		 */
		et_cardno.addTextChangedListener(new TextWatcher() {
			int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;
			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				onTextLength = s.length();
				if (onTextLength == 19) {
					bt_next.setBackgroundResource(R.drawable.btn_common_selected);
					bt_next.setEnabled(true);
				} else {
					bt_next.setBackgroundResource(R.drawable.btn_common_noselect);
					bt_next.setEnabled(false);
				}
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3
						|| isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == ' ') {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {
					location = et_cardno.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == ' ') {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						if ((index == 4 || index == 9 || index == 14 || index == 19)) {
							buffer.insert(index, ' ');
							konggeNumberC++;
						}
						index++;
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					et_cardno.setText(str);
					Editable etable = et_cardno.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}
		});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】解除绑定无忧会员卡
	 * 日期：	2015年11月4日
	 * 开发者：	任建飞
	 * 
	 * </pre>
	 */
	private void parseUnbindCard() {

		unbindProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		httpUtil.parse(httpUtil.POST, Constant.LLPAY_UNBIND_URL,
				CreditCardUnBindEntity.class, params,
				new onResult<CreditCardUnBindEntity>() {
					@Override
					public void onSuccess(CreditCardUnBindEntity result) {
						unbindProgress.dismiss();

						ToastUtil.show("解绑成功！");
						finish();

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						unbindProgress.dismiss();

						ToastUtil.show("解绑失败！");
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：获取用户绑卡信息
	 * 日期：	2015年11月4日
	 * 开发者：	任建飞
	 * 
	 * </pre>
	 */
	private void parseDoBindNext(String cardNumber) {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("card_no", cardNumber);

		httpUtil.parse(httpUtil.POST, Constant.LLPAY_BIN_URL,
				CreditCardQueryEntity.class, params,
				new onResult<CreditCardQueryEntity>() {
					@Override
					public void onSuccess(CreditCardQueryEntity result) {

						loadingProgress.dismiss();
						String bank_name = result.getData().getBank_name();
						String card_type = result.getData().getCard_type();
						String bank_code = result.getData().getBank_code();
						if ("3".equals(card_type)) {
							Intent intent = new Intent(context,
									CreditBindTwoActivity.class);

							intent.putExtra("cardNumber", et_cardno.getText()
									.toString().trim());
							intent.putExtra("bank_name", bank_name);
							intent.putExtra("card_type", card_type);

							startActivityForResult(intent, 10);
						} else {
							Toast.makeText(context, "银行卡不是信用卡！",
									Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();

						ToastUtil.show(errMsg);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 20) {
			tv_bankname.setText(data.getStringExtra("bank_name"));
			tv_credit_no.setText(data.getStringExtra("cardNumberStr")
					.subSequence(15, 19));
			// tv_realname.setText(data.getStringExtra("realnameStr"));
			isBind();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void isBind() {
		titleText.setText("信用卡");
		rl_is_noband.setVisibility(View.GONE);
		tv_tishi1.setVisibility(View.GONE);
		tv_tishi2.setVisibility(View.GONE);
		bindview1.setVisibility(View.GONE);
		bindview2.setVisibility(View.GONE);
		bt_next.setVisibility(View.GONE);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setTextSize(12f);
		ll_ts.setVisibility(View.VISIBLE);
		rl_isbind.setVisibility(View.VISIBLE);
	}

}
