package com.uubee.prepay.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uubee.prepay.core.PrePayImpl;
import cn.com.unispark.R;
import com.uubee.prepay.model.CashGift;
import com.uubee.prepay.model.CreateOrderResponse;
import com.uubee.prepay.model.PayResult;
import com.uubee.prepay.util.Utils;
import com.uubee.prepay.view.AnimatorProxy;
import com.uubee.prepay.view.CircleButton;
import com.uubee.prepay.view.SoftCloseEditText;
import com.uubee.prepay.view.SoftCloseEditText.SoftCloseListener;
import com.uubee.prepay.view.TextWatcherAdapter;

public class FirstPayActivity extends BasePayActivity {
	private View vDotLine;
	private View vPayLayout;
	private ValueAnimator animatorDetailLayout;
	private CircleButton mActiveView;
	private float mDetailViewHeight;
	private View dialogVerifyPhone;
	private View dialogVerifyName;
	private View dialogVerifyCode;
	private View dialogActivate;
	private View backCard;

	public FirstPayActivity() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.uubee_first_pay);
		this.backCard = this.findViewById(R.id.credit_account_layout);
		this.mDetailViewHeight = (float) this.getResources()
				.getDimensionPixelSize(R.dimen.pay_detail_view_height);
		this.initViaResponse();
	}

	private void initViaResponse() {
		if (this.mCreateOrderResponse.user_status == 0) {
			this.backCard.setVisibility(0);
			this.initViewNormal();
		} else if (this.mCreateOrderResponse.user_status == 2) {
			this.showDialogVerifyPhone();
		} else if ("1".equals(this.mCreateOrderResponse.need_real)) {
			this.showDialogVerifyName();
		} else {
			this.showDialogActivate();
		}

	}

	private void initViewNormal() {
		this.mPayButton = (Button) this.findViewById(R.id.btn_pay);
		this.mPayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (FirstPayActivity.this.mPassType != 1
						&& TextUtils
								.isEmpty(FirstPayActivity.this.mCreateOrderResponse.token)) {
					FirstPayActivity.this.showDialogVerifyCode();
				} else {
					FirstPayActivity.this.executePay();
				}
			}
		});
		if (this.mPassType == 1) {
			TextView vArrow = (TextView) this.findViewById(R.id.tv_dialog_top);
			vArrow.setText(R.string.pay_no_pass_tip_text);
			this.showTopTip(vArrow);
		}

		this.vPayLayout = this.findViewById(R.id.pay_layout);
		this.tvGiftNumber = (TextView) this.findViewById(R.id.cash_ticker_select);
		this.vGift = this.tvGiftNumber;
		this.vDotLine = this.findViewById(R.id.dot_line);
		if (this.canAnimator()) {
			this.vDotLine.setVisibility(0);
			this.vDotLine.setAlpha(0.0F);
		}

		final View vArrow1 = this.findViewById(R.id.pay_detail_open_view);
		this.showDownUpAnim(vArrow1);
		TextView mCompanyView = (TextView) this
				.findViewById(R.id.payee_company_content);
		mCompanyView.setText(this.mCreateOrderResponse.name_trader);
		TextView tvAccount = (TextView) this.findViewById(R.id.credit_account_id);
		StringBuilder sb = new StringBuilder(this.mCreateOrderResponse.mob_user);
		sb.insert(11, " ").insert(7, " ").insert(3, " ");
		tvAccount.setText(sb);
		this.setRepayDate(this.mCreateOrderResponse.date_repay);
		String balance = this.getString(R.string.pay_money_unit_text) + " "
				+ this.mCreateOrderResponse.acct_balance;
		TextView tvBalance = (TextView) this.findViewById(R.id.tv_balance);
		tvBalance.setText(this.getBigSizeMoney(2, balance));
		TextView tvPayMoney = (TextView) this.findViewById(R.id.pay_money_amount);
		this.setPayMoney(tvPayMoney, this.mPayOrder.optString("money_order"));
		TextView mProductView = (TextView) this
				.findViewById(R.id.product_name_content);

		try {
			mProductView.setText(URLDecoder.decode(
					this.mPayOrder.optString("name_goods"), "utf-8"));
		} catch (UnsupportedEncodingException var18) {
			var18.printStackTrace();
		}

		TextView mTranscodeView = (TextView) this
				.findViewById(R.id.transaction_id_content);
		mTranscodeView.setText(this.mPayOrder.optString("no_order"));
		TextView tvMarketing = (TextView) this.findViewById(R.id.tv_marketing);
		tvMarketing.setText(this.mCreateOrderResponse.word_marketing);
		final View mDetailView = this.findViewById(R.id.pay_detail_view);
		View detailLayout = this.findViewById(R.id.pay_detail_open_layout);
		RelativeLayout payMoneyLayout = (RelativeLayout) this
				.findViewById(R.id.pay_money_layout);
		OnClickListener toggleListener = new OnClickListener() {
			public void onClick(View v) {
				FirstPayActivity.this.toggleDetailLayout(vArrow1, mDetailView);
			}
		};
		payMoneyLayout.setOnClickListener(toggleListener);
		detailLayout.setOnClickListener(toggleListener);
		mDetailView.setOnClickListener(toggleListener);
		TextView attentView = (TextView) this
				.findViewById(R.id.attention_prepay_view);
		SpannableString attentStr = new SpannableString(
				this.getString(R.string.attention_prepay));
		attentStr.setSpan(new ForegroundColorSpan(-168632), 5, 11, 33);
		attentView.setText(attentStr);
		this.setCashGiftView();
		TextView tvContactUs = (TextView) this
				.findViewById(R.id.customer_service_view);
		if (!TextUtils.isEmpty(this.mCreateOrderResponse.word_contact_us)) {
			tvContactUs.setText(this.mCreateOrderResponse.word_contact_us);
		} else {
			tvContactUs.setText(R.string.word_contact_us);
		}

		if (this.needAuth) {
			this.showInfoSupplyPage();
		}

	}

	private void showTopTip(View tvTopTip) {
		tvTopTip.setVisibility(0);
		if (this.canAnimator()) {
			float height = (float) this.getResources().getDimensionPixelSize(
					R.dimen.pay_top_tip_height);
			ObjectAnimator translationY = ObjectAnimator.ofFloat(tvTopTip,
					"translationY", new float[] { -height, 0.0F });
			translationY.setDuration(1000L).start();
		}

	}

	private ValueAnimator getAnimatorDetailLayout(final View vArrow) {
		if (this.animatorDetailLayout != null) {
			return this.animatorDetailLayout;
		} else {
			this.animatorDetailLayout = ValueAnimator.ofFloat(new float[] {
					0.0F, this.mDetailViewHeight });
			this.animatorDetailLayout
					.addUpdateListener(new AnimatorUpdateListener() {
						public void onAnimationUpdate(ValueAnimator animation) {
							float value = ((Float) animation.getAnimatedValue())
									.floatValue();
							float alpha = value
									/ FirstPayActivity.this.mDetailViewHeight;
							FirstPayActivity.this.vDotLine.setAlpha(alpha);
							vArrow.setRotation(alpha * 180.0F);
							FirstPayActivity.this.vPayLayout
									.setTranslationY(value);
						}
					});
			return this.animatorDetailLayout;
		}
	}

	private void toggleDetailLayout(View vArrow, View mDetailView) {
		if (this.canAnimator()) {
			if ((double) this.vDotLine.getAlpha() > 0.5D) {
				this.getAnimatorDetailLayout(vArrow).reverse();
				mDetailView.setEnabled(false);
			} else {
				this.getAnimatorDetailLayout(vArrow).start();
				mDetailView.setEnabled(true);
			}
		} else if (this.vDotLine.isShown()) {
			this.vDotLine.setVisibility(8);
			AnimatorProxy.wrap(this.vPayLayout).setTranslationY(0.0F);
			AnimatorProxy.wrap(vArrow).setRotation(0.0F);
			mDetailView.setEnabled(false);
		} else {
			this.vDotLine.setVisibility(0);
			AnimatorProxy.wrap(this.vPayLayout).setTranslationY(
					this.mDetailViewHeight);
			AnimatorProxy.wrap(vArrow).setRotation(180.0F);
			mDetailView.setEnabled(true);
		}

	}

	private void showDownUpAnim(View vArrow) {
		if (this.canAnimator()) {
			float mDownUpRange = this.getResources().getDimension(
					R.dimen.down_up_view_range);
			ObjectAnimator animator = ObjectAnimator.ofFloat(vArrow,
					"translationY", new float[] { 0.0F, mDownUpRange / 2.0F,
							0.0F, -mDownUpRange / 2.0F, 0.0F });
			animator.setRepeatCount(-1);
			animator.setDuration(1500L);
			animator.start();
		}
	}

	private void showMarketingWord(View view) {
		TextView tvTopTip;
		if (!TextUtils.isEmpty(this.mCreateOrderResponse.word_active_above)) {
			tvTopTip = (TextView) view.findViewById(R.id.tv_dialog_above);
			tvTopTip.setText(this.mCreateOrderResponse.word_active_above);
		}

		if (!TextUtils.isEmpty(this.mCreateOrderResponse.word_active_top)) {
			tvTopTip = (TextView) view.findViewById(R.id.tv_dialog_top);
			tvTopTip.setText(this.mCreateOrderResponse.word_active_top);
			tvTopTip.setVisibility(0);
			tvTopTip.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.uubee_ic_money, 0, 0, 0);
			this.showTopTip(tvTopTip);
		}

	}

	private void showDialogActivate() {
		this.backCard.setVisibility(4);
		this.dialogActivate = ((ViewStub) this.findViewById(R.id.dialog_activate))
				.inflate();
		this.dialogActivate.setOnClickListener((OnClickListener) null);
		TextView tvAccount = (TextView) this.dialogActivate
				.findViewById(R.id.tv_credit_account);
		this.setAccountView(tvAccount);
		this.showMarketingWord(this.dialogActivate);
		this.mActiveView = (CircleButton) this.dialogActivate
				.findViewById(R.id.account_active_button);
		this.dialogActivate.findViewById(R.id.account_active_view)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							String e = FirstPayActivity.this.getIntent()
									.getStringExtra("pay_req");
							JSONObject reqJson = new JSONObject(e);
							reqJson.put(
									"mob_user",
									FirstPayActivity.this.mCreateOrderResponse.mob_user);
							reqJson.put(
									"user_login",
									FirstPayActivity.this.mCreateOrderResponse.user_login);
							reqJson.put(
									"oid_userno",
									FirstPayActivity.this.mCreateOrderResponse.oid_userno);
							reqJson.put("flag_modify", "1");
							if (!TextUtils
									.isEmpty(FirstPayActivity.this.mCreateOrderResponse.token)) {
								reqJson.put(
										"token",
										FirstPayActivity.this.mCreateOrderResponse.token);
							}

							reqJson.put("action", "active");
							FirstPayActivity.this.createOrder(reqJson
									.toString());
						} catch (JSONException var4) {
							var4.printStackTrace();
						}

					}
				});
		this.mHandler.sendEmptyMessage(1);
		TextView tvAgreement = (TextView) this.dialogActivate
				.findViewById(R.id.tv_agreement);
		this.setAgreement(tvAgreement);
	}

	private void setAccountView(TextView tvAccount) {
		String formatted = Utils
				.formatPhoneNumber(this.mCreateOrderResponse.mob_user);
		tvAccount.setText(this.getString(R.string.uubee_credit_account,
				new Object[] { formatted }));
	}

	private void showDialogVerifyPhone() {
		this.backCard.setVisibility(4);
		this.dialogVerifyPhone = ((ViewStub) this
				.findViewById(R.id.dialog_verify_phone)).inflate();
		this.dialogVerifyPhone.setOnClickListener((OnClickListener) null);
		final EditText etPhone = (EditText) this.dialogVerifyPhone
				.findViewById(R.id.et_phone);
		this.mVerifyEdit = (EditText) this.dialogVerifyPhone
				.findViewById(R.id.et_verify_code);
		this.mGetVerifyView = (TextView) this.dialogVerifyPhone
				.findViewById(R.id.get_msg_verify);
		this.mGetVerifyView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String phone = etPhone.getText().toString().trim();
				FirstPayActivity.this.getVerifyMsg(phone);
			}
		});
		this.dialogVerifyPhone.findViewById(R.id.btn_activate)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						String phone = etPhone.getText().toString().trim();
						String verifyCode = FirstPayActivity.this.mVerifyEdit
								.getText().toString().trim();

						try {
							String e = FirstPayActivity.this.getIntent()
									.getStringExtra("pay_req");
							JSONObject reqJson = new JSONObject(e);
							reqJson.put("verify_code", verifyCode);
							reqJson.put("mob_user", phone);
							reqJson.put("user_login", phone);
							reqJson.put("oid_userno", phone);
							reqJson.put("action", "login");
							FirstPayActivity.this.createOrder(reqJson
									.toString());
						} catch (JSONException var6) {
							var6.printStackTrace();
						}

					}
				});
	}

	private void showDialogVerifyCode() {
		if (this.dialogVerifyCode != null) {
			this.dialogVerifyCode.setVisibility(0);
			this.getVerifyMsg((String) null);
		} else {
			this.dialogVerifyCode = ((ViewStub) this
					.findViewById(R.id.dialog_verify_code)).inflate();
			this.dialogVerifyCode.setOnClickListener((OnClickListener) null);
			View ivClose = this.dialogVerifyCode.findViewById(R.id.iv_close);
			ivClose.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					FirstPayActivity.this.mHandler.removeMessages(3);
					FirstPayActivity.this.dialogVerifyCode.setVisibility(8);
				}
			});
			this.initViewVerifyCode(this.dialogVerifyCode);
		}
	}

	private void showDialogVerifyName() {
		this.backCard.setVisibility(4);
		this.dialogVerifyName = ((ViewStub) this
				.findViewById(R.id.dialog_verify_name)).inflate();
		this.dialogVerifyName.setOnClickListener((OnClickListener) null);
		final EditText etName = (EditText) this.dialogVerifyName
				.findViewById(R.id.et_name);
		final SoftCloseEditText etCard = (SoftCloseEditText) this.dialogVerifyName
				.findViewById(R.id.et_id_card);
		if (0 == this.mCreateOrderResponse.trader_flag_real) {
			etName.setEnabled(true);
			etCard.setEnabled(true);
			etName.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					String name = etName.getText().toString().trim();
					if (hasFocus) {
						etName.setText((String) etName.getTag());
					} else if (!name.isEmpty() && !name.contains("*")) {
						etName.setTag(name);
						etName.setText(Utils.formatUsername(name));
					}

				}
			});
			etCard.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					String idNumber = etCard.getText().toString().trim();
					if (hasFocus) {
						if (idNumber.contains("*")) {
							etCard.setText((String) etCard.getTag());
						}
					} else if (FirstPayActivity.this.isIDNumber(idNumber)) {
						etCard.setTag(idNumber);
						etCard.setText(Utils.formatIDCardNumber(idNumber));
					}

				}
			});
			etCard.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					String idNumber = etCard.getText().toString().trim();
					if (idNumber.contains("*")) {
						etCard.setText((String) etCard.getTag());
					}

					return false;
				}
			});
			etCard.addTextChangedListener(new TextWatcherAdapter() {
				public void afterTextChanged(Editable s) {
					super.afterTextChanged(s);
					if (s.length() == 18) {
						String idNumber = s.toString();
						if (FirstPayActivity.this.isIDNumber(idNumber)) {
							if (!idNumber.equals(etCard.getTag())) {
								etCard.setTag(idNumber);
								etCard.setText(Utils
										.formatIDCardNumber(idNumber));
							}

							Utils.toggleInput(FirstPayActivity.this
									.getApplicationContext());
						}

					}
				}
			});
		}

		etCard.setOnCloseListener(new SoftCloseListener() {
			public void onClose() {
				String idNumber = (String) etCard.getTag();
				etCard.setText(Utils.formatIDCardNumber(idNumber));
			}
		});
		TextView accountView = (TextView) this.dialogVerifyName
				.findViewById(R.id.tv_credit_account);
		this.setAccountView(accountView);
		this.showMarketingWord(this.dialogVerifyName);
		String payReq = this.getIntent().getStringExtra("pay_req");
		String requestName = "";
		String requestIDNumber = "";

		try {
			JSONObject tvAgreement = new JSONObject(payReq);
			if (tvAgreement.has("name_user")) {
				requestName = tvAgreement.getString("name_user");
			}

			if (tvAgreement.has("id_no")) {
				requestIDNumber = tvAgreement.getString("id_no");
			}
		} catch (JSONException var12) {
			var12.printStackTrace();
		}

		if (!requestName.isEmpty()) {
			etName.setTag(requestName);
			etName.setText(Utils.formatUsername(requestName));
		}

		if (!requestIDNumber.isEmpty()) {
			etCard.setTag(requestIDNumber);
			etCard.setText(Utils.formatIDCardNumber(requestIDNumber));
		}

		this.dialogVerifyName.findViewById(R.id.btn_activate).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						String name = etName.getText().toString().trim();
						if (name.contains("*")) {
							name = (String) etName.getTag();
						}

						String card = etCard.getText().toString().trim();
						if (card.contains("*")) {
							card = (String) etCard.getTag();
						}

						if (!FirstPayActivity.this.checkInputValid(name, card)) {
							Toast.makeText(
									FirstPayActivity.this
											.getApplicationContext(),
									"请输入正确的姓名和身份证！", 0).show();
						} else {
							try {
								String e = FirstPayActivity.this.getIntent()
										.getStringExtra("pay_req");
								JSONObject reqJson = new JSONObject(e);
								reqJson.put("name_user", name);
								reqJson.put("id_type", 0).put("id_no", card);
								reqJson.put(
										"user_login",
										FirstPayActivity.this.mCreateOrderResponse.user_login);
								reqJson.put(
										"mob_user",
										FirstPayActivity.this.mCreateOrderResponse.mob_user);
								reqJson.put(
										"oid_userno",
										FirstPayActivity.this.mCreateOrderResponse.oid_userno);
								if (!TextUtils
										.isEmpty(FirstPayActivity.this.mCreateOrderResponse.token)) {
									reqJson.put(
											"token",
											FirstPayActivity.this.mCreateOrderResponse.token);
								}

								reqJson.put("action", "active");
								FirstPayActivity.this.createOrder(reqJson
										.toString());
							} catch (JSONException var6) {
								var6.printStackTrace();
							}

						}
					}
				});
		TextView tvAgreement1 = (TextView) this.dialogVerifyName
				.findViewById(R.id.tv_agreement);
		this.setAgreement(tvAgreement1);
		TextView tvTipVerifyName = (TextView) this.dialogVerifyName
				.findViewById(R.id.tv_tip_verify_name);
		String tip = this.getString(R.string.uubee_tip_verify_name);
		SpannableString spanStr = new SpannableString(tip);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(-3407872);
		spanStr.setSpan(colorSpan, tip.indexOf(" "), tip.length(), 33);
		tvTipVerifyName.setText(spanStr);
	}

	private void setAgreement(TextView tvAgreement) {
		SpannableString spanStr = new SpannableString(
				this.getString(R.string.agreement_declare));
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(this
				.getResources().getColor(R.color.uubee_blue_light));
		spanStr.setSpan(colorSpan, 2, 12, 33);
		tvAgreement.setText(spanStr);
		tvAgreement.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FirstPayActivity.this.showAgreement();
			}
		});
	}

	private boolean isIDNumber(String idNumber) {
		if (TextUtils.isEmpty(idNumber)) {
			return false;
		} else if (idNumber.length() != 18) {
			return false;
		} else {
			if (!TextUtils.isDigitsOnly(idNumber)) {
				if (!TextUtils.isDigitsOnly(idNumber.subSequence(0, 17))) {
					return false;
				}

				char last = idNumber.charAt(17);
				if (last != 120 && last != 88) {
					return false;
				}
			}

			return true;
		}
	}

	private boolean checkInputValid(String username, String idNumber) {
		return !TextUtils.isEmpty(username) && this.isIDNumber(idNumber);
	}

	private SpannableString getBigSizeMoney(int start, String money) {
		SpannableString ssMoney = new SpannableString(money);
		int index = money.indexOf(".");
		int end = index == -1 ? money.length() : index;
		ssMoney.setSpan(new RelativeSizeSpan(2.0F), start, end, 17);
		return ssMoney;
	}

	private void setPayMoney(TextView tvPayMoney, String money) {
		money = String.format("%4.2f", new Object[] { Float.valueOf(money) });
		SpannableString moneyStr = new SpannableString(
				this.getString(R.string.pay_money_unit_text) + " " + money);
		moneyStr.setSpan(new RelativeSizeSpan(2.0F), 2, moneyStr.length(), 33);
		tvPayMoney.setText(moneyStr);
	}

	private void setRepayDate(String repayDate) {
		if (repayDate != null && repayDate.length() == 8) {
			String[] weeks = new String[] { "日", "一", "二", "三", "四", "五", "六" };
			int year = Integer.valueOf(repayDate.substring(0, 4)).intValue();
			int month = Integer.valueOf(repayDate.substring(4, 6)).intValue();
			int day = Integer.valueOf(repayDate.substring(6, 8)).intValue();
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.clear();
			calendar.set(year, month - 1, day);
			int weekIndex = calendar.get(7);
			String strDate = this.getString(R.string.uubee_repay_date,
					new Object[] { weeks[weekIndex - 1] });
			if (!TextUtils.isEmpty(this.mCreateOrderResponse.word_repay_date)) {
				strDate = strDate + " "
						+ this.mCreateOrderResponse.word_repay_date;
			}

			SpannableString dateSpan = new SpannableString(strDate);
			int indexOfWeek = strDate.indexOf("每周");
			dateSpan.setSpan(new StyleSpan(1), indexOfWeek, indexOfWeek + 3, 33);
			if (!TextUtils.isEmpty(this.mCreateOrderResponse.word_repay_date)) {
				ForegroundColorSpan btnRepayDate = new ForegroundColorSpan(
						-168632);
				dateSpan.setSpan(btnRepayDate, strDate.indexOf(" "),
						strDate.length(), 33);
			}

			TextView btnRepayDate1 = (TextView) this
					.findViewById(R.id.credit_limit_date);
			btnRepayDate1.setText(dateSpan);
			
//			TextView btnRepayDate2 = (TextView) this
//					.findViewById(R.id.credit_bill_date);
//			btnRepayDate2.setText(dateSpan);
		}
	}

	private void createOrder(final String params) {
		this.cancelTask();
		this.showLoadingView();
		this.mTask = new AsyncTask<Void,Void,PayResult>() {
			protected PayResult doInBackground(Void... v) {
				return PrePayImpl.INSTANCE.createOrder(
						FirstPayActivity.this.getApplicationContext(), params);
			}

			protected void onPostExecute(PayResult response) {
				super.onPostExecute(response);
				FirstPayActivity.this.hiddenLoadingView();
				FirstPayActivity.this.handleCreateOrderResponse(response);
			}
		};
		this.mTask.execute(new Void[0]);
	}

	private void handleCreateOrderResponse(PayResult res) {
		if (!"000000".equals(res.getRet_code())) {
			Toast.makeText(this.getApplicationContext(), res.getRet_msg(), 1)
					.show();
		} else {
			this.mCreateOrderResponse = (CreateOrderResponse) this.mGson
					.fromJson(res.toJson(), CreateOrderResponse.class);
			if (this.mCreateOrderResponse != null) {
				this.initData(res.toJson());
				if (1 == this.mCreateOrderResponse.user_status) {
					if (this.dialogVerifyPhone != null) {
						this.dialogVerifyPhone.setVisibility(8);
					}
				} else if (0 == this.mCreateOrderResponse.user_status) {
					if (this.dialogVerifyPhone != null) {
						this.dialogVerifyPhone.setVisibility(8);
					}

					if (this.dialogActivate != null) {
						this.dialogActivate.setVisibility(8);
					}

					if (this.dialogVerifyName != null) {
						this.dialogVerifyName.setVisibility(8);
					}
				}

				this.initViaResponse();
			}
		}
	}

	protected void onHandleMessage(Message msg) {
		super.onHandleMessage(msg);
		switch (msg.what) {
		case 1:
			this.mActiveView.startRingAnim();
		default:
		}
	}

	private void showAgreement() {
		Intent help = new Intent(this, HelpActivity.class);
		help.putExtra("is_help", false);
		this.startActivity(help);
	}

	protected void onCashSelect(int index) {
		this.mCashIndex = index;
		if (index > -1) {
			CashGift cash = (CashGift) this.mCreateOrderResponse.cashgiftlist
					.get(index);
			if (this.isBefore) {
				this.tvGiftTip.setText(R.string.cash_ticker_use);
			}

			int type = cash.type_cashgift - 1;
			String cashStr = this.mCashTypes[type] + " " + cash.amt_cashgift
					+ this.getString(R.string.cash_unit);
			SpannableString moneySpan = new SpannableString(cashStr);
			int cashIndex = cashStr.indexOf(" ") + 1;
			moneySpan.setSpan(new ForegroundColorSpan(this.getResources()
					.getColor(R.color.uubee_red)), cashIndex, moneySpan.length(),
					34);
			this.tvGiftNumber.setText(moneySpan);

			try {
				this.mPayOrder.put("oid_cashgift", cash.oid_cashgift);
			} catch (JSONException var8) {
				var8.printStackTrace();
			}
		} else {
			this.setCashGiftView();
			this.mPayOrder.remove("oid_cashgift");
		}

	}

	protected void setCashGiftView() {
		if (this.mCreateOrderResponse.cashgiftlist != null) {
			Collections.sort(this.mCreateOrderResponse.cashgiftlist);
		}

		if (this.isBefore) {
			this.tvGiftTip.setText(R.string.cash_ticker_tip);
		}

		int size = 0;
		if (this.mCreateOrderResponse.cashgiftlist != null) {
			size = this.mCreateOrderResponse.cashgiftlist.size();
		}

		String numTicker = this.getString(R.string.cash_ticker_num,
				new Object[] { Integer.valueOf(size) });
		this.tvGiftNumber.setText(numTicker);
		if (size == 0) {
			this.vGift.setEnabled(false);
			if (this.isBefore) {
				this.tvGiftNumber.setEnabled(false);
				this.tvGiftTip.setEnabled(false);
			}
		} else {
			this.vGift.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					FirstPayActivity.this.selectCashTicker();
				}
			});
			if (this.isBefore) {
				this.tvGiftNumber.setTextColor(this.getResources().getColor(
						R.color.high_light_text));
			}
		}

	}

	private void selectCashTicker() {
		Intent intent = new Intent(this, CashSelectActivity.class);
		intent.putExtra("cash_list", this.mCreateOrderResponse.cashgiftlist);
		intent.putExtra("cash_select_index", this.mCashIndex);
		intent.setFlags(67108864);
		this.startActivityForResult(intent, 1);
		this.overridePendingTransition(R.anim.uubee_slide_in_right,
				R.anim.uubee_slide_out_left);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == -1) {
			int index = data.getIntExtra("cash_select_index", -1);
			this.onCashSelect(index);
		}

	}

	protected void handlePayResult(PayResult result) {
		super.handlePayResult(result);
		this.mPayButton.setEnabled(true);
		if (this.isPayFail && "604001".equals(result.getRet_code())) {
			this.showDialogVerifyCode();
		}

	}
}
