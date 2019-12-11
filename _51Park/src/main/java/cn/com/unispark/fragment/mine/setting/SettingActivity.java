package cn.com.unispark.fragment.mine.setting;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.recharge.RechargeActivity;
import cn.com.unispark.fragment.mine.setting.aboutinfo.AboutInfoActivity;
import cn.com.unispark.fragment.mine.setting.clearcache.DataCleanManager;
import cn.com.unispark.fragment.mine.setting.offlinemap.DownLoadManagerActivity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * <pre>
 * 功能说明： 【设置】界面
 * 日期：	2014年12月5日
 * 开发者：	
 * 
 * 历史记录
 *    修改内容：
 *    修改人员： 陈丶泳佐
 *    修改日期： 2014年12月5日
 * </pre>
 */
@SuppressLint("SdCardPath")
public class SettingActivity extends BaseActivity {

	// 导航栏标题// 返回按钮,点击进入主页
	private TextView titleText;
	private LinearLayout backLLayout;

	// 自动支付
	/* private CheckBox auto_pay_cb; */

	// 离线地图
	private TextView offline_map_tv;

	// 清除缓存
	private RelativeLayout clear_rl;
	private TextView cache_tv;
	private String cachePath;

	// 检查更新 改为关于我们
	private RelativeLayout check_version_rl;
	private TextView version_tv;

	// 关于我们
	/* private TextView about_us_tv; */

	// 退出登录按钮
	private Button exit_login_btn;
	private File chahefile;

	public static Activity mActivity;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.setting_main);

		mActivity = SettingActivity.this;

		cachePath = Environment.getExternalStorageDirectory().toString()
				+ "/51Park/image/";
		chahefile = new File(cachePath);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (!chahefile.exists()) {
				chahefile.mkdirs();
			}
		}

	}

	@Override
	public void initView() {

		// 导航栏标题“设置”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.she_zhi));

		// 返回按钮,点击进入主页
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 自动支付
		/*
		 * auto_pay_cb = (CheckBox) findViewById(R.id.auto_pay_cb); //
		 * auto_pay_sb.setChecked(ShareUtil.getSharedBoolean("isautopay"));
		 * 
		 * 
		 * 判断自动支付按钮是否开启，1关闭 2开启
		 * 
		 * if (ParkApplication.getmUserInfo().getAutopay() == 1) {
		 * auto_pay_cb.setChecked(false); } else { auto_pay_cb.setChecked(true);
		 * }
		 * 
		 * auto_pay_cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		 * {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { if (isChecked) { parseDoAutoPay(2);// 开启自动支付 }
		 * else { parseDoAutoPay(1);// 关闭自动支付 } MobclickAgent.onEvent(context,
		 * "settings_voluntaryPayment_click"); } });
		 */

		// 离线地图
		offline_map_tv = (TextView) findViewById(R.id.offline_map_tv);
		offline_map_tv.setOnClickListener(this);

		// 清楚缓存
		clear_rl = (RelativeLayout) findViewById(R.id.close_rl);
		clear_rl.setOnClickListener(this);
		cache_tv = (TextView) findViewById(R.id.cache_tv);

		// 检查更新
		check_version_rl = (RelativeLayout) findViewById(R.id.check_version_rl);
		check_version_rl.setOnClickListener(this);
		version_tv = (TextView) findViewById(R.id.version_tv);

		// 关于我们
		/*
		 * about_us_tv = (TextView) findViewById(R.id.about_us_tv);
		 * about_us_tv.setOnClickListener(this);
		 */

		// 退出登录按钮
		exit_login_btn = (Button) findViewById(R.id.exit_login_btn);
		exit_login_btn.setOnClickListener(this);

		/*
		 * 若未登录，则隐藏自动支付提示语和退出登录按钮
		 */
		if (!ShareUtil.getSharedBoolean("islogin")) {
			// 未登录
			/*
			 * findViewById(R.id.auto_pay_rl).setVisibility(View.GONE);
			 * findViewById(R.id.prompt_tv).setVisibility(View.GONE);
			 * findViewById(R.id.line0_view).setVisibility(View.GONE);
			 * findViewById(R.id.line1_view).setVisibility(View.GONE);
			 */
			exit_login_btn.setVisibility(View.GONE);

		}

		/*
		 * 如果从交费界面跳转过来，开启自动支付功能，则不显示退出登录按钮
		 */
		if (getIntent() != null) {
			if (!getIntent().getBooleanExtra("isShow", true)) {
				exit_login_btn.setVisibility(View.GONE);
			}
		}

		/*
		 * 当前版本
		 */
		try {
			String versionString = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			if (TextUtils.isEmpty(versionString)) {
				version_tv.setText("无忧停车");
			} else {
				version_tv.setText("V" + versionString);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (DataCleanManager.getFolderSize(chahefile) != 0) {
				cache_tv.setText(DataCleanManager.getCacheSize(chahefile) + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.offline_map_tv:// 离线地图
			ToolUtil.IntentClass(activity, DownLoadManagerActivity.class, false);
			MobclickAgent.onEvent(context, "settings_offlineMap_click");
			break;
		case R.id.close_rl:// 清除缓存
			try {
				if (DataCleanManager.getFolderSize(chahefile) != 0) {
					final DialogUtil mDialog = new DialogUtil(context);
					mDialog.setMessage("确定要清除吗");
					mDialog.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							mDialog.dismiss();
							DataCleanManager.deleteFolderFile(cachePath, true);
							cache_tv.setText("0M");
						}
					});
					mDialog.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
							mDialog.dismiss();
						}
					});
				} else {
					ToastUtil.show("缓存为0，无需清除");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			MobclickAgent.onEvent(context, "settings_clearCache_click");
			break;
		case R.id.check_version_rl:// 检查更新
			/* doCheckUpdate(); */
			ToolUtil.IntentClass(activity, AboutInfoActivity.class, false);
			MobclickAgent.onEvent(context, "settings_About_click");
			break;
		/*
		 * case R.id.about_us_tv:// 关于我们 ToolUtil.IntentClass(activity,
		 * AboutInfoActivity.class, false); MobclickAgent.onEvent(context,
		 * "settings_About_click"); break;
		 */
		case R.id.exit_login_btn:// 退出登录
			final DialogUtil dialog = new DialogUtil(context);
			dialog.setMessage("确定要退出吗");
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					parseExitLogin();
				}
			});
			dialog.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			MobclickAgent.onEvent(context, "settings_logout_click");
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：检查更新
	 * 日期：	 2014年12月8日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	/*
	 * public void doCheckUpdate() { ToastUtil.showToast("正在检查更新，请稍候..."); //
	 * 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。 //
	 * com.umeng.common.Log.LOG = true; UmengUpdateAgent.setDeltaUpdate(true);
	 * // 增量更新 UmengUpdateAgent.setUpdateOnlyWifi(false); //
	 * 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
	 * UmengUpdateAgent.setUpdateAutoPopup(false);
	 * UmengUpdateAgent.setUpdateListener(updateListener);
	 * UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {
	 * 
	 * @Override public void OnDownloadStart() {
	 * 
	 * }
	 * 
	 * @Override public void OnDownloadUpdate(int progress) {
	 * 
	 * }
	 * 
	 * @Override public void OnDownloadEnd(int result, String file) {
	 * 
	 * } });
	 * 
	 * UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
	 * 
	 * @Override public void onClick(int status) { switch (status) { case
	 * UpdateStatus.Update:
	 * 
	 * break; case UpdateStatus.Ignore:
	 * 
	 * break; case UpdateStatus.NotNow:
	 * 
	 * break; } } }); UmengUpdateAgent.forceUpdate(SettingActivity.this); }
	 * 
	 * UmengUpdateListener updateListener = new UmengUpdateListener() {
	 * 
	 * @Override public void onUpdateReturned(int updateStatus, UpdateResponse
	 * updateInfo) { switch (updateStatus) { case 0: // has update
	 * UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
	 * break; case 1: // has no update ToastUtil.show("已经是最新版本"); break; case 2:
	 * // none wifi ToastUtil.show("没有wifi连接， 只在wifi下更新"); break; case 3: //
	 * time out ToastUtil.show("超时"); break; case 4: // is updating
	 * ToastUtil.show("正在下载更新..."); break; }
	 * 
	 * } };
	 */

	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size / 1000 / 1000 / 1000;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	/**
	 * <pre>
	 * 功能说明：重置别名，置空操作
	 * 日期：	2015年8月24日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void doRersetAlias() {
		// 别名置空
		JPushInterface.setAlias(context, "", new TagAliasCallback() {
			@Override
			public void gotResult(int code, String alias, Set<String> tags) {

			}
		});
		setSharedBoolean("islogin", false);
		setSharedString("uid", "");
		finish();
	}

	/**
	 * <pre>
	 * 功能说明：【解析】退出登录
	 * 日期：	2015年8月24日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void parseExitLogin() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("phone", ParkApplication.getmUserInfo().getUsername());
		params.put("push", ParkApplication.mUserAlias);
		httpUtil.parseno(httpUtil.POST, Constant.UNLOGIN_URL, params,
				new onResultTo() {

					@Override
					public void onResult(int code, String msg, String json) {
						if (code == httpUtil.SERVER_REQ_OK) {
//							ParkApplication.getmUserInfo().setUid("");
							setSharedString("token", "");
							setSharedBoolean("islogin", false);

							ParkApplication.setmUserInfo(null);

							doRersetAlias();
						}
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】开启/关闭自动支付功能
	 * 日期：	2015年11月16日
	 * 开发者：	任建飞
	 * 
	 * @param isAutoPay 1.关闭	2.开启
	 * </pre>
	 */
	private void parseDoAutoPay(final int isAutoPay) {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("autopay", String.valueOf(isAutoPay));

		LogUtil.d("【开启自动支付URL】" + Constant.AUTO_PAY_CONFIG_URL + params);

		httpUtil.parseno(httpUtil.POST, Constant.AUTO_PAY_CONFIG_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						loadingProgress.dismiss();
						if (code == httpUtil.SERVER_REQ_OK) {
							// 开启自动支付
							ToastUtil.showToast(msg);

							ParkApplication.getmUserInfo()
									.setAutopay(isAutoPay);
							// ShareUtil.setSharedBoolean("isautopay",
							// isAutoPay == 2 ? true : false);

							if ("0.00".equals(ParkApplication.getmUserInfo()
									.getUserscore()) && isAutoPay == 2) {

								showRemainRechargeDialog();
							}

						} else {
							ToastUtil.showToast(msg);
						}
					}
				});
	}

	protected void showRemainRechargeDialog() {
		final DialogUtil mLogoutDialog = new DialogUtil(context);
		mLogoutDialog.setMessage("使用余额自动支付更方便");
		mLogoutDialog.setPositiveButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLogoutDialog.dismiss();
			}
		});
		mLogoutDialog.setNegativeButton("去充值", new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLogoutDialog.dismiss();
				ToolUtil.IntentClass(activity, RechargeActivity.class, true);
			}
		});
	}

}
