package cn.com.unispark.fragment.mine.setting.aboutinfo;

import java.io.IOException;
import java.io.InputStream;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;

/**
 * <pre>
 * 功能说明： 【服务须知】界面
 * 日期：	2015年3月28日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年3月28日
 * </pre>
 */
public class ServiceAgreeActivity extends BaseActivity {
	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	private WebView webView;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.readagree_main);

	}

	@Override
	public void initView() {
	
		// 导航栏标题 
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("服务须知");

		//返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		webView = (WebView) findViewById(R.id.webview);

		try {
			InputStream openRawResource = getResources().openRawResource(
					R.raw.park_server);

			StringBuilder html = new StringBuilder();
			byte[] data = new byte[1024];
			for (int n; (n = openRawResource.read(data)) != -1;) {
				html.append(new String(data, 0, n, "GBK"));
			}
			webView.loadDataWithBaseURL(null, html.toString(), "text/html",
					"GBK", null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}
}