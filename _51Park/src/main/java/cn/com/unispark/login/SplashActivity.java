package cn.com.unispark.login;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import cn.com.unispark.MainActivity;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;

/**
 * <pre>
 * 功能说明： 51Park应用欢迎页面
 * 日期：	2014年12月3日
 * 开发者：	陈丶泳佐
 * 版本信息：V1.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：判断是否已登录，初始化用户信息
 *    修改人员：任建飞
 *    修改日期： 2015年3月16日
 * </pre>
 */
public class SplashActivity extends BaseActivity {

	private Handler handler = new Handler();
	
	@Override
	public void setContentLayout() {
		setContentView(R.layout.splash_main);
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				onIntentClass(activity, MainActivity.class, true);
			}
		}, 2000);
	}

	@Override
	public void initView() {

	}

	@Override
	public void onClickEvent(View view) {

	}

	@Override
	public void onResume() {
		super.onResume();
	}
}