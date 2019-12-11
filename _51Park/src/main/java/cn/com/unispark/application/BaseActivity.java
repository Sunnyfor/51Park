package cn.com.unispark.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.define.autolayout.AutoFrameLayout;
import cn.com.unispark.define.autolayout.AutoLayout;
import cn.com.unispark.define.autolayout.AutoLinearLayout;
import cn.com.unispark.define.autolayout.AutoRelativeLayout;
import cn.com.unispark.util.HttpUtil;
import cn.jpush.android.api.JPushInterface;


/**
 * <pre>
 * 功能说明: Activity的父类
 * 日期:	2014-10-21
 * 开发者:	陈丶泳佐
 *
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014-10-21
 * </pre>
 */
public abstract class BaseActivity extends FragmentActivity implements
        OnClickListener {

    /**
     * 全局AQuery对象
     */
    // public AQuery aQuery;

    /**
     * 全局Context对象
     */
    public Context context;

    /**
     * 全局Activity对象
     */
    public static Activity activity;

    /**
     * 是否允许全屏
     */
    public boolean isAllowFullScreen;

    // 获取每个类的包名
    /*
     * public String TAG = this.getClass().getCanonicalName()
	 * .split("cn.com.unispark.")[1];
	 */
    public String TAG = this.getClass().getCanonicalName();

    /**
     * 网络请求对象
     */
    public HttpUtil httpUtil;

    /**
     * 公用加载进度条
     */
    public LoadingProgress loadingProgress;

    /**
     * 屏幕自适应
     */
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null)
            return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = this;

        ParkApplication.addActivity(activity);

        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        AutoLayout.getInstance().auto(this, true);

        httpUtil = new HttpUtil(context);
        loadingProgress = new LoadingProgress(context);

        // 设置默认为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (isAllowFullScreen) {
            setFullScreen(true);
        } else {
            setFullScreen(false);
        }

        setContentLayout();
        initView();

        // 沉浸式状态栏
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // setTranslucentStatus(true);
        // SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // // 激活状态栏设置
        // tintManager.setStatusBarTintEnabled(true);
        // // 激活导航栏设置
        // tintManager.setNavigationBarTintEnabled(true);
        //
        // tintManager.setTintResource(R.color.gray_title_bg);
        // }

    }

    /**
     * <pre>
     * 功能说明：设置布局文件
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public abstract void setContentLayout();

    /**
     * <pre>
     * 功能说明：实例化布局文件/组件
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public abstract void initView();

    /**
     * <pre>
     * 功能说明：onClick方法的封装，在此方法中处理点击
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public abstract void onClickEvent(View v);

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParkApplication.delActivity(this);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * <pre>
     * 功能说明：打印log日志
     * 日期：	2015年6月29日
     * 开发者：	陈丶泳佐
     *
     * @param content
     * @param tag：1--i,2--e,3--d
     * </pre>
     */
    public void showLog(int tag, String content) {
        switch (tag) {
            case 1:
                Log.i("miao", "【左郁】（" + TAG + "）\n" + content);
                break;
            case 2:
                Log.e("miao", "【左郁】（" + TAG + "）\n" + content);
                break;
            case 3:
                Log.d("miao", "【左郁】（" + TAG + "）\n" + content);
                break;
        }
    }

    /**
     * <pre>
     * 功能说明：SharedPreferences保存数据
     * 日期：	2015年1月28日
     * 开发者：	陈丶泳佐
     *
     * @return SharedPreferences
     * </pre>
     */
    public SharedPreferences onSharedObject() {
        SharedPreferences shp = this.getSharedPreferences("51Park",
                Context.MODE_PRIVATE);
        return shp;
    }

    /**
     * <pre>
     * 功能说明：保存String字符串信息
     * 日期：	2015年1月28日
     * 开发者：	陈丶泳佐
     *
     * @param key    键值
     * @param content    内容
     * </pre>
     */
    public void setSharedString(String key, String content) {
        onSharedObject().edit().putString(key, content).commit();
    }

    public String getSharedString(String key) {
        return onSharedObject().getString(key, "");
    }

    /**
     * <pre>
     * 功能说明：保存Boolean类型的信息
     * 日期：	2015年1月28日
     * 开发者：	陈丶泳佐
     *
     * @param key    键值
     * @param flag    条件
     * </pre>
     */
    public void setSharedInteger(String key, int flag) {
        onSharedObject().edit().putInt(key, flag).commit();
    }

    public void setSharedBoolean(String key, boolean flag) {
        onSharedObject().edit().putBoolean(key, flag).commit();
    }

    public boolean getSharedBoolean(String key) {
        return onSharedObject().getBoolean(key, false);
    }

    public int getSharedInteger(String key) {
        return onSharedObject().getInt(key, 0);

    }

    /**
     * <pre>
     * 功能说明：得到屏幕宽度
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * @return 宽度
     * </pre>
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ParkApplication.applicationContext
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * <pre>
     * 功能说明：得到屏幕高度
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * @return 高度
     * </pre>
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ParkApplication.applicationContext
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * <pre>
     * 功能说明：是否全屏和显示标题，
     * 需要注意：true为全屏和无标题,false为无标题,请在setContentView()方法前调用
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * @param fullScreen
     * </pre>
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

    }

    /**
     * <pre>
     * 功能说明：短时间显示Toast
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * @param content  显示的文字内容
     * </pre>
     */
    public void showToast(String content) {
        Toast.makeText(ParkApplication.applicationContext, content,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * <pre>
     * 功能说明：长时间显示Toast
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * @param content  显示的文字内容
     * </pre>
     */
    public void showToastLong(String content) {
        Toast.makeText(ParkApplication.applicationContext, content,
                Toast.LENGTH_LONG).show();
    }

    /**
     * <pre>
     * 功能说明：短时间显示Toast
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public void showToast(int resId) {
        Toast.makeText(ParkApplication.applicationContext, resId,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * <pre>
     * 功能说明：长时间显示Toast
     * 日期：	2015年6月9日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public void showToastLong(int resId) {
        Toast.makeText(ParkApplication.applicationContext, resId,
                Toast.LENGTH_LONG).show();
    }

    /**
     * <pre>
     * 功能说明：网络不可用时返回的提示语："网络异常，请检查网络设置!"
     * 日期：	2015年1月20日
     * 开发者：陈丶泳佐
     *
     * </pre>
     */
    public void showToastNetError() {
        Toast.makeText(ParkApplication.applicationContext, "网络异常，请检查网络设置!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * <pre>
     * 功能说明：JSON获取为空时返回的提示语:"数据获取失败,请检查网络设置!"
     * 日期：	2015年3月3日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public void showToastJsonError() {
        Toast.makeText(ParkApplication.applicationContext, "数据获取失败,请检查网络设置!",
                Toast.LENGTH_SHORT).show();
    }


    /**
     * <pre>
     * 功能说明：Intent跳转
     * 日期：	2015年1月8日
     * 开发者：   陈丶泳佐
     *
     * @param activity    当前的Activity
     * @param clazz        跳转的Activity
     * @param isFinish    是否关闭当前的Activity
     * </pre>
     */
    public void onIntentClass(Activity activity, Class<?> clazz,
                              boolean isFinish) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        } else {

        }
    }

    /**
     * <pre>
     * 功能说明：Intent跳转传值
     * 日期：	2015年1月8日
     * 开发者：   陈丶泳佐
     *
     * @param clazz        跳转的Activity
     * @param paramsName    参数名称
     * @param paramsContent    参数值
     * @param isFinish    是否关闭当前的Activity
     * </pre>
     */
    public void onIntentClass(Activity context, Class<?> clazz,
                              String paramsName, String paramsContent, boolean isFinish) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(paramsName, paramsContent);
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        } else {

        }
    }

    /**
     * <pre>
     * 功能说明：判断当前是否有网络连接
     * 日期：	2015年6月4日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public boolean isNetConn() {
        ConnectivityManager connManager = (ConnectivityManager) this
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * <pre>
     * 功能说明：判断当前是否有网络连接
     * 日期：	2015年6月4日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public boolean isNetworkConn() {
        ConnectivityManager connManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnected()
                || connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnected();
    }

    /**
     * <pre>
     * 功能说明：判断当前是否是WIFI连接状态
     * 日期：	2015年6月4日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public boolean isWifiConn() {
        ConnectivityManager connManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    /**
     * <pre>
     * 功能说明：防止按钮连续点击
     * 日期：	2015年1月22日
     * 开发者：	陈丶泳佐
     *
     * @return flag
     * </pre>
     */
    public boolean isFastDoubleClick() {
        long lastClickTime = 0;
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * <pre>
     * 功能说明： 判断手机号码是否正确
     * 日期：	2015年6月12日
     * 开发者：	陈丶泳佐
     *
     * @param mobileNum 手机号
     * @return
     * </pre>
     */
    public boolean isMobileNum(String mobileNum) {
        char[] edit_shoujihao = mobileNum.toCharArray();
        for (int i = 0; i < edit_shoujihao.length; i++) {
            if (edit_shoujihao[i] < 48 || edit_shoujihao[i] > 57) {
                return false;
            }
        }
        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[012356789])|(18[0-9])|(14[57]))\\d{8}$");
        Pattern p = Pattern
                .compile("^((17[0-9])|(13[0-9])|(15[012356789])|(18[0-9])|(14[57]))\\d{8}$");
        Matcher m = p.matcher(mobileNum);
        Log.e("miao", "【手机号格式验证】" + m.matches());
        return m.matches();
    }


    /**
     * <pre>
     * 功能说明：追加参数到URL,方便调试
     * 日期：	2015年1月10日
     * 开发者：   陈丶泳佐
     *
     * @param params
     * @return URL
     * </pre>
     */
    public String buildUrlParams(Map<String, Object> params) {
        String urlParams = "?";
        Iterator<?> iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) iter.next();
            if (urlParams.length() > 1) {
                urlParams = urlParams + "&";
            }
            try {
                urlParams = urlParams
                        + entry.getKey().toString()
                        + "="
                        + URLEncoder.encode(entry.getValue().toString(),
                        "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return urlParams;
    }


    /**
     * <pre>
     * 功能说明：判断当前设备是否为手机
     * 日期：	2015年6月4日
     * 开发者：	陈丶泳佐
     *
     * @return flag
     * </pre>
     */
    public boolean isPhone() {
        TelephonyManager telephony = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false;
        } else {
            return true;
        }
    }


}
