package com.uubee.prepay.dialog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.unispark.R;
import com.uubee.prepay.model.InitResponse;
import com.uubee.prepay.util.Utils;
import com.uubee.prepay.view.TextWatcherAdapter;

public class InfoConfirmDialog {
	private static final String URL_AGREEMENT = "https://m.uubee.com/html5/pages/sdk/index_xy.html";
	private Context mContext;
	private InitResponse mInitResponse;
	private OnClickListener mDismissListener;
	private InfoConfirmDialog.OnActiveListener mActiveListener;

	public InfoConfirmDialog(Context context, InitResponse response) {
		this.mContext = context;
		this.mInitResponse = response;
	}

	public void setDismissListener(OnClickListener listener) {
		this.mDismissListener = listener;
	}

	public void setActiveListener(InfoConfirmDialog.OnActiveListener listener) {
		this.mActiveListener = listener;
	}

	public AlertDialog build() {
		View view = LayoutInflater.from(this.mContext).inflate(
				R.layout.uubee_dialog_info_confirm, (ViewGroup) null);
		Resources res = this.mContext.getResources();
		TextView tvAccount = (TextView) view.findViewById(R.id.info_detail_name);
		SpannableString accountStr = new SpannableString(
				this.mContext.getString(R.string.credit_account_no));
		accountStr.setSpan(
				new ForegroundColorSpan(res.getColor(R.color.uubee_red)), 0, 5,
				33);
		tvAccount.setText(accountStr);
		TextView tvPhone = (TextView) view.findViewById(R.id.info_detail_content);
		tvPhone.setText(this.mInitResponse.mob_user);
		View viewRepay = view.findViewById(R.id.account_repay_layout);
		TextView tvLabel = (TextView) viewRepay
				.findViewById(R.id.info_detail_name);
		tvLabel.setText(this.mContext.getString(R.string.repay_date));
		TextView tvDate = (TextView) viewRepay
				.findViewById(R.id.info_detail_content);
		String limitDate = this.mInitResponse.date_repay;
		if (limitDate != null && limitDate.length() == 8) {
			String vName = limitDate.substring(0, 4);
			int tvNameLabel = Integer.valueOf(limitDate.substring(4, 6))
					.intValue();
			String etName = limitDate.substring(6, 8);
			GregorianCalendar vCardId = new GregorianCalendar(Integer.valueOf(
					vName).intValue(), tvNameLabel - 1, Integer.valueOf(etName)
					.intValue());
			int tvCardLabel = vCardId.get(7) - 1;
			String etCard = res.getStringArray(R.array.repay_day_of_week)[tvCardLabel];
			tvDate.setText(etCard);
		}

		View vName1 = view.findViewById(R.id.account_name_layout);
		TextView tvNameLabel1 = (TextView) vName1
				.findViewById(R.id.info_detail_name);
		tvNameLabel1.setText(R.string.credit_apply_name);
		final EditText etName1 = (EditText) vName1
				.findViewById(R.id.info_detail_content);
		if (!TextUtils.isEmpty(this.mInitResponse.name_user)) {
			try {
				final String vCardId1 = URLDecoder.decode(
						this.mInitResponse.name_user, "utf-8");
				etName1.setText(Utils.formatUsername(vCardId1));
				etName1.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						if (etName1.getText().toString().contains("*")) {
							etName1.setText(vCardId1);
						}

						return false;
					}
				});
			} catch (UnsupportedEncodingException var22) {
				var22.printStackTrace();
			}
		}

		View vCardId2 = view.findViewById(R.id.account_identity_layout);
		TextView tvCardLabel1 = (TextView) vCardId2
				.findViewById(R.id.info_detail_name);
		tvCardLabel1.setText(R.string.identity_card_no);
		final EditText etCard1 = (EditText) vCardId2
				.findViewById(R.id.info_detail_content);
		if (!TextUtils.isEmpty(this.mInitResponse.id_no)) {
			etCard1.setText(Utils.formatIDCardNumber(this.mInitResponse.id_no));
			etCard1.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (etCard1.getText().toString().contains("*")) {
						etCard1.setText(InfoConfirmDialog.this.mInitResponse.id_no);
					}

					return false;
				}
			});
		}

		etCard1.addTextChangedListener(new TextWatcherAdapter() {
			public void afterTextChanged(Editable s) {
				super.afterTextChanged(s);
				if (s.length() == 18) {
					Utils.toggleInput(InfoConfirmDialog.this.mContext);
				}

			}
		});
		if ("0".equals(this.mInitResponse.flag_real)) {
			etName1.setEnabled(true);
			etCard1.setEnabled(true);
			ImageView btnActive = (ImageView) vName1.findViewById(R.id.iv_lock);
			ImageView btnCancel = (ImageView) vCardId2.findViewById(R.id.iv_lock);
			btnActive.setImageResource(R.drawable.uubee_ic_unlock);
			btnCancel.setImageResource(R.drawable.uubee_ic_unlock);
		}

		Button btnActive1 = (Button) view
				.findViewById(R.id.active_confirm_button);
		btnActive1.setEnabled(true);
		btnActive1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = etName1.getText().toString().trim();
				String idNumber = etCard1.getText().toString().trim();
				if (username.contains("*")) {
					username = null;
				}

				if (idNumber.contains("*")) {
					idNumber = null;
				}

				if (InfoConfirmDialog.this.checkInputValid(username, idNumber)) {
					InfoConfirmDialog.this.mActiveListener.onActive(username,
							idNumber);
				} else {
					Toast toast = Toast.makeText(
							InfoConfirmDialog.this.mContext,
							R.string.personal_info_invalid, 0);
					toast.setGravity(16, 0, 0);
					toast.show();
				}

			}
		});
		Button btnCancel1 = (Button) view.findViewById(R.id.btn_active_cancel);
		btnCancel1.setOnClickListener(this.mDismissListener);
		TextView declareView = (TextView) view.findViewById(R.id.declare_view);
		SpannableString spanStr = new SpannableString(
				this.mContext.getString(R.string.agreement_declare));
		spanStr.setSpan(
				new ForegroundColorSpan(res.getColor(R.color.uubee_blue_light)),
				2, 12, 33);
		declareView.setText(spanStr);
		final WebView wvAgreement = (WebView) view
				.findViewById(R.id.wv_agreement);
		declareView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (wvAgreement.isShown()) {
					wvAgreement.setVisibility(8);
				} else {
					wvAgreement.setVisibility(0);
				}

				if (!wvAgreement.canGoBack()) {
					wvAgreement
							.loadUrl("https://m.uubee.com/html5/pages/sdk/index_xy.html");
				}

			}
		});
		Builder builder = new Builder(this.mContext);
		builder.setView(view);
		builder.setCancelable(false);
		return builder.create();
	}

	private boolean checkInputValid(String username, String idNumber) {
		if (!TextUtils.isEmpty(idNumber)) {
			if (idNumber.length() != 18) {
				return false;
			}

			if (!TextUtils.isDigitsOnly(idNumber)) {
				if (!TextUtils.isDigitsOnly(idNumber.subSequence(0, 17))) {
					return false;
				}

				char last = idNumber.charAt(17);
				if (last != 120 && last != 88) {
					return false;
				}
			}
		}

		return !"1".equals(this.mInitResponse.need_real) ? true : !""
				.equals(username) && !"".equals(idNumber);
	}

	public interface OnActiveListener {
		void onActive(String var1, String var2);
	}
}
