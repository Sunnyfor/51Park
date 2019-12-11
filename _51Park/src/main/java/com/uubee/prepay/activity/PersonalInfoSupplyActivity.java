//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.uubee.prepay.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.com.unispark.R;
import com.uubee.prepay.util.DebugLog;

public class PersonalInfoSupplyActivity extends BaseActivity {
    private static final String SUPPLY_SUCCESS = "https://www.uubee.com/close.jsp";
    private static final String SUPPLY_FAILED = "failed";
    private WebView mWebView;

    public PersonalInfoSupplyActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.uubee_personal_info_supply);
        this.initView();
    }

    private void initView() {
        this.mWebView = (WebView)this.findViewById(R.id.webview);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setWebViewClient(new PersonalInfoSupplyActivity.MyWebViewClient());
        String queryRes = this.getIntent().getStringExtra("query_res");
        if(queryRes != null) {
            try {
                JSONObject e = new JSONObject(queryRes);
                this.mWebView.loadUrl(e.getString("url_webview"));
            } catch (JSONException var3) {
                var3.printStackTrace();
            }
        }

    }

    public void onBackPressed() {
        super.onBackPressed();
        if(this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            this.setResult(0);
            this.finish();
        }

    }

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            DebugLog.d("shouldOverrideUrlLoading,url = " + url);
            if("https://www.uubee.com/close.jsp".equals(url)) {
                PersonalInfoSupplyActivity.this.setResult(-1);
                PersonalInfoSupplyActivity.this.finish();
            } else if("failed".equals(url)) {
                PersonalInfoSupplyActivity.this.setResult(0);
                PersonalInfoSupplyActivity.this.finish();
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
