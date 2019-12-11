package com.uubee.prepay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.AbsoluteLayout.LayoutParams;

public class ProgressWebView extends WebView {
	private ProgressBar progressbar;
	private boolean isLoaded = false;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!this.isInEditMode()) {
			this.progressbar = new ProgressBar(context, (AttributeSet) null,
					16842871);
			this.progressbar.setLayoutParams(new LayoutParams(-1, -2, 0, 0));
			this.addView(this.progressbar);
			this.setWebChromeClient(new ProgressWebView.WebChromeClient());
		}
	}

	public void loadUrl(String url) {
		if (!this.isLoaded) {
			super.loadUrl(url);
		}
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		public WebChromeClient() {
		}

		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				ProgressWebView.this.isLoaded = true;
				ProgressWebView.this.progressbar.setVisibility(8);
			}

			super.onProgressChanged(view, newProgress);
		}
	}
}
