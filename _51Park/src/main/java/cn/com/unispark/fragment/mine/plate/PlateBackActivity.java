package cn.com.unispark.fragment.mine.plate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;
import cn.com.unispark.util.upload.MultiPartStack;
import cn.com.unispark.util.upload.MultiPartStringRequest;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * <pre>
 * 功能说明： 车牌找回界面
 * 日期：	2015年11月19日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月3日
 * </pre>
 */
public class PlateBackActivity extends BaseActivity implements
		LoaderCallbacks<Cursor> {

	private static final int SELECT_PIC_BY_PICK_PHOTO = 15;
	private TextView titleText;
	private LinearLayout backLLayout;
	private static Activity mActivity;
	private Button saveBtn;
	/**
	 * 判断是修改还是添加
	 */
	private boolean modify = false;
	/** 使用照相机拍照获取图片 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	private String modify_plateno_str;
	private String platecard;
	private Intent data;
	private TextView platePhotoLeft;
	private TextView platePhotoRight;
	private String SDCardRoot;
	private Button btn_take_photo;
	private Button btn_pick_photo;
	private Button btn_cancel;
	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;
	/** 从Intent获取图片路径的KEY */
	public static final String KEY_PHOTO_PATH = "photo_path";
	private Cursor cursor;
	private boolean isLeft = false;
	private String[] pojo = { MediaStore.Images.Media.DATA };
	private String leftjpegName;
	private String rightjpegName;
	private Button btSave;
	private TextView plateTextView;
	private String plate;
	private Dialog plateBackdialog;
	private TextView leftIcon;
	private TextView rightIcon;
	private static RequestQueue mSingleQueue;
	private static Map<String, String> stringUploads;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO 接收消息并且去更新UI线程上的控件内容
			switch (msg.what) {
			case 0:

				break;
			}
			super.handleMessage(msg);
		}
	};
	private TextView tishiText;
	private RelativeLayout contentRelativeLayout;
	private LinearLayout platePhotoLinearLayout;
	private View view;
	private TextView left_tv;
	private TextView right_tv;
	private TableLayout table;
	private TableLayout table2;
	private TextView detailLinearLayout;
	private LinearLayout saveLinearLayout;
	private LinearLayout dialogLinearLayout;

	public static LoadingProgress backloadingProgress;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.plate_find_main);
		mActivity = this;
		data = getIntent();
		SDCardRoot = Environment.getExternalStorageDirectory().toString()
				+ "/51Park/image/";
		File file = new File(SDCardRoot);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		backloadingProgress = new LoadingProgress(PlateBackActivity.this);
	}

	private void showDialog(Bitmap bm, String titleStr) {
		plateBackdialog = new Dialog(this, R.style.Dialog_Fullscreen);
		plateBackdialog.setContentView(R.layout.plate_find_dialog);
		plateBackdialog.show();
		ImageView iv_plate_back = (ImageView) plateBackdialog
				.findViewById(R.id.iv_plate_back);
		iv_plate_back.setImageBitmap(bm);
		TextView title = (TextView) plateBackdialog.findViewById(R.id.title_tv);
		title.setText(titleStr);
		ImageView iv = (ImageView) plateBackdialog.findViewById(R.id.iv_close);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (plateBackdialog.isShowing()) {
					plateBackdialog.dismiss();
				}
			}
		});
		RelativeLayout rl = (RelativeLayout) plateBackdialog
				.findViewById(R.id.dialog_rl);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (plateBackdialog.isShowing()) {
					plateBackdialog.dismiss();
				}
			}
		});
	}

	@Override
	public void initView() {
		// 提示
		tishiText = (TextView) findViewById(R.id.tishi);
		ViewUtil.setViewSize(tishiText, 66, 0);
		ViewUtil.setTextSize(tishiText, 28);
		ViewUtil.setMarginLeft(tishiText, 20, ViewUtil.RELATIVELAYOUT);
		// 车牌布局
		contentRelativeLayout = (RelativeLayout) findViewById(R.id.content_rl);
		ViewUtil.setViewSize(contentRelativeLayout, 490, 0);
		// 照片布局
		platePhotoLinearLayout = (LinearLayout) findViewById(R.id.plate_photo_linearLayout);
		ViewUtil.setViewSize(platePhotoLinearLayout, 160, 0);
		ViewUtil.setMarginTop(platePhotoLinearLayout, 40,
				ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(platePhotoLinearLayout, 50,
				ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(platePhotoLinearLayout, 50,
				ViewUtil.RELATIVELAYOUT);
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("车牌找回");
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 左边照片
		platePhotoLeft = (TextView) findViewById(R.id.plate_photo_left);
		ViewUtil.setViewSize(platePhotoLeft, 160, 246);
		ViewUtil.setPaddingBottom(platePhotoLeft, 20);
		ViewUtil.setTextSize(platePhotoLeft, 28);
		platePhotoLeft.setOnClickListener(this);
		// 中间view
		view = (View) findViewById(R.id.view);
		ViewUtil.setViewSize(view, 160, 50);
		// 右边照片
		platePhotoRight = (TextView) findViewById(R.id.plate_photo_right);
		ViewUtil.setViewSize(platePhotoRight, 160, 246);
		ViewUtil.setTextSize(platePhotoRight, 28);
		ViewUtil.setPaddingBottom(platePhotoRight, 20);
		platePhotoRight.setOnClickListener(this);
		// tablelayout
		table = (TableLayout) findViewById(R.id.table);
		ViewUtil.setMarginTop(table, 24, ViewUtil.RELATIVELAYOUT);
		// tablelayout2
		table2 = (TableLayout) findViewById(R.id.table2);
		ViewUtil.setMarginTop(table2, 10, ViewUtil.RELATIVELAYOUT);

		ViewUtil.setMarginLeft(table2, 12, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginBottom(table2, 10, ViewUtil.RELATIVELAYOUT);
		left_tv = (TextView) findViewById(R.id.left_tv);
		ViewUtil.setTextSize(left_tv, 28);
		right_tv = (TextView) findViewById(R.id.right_tv);
		ViewUtil.setTextSize(right_tv, 28);
		// 查看示例
		leftIcon = (TextView) findViewById(R.id.tv_left_icon);
		ViewUtil.setTextSize(leftIcon, 28);
		leftIcon.setOnClickListener(this);
		rightIcon = (TextView) findViewById(R.id.tv_right_icon);
		ViewUtil.setTextSize(rightIcon, 28);
		rightIcon.setOnClickListener(this);
		// 车牌号码
		plateTextView = (TextView) findViewById(R.id.tv_plate);
		ViewUtil.setViewSize(plateTextView, 130, 412);
		ViewUtil.setTextSize(plateTextView, 68);
		ViewUtil.setMarginTop(plateTextView, 32, ViewUtil.RELATIVELAYOUT);
		// 说明
		detailLinearLayout = (TextView) findViewById(R.id.detail_ll);
		ViewUtil.setMarginTop(detailLinearLayout, 42, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(detailLinearLayout, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setTextSize(detailLinearLayout, 28);
		plate = data.getStringExtra("plate");
		// plateTextView.setText(plate.substring(0, 2) + " ● "
		// + plate.substring(2, plate.length()));
		SpannableString noYhText = new SpannableString(plate.substring(0, 2)
				+ " ● " + plate.substring(2, plate.length()));
		noYhText.setSpan(new TextAppearanceSpan(context,
				R.style.plate_dian_style), 2, 4,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		plateTextView.setText(noYhText, TextView.BufferType.SPANNABLE);
		// 提交按钮
		saveLinearLayout = (LinearLayout) findViewById(R.id.save_ll);
		ViewUtil.setViewSize(saveLinearLayout, 128, 0);
		btSave = (Button) findViewById(R.id.bt_save);
		ViewUtil.setViewSize(btSave, 80, 400);
		ViewUtil.setTextSize(btSave, 35);
		// btSave.setBackgroundResource(R.drawable.isblack_icon);
		btSave.setEnabled(false);
		btSave.setOnClickListener(this);
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.tv_left_icon:
			Bitmap bm1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_plate_one);
			showDialog(bm1, "行驶证示例图");
			break;
		case R.id.tv_right_icon:
			Bitmap bm2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_plate_two);
			showDialog(bm2, "车辆正面照");
			break;
		case R.id.bt_save:
			complainPlate(plate);
			break;
		case R.id.backLLayout:
			finish();
			break;
		case R.id.plate_photo_left:
			isLeft = true;
			getPic("left");
			break;
		case R.id.plate_photo_right:
			isLeft = false;
			getPic("right");
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 选择图片后，上传图片
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * 
	 * </pre>
	 */
	@SuppressWarnings("deprecation")
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {// 从相册取图片，有些手机有异常情况，请注意
			if (data == null) {
				Toast.makeText(getApplicationContext(), "选择图片文件出错",
						Toast.LENGTH_SHORT).show();
				return;
			}
			photoUri = data.getData();
			Log.e("slx", "photoUri" + photoUri);
			if (photoUri == null) {
				Toast.makeText(getApplicationContext(), "选择图片文件出错",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		cursor = managedQuery(photoUri, pojo, null, null, null);
		// 这是最新的CursorLoader，已实现，暂时使用之前的managedQuery
		// getLoaderManager().initLoader(0, null, this);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			Log.e("slx", "picPath" + picPath);
		}
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG")
						|| picPath.endsWith(".JPEG") || picPath
							.endsWith(".jpeg"))) {
			BitmapFactory.Options bfOptions = new BitmapFactory.Options();
			bfOptions.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(picPath, bfOptions);// 此时返回bitmap为空
			bfOptions.inSampleSize = ImageUtil.calculateInSampleSize(bfOptions,
					480, 800);
			bfOptions.inJustDecodeBounds = false;
			// 重新读入图片，注意这次要把options.inJustDecodeBounds设为false
			bitmap = BitmapFactory.decodeFile(picPath + "", bfOptions);
			Drawable mDrawable = new BitmapDrawable(bitmap);
			if (isLeft) {
				/** 发给服务器的图片路径 */
				leftjpegName = saveMyBitmap("left", bitmap);
				platePhotoLeft.setBackgroundDrawable(mDrawable);
				platePhotoLeft.setText("");
			} else {
				/** 发给服务器的图片路径 */
				rightjpegName = saveMyBitmap("right", bitmap);
				platePhotoRight.setBackgroundDrawable(mDrawable);
				platePhotoRight.setText("");
			}
		} else {
			Toast.makeText(getApplicationContext(), "选择图片文件不正确",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 把mBitmap压缩然后保存到本地，返回图片路径
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	public String saveMyBitmap(String bitName, Bitmap mBitmap) {
		String picStr = SDCardRoot + bitName + ".png";
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
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
			try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return picStr;
	}

	@Override
	protected void onDestroy() {
		// cursor.close();
		super.onDestroy();
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 从相册选取图片
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 调取选择图片和拍照功能
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void getPic(final String dic) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		final View viewDialog = LayoutInflater.from(context).inflate(
				R.layout.plate_pic_dialog, null);
		dialog.setContentView(viewDialog);

		dialogLinearLayout = (LinearLayout) viewDialog
				.findViewById(R.id.dialog_layout);
		ViewUtil.setMarginBottom(dialogLinearLayout, 20,
				ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(dialogLinearLayout, 10, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(dialogLinearLayout, 10, ViewUtil.RELATIVELAYOUT);
		btn_take_photo = (Button) viewDialog.findViewById(R.id.btn_take_photo);
		ViewUtil.setViewSize(btn_take_photo, 88, 0);
		ViewUtil.setTextSize(btn_take_photo, 30);
		btn_pick_photo = (Button) viewDialog.findViewById(R.id.btn_pick_photo);
		ViewUtil.setViewSize(btn_pick_photo, 88, 0);
		ViewUtil.setTextSize(btn_pick_photo, 30);
		btn_cancel = (Button) viewDialog.findViewById(R.id.btn_cancel);
		ViewUtil.setViewSize(btn_cancel, 88, 0);
		ViewUtil.setTextSize(btn_cancel, 30);
		// 拍照
		btn_take_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 调用自定义相机 */
				// Intent leftIntent = new Intent(mActivity,
				// PhotoActivity.class);
				// leftIntent.putExtra("photo", dic);
				// startActivityForResult(leftIntent, 30);
				/* 调用系统相机 */
				takePhoto();
				dialog.dismiss();
			}
		});
		// 相册
		btn_pick_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pickPhoto();
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 拍照
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(getApplicationContext(), "内存卡不存在",
					Toast.LENGTH_SHORT).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Log.e("slx", "RESULT_OK");
			doPhoto(requestCode, data);
		}
		if (resultCode == 50) {
			if ("left".equals(data.getStringExtra("dic").toString().trim())) {
				/** 发给服务器的图片路径 */
				leftjpegName = SDCardRoot + "left.jpg";
				Bitmap mBitmap = BitmapFactory.decodeFile(leftjpegName);
				Drawable mDrawable = new BitmapDrawable(mBitmap);
				platePhotoLeft.setBackgroundDrawable(mDrawable);
				platePhotoLeft.setText("");
			}
			if ("right".equals(data.getStringExtra("dic").toString().trim())) {
				/** 发给服务器的图片路径 */
				rightjpegName = SDCardRoot + "right.jpg";
				Bitmap mBitmap = BitmapFactory.decodeFile(rightjpegName);
				Drawable mDrawable = new BitmapDrawable(mBitmap);
				platePhotoRight.setBackgroundDrawable(mDrawable);
				platePhotoRight.setText("");
			}
		}

	}

	@Override
	public void onResume() {
		if (!TextUtils.isEmpty(leftjpegName)
				&& !TextUtils.isEmpty(rightjpegName)) {
			Log.e("slx", "onResume1");
			btSave.setBackgroundResource(R.drawable.btn_common_selected);
			btSave.setEnabled(true);
		} else {
			btSave.setBackgroundResource(R.drawable.btn_common_noselect);
			btSave.setEnabled(false);
			Log.e("slx", "onResume2");
		}
		super.onResume();
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 审核车牌和上传图片请求
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void complainPlate(String modify_plateno_str) {
		backloadingProgress.show();
		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
		Map<String, File> files = new HashMap<String, File>();
		files.put("driving_license", new File(leftjpegName));
		files.put("car_image", new File(rightjpegName));
		String uri = Constant.PLATEBACK_URL;
		stringUploads = new HashMap<String, String>();
		stringUploads.put("uid", ParkApplication.getmUserInfo().getUid());
		stringUploads.put("plate", modify_plateno_str);
		JSONObject mJson = new JSONObject();
		try {
			mJson.put("uid", ParkApplication.getmUserInfo().getUid());
			mJson.put("plate", modify_plateno_str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addPutUploadFileRequest(uri, files, mJson, mResonseListener,
				mErrorListener, null);

	}

	Listener<JSONObject> mResonseListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			Log.i(TAG, " on response json" + response.toString());
		}
	};

	Listener<String> mResonseListenerString = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Log.i(TAG, " on response String" + response.toString());
		}
	};

	ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (error != null) {
				if (error.networkResponse != null)
					Log.i(TAG, " error "
							+ new String(error.networkResponse.data));
			}
		}
	};

	/**
	 * <pre>
	 * 功能说明：
	 * 审核请求具体方法
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	public static void addPutUploadFileRequest(final String url,
			final Map<String, File> files, final JSONObject mJson,
			final Listener<JSONObject> responseListener,
			final ErrorListener errorListener, final Object tag) {
		if (null == url || null == responseListener) {
			return;
		}
		MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
				Method.POST, url, "", new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						backloadingProgress.hide();
						Log.e("slx", "成功上传-------" + response);
						String code;
						try {
							code = response.getString("code");
							if ("200".equals(code)) {
								Log.e("slx", "成功上传");
								ToastUtil.showToast("提交成功");
								ToolUtil.IntentClassLogin(mActivity,
										PlateResultActivity.class, true);
							} else {
								ToastUtil.showToast(response.getString("msg"));
							}
						} catch (JSONException e) {
							ToastUtil.showToast("提交失败");
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						backloadingProgress.hide();
						ToastUtil.showToast("提交失败");
					}
				}) {
			@Override
			public Map<String, File> getFileUploads() {
				return files;

			}

			@Override
			public Map<String, String> getStringUploads() {
				return stringUploads;
			}
		};
		mSingleQueue.add(multiPartRequest);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(context, photoUri, pojo, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			Log.e("slx", "picPath" + picPath);
		}
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			Drawable mDrawable = new BitmapDrawable(bm);
			Log.e("slx", "isLeft------>" + isLeft);
			if (isLeft) {
				platePhotoLeft.setBackgroundDrawable(mDrawable);
			} else {
				platePhotoRight.setBackgroundDrawable(mDrawable);
			}
		} else {
			Toast.makeText(getApplicationContext(), "选择图片文件不正确",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		Log.e("slx", "ionLoaderReset");
	}

}
