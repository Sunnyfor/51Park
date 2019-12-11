package cn.com.unispark;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.MainViewPager;
import cn.com.unispark.fragment.home.HomeFragment;
import cn.com.unispark.fragment.mine.MineFragment;
import cn.com.unispark.fragment.mine.msgpush.util.ExampleUtil;
import cn.com.unispark.fragment.treasure.TreasureFragment;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * <pre>
 * 功能说明： 【首页】主界面
 * 日期：	2015年3月14日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 			
 * 历史记录
 *    修改内容：
 *    修改人员：		
 *    修改日期： 2015年6月10日
 * </pre>
 */
@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends BaseActivity {

	// 退出时间
	public long mLastPressTime;

	// 当前点击的View的ID,防止多次重复点击使用
	// private int currentViewId;
	public static MainViewPager mViewPager;
	// 设置别名
	private static final int MSG_SET_ALIAS = 10;
	private String imei;

	// 首页//百宝箱//我的
	public static RadioButton homeRBtn, babyRbtn, mineRBtn;

	private RadioGroup radiogroup;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.main);

		// doUmengForceUpdate();
		// 请求在线参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);

		// 调用如下函数得到在线参数的数值,需在友盟上自定义配置"char_update"
		String upgrade_mode = OnlineConfigAgent.getInstance().getConfigParams(
				this, "char_update");
		LogUtil.e("【友盟强制更新在线参数】" + upgrade_mode);

		/**
		 * <pre>
		 *  说明：
		 *  默认是在非Debug的模式下，在Debug模式下，可以输出日志信息。
		 *  默认在非Debug模式下，是受请求时间间隔的限制的，即两次请求的时间间隔应该大于10分钟， 而在Debug模式下，是不受时间间隔限制的。
		 * </pre>
		 */
		OnlineConfigAgent.getInstance().setDebugMode(true);

		if (TextUtils.isEmpty(upgrade_mode)) {
			return;
		}

		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);
		UmengUpdateAgent.forceUpdate(this); // 强制更新关键步骤

		try {
			PackageManager pmanager = this.getPackageManager();
			PackageInfo info = pmanager
					.getPackageInfo(this.getPackageName(), 0);
			String versionCode = String.valueOf(info.versionCode);
			LogUtil.e("【友盟自动更新版本号】" + versionCode);

			if (!upgrade_mode.equals(versionCode)) {

				// 进入强制更新：监听检测更新的结果
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateResponse) {
						switch (updateStatus) {
						case UpdateStatus.Yes:
							UmengUpdateAgent.showUpdateDialog(
									MainActivity.this, updateResponse);
							break;
						case UpdateStatus.No:

							break;
						case UpdateStatus.NoneWifi:
							ToastUtil.show("没有wifi连接， 只在wifi下更新");
							break;
						case UpdateStatus.Timeout:
							ToastUtil.show("更新超时");
							break;
						default:
							break;
						}

					}
				});

				// 监听对话框按键操作
				UmengUpdateAgent
						.setDialogListener(new UmengDialogButtonListener() {
							@Override
							public void onClick(int status) {

								switch (status) {
								case UpdateStatus.Update:
									// 用户选择现在更新;
									break;
								case UpdateStatus.Ignore:
									// 用户选择忽略该版;
									break;
								case UpdateStatus.NotNow:
									// 用户选择以后再说，点击回退键，关闭对话框。
									ToastUtil.show("非常抱歉，您需要更新应用才能继续使用");
									finish();
									break;

								}

							}
						});
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initView() {

		// 选项卡
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		ViewUtil.setViewSize(radiogroup, 98, 0);
		ViewUtil.setPaddingTop(radiogroup, 8);
		ViewUtil.setPaddingBottom(radiogroup, 8);

		// 首页
		homeRBtn = (RadioButton) findViewById(R.id.home_rbtn);
		ViewUtil.setTextSize(homeRBtn, 20);

		// 百宝箱
		babyRbtn = (RadioButton) findViewById(R.id.baby_rbtn);
		ViewUtil.setTextSize(babyRbtn, 20);

		// 我的
		mineRBtn = (RadioButton) findViewById(R.id.mine_rbtn);
		ViewUtil.setTextSize(mineRBtn, 20);

		mViewPager = (MainViewPager) findViewById(R.id.viewPager);
		mViewPager.setScanScroll(true);
		mViewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public Fragment getItem(int index) {
				switch (index) {
				case 0:
					return new HomeFragment();
				case 1:
					return new TreasureFragment();
				case 2:
					return new MineFragment();
				}
				return null;
			}
		});
		// 切换viewpagerItem
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (homeRBtn.getId() == checkedId) {
					mViewPager.setCurrentItem(0);
					MobclickAgent.onEvent(context, "home_Home_click");
				}
				if (babyRbtn.getId() == checkedId) {
					mViewPager.setCurrentItem(1);
					MobclickAgent.onEvent(context, "home_MagicBox_click");
				}
				if (mineRBtn.getId() == checkedId) {
					mViewPager.setCurrentItem(2);
					MobclickAgent.onEvent(context, "home_my_click");
				}
			}
		});
//		mViewPager.setOffscreenPageLimit(0);
		imei = ExampleUtil.getImei(getApplicationContext(), "");
		setAlias(ParkApplication.getmUserInfo().getUid(), imei); // 设置别名的方法
	}

	@Override
	public void onClickEvent(View v) {

	}

	private Handler hanler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SET_ALIAS:
				LogUtil.e("【别名】" + (String) msg.obj);
				JPushInterface.setAliasAndTags(context, (String) msg.obj, null,
						mAliasCallback);
				break;
			}
			return false;
		}
	});

	/**
	 * <pre>
	 * 功能说明：友盟强制更新
	 * 日期：	2015年12月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void doUmengForceUpdate() {

		// 请求在线参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);

		// 调用如下函数得到在线参数的数值,需在友盟上自定义配置"char_update"
		String upgrade_mode = OnlineConfigAgent.getInstance().getConfigParams(
				this, "char_update");
		LogUtil.e("【友盟强制更新在线参数】" + upgrade_mode);

		/**
		 * <pre>
		 *  说明：
		 *  默认是在非Debug的模式下，在Debug模式下，可以输出日志信息。
		 *  默认在非Debug模式下，是受请求时间间隔的限制的，即两次请求的时间间隔应该大于10分钟， 而在Debug模式下，是不受时间间隔限制的。
		 * </pre>
		 */
		OnlineConfigAgent.getInstance().setDebugMode(true);

		if (TextUtils.isEmpty(upgrade_mode)) {
			return;
		}

		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);
		UmengUpdateAgent.forceUpdate(this); // 强制更新关键步骤

		try {
			PackageManager pmanager = this.getPackageManager();
			PackageInfo info = pmanager
					.getPackageInfo(this.getPackageName(), 0);
			String versionCode = String.valueOf(info.versionCode);
			LogUtil.e("【友盟自动更新版本号】" + versionCode);

			if (!upgrade_mode.equals(versionCode)) {

				// 进入强制更新：监听检测更新的结果
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateResponse) {
						switch (updateStatus) {
						case UpdateStatus.Yes:
							UmengUpdateAgent.showUpdateDialog(
									MainActivity.this, updateResponse);
							break;
						case UpdateStatus.No:

							break;
						case UpdateStatus.NoneWifi:
							ToastUtil.show("没有wifi连接， 只在wifi下更新");
							break;
						case UpdateStatus.Timeout:
							ToastUtil.show("更新超时");
							break;
						default:
							break;
						}

					}
				});

				// 监听对话框按键操作
				UmengUpdateAgent
						.setDialogListener(new UmengDialogButtonListener() {
							@Override
							public void onClick(int status) {

								switch (status) {
								case UpdateStatus.Update:
									// 用户选择现在更新;
									break;
								case UpdateStatus.Ignore:
									// 用户选择忽略该版;
									break;
								case UpdateStatus.NotNow:
									// 用户选择以后再说，点击回退键，关闭对话框。
									ToastUtil.show("非常抱歉，您需要更新应用才能继续使用");
									finish();
									break;

								}

							}
						});
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
	            if ((System.currentTimeMillis() - mLastPressTime) > 2000) {
	                ToastUtil.show("再按一次退出程序");
	                mLastPressTime = System.currentTimeMillis();
	            } else {
	            	ParkApplication.quitActivity();
	                finish();
	                return false;
	            }
	        }
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	

	/**
	 * 设置别名 的回调
	 */
	private TagAliasCallback mAliasCallback = new TagAliasCallback() {
		/*
		 * responseCode = 0，则确认设置别名成功 其他返回码请参考错误码定义。 alias 原设置的别名 tags 原设置的标签
		 */
		@Override
		public void gotResult(int code, final String alias, Set<String> tags) {
			switch (code) {
			case 0:
				LogUtil.e("别名设置成功");
				new Thread(new Runnable() {
					@Override
					public void run() {
						ParkApplication.mUserAlias = alias;
						parseSetAlias(alias);
					}
				}).start();
				break;
			case 6002:
				LogUtil.e("别名设置超时，60秒后重试");
				if (ExampleUtil.isConnected(getApplicationContext())) {
					hanler.sendMessageDelayed(
							hanler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				}
				break;
			}
		}
	};

	/**
	 * <pre>
	 * 功能说明：设置别名的方法
	 * 日期：	2015年12月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param user_id
	 * @param imei
	 * </pre>
	 */
	private void setAlias(String user_id, String imei) {
		// 所以在判断字符串是否为空时，先判断是不是对象，如果是，再判断是不是空字符串
		if (user_id == null || user_id.length() <= 0) {
			// ToastUtil.showToast("alias不能为空");
			return;
		}

		if (imei == null || imei.length() <= 0) {
			// ToastUtil.showToast("alias不能为空");
			return;
		}
		StringBuilder bulider = new StringBuilder();
		bulider.append(user_id).append(imei).toString();
		String myAlias = bulider.toString();

		// 校验Tag Alias 只能是数字,英文字母和中文
		if (!ExampleUtil.isValidTagAndAlias(myAlias)) {
			Toast.makeText(context, "别名格式不对", Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用JPush API设置Alias
		hanler.sendMessage(hanler.obtainMessage(MSG_SET_ALIAS, myAlias));
	}

	/**
	 * <pre>
	 * 功能说明：【解析】设置别名
	 * 日期：	2015年11月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param alias
	 * </pre>
	 */
	private void parseSetAlias(final String alias) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());// 用户或收费员端id号
		params.put("phone", ParkApplication.getmUserInfo().getUsername());// 手机号
		params.put("registration_id", imei);// 设备标识（终端唯一标识）
		params.put("tag", "51park");// 设备的标签
		params.put("alias", alias);// 设备的别名（针对推送该用户标识）
		params.put("type", "1");// 1：用户端；2：收费员端；3：无忧停车管家
		params.put("os", "1");// // 1: 安卓，2: iOS

		LogUtil.d("【设置别名 URL】" + Constant.ALIAS_URL + params);

		httpUtil.parseno(httpUtil.POST, Constant.ALIAS_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						if (code == httpUtil.SERVER_REQ_OK) {
							ParkApplication.mUserAlias = alias;
						} else {
							LogUtil.i("设置别名："+msg);
						}
					}
				});

	}

}
