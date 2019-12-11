package com.uubee.prepay.activity;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uubee.prepay.api.PrepayAgent;
import com.uubee.prepay.core.PrepayApi;

import cn.com.unispark.R;
import cn.com.unispark.fragment.home.pay.uubeepay.ResultPay;

import com.uubee.prepay.model.CreateOrderResponse;
import com.uubee.prepay.model.MsgVerifyOrder;
import com.uubee.prepay.model.PayResult;
//import com.uubee.prepay.model.ResultPay;
import com.uubee.prepay.util.DebugLog;
import com.uubee.prepay.util.Utils;
import com.uubee.prepay.view.TextWatcherAdapter;

public class BasePayActivity extends BaseActivity {
	protected static final int MSG_START_ACTIVE_ANIM = 1;
	protected static final int MSG_HANDLE_RESET_MSG_REMAIN = 3;
	protected static final int RESET_VERIFY_MSG_TIME = 60;
	protected static final int SEND_VERIFY_MSG_TIME_DURATION = 1000;
	protected boolean isBefore = false;
	protected TextView tvGiftNumber;
	protected TextView tvGiftTip;
	protected View vGift;
	protected TextView mGetVerifyView;
	protected int mPassType;
	protected CreateOrderResponse mCreateOrderResponse;
	protected EditText mVerifyEdit;
	protected Button mPayButton;
	protected JSONObject mPayOrder;
	protected String[] mCashTypes;
	protected AsyncTask<Void, Void, PayResult> mTask;
	protected int mCashIndex;
	protected boolean needAuth;
	protected boolean isPayFail = false;

	public BasePayActivity() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String initResponse = this.getIntent().getStringExtra("query_res");
		this.initData(initResponse);
	}

	protected void initData(String response) {
		this.mCashIndex = -1;
		this.mCreateOrderResponse = (CreateOrderResponse) this.mGson.fromJson(
				response, CreateOrderResponse.class);
		if (this.mCreateOrderResponse != null
				&& this.mCreateOrderResponse.user_status == 0) {
			this.mPassType = Integer
					.parseInt(this.mCreateOrderResponse.type_passwd);
			this.needAuth = !"false"
					.equals(this.mCreateOrderResponse.need_auth);
			if (TextUtils.isEmpty(this.mCreateOrderResponse.acct_balance)) {
				this.mCreateOrderResponse.acct_balance = "0.00";
			}

			if (!this.mCreateOrderResponse.acct_balance.contains(".")) {
				this.mCreateOrderResponse.acct_balance = this.mCreateOrderResponse.acct_balance
						+ ".00";
			}

			this.mCashTypes = this.getResources().getStringArray(
					R.array.cash_type);
			String payReq = this.getIntent().getStringExtra("pay_req");

			try {
				this.mPayOrder = new JSONObject(payReq);
			} catch (JSONException var4) {
				var4.printStackTrace();
			}

			this.mPayOrder.remove("verify_code");
		}
	}

	protected void initViewVerifyCode(View dialogVerifyCode) {
		this.mVerifyEdit = (EditText) dialogVerifyCode
				.findViewById(R.id.et_verify_code);
		this.mVerifyEdit.setHint(this
				.getString(R.string.uubee_tip_input_verify_code,
						new Object[] { this.mCreateOrderResponse.mob_user
								.substring(7) }));
		this.mGetVerifyView = (TextView) dialogVerifyCode
				.findViewById(R.id.get_msg_verify);
		this.mGetVerifyView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BasePayActivity.this.getVerifyMsg((String) null);
			}
		});
		this.mVerifyEdit.addTextChangedListener(new TextWatcherAdapter() {
			public void afterTextChanged(Editable s) {
				super.afterTextChanged(s);
				if (s.length() == 6) {
					if (!BasePayActivity.this.checkVerifyData()) {
						Toast.makeText(
								BasePayActivity.this.getApplicationContext(),
								R.string.verify_msg_invalid, 0).show();
					} else {
						Utils.toggleInput(BasePayActivity.this
								.getApplicationContext());
						BasePayActivity.this.executePay();
					}
				}
			}
		});
		this.getVerifyMsg((String) null);
	}

	protected void onDestroy() {
		super.onDestroy();
		this.cancelTask();
		if (PrepayAgent.mResultListener != null && !this.isPayFail) {
			PayResult result = new PayResult("777777", "用户取消付款");
			PrepayAgent.mResultListener.onResult(result.toJson());
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (resultCode == -1 && this.mPassType > 2) {
				this.getVerifyMsg((String) null);
			} else if (resultCode == 0) {
				this.finish();
			}
		}

	}

	protected void showInfoSupplyPage() {
		Intent intent = new Intent(this, PersonalInfoSupplyActivity.class);
		intent.setFlags(67108864);
		intent.putExtra("query_res",
				this.getIntent().getStringExtra("query_res"));
		this.startActivityForResult(intent, 2);
	}

	protected void executePay() {
		this.cancelTask();
		this.showLoadingView();
		this.mTask = new AsyncTask<Void,Void,PayResult>() {
			protected PayResult doInBackground(Void... params) {
				try {
					BasePayActivity.this.mPayOrder.put("mob_user",
							BasePayActivity.this.mCreateOrderResponse.mob_user);
					BasePayActivity.this.mPayOrder.put("token",
							BasePayActivity.this.mCreateOrderResponse.token);
				} catch (JSONException var3) {
					var3.printStackTrace();
				}

				return PrepayApi.sendPayReq(
						BasePayActivity.this.getApplicationContext(),
						BasePayActivity.this.mPayOrder.toString());
			}

			protected void onPostExecute(PayResult payResult) {
				super.onPostExecute(payResult);
				BasePayActivity.this.hiddenLoadingView();
				BasePayActivity.this.handlePayResult(payResult);
			}
		};
		this.mTask.execute(new Void[0]);
	}

	protected boolean checkVerifyData() {
		String verifyNo = this.mVerifyEdit.getText().toString();
		Pattern pattern = Pattern.compile("\\d+");
		if (!verifyNo.isEmpty() && pattern.matcher(verifyNo).find()) {
			try {
				this.mPayOrder.put("verify_code", verifyNo);
			} catch (JSONException var4) {
				var4.printStackTrace();
			}

			return true;
		} else {
			return false;
		}
	}

	protected void getVerifyMsg(String phone) {
		this.mGetVerifyView.setClickable(false);
		this.mVerifyEdit.setText("");
		this.mHandler.removeMessages(3);
		this.handleResetVerifyTime(60);
		if (this.mTask != null && this.mTask.getStatus() != Status.FINISHED) {
			this.mTask.cancel(true);
		}

		MsgVerifyOrder order = new MsgVerifyOrder();
		if (phone != null) {
			order.user_login = phone;
			order.mob_user = phone;
			order.oid_userno = phone;
			order.sms_type = "0";
		} else {
			order.user_login = this.mCreateOrderResponse.user_login;
			order.mob_user = this.mCreateOrderResponse.mob_user;
			order.oid_userno = this.mCreateOrderResponse.oid_userno;
			order.money_order = this.mCreateOrderResponse.money_order;
		}

		final String strOrder = this.mGson.toJson(order);
		this.mTask = new AsyncTask<Void,Void,PayResult>() {
			protected PayResult doInBackground(Void... params) {
				return PrepayApi.getMsgVerifyCode(
						BasePayActivity.this.getApplicationContext(), strOrder);
			}

			protected void onPostExecute(PayResult payResult) {
				super.onPostExecute(payResult);
				BasePayActivity.this.handleVerifyMsgResult(payResult);
			}
		};
		this.mTask.execute(new Void[0]);
	}

	private void handleResetVerifyTime(int time) {
		if (time == 0) {
			this.mHandler.removeMessages(3);
			this.mGetVerifyView.setClickable(true);
			this.mGetVerifyView.setText(R.string.uubee_get_again);
		} else {
			this.mGetVerifyView.setText(time + "秒");
			Message msg = Message.obtain();
			msg.what = 3;
			--time;
			msg.arg1 = time;
			this.mHandler.sendMessageDelayed(msg, 1000L);
		}

	}

	protected void cancelTask() {
		if (this.mTask != null && this.mTask.getStatus() != Status.FINISHED) {
			this.mTask.cancel(true);
		}

	}

	protected void handlePayResult(PayResult result) {
		if ("700002".equals(result.getRet_code())) {
			if (this.mGetVerifyView != null) {
				this.mGetVerifyView.setClickable(true);
			}

			Toast.makeText(this.getApplicationContext(), result.getRet_msg(), 0)
					.show();
		} else if ("000000".equals(result.getRet_code())) {
			this.isPayFail = false;
			if (PrepayAgent.mResultListener != null) {
				Gson inflater = new Gson();
				ResultPay v = (ResultPay) inflater.fromJson(result.toJson(),
						ResultPay.class);
				PrepayAgent.mResultListener.onResult(inflater.toJson(v));
				PrepayAgent.mResultListener = null;
			}

			if (this.getIntent().getBooleanExtra("show_success_page", false)) {
				this.showPayResultPage(result);
			}

			this.finish();
		} else {
			this.isPayFail = true;
			if ("604003".equals(result.getRet_code())) {
				LayoutInflater inflater1 = this.getLayoutInflater();
				View v1 = inflater1.inflate(R.layout.uubee_toast,
						(ViewGroup) this.findViewById(R.id.toast_root));
				TextView tv = (TextView) v1.findViewById(R.id.tv_toast);
				tv.setText(result.getRet_msg());
				Toast toast = new Toast(this.getApplicationContext());
				toast.setGravity(17, 0, 0);
				toast.setDuration(1);
				toast.setView(v1);
				toast.show();
			} else if ("604001".equals(result.getRet_code())) {
				DebugLog.i("604001");
			} else {
				this.showPayResultPage(result);
			}

			if (this.mGetVerifyView != null) {
				this.mGetVerifyView.setClickable(true);
			}
		}

	}

	private void showPayResultPage(PayResult result) {
		Intent intent = new Intent(this, PayResultActivity.class);
		intent.putExtra("pay_result", result);
		this.startActivity(intent);
	}

	private void handleVerifyMsgResult(PayResult res) {
		if (!"000000".equals(res.getRet_code())) {
			this.handleResetVerifyTime(0);
		}

	}

	protected void onHandleMessage(Message msg) {
		switch (msg.what) {
		case 3:
			this.handleResetVerifyTime(msg.arg1);
		default:
		}
	}
}
