package cn.com.unispark.fragment.home.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.bigimage.UrlTouchImageView;
import cn.com.unispark.fragment.home.map.entity.ParkInOutEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInfoEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInOutEntity.DataObject.ParkInOutInfo;
import cn.com.unispark.fragment.home.map.entity.ParkInfoEntity.DataObject;
import cn.com.unispark.fragment.home.map.entity.ParkInfoEntity.DataObject.TimeState;
import cn.com.unispark.fragment.home.map.navigation.NavVoiceActivity;
import cn.com.unispark.fragment.treasure.lease.LeaseDetailActivity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.TimeUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapScreenShotListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.navi.AMapNaviViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 停车场的详情界面
 * 日期：	2015年11月10日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月16日
 * </pre>
 */
public class ParkInfoActivity extends BaseActivity implements
		AMapNaviViewListener, OnMapClickListener {

	// 导航栏标题// 返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 收费详情//租赁//到这去
	/* private RelativeLayout price_rl, lease_rl, nav_rl; */
	private RelativeLayout price_rl, nav_rl;
	private TextView price_tv, nav_tv;

	// 停车场描述图片
	private ImageView desc_iv, isjoin_iv;

	// 停车场名称//地址//总车位数//空车位数
	private TextView name_tv, address_tv, count_tv, empty_count_tv;

	// 日间价格//夜间价格
	private TextView today_price_tv, night_price_tv;

	// 租赁类型：//包月 //计次
	/* private ImageView lease_iv; */

	// 收费详情的对话框
	/*
	 * private Dialog priceDialog; private Button close_btn; private
	 * List<PriceInfo> todayList; private List<PriceInfo> nightList;
	 */

	// 停车场详情实体类
	private DataObject data;

	// 未来五小时车位预测情况
	private List<TimeState> stateList;
	private LinearLayout detail_ll;
	private ImageView one_hour_iv, two_hour_iv, three_hour_iv, four_hour_iv,
			five_hour_iv;
	private TextView one_hour_tv, two_hour_tv, three_hour_tv, four_hour_tv,
			five_hour_tv;

	// 停车场详情图片点击放大对话框
	private Dialog dialog;
	private String imageUrl;

	/*
	 * 高德地图需要的属性
	 */
	public static AMap mAMap;
	private MapView mMapView;
	/* private ImageView screenshotImg; */

	// 出入口的Marker
	private List<Marker> mInOutMarkars = new ArrayList<Marker>();

	// // 中心点坐标，出入口坐标
	// private LatLng centerLatLng, inOutLatLng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMap(savedInstanceState);
	}

	@Override
	public void setContentLayout() {
		setContentView(R.layout.park_info_main);

		parseLoadInOut();
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("停车场详情");

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		/*
		 * 停车场描述图片
		 */
		desc_iv = (ImageView) findViewById(R.id.desc_iv);
		desc_iv.setScaleType(ScaleType.FIT_XY);
		desc_iv.setOnClickListener(this);
		ViewUtil.setViewSize(desc_iv, 134, 235);
		ViewUtil.setMargin(desc_iv, 20, ViewUtil.RELATIVELAYOUT);

		// 无忧角标
		isjoin_iv = (ImageView) findViewById(R.id.isjoin_iv);

		// 停车场名称
		name_tv = (TextView) findViewById(R.id.name_tv);
		ViewUtil.setTextSize(name_tv, 24);
		// ViewUtil.setMarginTop(name_tv, 15, ViewUtil.RELATIVELAYOUT);

		// 地址
		address_tv = (TextView) findViewById(R.id.address_tv);
		ViewUtil.setTextSize(address_tv, 22);
		ViewUtil.setMarginTop(address_tv, 20, ViewUtil.RELATIVELAYOUT);

		// 总车位数
		count_tv = (TextView) findViewById(R.id.count_tv);
		ViewUtil.setTextSize(count_tv, 20);
		ViewUtil.setMarginTop(count_tv, 22, ViewUtil.RELATIVELAYOUT);

		// 空车位数
		empty_count_tv = (TextView) findViewById(R.id.empty_count_tv);
		ViewUtil.setTextSize(empty_count_tv, 20);
		ViewUtil.setMarginLeft(empty_count_tv, 10, ViewUtil.RELATIVELAYOUT);

		/*
		 * 暂无收费信息
		 */
		price_tv = (TextView) findViewById(R.id.price_tv);
		ViewUtil.setTextSize(price_tv, 30);
		ViewUtil.setViewSize(price_tv, 150, 0);
		ViewUtil.setDrawablePadding(price_tv, 20);
		ViewUtil.setPaddingLeft(price_tv, 20);

		/*
		 * 价格详情子条目
		 */
		price_rl = (RelativeLayout) findViewById(R.id.price_rl);
		ViewUtil.setViewSize(price_rl, 150, 0);

		// 价格图标
		ImageView price_iv = (ImageView) findViewById(R.id.price_iv);
		ViewUtil.setMarginLeft(price_iv, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(price_iv, 20, ViewUtil.RELATIVELAYOUT);

		// 白天（文字）
		TextView test1_tv = (TextView) findViewById(R.id.test1_tv);
		ViewUtil.setTextSize(test1_tv, 24);
		ViewUtil.setViewSize(test1_tv, 75, 0);

		// 夜间（文字）
		TextView test2_tv = (TextView) findViewById(R.id.test2_tv);
		ViewUtil.setTextSize(test2_tv, 24);
		ViewUtil.setViewSize(test2_tv, 75, 0);

		// 日间价格
		today_price_tv = (TextView) findViewById(R.id.today_price_tv);
		ViewUtil.setTextSize(today_price_tv, 18);
		ViewUtil.setViewSize(today_price_tv, 75, 0);

		// 夜间价格
		night_price_tv = (TextView) findViewById(R.id.night_price_tv);
		ViewUtil.setTextSize(night_price_tv, 18);
		ViewUtil.setViewSize(night_price_tv, 75, 0);

		// 更多按钮
		ImageView more_iv = (ImageView) findViewById(R.id.more_iv);
		ViewUtil.setMarginRight(more_iv, 20, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginLeft(more_iv, 40, ViewUtil.RELATIVELAYOUT);

		/*
		 * 租赁详情子条目
		 */
		/*
		 * lease_rl = (RelativeLayout) findViewById(R.id.lease_rl);
		 * lease_rl.setOnClickListener(this); ViewUtil.setViewSize(lease_rl, 88,
		 * 0); ViewUtil.setPaddingLeft(lease_rl, 20);
		 * ViewUtil.setPaddingRight(lease_rl, 20);
		 */

		/*
		 * 去这儿
		 */
		nav_rl = (RelativeLayout) findViewById(R.id.nav_rl);
		nav_rl.setOnClickListener(this);

		// 去这儿
		nav_tv = (TextView) findViewById(R.id.nav_tv);
		ViewUtil.setTextSize(nav_tv, 30);
		ViewUtil.setViewSize(nav_tv, 88, 0);

		// 租赁类型：包月/计次
		/*
		 * lease_iv = (ImageView) findViewById(R.id.lease_iv);
		 * ViewUtil.setMarginRight(lease_iv, 10, ViewUtil.RELATIVELAYOUT);
		 */

		// 租赁类型：计次
		// meter_iv = (ImageView) findViewById(R.id.meter_iv);
		// ViewUtil.setMarginRight(meter_iv, 10, ViewUtil.RELATIVELAYOUT);

		/*
		 * 车位预测：未来5小时车位预测图标和文字
		 */
		detail_ll = (LinearLayout) findViewById(R.id.detail_ll);
		ViewUtil.setViewSize(detail_ll, 187, 0);

		one_hour_iv = (ImageView) findViewById(R.id.one_hour_iv);
		one_hour_tv = (TextView) findViewById(R.id.one_hour_tv);
		setParkDivine(one_hour_tv);

		two_hour_iv = (ImageView) findViewById(R.id.two_hour_iv);
		two_hour_tv = (TextView) findViewById(R.id.two_hour_tv);
		setParkDivine(two_hour_tv);

		three_hour_iv = (ImageView) findViewById(R.id.three_hour_iv);
		three_hour_tv = (TextView) findViewById(R.id.three_hour_tv);
		setParkDivine(three_hour_tv);

		four_hour_iv = (ImageView) findViewById(R.id.four_hour_iv);
		four_hour_tv = (TextView) findViewById(R.id.four_hour_tv);
		setParkDivine(four_hour_tv);

		five_hour_iv = (ImageView) findViewById(R.id.five_hour_iv);
		five_hour_tv = (TextView) findViewById(R.id.five_hour_tv);
		setParkDivine(five_hour_tv);

		// 展示地图,高度动态。
		mMapView = (MapView) findViewById(R.id.mapview);

		/*
		 * screenshotImg = (ImageView) findViewById(R.id.iv_screenshot);
		 * ViewUtil.setViewSize(screenshotImg, 376, 640);
		 */

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.desc_iv:
			if (!TextUtils.isEmpty(imageUrl)) {
				showBigImageDialog(imageUrl);
			}
			MobclickAgent.onEvent(context, "detailParking_pic_click");
			break;
		/*
		 * case R.id.price_rl: // 收费详情 showPriceDialog();
		 * MobclickAgent.onEvent(context, "detailParking_charge_click"); break;
		 * case R.id.close_btn: // 收费详情对话框的关闭按钮 priceDialog.dismiss();
		 * MobclickAgent.onEvent(context, "chargeDetails_closeBtn_click");
		 * break;
		 */
		case R.id.lease_rl: // 租赁详情
			ParkApplication.mLeaseType = data.getDefaluttype();
			ToolUtil.IntentClass(activity, LeaseDetailActivity.class, false);
			MobclickAgent.onEvent(context, "detailParking_lease_click");
			break;
		case R.id.nav_rl: // 到这去
			ParkApplication.mLatEnd = data.getLatitude();
			ParkApplication.mLonEnd = data.getLongitude();
			ToolUtil.IntentClass(activity, NavVoiceActivity.class, false);
			MobclickAgent.onEvent(context, "detailParking_GPSbtn_click");
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：车位预测样式表
	 * 日期：	2016年12月16日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	private void setParkDivine(TextView text) {
		ViewUtil.setTextSize(text, 18);
		ViewUtil.setMarginLeft(text, 5, ViewUtil.RELATIVELAYOUT);
	}

	/**
	 * <pre>
	 * 功能说明：初始化地图
	 * 日期：	2015年5月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param savedInstanceState
	 * </pre>
	 */
	public void initMap(Bundle savedInstanceState) {

		mMapView.onCreate(savedInstanceState);
		mAMap = mMapView.getMap();

		// 得到地图UI设置类
		// 设置是否允许显示缩放按钮,默认缩放按钮为显示
		mAMap.getUiSettings().setZoomControlsEnabled(false);

		// // 设置定位按钮是否显示, 默认定位按钮为显示
		// // 开启图层定位
		// mAMap.setOnMapClickListener(this);

		// 自定义定位蓝点图标
		// showLocationIcon();

		// 中心点坐标
		LatLng latlng = getIntent().getParcelableExtra("latlng");

		// 移动地图
		float level = 18f;
		mAMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(latlng, level, 0f, 0f)));

		// // 设置默认缩放度，默认级别3~20
		// mAMap.moveCamera(CameraUpdateFactory.zoomTo(level));

		// 设置中心点
		MarkerOptions marker = new MarkerOptions();
		marker.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.icon_park_center)));
		marker.position(latlng);

		mAMap.addMarker(marker);

		mAMap.postInvalidate();

		mAMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChangeFinish(CameraPosition arg0) {
				// getMapScreenShot();
			}

			@Override
			public void onCameraChange(CameraPosition arg0) {

			}
		});
		// float miao = AMapUtils.calculateLineDistance(
		// new com.amap.api.maps.model.LatLng(ParkApplication.mLat,
		// ParkApplication.mLon),
		// new com.amap.api.maps.model.LatLng(ParkApplication.mLatEnd,
		// ParkApplication.mLonEnd));
		//
		// System.out.println(miao);
		//
		// mAMap.setOnCameraChangeListener(new OnCameraChangeListener() {
		//
		// @Override
		// public void onCameraChangeFinish(CameraPosition arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onCameraChange(CameraPosition arg0) {
		// System.out.println("级别：" + arg0.zoom);
		//
		// }
		// });

	}

	/**
	 * <pre>
	 * 功能说明：地图截屏
	 * 日期：	2016年12月14日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	private void getMapScreenShot() {

		mAMap.getMapScreenShot(new OnMapScreenShotListener() {

			@Override
			public void onMapScreenShot(Bitmap bitmap) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
						Locale.getDefault());
				if (null == bitmap) {
					return;
				}
				try {

					String path = Environment.getExternalStorageDirectory()
							.toString() + "/51Park/screenshot/";
					File file = new File(path);
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						if (!file.exists()) {
							file.mkdirs();
						}
					}

					FileOutputStream fos = new FileOutputStream(path
							+ "/51Park_" + sdf.format(new java.util.Date())
							+ ".png");
					boolean b = bitmap.compress(CompressFormat.PNG, 100, fos);

					try {
						fos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					StringBuffer buffer = new StringBuffer();
					if (b) {
						buffer.append("截屏成功 ");
						mMapView.setVisibility(View.GONE);
						/* screenshotImg.setImageBitmap(bitmap); */
					} else {
						buffer.append("截屏失败 ");
					}
					// if (status != 0)
					// buffer.append("地图渲染完成，截屏无网格");
					// else {
					// buffer.append( "地图未渲染完成，截屏有网格");
					// }
					//
					ToastUtil.show(buffer.toString());

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * <pre>
	 * 功能说明：功能说明：将出入口的URL转换成Bitmap对象
	 * 日期：	2015年12月28日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param type	类型(0: 入口坐标; 1: 出口坐标; 2: 出入口坐标)
	 * @param direct 方向(1: 北; 2: 南; 3: 西; 4: 东 )
	 * @return Bitmap对象
	 * </pre>
	 */
	public Bitmap showInOutBitmap(int type, int direct) {

		int resId = 0;
		if (type == 0) {
			resId = R.drawable.icon_park_entry;
		} else if (type == 1) {
			resId = R.drawable.icon_park_exit;
		} else if (type == 2) {
			resId = R.drawable.icon_park_exit_entry;
		}

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId)
				.copy(Bitmap.Config.ARGB_8888, true);

		return bitmap;
	}

	/**
	 * <pre>
	 * 功能说明：加载停车场的出入口
	 * 日期：	2015年12月28日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param parkId 停车场Id
	 * </pre>
	 */
	public void parseLoadInOut() {

		// searchPBar.setVisibility(View.VISIBLE);
		LogUtil.e("出入口方法");
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("parkid", ParkApplication.mParkId);

		LogUtil.d("【停车场出入口URL】" + Constant.ENEXIT_URL, params);

		httpUtil.parse(httpUtil.POST, Constant.ENEXIT_URL,
				ParkInOutEntity.class, params,
				new HttpUtil.onResult<ParkInOutEntity>() {

					@Override
					public void onSuccess(ParkInOutEntity result) {

						List<ParkInOutInfo> list = result.getData().getList();

						mInOutMarkars.clear();

						int size = list.size();

						for (int i = 0; i < size; i++) {

							MarkerOptions makerOptions = new MarkerOptions();

							int type = list.get(i).getIseneixt();
							int direct = list.get(i).getDirection();

							/**
							 * 设置出入口的图片
							 */
							makerOptions.icon(BitmapDescriptorFactory
									.fromBitmap(showInOutBitmap(type, direct)));

							// 出入口坐标
							LatLng latlng = new LatLng(Double.valueOf(list.get(
									i).getLatitude()), Double.valueOf(list.get(
									i).getLongitude()));
							makerOptions.position(latlng);

							Marker addMarker = mAMap.addMarker(makerOptions);
							addMarker.setObject(list.get(i));
							mInOutMarkars.add(addMarker);

						}

						// searchPBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						// searchPBar.setVisibility(View.INVISIBLE);
					}
				});

	}

	/**
	 * <pre>
	 * 功能说明：停车场详情图片点击放大效果
	 * 日期：	2015年12月11日
	 * 开发者：	任建飞
	 * 
	 * @param imgUrl 图片地址
	 * </pre>
	 */
	private void showBigImageDialog(String imgUrl) {
		dialog = new Dialog(this, R.style.Dialog_Fullscreen);
		UrlTouchImageView iv = new UrlTouchImageView(getApplicationContext());
		iv.setUrl(imgUrl);
		iv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		dialog.setContentView(iv);
		iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					dialog.hide();
					break;
				}
				return true;
			}
		});
		dialog.show();
	}

	/**
	 * <pre>
	 * 功能说明：展示收费详情对话框
	 * 日期：	2015年11月13日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	/*
	 * private void showPriceDialog() {
	 * 
	 * priceDialog = new Dialog(context);
	 * priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * priceDialog.setContentView(R.layout.park_info_price);
	 * 
	 * 
	 * 设置dialog样式
	 * 
	 * Window window = priceDialog.getWindow();
	 * window.setGravity(Gravity.BOTTOM);
	 * window.setBackgroundDrawableResource(R.color.transparent); LayoutParams
	 * attributes = priceDialog.getWindow().getAttributes(); attributes.width =
	 * ViewUtil.getWidth(640); // attributes.height = ViewUtil.getHeight(800);
	 * 
	 * // 关闭按钮 close_btn = (Button) priceDialog.findViewById(R.id.close_btn);
	 * close_btn.setOnClickListener(this); ViewUtil.setViewSize(close_btn, 38,
	 * 38); ViewUtil.setMarginRight(close_btn, 20, ViewUtil.RELATIVELAYOUT);
	 * ViewUtil.setMarginTop(close_btn, 20, ViewUtil.RELATIVELAYOUT);
	 * 
	 * // 标题 TextView price_tv = (TextView)
	 * priceDialog.findViewById(R.id.price_tv); ViewUtil.setTextSize(price_tv,
	 * 36); ViewUtil.setViewSize(price_tv, 96, 0);
	 * 
	 * // 虚线 View line_view = priceDialog.findViewById(R.id.line_view);
	 * ViewUtil.setViewSize(line_view, 2, 600);
	 * ViewUtil.setMarginBottom(line_view, 20, ViewUtil.RELATIVELAYOUT);
	 * 
	 * // 白天价格 TextView today_price_tv = (TextView) priceDialog
	 * .findViewById(R.id.today_price_tv); ViewUtil.setViewSize(today_price_tv,
	 * 50, 580); ViewUtil.setTextSize(today_price_tv, 24);
	 * today_price_tv.setText(data.getDpricedaytime());
	 * 
	 * // 白天价格内容 LinearLayout today_price_ll = (LinearLayout) priceDialog
	 * .findViewById(R.id.today_price_ll); ViewUtil.setViewSize(today_price_tv,
	 * 0, 580); ViewUtil.setMarginBottom(today_price_ll, 40,
	 * ViewUtil.RELATIVELAYOUT);
	 * 
	 * // 夜间价格 TextView night_price_tv = (TextView) priceDialog
	 * .findViewById(R.id.night_price_tv); ViewUtil.setViewSize(night_price_tv,
	 * 50, 580); ViewUtil.setTextSize(night_price_tv, 24);
	 * night_price_tv.setText(data.getDpricenighttime());
	 * 
	 * // 夜晚价格内容 LinearLayout night_price_ll = (LinearLayout) priceDialog
	 * .findViewById(R.id.night_price_ll); ViewUtil.setViewSize(night_price_ll,
	 * 0, 580); ViewUtil.setMarginBottom(night_price_ll, 40,
	 * ViewUtil.RELATIVELAYOUT);
	 *//**
	 * 白天价格所有item
	 */
	/*
	 * for (int i = 0; i < todayList.size(); i++) { RelativeLayout price_rl =
	 * new RelativeLayout(context);
	 * 
	 * price_rl.setGravity(Gravity.CENTER_VERTICAL); LinearLayout.LayoutParams
	 * layoutParams = new LinearLayout.LayoutParams( ViewUtil.getWidth(580),
	 * ViewUtil.getHeight(50)); price_rl.setLayoutParams(layoutParams);
	 * 
	 * // 信息 TextView info = new TextView(context);
	 * info.setTextColor(Color.parseColor("#313131"));
	 * info.setText(todayList.get(i).getInfo()); ViewUtil.setTextSize(info, 24);
	 * ViewUtil.setMarginLeft(info, 43, ViewUtil.RELATIVELAYOUT);
	 * 
	 * price_rl.addView(info);
	 * 
	 * // 价格 TextView price = new TextView(context);
	 * price.setTextColor(Color.parseColor("#313131"));
	 * price.setText(todayList.get(i).getPrice()); ViewUtil.setTextSize(price,
	 * 24); ViewUtil.setMarginLeft(price, 460, ViewUtil.RELATIVELAYOUT);
	 * 
	 * price_rl.addView(price);
	 * price_rl.setBackgroundResource(R.drawable.draw_view_border);
	 * 
	 * today_price_ll.addView(price_rl); }
	 *//**
	 * 晚上天价格所有item
	 */
	/*
	 * for (int i = 0; i < nightList.size(); i++) { RelativeLayout price_rl =
	 * new RelativeLayout(context);
	 * 
	 * price_rl.setGravity(Gravity.CENTER_VERTICAL); LinearLayout.LayoutParams
	 * layoutParams = new LinearLayout.LayoutParams( ViewUtil.getWidth(580),
	 * ViewUtil.getHeight(50)); price_rl.setLayoutParams(layoutParams);
	 * 
	 * // 信息 TextView info = new TextView(context);
	 * info.setTextColor(Color.parseColor("#313131"));
	 * info.setText(nightList.get(i).getInfo()); ViewUtil.setTextSize(info, 24);
	 * ViewUtil.setMarginLeft(info, 43, ViewUtil.RELATIVELAYOUT);
	 * 
	 * price_rl.addView(info);
	 * 
	 * // 价格 TextView price = new TextView(context);
	 * price.setTextColor(Color.parseColor("#313131"));
	 * price.setText(nightList.get(i).getPrice()); ViewUtil.setTextSize(price,
	 * 24); ViewUtil.setMarginLeft(price, 460, ViewUtil.RELATIVELAYOUT);
	 * 
	 * price_rl.addView(price);
	 * price_rl.setBackgroundResource(R.drawable.draw_view_border);
	 * 
	 * night_price_ll.addView(price_rl); }
	 * 
	 * priceDialog.show(); }
	 */

	/**
	 * <pre>
	 * 功能说明：展示未来五小时车位预测情况
	 * 日期：	2015年11月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showTimeStateInfo() {

		int size = stateList.size();
		int timeByHour = TimeUtil.getHourTime();
		for (int i = 0; i < size; i++) {
			int hour = timeByHour + i + 1;
			if (hour > 24) {
				hour = hour - 24;
			}
			if (i == 0) {
				one_hour_iv.setImageResource(showTimeStateImage(stateList
						.get(i).getState()));
				one_hour_tv.setText(hour + "时");

			} else if (i == 1) {
				two_hour_iv.setImageResource(showTimeStateImage(stateList
						.get(2).getState()));

				two_hour_tv.setText(hour + "时");

			} else if (i == 2) {
				three_hour_iv.setImageResource(showTimeStateImage(stateList
						.get(1).getState()));
				three_hour_tv.setText(hour + "时");

			} else if (i == 3) {
				four_hour_iv.setImageResource(showTimeStateImage(stateList.get(
						2).getState()));
				four_hour_tv.setText(hour + "时");

			} else if (i == 4) {
				five_hour_iv.setImageResource(showTimeStateImage(stateList.get(
						2).getState()));
				five_hour_tv.setText(hour + "时");

			}
		}
	}

	/**
	 * <pre>
	 * 功能说明： 设置预测时间块的颜色
	 * 日期：	2015年11月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param state
	 * @return
	 * </pre>
	 */
	private int showTimeStateImage(String state) {

		// 空车位的状态图片显示(1: 充足[蓝色]； 2：够用[绿色]； 3：较少[黄色]； 4：紧张[红色]； 5：未知[白色]；6：关闭)
		if (state.equalsIgnoreCase("P1001.jpg")) {
			return R.drawable.icon_park_state_green;

		} else if (state.equalsIgnoreCase("P1002.jpg")) {
			return R.drawable.icon_park_state_green;

		} else if (state.equalsIgnoreCase("P1003.jpg")) {
			return R.drawable.icon_park_state_yellow;

		} else {
			return R.drawable.icon_park_state_red;

		}

	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取停车场详情
	 * 日期：	2015年11月13日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseParkInfo() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("parkid", ParkApplication.mParkId);

		LogUtil.d("【获取停车场详情URL】" + Constant.PARK_DETAIL_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.PARK_DETAIL_URL,
				ParkInfoEntity.class, params, new onResult<ParkInfoEntity>() {

					@Override
					public void onSuccess(ParkInfoEntity result) {

						loadingProgress.dismiss();
						findViewById(R.id.scrollview).setVisibility(
								View.VISIBLE);

						data = result.getData();
						imageUrl = data.getImg().toString().trim();

						// 停车场描述图
						ImageUtil.loadImage(context, desc_iv, imageUrl);

						// 无忧角标:1为无忧2为非无忧
						switch (data.getIs_interior()) {
						case 1:
							isjoin_iv.setVisibility(View.VISIBLE);
							break;
						case 2:
							isjoin_iv.setVisibility(View.GONE);
							break;
						}

						// 停车场名称,当停车场名称长度12时，缩小上边距
						if (data.getName().length() > 12) {
							ViewUtil.setMarginTop(name_tv, 5,
									ViewUtil.RELATIVELAYOUT);
						}
						name_tv.setText(data.getName());

						// 地址//总车位
						address_tv.setText(data.getAddress());
						count_tv.setText("总车位: " + data.getTotalcount());

						// 空车位数
						String str = "空车位: "
								+ (TextUtils.isEmpty(data.getFreecount()) ? "未知"
										: data.getFreecount());
						SpannableStringBuilder style = new SpannableStringBuilder(
								str);
						style.setSpan(
								new ForegroundColorSpan(MapFragment
										.showParkStateColor(data.getState())),
								5, str.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						empty_count_tv.setText(style);

						// 判断有无收费信息
						if (TextUtils.isEmpty(data.getDpriceday())
								|| TextUtils.isEmpty(data.getDpricenight())) {
							price_tv.setVisibility(View.VISIBLE);
						} else {
							price_rl.setVisibility(View.VISIBLE);
							// 日间价格// 夜间价格
							today_price_tv.setText(data.getDpriceday());
							night_price_tv.setText(data.getDpricenight());

							// 收费详情日间/夜间价格列表
							/*
							 * todayList = data.getDpricedaylist(); nightList =
							 * data.getDpricenightlist();
							 * price_rl.setOnClickListener
							 * (ParkInfoActivity.this);
							 */
						}

						// 未来五小时车位预测
						stateList = data.getTimesstate();
						if (stateList.size() != 0) {
							detail_ll.setVisibility(View.VISIBLE);
							// 动态设置地图的高度
//							ViewUtil.setViewSize(mMapView, 320, 640);
							showTimeStateInfo();
						} else {
							// 高度 = 186+376
							ViewUtil.setViewSize(mMapView, 562, 640);
						}
						mMapView.setVisibility(View.VISIBLE);
						
						
						// == 9 表示该停车场非包月计次停车场
						if (data.getDefaluttype() == 9) {
							findViewById(R.id.line0_view).setVisibility(
									View.GONE);
							findViewById(R.id.line1_view).setVisibility(
									View.GONE);
							/* lease_rl.setVisibility(View.GONE); */
						}

						/*
						 * // 是否为包租车位 if (data.getIsmonth() == 0 &&
						 * data.getIstimes() == 0) {
						 * lease_iv.setVisibility(View.GONE); } else if
						 * (data.getIsmonth() == 1 || data.getIstimes() == 1) {
						 * lease_iv.setVisibility(View.VISIBLE); }
						 */

						// // 租赁类型？//包月//计次
						// switch (data.getIsmonth()) {
						// case 0:
						// month_iv.setVisibility(View.GONE);
						// break;
						// case 1:
						// month_iv.setVisibility(View.VISIBLE);
						// break;
						// }
						// switch (data.getIstimes()) {
						// case 0:
						// meter_iv.setVisibility(View.GONE);
						// break;
						// case 1:
						// meter_iv.setVisibility(View.VISIBLE);
						// break;
						// }

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.show(errMsg);
					}
				});

	}

	@Override
	public void onLockMap(boolean arg0) {

	}

	@Override
	public void onNaviCancel() {

	}

	@Override
	public void onNaviMapMode(int arg0) {

	}

	@Override
	public void onNaviSetting() {

	}

	@Override
	public void onNaviTurnClick() {

	}

	@Override
	public void onNextRoadClick() {

	}

	@Override
	public void onScanViewButtonClick() {

	}

	@Override
	public void onMapClick(LatLng arg0) {

	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		parseParkInfo();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAMap.clear();
		mMapView.onDestroy();
	}

}
