
package com.uubee.prepay.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.com.unispark.R;

public class HelpActivity extends BaseActivity {
    private static final String URL_HELP = "https://m.uubee.com/html5/pages/sdk/index_help.html";
    private static final String URL_AGREEMENT = "https://m.uubee.com/html5/pages/sdk/index_xy.html";

    public HelpActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.uubee_activity_help);
        WebView wvHelp = (WebView)this.findViewById(R.id.wv_help);
        wvHelp.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        boolean isHelp = this.getIntent().getBooleanExtra("is_help", false);
        if(isHelp) {
            this.hideBtnHelp();
            wvHelp.loadUrl("https://m.uubee.com/html5/pages/sdk/index_help.html");
        } else {
            wvHelp.loadUrl("https://m.uubee.com/html5/pages/sdk/index_xy.html");
        }

    }
}
