package cn.com.unispark.fragment.home.recordcar;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.util.DialogUtil;

import com.bumptech.glide.Glide;

/**
 * <pre>
 * 功能说明： 【记录车位】【座驾位置】界面
 * 日期：	2015年10月20日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月20日
 * </pre>
 */
public class RecordCarActivity extends BaseActivity {
	/**
	 * 记录车位
	 */
	private final int FLAG_RECORD_CAR = 1;

	/**
	 * 座驾位置
	 */
	private final int FLAG_CAR_LOCATION = 2;

	// 导航栏标题
	private TextView titleText;
	// 返回按钮 // 右侧拍照按钮
	private ImageView moreImgView;
	private LinearLayout backLLayout, moreLLayout;

	// 完成找车， 清除图片
	private Button finish_btn;
	// 展示记录车位的照片
	private ViewPager viewpager;
	// 存放轮播图页数的点
	private LinearLayout pointLLayout;
	// 轮播页滑动到最后一个点
	private int lastPosition;
	// ViewPager展示图片的适配器
	private PagerAdapter adapter;
	// 照片的物理路径
	private Uri imageUri;
	// 照片保存目录
	@SuppressLint("SdCardPath")
	private String path = "/sdcard/51Park/image/";
	// 存放保存在本地 当前车辆记录的三张或小于三张照片的集合
	private List<String> imageList = new ArrayList<String>();
	// 清除图片
	private Button clear_btn;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.record_car_main);

	}

	@Override
	public void initView() {
		// 导航栏标题// 返回按钮
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("座驾位置");
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 右侧拍照按钮
		moreImgView = (ImageView) findViewById(R.id.moreImgView);
		moreImgView.setImageResource(R.drawable.btn_take_photo);
		moreImgView.setVisibility(View.VISIBLE);
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);

		// 完成找车
		finish_btn = (Button) findViewById(R.id.finish_btn);
		finish_btn.setOnClickListener(this);
		clear_btn = (Button) findViewById(R.id.clear_btn);
		clear_btn.setOnClickListener(this);

		// 初始化ViewPager
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 存放轮播图页数的点
		pointLLayout = (LinearLayout) findViewById(R.id.point_ll);

		// ViewPager展示图片的适配器
		adapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View view, Object obj) {
				return view.equals(obj);
			}

			@Override
			public int getCount() {
				return imageList.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				if (imageList.size() != 0) {
					Glide.with(context).fromFile()
							.load(new File(imageList.get(position)))
							.into(imageView);
				}

				container.addView(imageView);

				return imageView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}
		};

		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position >= imageList.size()) {
					position = 0;
				}
				pointLLayout.getChildAt(position).setEnabled(true);
				pointLLayout.getChildAt(lastPosition).setEnabled(false);
				lastPosition = position;
			}

			@Override
			/**
			 * 页面正在滑动的时候，回调
			 */
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			/**
			 * 当页面状态发生变化的时候，回调
			 */
			public void onPageScrollStateChanged(int state) {

			}
		});

		viewpager.setAdapter(adapter);

		/**
		 * <pre>
		 * 判断是从记录车位还是座驾位置进入的此页面
		 * 记录车位：调用系统的相机
		 * 座驾位置：展示记录车位的照片
		 * </pre>
		 */
		int flagCar = getSharedInteger("flagCar") == 0 ? 1
				: getSharedInteger("flagCar");
		switch (flagCar) {
		case FLAG_RECORD_CAR:
			onClickTakePhoto();
			break;
		case FLAG_CAR_LOCATION:
			onShowPhoto();
			onShowPoint();
			break;
		}

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout:// 拍照
			onClickTakePhoto();
			break;
		case R.id.finish_btn:
			onClickClearImage();
			break;
		case R.id.clear_btn:
			onClickClearImage();
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：点击拍照按钮,调用系统相机,仅当图片小于三张时可以调用
	 * 日期：	2015年10月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void onClickTakePhoto() {
		if (imageList.size() < 3) {

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String name = "51Park"
					+ DateFormat.format("yyyyMMddhhmmss",
							Calendar.getInstance(Locale.CHINA)) + ".jpg";

			imageUri = Uri.parse("file://" + path + name);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, 1);
		}

	}

	/**
	 * <pre>
	 * 功能说明：点击完成找车，清除图片按钮，清空所有的图片
	 * 日期：	2015年10月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void onClickClearImage() {
		final DialogUtil dialog = new DialogUtil(context);
		dialog.setMessage("确认清除图片么?");
		dialog.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				for (int i = 0; i < imageList.size(); i++) {
					File file = new File(imageList.get(i));
					file.delete();
				}
				imageList.clear();
				adapter.notifyDataSetChanged();
				setSharedInteger("flagCar", FLAG_RECORD_CAR);
				setSharedInteger("imageSize", 0);
				finish();

			}
		});
		dialog.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}

	/**
	 * <pre>
	 * 功能说明：展示记录车位的图片
	 * 日期：	2015年10月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void onShowPhoto() {
		int size = getSharedInteger("imageSize");
		for (int i = 0; i < size; i++) {
			imageList.add(getSharedString("photo" + i));
		}

		// 当本地保存的照片大于或等于三张时，隐藏拍照按钮
		if (imageList.size() >= 3) {
			moreImgView.setVisibility(View.GONE);
		}

		adapter.notifyDataSetChanged();

	}

	/**
	 * <pre>
	 * 功能说明：展示ViewPager页数的点
	 * 日期：	2015年10月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void onShowPoint() {
		int size = imageList.size();
		if (size == 1) {
			pointLLayout.setVisibility(View.GONE);
		}
		if (size > 1) {
			pointLLayout.setVisibility(View.VISIBLE);
			pointLLayout.removeAllViewsInLayout();
			for (int i = 0; i < size; i++) {
				// 添加指示点
				ImageView point = new ImageView(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.rightMargin = 20;
				point.setLayoutParams(params);
				point.setBackgroundResource(R.drawable.draw_point_bg);
				// if (i == 0) {
				// point.setEnabled(true);
				// } else {
				point.setEnabled(false);
				// }
				pointLLayout.addView(point);
			}
			pointLLayout.getChildAt(viewpager.getCurrentItem())
					.setEnabled(true);

		} else {
			Log.e("slx", "cn.com.unispark.Constants.islocation=false;");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 拍照成功时返回-1，若失败返回0，当点击相机的返回键时返回首页
		if (resultCode == Activity.RESULT_OK) {

			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				showToast("SDcard不可用！");
				return;
			}

			imageList.add(imageUri.getEncodedPath());
			setSharedString(
					"photo" + imageList.indexOf(imageUri.getEncodedPath()),
					imageUri.getEncodedPath());
			setSharedInteger("imageSize", imageList.size());
			setSharedInteger("flagCar", FLAG_CAR_LOCATION);
			adapter.notifyDataSetChanged();
			onShowPoint();
			viewpager.setCurrentItem(imageList.size() - 1);

			// 当本地保存的照片大于或等于三张时，隐藏拍照按钮
			if (imageList.size() >= 3) {
				moreImgView.setVisibility(View.GONE);
			}
		} else {
			finish();
		}
	}

}
