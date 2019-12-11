package com.uubee.prepay.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.uubee.prepay.activity.BasePayActivity;
import cn.com.unispark.R;

public class FastPayActivity extends BasePayActivity {
	public FastPayActivity() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.uubee_activity_repeat_pay);
		this.isBefore = true;
		this.findViewById(R.id.iv_close).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						FastPayActivity.this.finish();
					}
				});
		this.initViewVerifyCode(this.findViewById(R.id.dialog_verify_code));
		if (this.needAuth) {
			this.showInfoSupplyPage();
		}

	}
}
