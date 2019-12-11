//package cn.com.unispark.fragment.home.viewpager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnKeyListener;
//import android.view.View.OnTouchListener;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebSettings.RenderPriority;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import cn.com.unispark.R;
//import cn.com.unispark.application.BaseActivity;
//import cn.com.unispark.fragment.mine.coupons.CouponsActivity;
//import cn.com.unispark.util.ToastUtil;
//import cn.com.unispark.util.ToolUtil;
//
///**
// * <pre>
// * 功能说明： 跳转到某【活动页面】的布局
// * 日期：	2016年9月30日
// * 开发者：	陈丶泳佐
// * 版本信息：V
// * 版权声明：版权所有@北京百会易泊科技有限公司
// * 
// * 历史记录
// *    修改内容：
// *    修改人员：
// *    修改日期： 2016年9月30日
// * </pre>
// */
//public class WebViewActivity extends BaseActivity {
//
//	// 导航栏标题 //返回按钮
//	private TextView titleText;
//	private LinearLayout backLLayout;
//
//
////	private ProgressBar progressBar;
//	private List<String> history = new ArrayList<String>();
//	private boolean blockLoadingNetworkImage;
//	
//	private Handler mHandler; 
//	private String mWebUrl;
//	private WebView mWebView;
//	private WebSettings mWebSettings; 
//
//	@Override
//	public void setContentLayout() {
//		setContentView(R.layout.webview_main);
//		loadingProgress.show();
//	}
//
//	@SuppressLint("JavascriptInterface")
//	@Override
//	public void initView() {
//
//		// 导航栏标题
//		String title = getIntent().getStringExtra("title");
//		titleText = (TextView) findViewById(R.id.titleText);
//		titleText.setText(!TextUtils.isEmpty(title) ? title : "活动页面");
//
//		// 返回按钮
//		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
//		backLLayout.setOnClickListener(this);
//
//		mWebView = (WebView) findViewById(R.id.webview);
//		mWebView.setBackgroundColor(0);
//		
//	    mHandler = new Handler(); 
//	 
//	        // 设置支持JavaScript等 
//	        mWebSettings = mWebView.getSettings(); 
//	        mWebSettings.setJavaScriptEnabled(true); 
//	        mWebSettings.setBuiltInZoomControls(true); 
//	        mWebSettings.setLightTouchEnabled(true); 
//	        mWebSettings.setSupportZoom(true); 
//	        mWebView.setHapticFeedbackEnabled(false); 
//	        // mWebView.setInitialScale(0); // 改变这个值可以设定初始大小 
//	 
//	        //重要,用于与页面交互! 
//	        mWebView.addJavascriptInterface(new Object() { 
//	            @SuppressWarnings("unused") 
//	            public void oneClick(final String locX, final String locY) {//此处的参数可传入作为js参数 
//	                mHandler.post(new Runnable() { 
//	                    public void run() { 
//	                        mWebView.loadUrl("javascript:shows(" + locX + "," + locY + ")"); 
//	                    } 
//	                }); 
//	            } 
//	        }, "demo");//此名称在页面中被调用,方法如下: 
//	        //<body onClick="window.demo.clickOnAndroid(event.pageX,event.pageY)"> 
//	 
//	        final String mimeType = "text/html"; 
//	        final String encoding = "utf-8"; 
//	        final String html = "";// TODO 从本地读取HTML文件 
//	 
//	        mWebView.loadDataWithBaseURL("file:///sdcard/", html, mimeType, 
//	                encoding, ""); 
//	 
//	        
////			 webUrl = "http://120.55.116.225:8888/cnpc/qdy/loading.shtml";
////			 webUrl = "http://192.168.1.36/a/index.html";
////			 webUrl = "http://www.leizw.net/wap2/A.html";
//			mWebUrl = "http://124.172.220.199:10001/illegal?user_id=123456&user_from=sograndwap&tel=13126596191&token=bUSNUlLO%2FOBf4WOK9GHw1wUBh6DN5amHLLqwkatz5VM%3D";
////			webUrl = getIntent().getStringExtra("url");
//			mWebView.loadUrl(mWebUrl);
//			
//	    } 
//
//
//	@Override
//	public void onClickEvent(View v) {
//		switch (v.getId()) {
//		case R.id.backLLayout:
//			finish();
//			break;
//		}
//
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onResume() {
//		super.onResume();
////		 webUrl = "http://120.55.116.225:8888/cnpc/qdy/loading.shtml";
////		 webUrl = "http://192.168.1.36/a/index.html";
////		 webUrl = "http://www.leizw.net/wap2/A.html";
//		mWebUrl = "http://124.172.220.199:10001/illegal?user_id=123456&user_from=sograndwap&tel=13126596191&token=bUSNUlLO%2FOBf4WOK9GHw1wUBh6DN5amHLLqwkatz5VM%3D";
////		webUrl = getIntent().getStringExtra("url");
//		if (mWebUrl.equals("www.baidu.com")) {
//			mWebUrl = "http://www.baidu.com";
//		}
//		history.clear();
//		mWebView.clearView();
//		mWebView.loadUrl(mWebUrl);
//	}
//
//	@TargetApi(11)
//	private void setZoomControls(WebView webView) {
//		// 隐藏缩放按钮
//		mWebView.getSettings().setBuiltInZoomControls(true);
//		// 设定缩放控件隐藏
//		mWebView.getSettings().setDisplayZoomControls(false);
//		
//	}
//
//	@SuppressWarnings("deprecation")
//	@TargetApi(11)
//	private void setSmoothTransition(WebView webView) {
//		webView.getSettings().enableSmoothTransition();
//	}
//
//	@SuppressWarnings("deprecation")
//	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
//	
//	private void initWebView() {
//
//		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.getSettings().setSupportZoom(true);
////		让web视图DomStorage持续性的应用程序关闭后
//		mWebView.getSettings().setDomStorageEnabled(true); 
//		if (Build.VERSION.SDK_INT >= 11) {
//			setZoomControls(mWebView);
//			setSmoothTransition(mWebView);
//		}
//		// webview.getSettings().setLightTouchEnabled(true); //启用后搜狐视频打不开播放器
//
//				// 将定义回调方法的对象注入WebView中，这样html5就能通过注入的这个对象调用本地方法。【注入的对象的引用叫
//				// “robot”】
//				mWebView.addJavascriptInterface(new Object() {
//					@JavascriptInterface
//					public void callFromJSBasicDataType(String userId) {
//						// 操作订单号
//						System.out.println("截取Userid" + userId);
//						ToastUtil.show("截取Userid" + userId);
//					}
//				}, "interface");
//
//		// // 将定义回调方法的对象注入WebView中，这样html5就能通过注入的这个对象调用本地方法。【注入的对象的引用叫 “robot”】
//		// webview.addJavascriptInterface(new Object() {
//		// @JavascriptInterface
//		// public void callAndroidMethod() {
//		// ToolUtil.IntentClass(activity, CouponsActivity.class, false);
//		// }
//		// }, "robot");
//
//		// html5缓存<<<
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setDomStorageEnabled(true);// 设置可以使用localStorage
//
//		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 默认使用缓存
//		// webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
//		// webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//有缓存不加载网络
//		webSettings.setAppCacheEnabled(true);
//		webSettings.setAppCacheMaxSize(50 * 1024 * 1024);
//		webSettings.setAllowFileAccess(true);
//		// >>>html5缓存
//
//		// 自适应窗口
//		webSettings.setUseWideViewPort(true); // WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
//		webSettings.setLoadWithOverviewMode(true);
//
//		// 浏览器不支持多窗口显示，意思就是说所有页面在单一窗口打开，这样避免了页面布局控制显示问题，也便于操作控制页面
//		webSettings.setSupportMultipleWindows(false);
//
//		webSettings.setRenderPriority(RenderPriority.HIGH);
//
//		webSettings.setBlockNetworkImage(true); // 阻止网络图片
//		blockLoadingNetworkImage = true;
//
//		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		// //图片适应窗口
//
////		webview.setWebChromeClient(new WebChromeClient() {
////			public void onProgressChanged(WebView view, int progress) {
////				// 根据加载程度决定进度条的进度大小 当加载到100%的时间进度条自动消失
////				if (mLoading.getVisibility() != View.VISIBLE) {
////					mLoading.setVisibility(View.VISIBLE);
////				}
////				progressBar.setProgress(progress);
////				if (progress == 100) {
////					mLoading.setVisibility(View.GONE);
////				}
////				if (progress >= 100) {
////					if (blockLoadingNetworkImage) {
////						webview.getSettings().setBlockNetworkImage(false);
////						blockLoadingNetworkImage = false;
////					}
////				}
////			}
////		});
//
//		mWebView.setOnTouchListener(new OnTouchListener() {
//			@SuppressLint("ClickableViewAccessibility")
//			@Override
//			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
//				// if (mLoadingText.getVisibility() != View.GONE) {
//				// if (progressBar.getProgress() > 40) {
//				// mLoadingText.setVisibility(View.GONE);
//				// }
//				// }
//				loadingProgress.dismiss();
//				return false;
//			}
//		});
//
//		mWebView.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				super.onPageFinished(view, url);
//				// mLoadingText.setVisibility(View.GONE);
//				loadingProgress.dismiss();
//				mWebView.requestFocus();
//			}
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//			}
//
//			// 页面跳转
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view,
//					final String url) {
//				mWebView.getSettings().setBlockNetworkImage(true);
//				blockLoadingNetworkImage = true;
//				return false;
//			}
//		});
//		mWebView.setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//					if (mWebView != null && mWebView.canGoBack()
//							&& history.size() > 1) {
//						history.remove(0);
//						mWebView.loadUrl(history.get(0));
//						return true;
//					} else {
//						return false;
//					}
//				}
//				return false;
//			}
//		});
//	}
//
//	@Override
//	public void onDestroy() {
//		mWebView.stopLoading();
//		mWebView.destroy();
//		super.onDestroy();
//	}
//
//}
