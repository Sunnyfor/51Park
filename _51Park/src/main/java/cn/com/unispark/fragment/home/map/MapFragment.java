package cn.com.unispark.fragment.home.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.MapMarkLayout;
import cn.com.unispark.fragment.home.map.MapActivity.updateLocalName;
import cn.com.unispark.fragment.home.map.entity.InOutEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInOutEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInOutEntity.DataObject.ParkInOutInfo;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity.DataObject.ParkItemInfo;
import cn.com.unispark.fragment.home.map.entity.SearchItemEntity.SearchData.SearchDataChild;
import cn.com.unispark.fragment.home.map.navigation.NavVoiceActivity;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.NavEntryUtil;
import cn.com.unispark.util.NavEntryUtil.OnInOutResult;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapTouchListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

/**
 * <pre>
 * 功能说明： 高德地图定位、导航、找车位界面
 * 日期：	2015年5月12日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改人员：
 *    修改日期： 2015年5月19日
 * </pre>
 */
public class MapFragment extends Fragment implements LocationSource,
		AMapLocationListener, OnMapClickListener, OnClickListener {

	private Context context;
	private View view;

	private HttpUtil httpUtil;

	// 路况图标显示与隐藏
	private CheckBox lukuangCbox, parkCBox;

	// 停车场类型
	private int parkType = 1;

	/*
	 * 停车场详情悬浮窗
	 */
	private RelativeLayout map_window;// 地图停车场详情的悬浮窗

	private ImageView nav_iv;// 到这去导航按钮

	/**
	 * 停车场布局（点击进入详情）
	 */
	private RelativeLayout detail_rl;
	// 停车场名称// 停车场地址// 停车场详情// 到这去的距离// 总车位数
	private TextView name_tv, price_tv, distance_tv, total_count_tv,
			empty_count_tv;
	// 车位状态图标//更多按钮//包租车位
	private ImageView state_iv, more_iv, lease_iv;

	// private FrameLayout window_fl;

	/*
	 * 高德地图需要的属性
	 */
	private AMap mAMap;
	private MapView mMapView;
	private UiSettings mUiSettings;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mLocationManagerProxy;
	private LatLng targetLatLng;

	private List<SearchDataChild> parklist;
	// Marker坐标
	private Map<String, Marker> mMarkars = new HashMap<String, Marker>();

	// 出入口的Marker
	private List<Marker> mInOutMarkars = new ArrayList<Marker>();

	// 存放起始位置点和结束位置点的集合
	private double mLat;
	private double mLon;

	private Marker oldMark;

	private LatLng satrtLatLng;

	private int locationMargin = 0;
	private MyLocationStyle myLocationStyle;

	private updateLocalName updateLoalName;

	public void setUpdateLcaoNameListener(updateLocalName updateLocalName) {
		this.updateLoalName = updateLocalName;
	}

	// 地图中心点自定义组件
	private MapMarkLayout mapMarkLayout;

	// 当前位置// 定位当前位置图标
	private TextView location_tv;
	private ImageButton location_ibtn;

	// 放大图标按钮// 缩小图标按钮
	private LinearLayout add_cut_ll;
	private ImageButton btn_zoom_up, btn_zoom_down;

	private boolean isload = false;

	private AMapOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		// activity = getActivity();
		view = inflater.inflate(R.layout.map_main, container, false);
		httpUtil = new HttpUtil(context);

		initView();
		initMap(savedInstanceState);

		return view;
	}

	public void initView() {

		/*
		 * 放大缩小图标按钮布局
		 */
		add_cut_ll = (LinearLayout) view.findViewById(R.id.add_cut_ll);
		ViewUtil.setMarginBottom(add_cut_ll, 30, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(add_cut_ll, 10, ViewUtil.RELATIVELAYOUT);

		// 放大缩小图标按钮
		btn_zoom_up = (ImageButton) view.findViewById(R.id.zoom_up_ibtn);
		btn_zoom_up.setOnClickListener(this);
		btn_zoom_down = (ImageButton) view.findViewById(R.id.zoom_down_ibtn);
		btn_zoom_down.setOnClickListener(this);

		// 路况图标显示与隐藏
		lukuangCbox = (CheckBox) view.findViewById(R.id.lukuang_cb);
		lukuangCbox.setOnClickListener(this);

		// 车位-价格图标切换
		parkCBox = (CheckBox) view.findViewById(R.id.cbox_park);
		parkCBox.setOnClickListener(this);

		// 地图悬浮窗
		map_window = (RelativeLayout) view.findViewById(R.id.map_window);
		map_window.setOnClickListener(this);

		// 定位当前位置的图标
		location_ibtn = (ImageButton) view.findViewById(R.id.location_ibtn);
		location_ibtn.setOnClickListener(this);
		locationMargin = ((LayoutParams) location_ibtn.getLayoutParams()).bottomMargin;

		/*
		 * 当前位置
		 */
		location_tv = (TextView) view.findViewById(R.id.location_tv);
		ViewUtil.setTextSize(location_tv, 24);
		ViewUtil.setViewSize(location_tv, 70, 0);

		// window_fl = (FrameLayout) view.findViewById(R.id.window_fl);

		mapMarkLayout = (MapMarkLayout) view.findViewById(R.id.markLayout);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lukuang_cb:
			if (ShareUtil.getSharedBoolean("traffic")) {
				mAMap.setTrafficEnabled(false);
				lukuangCbox
						.setBackgroundResource(R.drawable.btn_lukuang_noselect);
				ToastUtil.showToast("实时路况已关闭");
				ShareUtil.setSharedBoolean("traffic", false);
			} else {
				mAMap.setTrafficEnabled(true);
				lukuangCbox
						.setBackgroundResource(R.drawable.btn_lukuang_selected);
				ToastUtil.showToast("实时路况已打开");
				ShareUtil.setSharedBoolean("traffic", true);
			}
			break;
		case R.id.cbox_park:
			parseLoadPark(parkType, targetLatLng);
			break;
		case R.id.location_ibtn:
			mAMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(ParkApplication.mCurrentPosition));
			break;
		case R.id.zoom_up_ibtn:
			mAMap.moveCamera(CameraUpdateFactory.zoomTo(mAMap
					.getCameraPosition().zoom + 1));
			break;
		case R.id.zoom_down_ibtn:
			mAMap.moveCamera(CameraUpdateFactory.zoomTo(mAMap
					.getCameraPosition().zoom - 1));
			break;

		}

		// 屏蔽view的传递事件
		map_window.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
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
		// 展示地图
		mMapView = (MapView) view.findViewById(R.id.mapview);
		mMapView.onCreate(savedInstanceState);
		if (mAMap == null) {
			mAMap = mMapView.getMap();

			// 实时图示
			// mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
			// mAMap.setTrafficEnabled(true);

			// 得到地图UI设置类
			mUiSettings = mAMap.getUiSettings();
			// 注册原点
			mAMap.setLocationSource(this);
			// 注册定位监听
			mAMap.setMyLocationEnabled(true);
			// 设置默认缩放度
			mAMap.moveCamera(CameraUpdateFactory
					.zoomTo(ParkApplication.mMapZoomLevel));
			// 设置是否允许显示缩放按钮,默认缩放按钮为显示
			// mUiSettings.setZoomPosition(1);
			mUiSettings.setZoomControlsEnabled(false);
			// 设置定位按钮是否显示, 默认定位按钮为显示
			mAMap.getUiSettings().setMyLocationButtonEnabled(false);
			// 开启图层定位
			mAMap.setMyLocationEnabled(true);
			mAMap.setOnMapClickListener(this);
		}

		options = new AMapOptions();

		/*
		 * 地图触摸监听事件
		 */
		mAMap.setOnMapTouchListener(new OnMapTouchListener() {

			@Override
			public void onTouch(MotionEvent arg0) {

				float currentZoom = mAMap.getCameraPosition().zoom;

				if (currentZoom <= 3) {
					options.zoomControlsEnabled(false);
					options.zoomGesturesEnabled(false);
					mUiSettings.setAllGesturesEnabled(false);
				} else {
					if (!mUiSettings.isScrollGesturesEnabled()) {
						options.zoomControlsEnabled(true);
						options.zoomGesturesEnabled(true);
						mUiSettings.setAllGesturesEnabled(true);
					}
				}

			}
		});

		// 自定义定位蓝点图标
		showLocationIcon();

		/**
		 * 移动地图操作
		 */
		mAMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChangeFinish(CameraPosition position) {

				targetLatLng = position.target;
				if (isload) {
					parseLoadPark(parkType, targetLatLng);
					LogUtil.e("移动地图记载方法调用！");
				}

				hideMapWindow();
			}

			@Override
			public void onCameraChange(CameraPosition position) {

			}
		});

		/**
		 * 当一个marker 对象被点击时调用此方法
		 */
		mAMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				System.out.println("点击marker");

				if (oldMark != null) {

					ParkItemInfo oldItem = (ParkItemInfo) oldMark.getObject();
					String str = !parkCBox.isChecked() ? oldItem.getFreecount()
							: oldItem.getMoney();

					if (oldItem != null) {
						if (parkType != 4) {

							oldMark.setIcon(BitmapDescriptorFactory
									.fromBitmap(showParkIcon(str,
											oldItem.getState())));

						} else {
							boolean isMonth = oldItem.getIsmonth() == 0 ? false
									: true;
							oldMark.setIcon(BitmapDescriptorFactory
									.fromBitmap(getBitmapIcon(str, isMonth,
											true)));
						}

					}
				}

				ParkItemInfo item = (ParkItemInfo) marker.getObject();
				String str = !parkCBox.isChecked() ? item.getFreecount() : item
						.getMoney();

				if (parkType != 4) {
					marker.setIcon(BitmapDescriptorFactory
							.fromBitmap(showParkIcon2x(str, item.getState())));

				} else {
					boolean isMonth = item.getIsmonth() == 0 ? false : true;
					marker.setIcon(BitmapDescriptorFactory
							.fromBitmap(getBitmapIcon(str, isMonth, false)));
				}

				oldMark = marker;

				/*
				 * // 清除出入口的Marker for (int i = 0; i < mInOutMarkars.size();
				 * i++) { mInOutMarkars.get(i).destroy(); }
				 */

				// 出入口
				/* parseLoadInOut(item.getParkid()); */

				// showViewPager(false, true, false);
				showParkWindow(item);

				// // 清除出入口的Marker
				// for (int i = 0; i < mInOutMarkars.size(); i++) {
				// mInOutMarkars.get(i).destroy();
				// }
				//
				// parseLoadInOut(item.getParkid());

				mAMap.invalidate();

				MapActivity.parkFragment.setLatlon(
						marker.getPosition().latitude,
						marker.getPosition().longitude);

				return true;
			}
		});
	}

	/**
	 * <pre>
	 * 功能说明：设置选项卡的点击事件
	 * 日期：	2015年12月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param group
	 * </pre>
	 */

	/*
	 * public void setRadioGroup(RadioGroup group) {
	 * group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	 * 
	 * @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
	 * switch (checkedId) { case R.id.all_rbtn: clearMapData(); parkType = 1;
	 * parseLoadPark(1, null); hideMapWindow(); MobclickAgent.onEvent(context,
	 * "seekParking_all_click"); break; case R.id.park_rbtn: clearMapData();
	 * parkType = 2; parseLoadPark(2, null); hideMapWindow();
	 * MobclickAgent.onEvent(context, "seekParking_51_click"); break; case
	 * R.id.free_rbtn: clearMapData(); parkType = 3; parseLoadPark(3, null);
	 * hideMapWindow(); MobclickAgent.onEvent(context,
	 * "seekParking_free_click"); break; case R.id.month_rbtn: clearMapData();
	 * parkType = 4; parseLoadPark(4, null); hideMapWindow();
	 * MobclickAgent.onEvent(context, "seekParking_monthly_click"); break; } }
	 * }); }
	 */

	public void setparklist(List<SearchDataChild> parklist) {
		this.parklist = parklist;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		hideMapWindow();

		// 清除出入口的Marker
		for (int i = 0; i < mInOutMarkars.size(); i++) {
			mInOutMarkars.get(i).destroy();
		}

	}

	/**
	 * <pre>
	 * 功能说明：隐藏底部悬浮窗是否展示
	 * 日期：	2015年12月3日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void hideMapWindow() {
		// if (map_window.isShown()) {
		ViewUtil.setMarginBottom(add_cut_ll, 30, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginBottom(location_ibtn, 30, ViewUtil.RELATIVELAYOUT);
		map_window.setVisibility(View.GONE);
		((LayoutParams) location_ibtn.getLayoutParams()).bottomMargin = locationMargin;

		if (oldMark != null) {
			ParkItemInfo oldItem = (ParkItemInfo) oldMark.getObject();
			// String money = oldItem.getMoney();
			String str = !parkCBox.isChecked() ? oldItem.getFreecount()
					: oldItem.getMoney();

			if (oldItem != null) {

				if (parkType != 4) {

					oldMark.setIcon(BitmapDescriptorFactory
							.fromBitmap(showParkIcon(str, oldItem.getState())));

				} else {
					boolean isMonth = oldItem.getIsmonth() == 0 ? false : true;
					oldMark.setIcon(BitmapDescriptorFactory
							.fromBitmap(getBitmapIcon(str, isMonth, true)));
				}
				mAMap.invalidate();

			}
		}

	}

	// }

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		showLocationIcon();

	}

	public void moveMap(LatLng latlng) {
		// 移动地图
		mAMap.moveCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(latlng,
						ParkApplication.mMapZoomLevel, 0f, 0f)));
		mAMap.invalidate();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
		deactivate();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		httpUtil.close();
		mMarkars.clear();
		mAMap.clear();
		mMapView.onDestroy();
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		mListener = arg0;
		if (mLocationManagerProxy == null) {
			mLocationManagerProxy = LocationManagerProxy.getInstance(context);
			mLocationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destroy();
		}
		mLocationManagerProxy = null;
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (mListener != null && location != null) {
			mListener.onLocationChanged(location);// 显示系统小蓝点
			mLat = location.getLatitude();
			mLon = location.getLongitude();
			satrtLatLng = new LatLng(mLat, mLon);// 创建包含经纬度的对象
			ParkApplication.mCurrentPosition = new CameraPosition(satrtLatLng,
					ParkApplication.mMapZoomLevel, 0f, 0f); // 记录定位后的经纬度

			ParkApplication.CurrentCity = location.getCity();
			ParkApplication.CityCode = location.getCityCode();

			// mCurrentLocation = location.getPoiName();
			location_tv.setText("当前位置：" + location.getPoiName());
			LogUtil.e("当前位置：" + location.getPoiName());

			updateLoalName.update(location.getPoiName());
			if (!isload) {
				isload = true;
			}

		}

	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	/**
	 * <pre>
	 * 功能说明：初始化地图悬浮窗
	 * 日期：	2015年6月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param item
	 * </pre>
	 */
	public void showParkWindow(final ParkItemInfo item) {
		map_window.setVisibility(View.VISIBLE);
		ViewUtil.setMarginBottom(add_cut_ll, 200, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginBottom(location_ibtn, 150, ViewUtil.RELATIVELAYOUT);
		mAMap.invalidate();
		/*
		 * 停车场悬浮框布局大小,点击进入停车场详情
		 */
		if (detail_rl == null) {
			detail_rl = (RelativeLayout) view.findViewById(R.id.detail_rl);
			ViewUtil.setViewSize(detail_rl, 176, 0);
			ViewUtil.setMarginTop(detail_rl, 50, ViewUtil.RELATIVELAYOUT);
		}
		detail_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParkApplication.mParkId = item.getParkid();

				// ToolUtil.IntentClass(getActivity(), ParkInfoActivity.class,
				// false);

				LatLng latlng = new LatLng(item.getLatitude(), item
						.getLongitude());

				Intent intent = new Intent(getActivity(),
						ParkInfoActivity.class);
				intent.putExtra("latlng", latlng);
				startActivity(intent);

			}
		});

		/*
		 * 到这去图标
		 */
		if (nav_iv == null) {
			nav_iv = (ImageView) view.findViewById(R.id.nav_iv);
			ViewUtil.setMarginRight(nav_iv, 20, ViewUtil.RELATIVELAYOUT);

		}

		ParkApplication.mLatEnd = Double.valueOf(item.getLatitude());
		ParkApplication.mLonEnd = Double.valueOf(item.getLongitude());

		nav_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				===========================================================
				NavEntryUtil navEntryUtil = new NavEntryUtil(context, item
						.getParkid());
				navEntryUtil.getLatLng(new OnInOutResult() {
					
					@Override
					public void onResult(List<InOutEntity> inOutList) {
//						System.out.println("inOutList：" + inOutList.size());
//						for (int i = 0; i < inOutList.size(); i++) {
//							System.out.println(inOutList.get(i).getLatitude() + " - "
//									+ inOutList.get(i).getLongitude());
//						}
					}
				});
				
//				===========================================================
				ToolUtil.IntentClass(getActivity(), NavVoiceActivity.class,
						false);
			}
		});

		/*
		 * 车位数状态图标
		 */
		if (state_iv == null) {
			state_iv = (ImageView) view.findViewById(R.id.state_iv);
		}
		showParkStateColor(item.getState(), state_iv);

		/*
		 * 更多按钮
		 */
		if (more_iv == null) {
			more_iv = (ImageView) view.findViewById(R.id.more_iv);
			ViewUtil.setMarginRight(more_iv, 20, ViewUtil.RELATIVELAYOUT);
		}

		/*
		 * 停车场的名称
		 */
		if (name_tv == null) {
			name_tv = (TextView) view.findViewById(R.id.name_tv);
			ViewUtil.setMargin(name_tv, 24, 160, 16, 28,
					ViewUtil.RELATIVELAYOUT);
			ViewUtil.setTextSize(name_tv, 28);
		}
		name_tv.setText(item.getName());

		/*
		 * 总车位数
		 */
		if (total_count_tv == null) {
			total_count_tv = (TextView) view.findViewById(R.id.total_count_tv);
			ViewUtil.setTextSize(total_count_tv, 24);
			ViewUtil.setMarginRight(total_count_tv, 5, ViewUtil.RELATIVELAYOUT);
		}
		total_count_tv.setText("总: " + item.getAllcount());
		// String str = "总: " + item.getAllcount();
		// SpannableStringBuilder style = new SpannableStringBuilder(str);
		// style.setSpan(
		// new ForegroundColorSpan(showParkStateColor(item.getState(),
		// state_iv)), 3, str.length(),
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// total_count_tv.setText(style);

		// total_count_tv.setTextColor(showParkStateColor(item.getState(),
		// state_iv));

		/*
		 * 空车位数量
		 */
		if (empty_count_tv == null) {
			empty_count_tv = (TextView) view.findViewById(R.id.empty_count_tv);
			ViewUtil.setTextSize(empty_count_tv, 24);
		}

		// 空车位数量用红色标识
		if (!TextUtils.isEmpty(item.getFreecount())) {
			String str = "空: " + item.getFreecount() + " (空余车位仅供参考)";
			SpannableStringBuilder style = new SpannableStringBuilder(str);
			style.setSpan(
					new ForegroundColorSpan(showParkStateColor(item.getState())),
					3, str.length() - 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			// (空余车位仅供参考)颜色改为灰色，字号18
			style.setSpan(new ForegroundColorSpan(Color.parseColor("#bbbbbb")),
					str.length() - 10, str.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new AbsoluteSizeSpan(ViewUtil.getWidth(18), false),
					str.length() - 10, str.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			empty_count_tv.setText(style);
		} else {
			String str = "空: 未知";
			SpannableStringBuilder style = new SpannableStringBuilder(str);
			style.setSpan(
					new ForegroundColorSpan(getResources().getColor(
							R.color.gray_fontbb)), 3, str.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			empty_count_tv.setText(style);
		}

		/*
		 * 当前位置到指定位置的距离
		 */
		if (distance_tv == null) {
			distance_tv = (TextView) view.findViewById(R.id.distance_tv);
			ViewUtil.setTextSize(distance_tv, 20);
			ViewUtil.setDrawablePadding(distance_tv, 5);
			ViewUtil.setMarginLeft(distance_tv, 126, ViewUtil.RELATIVELAYOUT);
		}

		distance_tv.setText(ReckonUtil.getDistanceFormat(item.getFar()));
		// System.err.println("距离：" + item.getFar());

		/*
		 * 停车场价格
		 */
		if (price_tv == null) {
			price_tv = (TextView) view.findViewById(R.id.price_tv);
			ViewUtil.setTextSize(price_tv, 24);
			ViewUtil.setMargin(price_tv, 18, 0, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginRight(price_tv, 10, ViewUtil.RELATIVELAYOUT);
		}

		price_tv.setText("价格: " + item.getPrice());

		/*
		 * 停车场类型:包月
		 */
		if (lease_iv == null) {
			lease_iv = (ImageView) view.findViewById(R.id.lease_iv);
		}
		if (item.getIsmonth() == 0 && item.getIstimes() == 0) {
			lease_iv.setVisibility(View.GONE);
		} else if (item.getIsmonth() == 1 || item.getIstimes() == 1) {
			lease_iv.setVisibility(View.VISIBLE);
		}

		// 显示底部悬浮窗口
		// ((LayoutParams) location_ibtn.getLayoutParams()).bottomMargin =
		// locationMargin + 88;
		// window_fl.getParent().bringChildToFront(window_fl);
	}

	/**
	 * <pre>
	 * 功能说明：更改浮窗中车位状态标识
	 * 日期：	2015年6月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param state
	 * @param img
	 * @return
	 * </pre>
	 */
	public void showParkStateColor(String state, ImageView img) {
		if (state.equalsIgnoreCase("P1001.jpg")
				|| state.equalsIgnoreCase("P1001P.jpg")) {
			img.setImageResource(R.drawable.label_park_green);
		} else if (state.equalsIgnoreCase("P1002.jpg")
				|| state.equalsIgnoreCase("P1002P.jpg")) {
			img.setImageResource(R.drawable.label_park_green);
		} else if (state.equalsIgnoreCase("P1003.jpg")
				|| state.equalsIgnoreCase("P1003P.jpg")) {
			img.setImageResource(R.drawable.label_park_yellow);
		} else if (state.equalsIgnoreCase("P1004.jpg")
				|| state.equalsIgnoreCase("P1004P.jpg")) {
			img.setImageResource(R.drawable.label_park_red);
		} else if (state.equalsIgnoreCase("P1005.jpg")
				|| state.equalsIgnoreCase("P1005P.jpg")) {
			img.setImageResource(R.drawable.label_park_gray);
		} else if (state.equalsIgnoreCase("P1006.jpg")
				|| state.equalsIgnoreCase("P1006P.jpg")) {
			img.setImageResource(R.drawable.label_park_red);
		} else {
			img.setImageResource(R.drawable.label_park_red);
		}

	}

	/**
	 * <pre>
	 * 功能说明：更改浮窗中车位数量的颜色
	 * 日期：	2015年6月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param state
	 * @param img
	 * @return
	 * </pre>
	 */
	public static int showParkStateColor(String state) {
		int color = 0;
		if (state.equalsIgnoreCase("P1001.jpg")
				|| state.equalsIgnoreCase("P1001P.jpg")) {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.green_park);
		} else if (state.equalsIgnoreCase("P1002.jpg")
				|| state.equalsIgnoreCase("P1002P.jpg")) {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.green_park);
		} else if (state.equalsIgnoreCase("P1003.jpg")
				|| state.equalsIgnoreCase("P1003P.jpg")) {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.yellow_park);
		} else if (state.equalsIgnoreCase("P1004.jpg")
				|| state.equalsIgnoreCase("P1004P.jpg")) {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.red_park);
		} else if (state.equalsIgnoreCase("P1005.jpg")
				|| state.equalsIgnoreCase("P1005P.jpg")) {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.gray_park);
		} else if (state.equalsIgnoreCase("P1006.jpg")
				|| state.equalsIgnoreCase("P1006P.jpg")) {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.red_park);
		} else {
			color = ParkApplication.applicationContext.getResources().getColor(
					R.color.red_park);
		}
		return color;

	}

	/**
	 * <pre>
	 * 功能说明： 通过状态码判断显示的图标
	 * 日期：	2015年6月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param state
	 * @param img
	 * @return
	 * </pre>
	 */
	public Bitmap showParkIcon(String money, String state) {
		int icon = 0;
		int clolor = 0;
		boolean isWuyou = false;
		if (state.equalsIgnoreCase("P1001.jpg")) {// 普通绿
			icon = !parkCBox.isChecked() ? R.drawable.park_green
					: R.drawable.park_green_rmb;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1002.jpg")) {// 普通绿
			icon = !parkCBox.isChecked() ? R.drawable.park_green
					: R.drawable.park_green_rmb;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1003.jpg")) {// 普通黄
			icon = !parkCBox.isChecked() ? R.drawable.park_yellow
					: R.drawable.park_yellow_rmb;
			clolor = R.color.orange_font;
		} else if (state.equalsIgnoreCase("P1004.jpg")) {// 普通红
			icon = !parkCBox.isChecked() ? R.drawable.park_red
					: R.drawable.park_red_rmb;
			clolor = R.color.red_font;
		} else if (state.equalsIgnoreCase("P1005.jpg")) {// 普通灰
			icon = !parkCBox.isChecked() ? R.drawable.park_gray
					: R.drawable.park_gray_rmb;
			clolor = R.color.gray_font;
			// clolor = parkCBox.isChecked() ? R.color.gray_font :
			// R.color.white;
		} else if (state.equalsIgnoreCase("PFree.jpg")) {// 免费
			icon = R.drawable.park_free;

		} else if (state.equalsIgnoreCase("P1001P.jpg")) {// 无忧绿
			icon = R.drawable.park_51_green;
			clolor = R.color.green_font;
			isWuyou = true;
		} else if (state.equalsIgnoreCase("P1002P.jpg")) {// 无忧绿
			icon = R.drawable.park_51_green;
			clolor = R.color.green_font;
			isWuyou = true;
		} else if (state.equalsIgnoreCase("P1003P.jpg")) {// 无忧黄
			icon = R.drawable.park_51_yellow;
			clolor = R.color.orange_font;
			isWuyou = true;
		} else if (state.equalsIgnoreCase("P1004P.jpg")) {// 无忧红
			icon = R.drawable.park_51_red;
			clolor = R.color.red_font;
			isWuyou = true;
		} else if (state.equalsIgnoreCase("P1005P.jpg")) {// 无忧灰
			icon = R.drawable.park_51_gray;
			clolor = R.color.gray_font;
			isWuyou = true;
		} else {// 普通红
			icon = R.drawable.park_red;
			clolor = R.color.red_font;
		}

		return showBitmapIcon(money, icon, clolor, 18, isWuyou, false);
	}

	/**
	 * <pre>
	 * 功能说明： 通过状态码判断显示的图标（2倍图标）
	 * 日期：	2015年6月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param state
	 * @param img
	 * @return
	 * </pre>
	 */
	public Bitmap showParkIcon2x(String money, String state) {
		int icon = 0;
		int clolor = 0;
		boolean isJoin = false;
		if (state.equalsIgnoreCase("P1001.jpg")) {// 普通绿
			icon = !parkCBox.isChecked() ? R.drawable.park_green2x
					: R.drawable.park_green_rmb2x;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1002.jpg")) {// 普通绿
			icon =!parkCBox.isChecked() ? R.drawable.park_green2x
					: R.drawable.park_green_rmb2x;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1003.jpg")) {// 普通黄
			icon = !parkCBox.isChecked() ? R.drawable.park_yellow2x
					: R.drawable.park_yellow_rmb2x;
			clolor = R.color.orange_font;
		} else if (state.equalsIgnoreCase("P1004.jpg")) {// 普通红
			icon = !parkCBox.isChecked() ? R.drawable.park_red2x
					: R.drawable.park_red_rmb2x;
			clolor = R.color.red_font;
		} else if (state.equalsIgnoreCase("P1005.jpg")) {// 普通灰
			icon = !parkCBox.isChecked() ? R.drawable.park_gray2x
					: R.drawable.park_gray_rmb2x;
			clolor = R.color.gray_font;
		} else if (state.equalsIgnoreCase("PFree.jpg")) {// 免费
			icon = R.drawable.park_free2x;
		} else if (state.equalsIgnoreCase("P1001P.jpg")) {// 无忧绿
			icon = R.drawable.park_51_green2x;
			clolor = R.color.green_font;
			isJoin = true;
		} else if (state.equalsIgnoreCase("P1002P.jpg")) {// 无忧绿
			icon = R.drawable.park_51_green2x;
			clolor = R.color.green_font;
			isJoin = true;
		} else if (state.equalsIgnoreCase("P1003P.jpg")) {// 无忧黄
			icon = R.drawable.park_51_yellow2x;
			clolor = R.color.orange_font;
			isJoin = true;
		} else if (state.equalsIgnoreCase("P1004P.jpg")) {// 无忧红
			icon = R.drawable.park_51_red2x;
			clolor = R.color.red_font;
			isJoin = true;
		} else if (state.equalsIgnoreCase("P1005P.jpg")) {// 无忧灰
			icon = R.drawable.park_51_gray2x;
			clolor = R.color.gray_font;
			isJoin = true;
		} else {// 普通红
			icon = R.drawable.park_red2x;
			clolor = R.color.red_font;
		}

		return showBitmapIcon(money, icon, clolor, 22, isJoin, true);
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
	 * 功能说明：全部/免费/无忧 自定义覆盖物样式
	 * 日期：	2015年11月17日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param moeny 覆盖物上的文字
	 * @param res 覆盖物图标
	 * @param isBigIcon 是否为2倍图
	 * @return
	 * </pre>
	 */
	public Bitmap showBitmapIcon(String moeny, int res, int textColor,
			int textSize, boolean isJoin, boolean isBigIcon) {

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res).copy(
				Bitmap.Config.ARGB_8888, true);

		Canvas canvas = new Canvas(bitmap);
		TextPaint text = new TextPaint(Paint.FAKE_BOLD_TEXT_FLAG);
		text.setTextSize(ViewUtil.getWidth(textSize));
		if (textColor != 0) {
			text.setColor(getResources().getColor(textColor));
		}

		int byteLength;
		if (isBigIcon) {
			byteLength = 12;
		} else {
			byteLength = 10;
		}

		int x = (bitmap.getWidth() - ViewUtil.getWidth(byteLength)
				* moeny.length()) / 2;

		int y = 0;
		if (!parkCBox.isChecked()) {
			y = (int) (bitmap.getHeight() / 3 * 1.3);
		}else{
			y = (int) (bitmap.getHeight() / 3 * 1.4);
		}

		if (isJoin) {
			y = (int) (bitmap.getHeight() / 3 * 0.9);
		}

		canvas.drawText(moeny, x, y, text);// 设置bitmap上面的文字位置
		return bitmap;

	}

	/**
	 * <pre>
	 * 功能说明：包月/计次 自定义覆盖物样式
	 * 日期：	2015年11月17日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param money 覆盖物上的文字
	 * @param isMonth true为包月 false为计次
	 * @param isSmall true 小图  false 大图
	 * @return
	 * </pre>
	 */
	public Bitmap getBitmapIcon(String money, boolean isMonth, boolean isSmall) {
		Bitmap bitmap;
		if (isMonth) {
			if (isSmall) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.park_month).copy(Bitmap.Config.ARGB_8888,
						true);
			} else {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.park_month2x).copy(Bitmap.Config.ARGB_8888,
						true);
			}
		} else {
			if (isSmall) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.park_meter).copy(Bitmap.Config.ARGB_8888,
						true);
			} else {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.park_meter2x).copy(Bitmap.Config.ARGB_8888,
						true);
			}
		}

		Canvas canvas = new Canvas(bitmap);
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(ViewUtil.getWidth(18));
		textPaint.setColor(Color.RED);
		int x = (bitmap.getWidth() - ViewUtil.getWidth(12) * money.length()) / 2;
		int y = (int) (bitmap.getHeight() / 3 * 1);
		canvas.drawText(money, x, y, textPaint);// 设置bitmap上面的文字位置
		return bitmap;
	}

	/**
	 * <pre>
	 * 功能说明：清理地图标记
	 * 日期：	2015年11月17日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void clearMapData() {
		mAMap.clear();
		mMarkars.clear();
		mInOutMarkars.clear();
		oldMark = null;
		showLocationIcon();
	}

	/**
	 * <pre>
	 * 功能说明：自定义定位蓝点图标
	 * 日期：	2015年12月3日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void showLocationIcon() {
		myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_location));

		myLocationStyle.radiusFillColor(Color.parseColor("#120000ff"));
		mAMap.setMyLocationStyle(myLocationStyle);
	}

	/**
	 * <pre>
	 * 功能说明：【解析】加载停车场
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param parkType 停车场类型
	 * 1--全部
	 * 2--无忧
	 * 3--免费
	 * 4--包月
	 * </pre>
	 */
	public void parseLoadPark(final int parkType, LatLng latlng) {
		clearMapData();
		// mMarkars.clear();
		// mAMap.clear();

		// searchPBar.setVisibility(View.VISIBLE);
		mapMarkLayout.showDialog();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("type", Integer.toString(parkType));
		params.put(
				"distance",
				Float.toString(mAMap.getScalePerPixel()
						* ViewUtil.getWidth(640)));

		if (latlng != null) {
			params.put("latitude", String.valueOf(latlng.latitude));
			params.put("longitude", String.valueOf(latlng.longitude));
		} else {
			params.put("latitude", String.valueOf(ParkApplication.mLat));
			params.put("longitude", String.valueOf(ParkApplication.mLon));
		}

		httpUtil.parse(httpUtil.POST, Constant.PARK_LIST_URL,
				ParkItemEntity.class, params,
				new HttpUtil.onResult<ParkItemEntity>() {

					@Override
					public void onSuccess(ParkItemEntity result) {

						List<ParkItemInfo> list = result.getData().getList();
						int size = list.size();
						// markLayout.setCount(size);
						mapMarkLayout.closeDialog();

						LogUtil.e("网络请求调用！");

						if (result.getData().getList() == null
								|| result.getData().getList().size() == 0) {
							ToastUtil.show("附近暂无停车场");
						}

						for (int i = 0; i < size; i++) {

							if (mMarkars.get(list.get(i).getName()) == null) {

								/**
								 * 设置停车场标记
								 */
								MarkerOptions makerOptions = new MarkerOptions();

								String str = !parkCBox.isChecked() ? list.get(i)
										.getFreecount() : list.get(i)
										.getMoney();
								/**
								 * 设置图标颜色
								 */
								if (parkType != 4) {

									makerOptions.icon(BitmapDescriptorFactory
											.fromBitmap(showParkIcon(str, list
													.get(i).getState())));

								} else {
									// 是否有月卡？ 0不是月卡： 1是月卡
									boolean isMonth = list.get(i).getIsmonth() == 0 ? false
											: true;
									makerOptions.icon(BitmapDescriptorFactory
											.fromBitmap(getBitmapIcon(str,
													isMonth, true)));
								}

								/**
								 * 设置标记的位置（经纬度 目前Y为纬度 X为经度）
								 */
								makerOptions.position(new LatLng(Double
										.valueOf(list.get(i).getLatitude()),
										Double.valueOf(list.get(i)
												.getLongitude())));
								Marker addMarker = mAMap
										.addMarker(makerOptions);
								addMarker.setObject(list.get(i));

								mMarkars.put(list.get(i).getName(), addMarker);
							}

						}

						// searchPBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
						// searchPBar.setVisibility(View.INVISIBLE);
					}
				});

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
	public void parseLoadInOut(String parkId) {

		// searchPBar.setVisibility(View.VISIBLE);
		LogUtil.e("出入口方法");
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("parkid", parkId);

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

							/**
							 * 设置标记的位置（经纬度 目前Y为纬度 X为经度）
							 */
							makerOptions.position(new LatLng(Double
									.valueOf(list.get(i).getLatitude()), Double
									.valueOf(list.get(i).getLongitude())));

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

}
