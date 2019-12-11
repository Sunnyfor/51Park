package cn.com.unispark.application;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.model.CameraPosition;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.unispark.login.LoginActivity;
import cn.com.unispark.login.entity.UserInfo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToolUtil;

public class ParkApplication extends MultiDexApplication implements
		AMapLocationListener {
	/**
	 * 用户基本信息实体类
	 */
	private static UserInfo mUserInfo;
	/**
	 * 用户别名
	 */
	public static String mUserAlias = null;
	/**
	 * 用户Id号
	 */
	public static String mUserId = "";
	/**
	 * 停车场Id号
	 */
	public static String mParkId = "";
	/**
	 * 订单号
	 */
	public static String mOrderNum = "";
	/**
	 * 订单状态(根据此状态跳转不同的界面) mOrderState == 6 ？交费成功界面 ： 交停车费界面
	 */
	public static int mOrderState;
	/**
	 * 当前位置的纬度
	 */
	public static double mLat;
	/**
	 * 当前位置的经度
	 */
	public static double mLon;

	/**
	 * 目的地位置的纬度
	 */
	public static double mLatEnd;
	/**
	 * 目的地位置的经度
	 */
	public static double mLonEnd;

	/**
	 * <pre>
	 * 交停车费的支付方式
	 * 1.支付宝支付
	 * 2.微信支付
	 * 3.有贝支付
	 * 4.信用卡支付
	 * 5.余额支付
	 * 5.零元支付
	 * </pre>
	 */
	public static int mPayParkType;

	/**
	 * 租赁订单号
	 */
	public static String mLeaseOrderNum;

	/**
	 * 优惠劵id
	 */
	public static String mCouponId = "";

	/**
	 * 实付费（应交金额+余额）
	 */
	public static String mCostAfter = "0";

	/**
	 * 余额交的金额
	 */
	public static String mBalance = "0";

	/**
	 * 有关地图的属性设置
	 */
	public static float mMapZoomLevel = 17f;// 地图缩放级别

	/**
	 * 高德地图当前位置的坐标点
	 */
	public static CameraPosition mCurrentPosition = null;

	/**
	 * <pre>
	 * 第三方支付,根据回调地址来判断是哪个界面进入的
	 * 1.支付宝支付的回调：ALIPAY_NOTIFY_URL
	 * 2.支付宝充值的回调：ALIPAY_RECHARGE_URL
	 * 3.支付宝租赁的回调：ALIPAY_PARKCARD_URL
	 * 4.微信支付的回调：WEIXIN_NOTIFY_URL
	 * 5.微信充值的回调：WEIXIN_RECHARGE_URL
	 * 6.微信宝租赁的回调：WEIXIN_PARKCARD_URL
	 * </pre>
	 */
	public static String mNotifyUrlPage;

	/**
	 * <pre>
	 * 停车场租赁类型：包月or计次,在租赁结果页展示不同布局所用
	 * 0.月卡日卡 
	 * 1.月卡 
	 * 2.日卡
	 * </pre>
	 */
	public static int mLeaseType;

	/**
	 * 定位服务类,这个定位类允许应用定时更新获取设备的地理位置,或者当这个设备进入指定的地理位置时,启动一个应用指定的Intent
	 */
	private LocationManagerProxy mLocationManagerProxy;

	/**
	 * 全局Context
	 */
	public static Context applicationContext = null;
	
	/**
	 * 左郁zuoyu
	 */

	/**
	 * 判断是否为第一次更新
	 */
//	public static boolean isFirstUpdate = true;
	/**
	 * 判断是否为第一次进入应用
	 */
//	public static boolean isLogin = false;
//	public static boolean isFirstIn = true;


//	public static String mApiUrl = "http://cs.51park.com.cn/";
	public static boolean isSearch = false;// 推荐停车场搜索完毕为true

	/**
	 * 用户的手机号
	 */
	// public static String userMobile;
	public static int parkOnSaleID = 12345678; // 停车场业务id
	public static String parkSerial = "012345678";
	public static String parkName = "test停车场";
	public static String carNo;// = "京PNHO74";//"京G12185"; 京B123456 //"京PNHO74"
	/**
	 * 用户帐号，即手机号
	 */
	public static boolean isMsgDetail = false;
	public static boolean isopen = false;
	public static boolean map_windowISvisible = false;
	public static boolean isMapVisible = false;

	public static ArrayList<HashMap<String, String>> locPayParksList = null;

	public static double bMapLat;
	public static double bMapLon;
	public static double aMapLat;
	public static double aMapLon;
	public static String citySheng = null;
	/**
	 * 当前城市
	 */
	public static String CurrentCity = "";
	/**
	 * 城市编码
	 */
	public static String CityCode = "";
	public static boolean isPay = false;

	/**
	 * 订单号
	 */

	// public static int httpparkFee;
	// public static boolean isHttpGet = false;
	/**
	 * 判断是从哪个页面进入 （true代表是PayFeeActivity交停车费界面，false代表是YouHuiQuanActivity停车券界面）
	 */
	public static boolean isComePayFeeActivityPage = false;
	public static String coupon_id = "";
	public static String shoufeiname;
	public static boolean isRecharge = false;
	public static boolean isParkCardRelease = false;
	public static String min_goods_amount;
	/**
	 * 会员卡号，可以根据其是否有值来判断是绑卡界面还是为绑卡界面
	 */
	// public static String CardNOQr;
	// public static String CardNOdate;

	// public static String name;// 用户姓名
	// public static String address;
	// public static String paytime;// 付费时间
	// public static int payStatus;// 付款状态

	private static ParkApplication sInstance;
	private RequestQueue mRequestQueue;
	public static final String TAG = "VolleyPatterns";

	/**
	 * 判断是否绑定无忧会员卡
	 */
	public static boolean cardManager = false;
	public static double desLongitude;
	public static double desLatitude;
	public static String isRed = "";
	public static String rechargeOrderNum;// 充值订单

	/**
	 * 判断是否为零元支付
	 */
	public static boolean isZeroPay = false;
	/**
	 * 判断从停车记录中支付成功后是否刷新
	 */
	// public static boolean isRecodeFlush;

	// 注册时间
	// public static String RegDate;
	// 连连支付签约号
	// public static String Noagree;

	/**
	 * 保存从服务器上获取的最初交费金额，不包括使用的优惠券
	 */
	public static String originalMoney;

	/**
	 * 使用优惠券改变后的金额
	 */
//	public static String changeMoney;

	/**
	 * 有贝支付信用额度
	 */
//	public static String uubeeLimit = null;

	/**
	 * 包月服务开始日期
	 */
	public static String monthStartDate;
	

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		sInstance = this;
		/*
		 * 初始化有贝先付
		 */
//		PrepayAgent.init(this, true);

		/*
		 * 初始化高德定位
		 */
		initLocation();

		/*
		 * 初始化极光推送
		 */
		// JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
//		JPushInterface.init(this);

//		/*
//		 * 以上为AppSettings内容
//		 */
////		 LogUtil.autoGetUncaughtException();
////		if (ReckonUtil.getDebuggable(this)) {
////			MobclickAgent.setDebugMode(true);
////		} else {
////			MobclickAgent.setDebugMode(false);
////		}
////		MobclickAgent.setAutoLocation(false); // 不收集地理信息
////		if (getMetaData(this, "UMENG_APPKEY")
////				.equals("518275d856240b159102f6cb")) {
////			mApiUrl = "http://cs.51park.com.cn/";
////		} else {
////			mApiUrl = "http://www.51park.com.cn/";
////		}

	}

	/**
	 * <pre>
	 * 功能说明：获取用户基本信息
	 * 日期：	2015年12月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return
	 * </pre>
	 */
	public static UserInfo getmUserInfo() {
		if (mUserInfo == null) {
			mUserInfo = new UserInfo();
			String uid = ShareUtil.getSharedString("uid");
			if(uid == null){
				uid = "";
			}
			mUserInfo.setUid(uid);
		}
		return mUserInfo;
	}

	/**
	 * <pre>
	 * 功能说明：用户基本信息赋值
	 * 日期：	2015年12月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param userInfo
	 * </pre>
	 */
	public static void setmUserInfo(UserInfo userInfo) {
		mUserInfo = userInfo;
	}

	/**
	 * <pre>
	 * 功能说明：判断是否登录
	 * 日期：	2015年12月16日
	 * 开发者：	陈丶泳佐
	 * 	
	 * @return	true 登录		false 未登录
	 * </pre>
	 */
	public static boolean isLogin(Activity activity) {
		if (!TextUtils.isEmpty(ParkApplication.getmUserInfo().getUid())) {
			return true;
		} else {
			ToolUtil.IntentClass(activity, LoginActivity.class, false);
		}
		return false;
	}

	public static synchronized ParkApplication getInstance() {
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public void addToRequestQueue(Request req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		// VolleyLog.d("Adding request to queue: %s", req.getUrl());
		getRequestQueue().add(req);
	}

	// public void addToRequestQueue(Request req) {
	// // set the default tag if tag is empty
	// req.setTag(TAG);
	// getRequestQueue().add(req);
	// }

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	private void initLocation() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 2 * 1000, 10, this);
	}

	public static String getMetaData(Context context, String strMetaDataName) {
		if (context == null)
			return null;
		PackageManager packageManager = context.getPackageManager();

		try {
			ApplicationInfo ai = packageManager.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);

			if (ai != null) {

				Bundle bundle = ai.metaData;
				if (bundle == null)
					return null;

				String strName = bundle.getString(strMetaDataName);
				return strName;
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 退出应用
	 */
	public static List<Activity> activityList = new ArrayList<Activity>();
	public static boolean isRecodeFlush;
	public static String[] CityPoint = new String[2];

	public static void quitActivity() {
		for (Activity activity : activityList) {
			if (null != activity) {
				activity.finish();
			}
		}
		activityList.clear();
	}

	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public static void delActivity(Activity activity) {
		activityList.remove(activity);
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {

		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			mLat = amapLocation.getLatitude();
			mLon = amapLocation.getLongitude();
			if (amapLocation.getProvince() != null) {
				// province = amapLocation.getProvince();
			}
		} else {
			LogUtil.showLog(3, "Location ERR:"
					+ amapLocation.getAMapException().getErrorCode());
		}

	}

}
