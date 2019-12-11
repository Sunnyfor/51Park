package cn.com.unispark.fragment.mine.vipcard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.fragment.home.recordcar.RecordCarActivity;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 简单的自定义相机
 * 日期：	2015年12月14日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class PhotoActivity extends BaseActivity implements Callback,
		AnimationListener {
	// 相机配置属性
	private static final Pattern COMMA_PATTERN = Pattern.compile(",");
	// 扫描框宽高以及扫描区域的宽高
	private Intent data;
	// 加载进度条
	private TextView titleText;
	private LinearLayout backLLayout;
	private Button bt_photo;
	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private String SDCardRoot;
	private String photoStr;
	private ImageView backImgView;
	private RelativeLayout capture_rl;
	private ImageView photoImageView;
	private int picName = 0;
	/**
	 * 记录车位
	 */
	private final int FLAG_RECORD_CAR = 1;
	/**
	 * 座驾位置
	 */
	private final int FLAG_CAR_LOCATION = 2;
	private ImageView tempImageView;
	private Animation animTempview;
	private Bitmap minBitmap;
	private Bitmap mBitmap;
	protected String path;
	private Matrix matrix;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.photo_main);
		SDCardRoot = Environment.getExternalStorageDirectory().toString()
				+ "/51Park/image/";
		File file = new File(SDCardRoot);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	@Override
	public void initView() {
		data = getIntent();
		matrix = new Matrix();
		matrix.setRotate((float) 90.0);
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("记录车位");
		backImgView = (ImageView) findViewById(R.id.backImgView);
		backImgView.setImageResource(R.drawable.btn_close_red);
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		capture_rl = (RelativeLayout) findViewById(R.id.capture_rl);
		ViewUtil.setViewSize(capture_rl, 168, 0);
		bt_photo = (Button) findViewById(R.id.bt_photo);
		ViewUtil.setViewSize(bt_photo, 132, 132);
		bt_photo.setOnClickListener(this);
		tempImageView = (ImageView) findViewById(R.id.temp_iv);
		// 必须设置为INVISIBLE，为Gone动画无效
		tempImageView.setVisibility(View.INVISIBLE);
		photoImageView = (ImageView) findViewById(R.id.photo_iv);
		ViewUtil.setViewSize(photoImageView, 92, 92);
		ViewUtil.setMarginRight(photoImageView, 20, ViewUtil.RELATIVELAYOUT);
		photoImageView.setOnClickListener(this);
		photoStr = data.getStringExtra("photo");
		mSurfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		// 获取surfaceHolder
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		// surfaceView.setZOrderOnTop(false);
		// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		animTempview = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.tempview_show);
		animTempview.setAnimationListener(this);
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.bt_photo:
			capture();
			break;
		case R.id.photo_iv:
			ToolUtil.IntentClass(activity, RecordCarActivity.class, false);
			break;
		}
	}

	@Override
	public void onBackPressed()
	// 无意中按返回键时要释放内存
	{
		super.onBackPressed();
		finish();
	}

	/**
	 * 拍照处理
	 */
	@SuppressLint("InlinedApi")
	private void capture() {
		bt_photo.setEnabled(false);
		if (null != mCamera) {
			Camera.Parameters myParam = mCamera.getParameters();
			// 设置拍照后存储的图片格式
			myParam.setPictureFormat(ImageFormat.JPEG);
			// 设置大小和方向等参数
			// myParam.setPictureSize(1280, 960);
			myParam.setPreviewSize(960, 720);
			// // myParam.set("rotation", 90);
			// 自动对焦，前提是系统支持自动对焦
			myParam.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);// 1连续对焦
			mCamera.setParameters(myParam);
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if (success) {
						mCamera.takePicture(myShutterCallback, null,
								myJpegCallback);
					}
				}
			});
		}
	}

	/* 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量 */
	ShutterCallback myShutterCallback = new ShutterCallback()
	// 快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	{
		public void onShutter() {
			// Log.e("slx", "myShutterCallback:onShutter...");
		}
	};
	PictureCallback myRawCallback = new PictureCallback()
	// 拍摄的未压缩原数据的回调,可以为null
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.e("slx", "myRawCallback:onPictureTaken...");
		}
	};

	/**
	 * 对jpeg图像数据的回调,最重要的一个回调
	 */
	PictureCallback myJpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

			try {
				if (null != data) {
					BitmapFactory.Options bfOptions = new BitmapFactory.Options();
					bfOptions.inJustDecodeBounds = true;
					mBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length, bfOptions);// data是字节数据，将其解析成位图,此时返回bitmap为空
					bfOptions.inSampleSize = ImageUtil.calculateInSampleSize(
							bfOptions, 320, 480);
					bfOptions.inJustDecodeBounds = false;
					mBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					path = saveMyBitmap("recode" + picName, mBitmap);
					// 动画
					tempImageView.startAnimation(animTempview);
					// mCamera.startPreview();
				} else {
					ToastUtil.show("拍照失败，请重试");
				}
			} catch (Exception e) {
				ToastUtil.show("拍照失败，请重试");
				e.printStackTrace();
			}

		}
	};

	protected void onNewIntent(Intent intent) {
		Log.e("slx", "onNewIntent");
		if (getSharedInteger("imageSize") > 0) {
			picName = getSharedInteger("imageSize");
		}
	};

	/**
	 * <pre>
	 * 功能说明：
	 * 把mBitmap压缩然后保存到本地，返回图片路径
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	public String saveMyBitmap(String bitName, Bitmap mBitmap) {
		String name = "51Park"
				+ DateFormat.format("yyyyMMddhhmmss",
						Calendar.getInstance(Locale.CHINA)) + ".png";
		String picStr = SDCardRoot + name;
		try {
			File dirFile = new File(picStr);
			// 检测图片是否存在
			if (dirFile.exists()) {
				dirFile.delete(); // 删除原图片
			}
			File myCaptureFile = new File(picStr);
			BufferedOutputStream bos;
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			// 100表示不进行压缩，70表示压缩率为30%
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return picStr;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setStartPreview(mCamera, mSurfaceHolder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mCamera != null) {
			mCamera.stopPreview();
			setStartPreview(mCamera, mSurfaceHolder);
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if (success) {
						camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
					}
				}

			});
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		releaseCamera();

	}

	/**
	 * 开始相机预览
	 * 
	 * @param camera
	 * @param holder
	 */
	private void setStartPreview(Camera camera, SurfaceHolder holder) {
		if (camera != null) {
			try {
				camera.setPreviewDisplay(mSurfaceHolder);
				// 将camera的预览角度设置为竖屏
				camera.setDisplayOrientation(90);
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ToastUtil.show("未授权运行开摄像机");
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (mCamera == null) {
			mCamera = getCamera();
			if (mSurfaceHolder != null) {
				setStartPreview(mCamera, mSurfaceHolder);
			}
		}
		if (getSharedInteger("imageSize") == 0) {
			photoImageView.setVisibility(View.INVISIBLE);
			picName = getSharedInteger("imageSize");
			bt_photo.setEnabled(true);
		} else {
			picName = getSharedInteger("imageSize");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 获取相机资源
	 */
	private Camera getCamera() {
		Camera mCamera;
		try {
			mCamera = Camera.open();
		} catch (Exception e) {
			mCamera = null;
			e.printStackTrace();
		}
		return mCamera;
	}

	/**
	 * 释放相机资源
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		tempImageView.setVisibility(View.VISIBLE);
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth() / 2,
				mBitmap.getHeight() / 2, matrix, false);
		tempImageView.setImageBitmap(mBitmap);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mCamera.startPreview();
		tempImageView.setVisibility(View.GONE);
		tempImageView.setImageBitmap(null);
		mBitmap.recycle();
		mBitmap = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 10; // width，hight设为原来的十分一
			minBitmap = BitmapFactory.decodeStream(fis, null, options);
			int size = minBitmap.getWidth() > minBitmap.getHeight() ? minBitmap
					.getHeight() : minBitmap.getWidth();
			minBitmap = Bitmap.createBitmap(minBitmap, 0, 0, size, size,
					matrix, false);
			photoImageView.setImageBitmap(minBitmap);
			setSharedString("recode" + picName, path);
			setSharedInteger("flagCar", FLAG_CAR_LOCATION);
			picName++;
			setSharedInteger("imageSize", picName);
			photoImageView.setVisibility(View.VISIBLE);
			if (getSharedInteger("imageSize") <= 3) {
				bt_photo.setEnabled(true);
			} else {
				ToastUtil.show("相片不能超过三张！");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}