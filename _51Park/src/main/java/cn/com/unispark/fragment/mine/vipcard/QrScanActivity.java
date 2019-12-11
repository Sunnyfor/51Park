package cn.com.unispark.fragment.mine.vipcard;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.request.JsonRequestWithAuth;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

/**
 * <pre>
 * 功能说明：  二维码扫描界面
 * 日期：	2015年3月16日
 * 开发者：	任建飞
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容： 继承统一父类,优化闪关灯，扫描代码
 *    修改人员：  陈丶泳佐
 *    2015年8月12日
 * </pre>
 */
public class QrScanActivity extends BaseActivity implements Callback {

	// 导航栏标题//返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 相机配置属性
	private boolean hasSurface;
	private boolean playBeep;
	private boolean vibrate;
	private InactivityTimer inactivityTimer;
	private CaptureActivityHandler handler;
	private final float BEEP_VOLUME = 0.50f;
	private final long VIBRATE_DURATION = 200L;
	private MediaPlayer mediaPlayer;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;

	// 扫描框宽高以及扫描区域的宽高
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;

	// // 加载进度条
	// private Dialog progressDialog;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.qrscan_main);
		// aQuery = new AQuery(activity);
		initScan();
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("扫一扫");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);


		// 判断闪关灯是否开启
		CheckBox starCheckBox = (CheckBox) findViewById(R.id.light_cb);// 判断扫描页闪光灯是否开启
		
//		if (camera != null) {
//			try {
//				camera.setPreviewDisplay(mSurfaceHolder);
//				// 将camera的预览角度设置为竖屏
//				camera.setDisplayOrientation(90);
//				camera.startPreview();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else {
//			ToastUtil.show("未授权运行开摄像机");
//		}
		starCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					// 打开闪光灯
					CameraManager.get().openLight();
				} else {
					// 关闭闪光灯
					CameraManager.get().offLight();
				}
			}
		});

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	public void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 用户绑定
	 */
	/**
	 * <pre>
	 * 功能说明：绑定无忧会员卡
	 * 日期：	2015年8月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param resultStr
	 * </pre>
	 */
	private void parseBindVipCard(String resultStr) {
		
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("uid", ParkApplication.getmUserInfo().getUid());
			jsonObject.put("encryptcode", resultStr);
			jsonObject.put("phone", ParkApplication.getmUserInfo().getUsername());
			// LoginEntity mLoginEntity = new LoginEntity();
			JsonRequestWithAuth request = new JsonRequestWithAuth(Method.POST,
					Constant.BIND_URL, jsonObject, Constant.APP_ID,
					Constant.APP_KEY, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {

							try {
								if ("200".equals(response.getString("code"))) {
									ToastUtil.showToast(response
											.getString("msg"));
									if (ParkApplication.cardManager) {
										Intent data = new Intent();
										data.putExtra("bind", "ture");
										QrScanActivity.this.setResult(20, data);
										QrScanActivity.this.finish();
										MobclickAgent.onEvent(context, "home_ScanQRcode_succeed");
									} else {
										QrScanActivity.this.finish();
									}
								} else {
									final DialogUtil mDialog = new DialogUtil(
											context);
									mDialog.setMessage(response
											.getString("msg"));
									mDialog.setPositiveButton("确定",
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													mDialog.dismiss();
													handler.sendEmptyMessage(R.id.restart_preview);
												}
											});

								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {

						}
					});
			ParkApplication.getInstance().addToRequestQueue(request, "My Tag");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**************************************************************************************/

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/**************************************************************************************/

	/**
	 * <pre>
	 * 功能说明：初始化 CameraManager
	 * 日期：	2015年8月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void initScan() {

		CameraManager.init(getApplication());

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		// 加载整个二维码扫描页面
		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		// 二维码扫描的窗口
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		// 扫描线
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);

		ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(1200);
		mQrLineView.startAnimation(animation);

	}

	/**
	 * <pre>
	 * 功能说明：初始化相机
	 * 日期：	2015年8月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param surfaceHolder
	 * </pre>
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(QrScanActivity.this);
		}
	}

	/**
	 * <pre>
	 * 功能说明：扫描频率声音
	 * 日期：	2015年8月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		// progressDialog.show();
		
		if (!TextUtils.isEmpty(result)) {
			if (result.contains(Constant.QRCODE_URL)) {
				String[] s1 = result.split("CardID/", 2);
				parseBindVipCard(s1[1]);
			} else {
				if (loadingProgress.isShowing()) {
					loadingProgress.hide();
				}
				final DialogUtil mDialog = new DialogUtil(context);
				mDialog.setMessage("二维码未识别！");
				mDialog.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						mDialog.dismiss();
						handler.sendEmptyMessage(R.id.restart_preview);
					}
				});

			}
		} else {
			if (loadingProgress.isShowing()) {
				loadingProgress.hide();
			}
			final DialogUtil mDialog = new DialogUtil(context);
			mDialog.setMessage("二维码为空!");
			mDialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					handler.sendEmptyMessage(R.id.restart_preview);
				}
			});
		}
		
		MobclickAgent.onEvent(context, "home_ScanQRcode_failed");
		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		// handler.sendEmptyMessage(R.id.restart_preview);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}