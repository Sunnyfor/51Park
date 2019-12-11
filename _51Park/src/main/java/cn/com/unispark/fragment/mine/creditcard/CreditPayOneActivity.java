package cn.com.unispark.fragment.mine.creditcard;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
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
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.fragment.mine.creditcard.entity.LLpayBinEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 无密支付第一步
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
public class CreditPayOneActivity extends BaseActivity {

	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	private ClearEditText et_cardno;
	private Button bt_next;
	private TextView tv_tishi2;
	private String tStr = "2.自动交费是指在绑定信用卡后，账单生成之后2小时内，如果没有通过其他支付方式，系统将自动从您的信用卡中扣除未支付的金额。";
	// private TextView tv_bankname;
	// private TextView tv_credit_no;
	// private RelativeLayout rl_isbind;
	// private TextView tv_tishi1;
	private TextView ordernumber;
	private TextView payfee;
	// private String no_order;
	private String money_order;
	private LinearLayout iv_credit;
	private Dialog creditdialog;

	// 在绑卡第二部操作完成后需要关闭此页面
	public static Activity activity;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.credit_pay_one_main);

		activity = this;

	}

	@Override
	public void initView() {

		// 导航栏标题以及返回按钮
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("交停车费");
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		et_cardno = (ClearEditText) findViewById(R.id.et_cardno);
		bt_next = (Button) findViewById(R.id.bt_next);
		bt_next.setEnabled(false);
		tv_tishi2 = (TextView) findViewById(R.id.tv_tishi2);
		SpannableStringBuilder style = new SpannableStringBuilder(tStr);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#70B1DF")), 22,
				26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_tishi2.setText(style);
		ordernumber = (TextView) findViewById(R.id.ordernumber);

		// no_order = getIntent().getStringExtra("order_num");

		ordernumber.setText(ParkApplication.mOrderNum);
		payfee = (TextView) findViewById(R.id.payfee);

		money_order = getIntent().getStringExtra("money");
		payfee.setText("¥ " + money_order);

		iv_credit = (LinearLayout) findViewById(R.id.iv_credit);
		iv_credit.setOnClickListener(this);
		bt_next.setOnClickListener(this);

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

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.iv_credit:

			showCreditDialog("信用卡提示", "请确保您要绑定的信用卡真实有效,以成功完成绑定!");
			break;

		case R.id.bt_next:
			parseGetBindCardInfo();
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
		ImageView line = (ImageView) creditdialog.findViewById(R.id.line);

		ViewUtil.setMarginLeft(line, 20, ViewUtil.RELATIVELAYOUT);

		ViewUtil.setMarginRight(line, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(content, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(content, 20, ViewUtil.RELATIVELAYOUT);
		content.setText(contentStr);
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
	 * 功能说明：【解析】获取用户绑卡信息
	 * 日期：	2015年11月17日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param cardnumberstr
	 * </pre>
	 */
	private void parseGetBindCardInfo() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("card_no",
				et_cardno.getText().toString().trim().replace(" ", ""));// 银行卡号

		httpUtil.parse(httpUtil.POST, Constant.LLPAY_BIN_URL,
				LLpayBinEntity.class, params, new onResult<LLpayBinEntity>() {

					@Override
					public void onSuccess(LLpayBinEntity result) {

						loadingProgress.dismiss();

						// bank_code string 所属银行编号
						// bank_name string 所属银行名称
						// card_type string 银行卡类型 （2-储蓄卡 3-信用卡）

						// 银行卡类型 （2-储蓄卡 3-信用卡）
						switch (result.getData().getCard_type()) {
						case 2:
							ToastUtil.showToast("您填写的不是信用卡！");
							break;
						case 3:
							Intent intent = new Intent(context,
									CreditPayTwoActivity.class);
							intent.putExtra("bank_name", result.getData()
									.getBank_name());
							intent.putExtra("card_type", result.getData()
									.getCard_type());
							intent.putExtra("money", money_order);
							intent.putExtra("card_number", et_cardno.getText()
									.toString().trim());
							startActivity(intent);

							break;
						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});

	}

}
