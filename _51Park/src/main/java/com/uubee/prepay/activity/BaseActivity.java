package com.uubee.prepay.activity;

import java.lang.ref.SoftReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import cn.com.unispark.R;

import com.google.gson.Gson;

public abstract class BaseActivity extends Activity {
	protected boolean isFinished;
	protected BaseActivity.MyHandler mHandler;
	protected Gson mGson = new Gson();
	private View mLoadView;
	private View btnHelp;

	public BaseActivity() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.isFinished = false;
		this.mHandler = new BaseActivity.MyHandler(this);
	}

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		View btnBack = this.findViewById(R.id.actionbar_backButton);
		this.btnHelp = this.findViewById(R.id.actionbar_helpButton);
		if (btnBack != null) {
			btnBack.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					BaseActivity.this.finish();
				}
			});
			this.btnHelp.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					BaseActivity.this.onHelpButtonClick();
				}
			});
			this.setLoadingView(this.findViewById(R.id.first_pay_loading_layout));
		}
	}

	protected void hideBtnHelp() {
		this.btnHelp.setVisibility(8);
	}

	public void finish() {
		this.isFinished = true;
		this.mHandler.removeCallbacksAndMessages((Object) null);
		super.finish();
	}

	protected final void setLoadingView(View view) {
		this.mLoadView = view;
	}

	protected void showLoadingView() {
		if (this.mLoadView != null && !this.mLoadView.isShown()) {
			this.mLoadView.setVisibility(0);
		}

	}

	protected void hiddenLoadingView() {
		if (this.mLoadView != null && this.mLoadView.isShown()) {
			this.mLoadView.setVisibility(4);
		}

	}

	protected void onHelpButtonClick() {
		Intent help = new Intent(this, HelpActivity.class);
		help.putExtra("is_help", true);
		this.startActivity(help);
	}

	protected void onHandleMessage(Message msg) {
	}

	protected boolean canAnimator() {
		return VERSION.SDK_INT > 10;
	}

	protected static class MyHandler extends Handler {
		private SoftReference<BaseActivity> mRef;

		public MyHandler(BaseActivity a) {
			this.mRef = new SoftReference(a);
		}

		public void handleMessage(Message msg) {
			BaseActivity activity = (BaseActivity) this.mRef.get();
			if (activity != null && !activity.isFinished) {
				activity.onHandleMessage(msg);
				super.handleMessage(msg);
			}
		}
	}
}
