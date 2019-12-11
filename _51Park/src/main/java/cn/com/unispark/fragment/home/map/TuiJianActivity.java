package cn.com.unispark.fragment.home.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.CycleViewPager;
import cn.com.unispark.fragment.home.map.adapter.TuiJianAdapter;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity.DataObject.ParkItemInfo;
import cn.com.unispark.fragment.home.map.entity.SearchItemEntity;
import cn.com.unispark.fragment.home.map.entity.SearchItemEntity.SearchData.SearchDataChild;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

/**
 * <pre>
 * 功能说明： 智能推荐停车场
 * 日期：	2016年1月12日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年1月12日
 * </pre>
 */
public class TuiJianActivity extends BaseActivity implements OnMapClickListener {

	// 标题栏以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 加载地图按钮
	private ProgressBar searchPBar;
	// 路况图标显示与隐藏
	private CheckBox lukuangCbox;

	// 停车场类型
	private int parkType = 1;

	// 智能推荐停车场
	private CycleViewPager viewpager;
	/*
	 * 高德地图需要的属性
	 */
	public static AMap mAMap;
	private MapView mMapView;
	private UiSettings mUiSettings;

	private List<SearchDataChild> parklist;

	// Marker坐标
	private Map<String, Marker> mMarkars = new HashMap<String, Marker>();

	// 出入口的Marker
	private List<Marker> mInOutMarkars = new ArrayList<Marker>();

	// // 存放起始位置点和结束位置点的集合
	// private double mLat;
	// private double mLon;
	// private LatLng satrtLatLng;

	private Marker oldMark;

	/**
	 * 智能推荐停车场
	 */
	private TuiJianAdapter tuiJianAdapter;

	// private MyLocationStyle myLocationStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMap(savedInstanceState);
	}

	@Override
	public void setContentLayout() {
		setContentView(R.layout.tuijian_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("推荐停车场");

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 加载地图按钮
		searchPBar = (ProgressBar) findViewById(R.id.search_pb);

		// 路况图标显示与隐藏
		lukuangCbox = (CheckBox) findViewById(R.id.lukuang_cb);
		lukuangCbox.setOnClickListener(this);

		// 展示地图
		mMapView = (MapView) findViewById(R.id.mapview);

		// 智能推荐停车场
		viewpager = (CycleViewPager) findViewById(R.id.viewpager);

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
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
		}

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

	public void setparklist(List<SearchDataChild> parklist) {
		this.parklist = parklist;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (!viewpager.isShown()) {
			viewpager.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		// showLocationIcon();
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
		httpUtil.close();
		mMarkars.clear();
		mAMap.clear();
		mMapView.onDestroy();
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
		mUiSettings = mAMap.getUiSettings();
		// 设置默认缩放度
		mAMap.moveCamera(CameraUpdateFactory
				.zoomTo(ParkApplication.mMapZoomLevel));
		// 设置是否允许显示缩放按钮,默认缩放按钮为显示
		mUiSettings.setZoomControlsEnabled(false);
		// 设置定位按钮是否显示, 默认定位按钮为显示
		// 开启图层定位
		mAMap.setOnMapClickListener(this);

		// 自定义定位蓝点图标
		// showLocationIcon();

		/**
		 * 加载停车场图标
		 */
		mAMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChangeFinish(CameraPosition arg0) {

				LatLng target = arg0.target;
				parseLoadPark(parkType, target);
			}

			@Override
			public void onCameraChange(CameraPosition arg0) {

			}
		});

		/**
		 * 当一个marker 对象被点击时调用此方法
		 */
		mAMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				if (oldMark != null) {

					ParkItemInfo oldItem = (ParkItemInfo) oldMark.getObject();
					String money = oldItem.getMoney();

					if (oldItem != null) {

						oldMark.setIcon(BitmapDescriptorFactory
								.fromBitmap(showParkIcon(money,
										oldItem.getState())));

					}
				}

				ParkItemInfo item = (ParkItemInfo) marker.getObject();
				String money = item.getMoney();

				marker.setIcon(BitmapDescriptorFactory
						.fromBitmap(showParkIcon2x(money, item.getState())));

				oldMark = marker;

				return false;
			}
		});

		try {
			parklist = ((SearchItemEntity) getIntent().getSerializableExtra(
					"searchItem")).getData().getList();
			LatLng latLng = getIntent().getParcelableExtra("latlng");

//			ToastUtil.show(latLng.latitude + ":" + latLng.longitude);

			// 移动地图
			mAMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(latLng,
							ParkApplication.mMapZoomLevel, 0f, 0f)));

			MarkerOptions marker = new MarkerOptions();
			/*
			 * 设置中心点
			 */
//			marker.icon(BitmapDescriptorFactory
//					.fromResource(R.drawable.icon_marker));
			marker.icon(BitmapDescriptorFactory
					.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icon_marker)));
			
			marker.position(latLng);

			mAMap.addMarker(marker);

			if (parklist != null) {
				showParkViewPager();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * <pre>
	 * 功能说明：初始化智能推荐停车场
	 * 日期：	2015年6月16日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	public void showParkViewPager() {

		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int p) {
				int page = parklist.size();
				int position = p - 1;

				if (position == -1) {
					position = parklist.size() - 1;
				}

				if (position == page) {
					position = 0;
				}
				Marker marker = mMarkars.get(parklist.get(position).getName());
				
				if (oldMark != null) {

					ParkItemInfo oldItem = (ParkItemInfo) oldMark.getObject();
					String money = oldItem.getMoney();

					if (oldItem != null) {

						oldMark.setIcon(BitmapDescriptorFactory
								.fromBitmap(showParkIcon(money,
										oldItem.getState())));

					}
				}

				ParkItemInfo item = (ParkItemInfo) marker.getObject();
				String money = item.getMoney();

				marker.setIcon(BitmapDescriptorFactory
						.fromBitmap(showParkIcon2x(money, item.getState())));

				oldMark = marker;
				
				
				// 移动地图
				mAMap.moveCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(marker.getPosition(),
								ParkApplication.mMapZoomLevel, 0f, 0f)));

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		viewpager.setPageMargin((int) context.getResources()
				.getDimensionPixelOffset(R.dimen.ui_f5_dip));

		tuiJianAdapter = new TuiJianAdapter(context, activity, parklist);
		viewpager.setAdapter(tuiJianAdapter);

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
			icon = R.drawable.park_green;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1002.jpg")) {// 普通绿
			icon = R.drawable.park_green;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1003.jpg")) {// 普通黄
			icon = R.drawable.park_yellow;
			clolor = R.color.orange_font;
		} else if (state.equalsIgnoreCase("P1004.jpg")) {// 普通红
			icon = R.drawable.park_red;
			clolor = R.color.red_font;
		} else if (state.equalsIgnoreCase("P1005.jpg")) {// 普通灰
			icon = R.drawable.park_gray;
			clolor = R.color.gray_font;

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

		return showBitmapIcon(money, icon, clolor, 18, isWuyou);
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
			icon = R.drawable.park_green2x;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1002.jpg")) {// 普通绿
			icon = R.drawable.park_green2x;
			clolor = R.color.green_font;
		} else if (state.equalsIgnoreCase("P1003.jpg")) {// 普通黄
			icon = R.drawable.park_yellow2x;
			clolor = R.color.orange_font;
		} else if (state.equalsIgnoreCase("P1004.jpg")) {// 普通红
			icon = R.drawable.park_red2x;
			clolor = R.color.red_font;
		} else if (state.equalsIgnoreCase("P1005.jpg")) {// 普通灰
			icon = R.drawable.park_gray2x;
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

		return showBitmapIcon(money, icon, clolor, 22, isJoin);
	}

	/**
	 * <pre>
	 * 功能说明：全部/免费/无忧 自定义覆盖物样式
	 * 日期：	2015年11月17日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param moeny 覆盖物上的文字
	 * @param res 覆盖物图标
	 * @return
	 * </pre>
	 */
	public Bitmap showBitmapIcon(String moeny, int res, int textColor,
			int textSize, boolean isJoin) {

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res).copy(
				Bitmap.Config.ARGB_8888, true);

		Canvas canvas = new Canvas(bitmap);
		TextPaint text = new TextPaint(Paint.FAKE_BOLD_TEXT_FLAG);
		text.setTextSize(ViewUtil.getWidth(textSize));
		if (textColor != 0) {
			text.setColor(getResources().getColor(textColor));
		}
		int x = (bitmap.getWidth() - ViewUtil.getWidth(12) * moeny.length()) / 2;
		int y = (int) (bitmap.getHeight() / 3 * 1.4);

		if (isJoin) {
			y = (int) (bitmap.getHeight() / 3 * 0.9);
		}

		canvas.drawText(moeny, x, y, text);// 设置bitmap上面的文字位置
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
		// showLocationIcon();
	}

	// /**
	// * <pre>
	// * 功能说明：自定义定位蓝点图标
	// * 日期： 2015年12月3日
	// * 开发者： 陈丶泳佐
	// *
	// * </pre>
	// */
	// public void showLocationIcon() {
	// myLocationStyle = new MyLocationStyle();
	// myLocationStyle.myLocationIcon(BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_location_map));
	//
	// myLocationStyle.radiusFillColor(Color.parseColor("#220000ff"));
	// mAMap.setMyLocationStyle(myLocationStyle);
	// }

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

		searchPBar.setVisibility(View.VISIBLE);

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("type", Integer.toString(parkType));
		// params.put("distance", "5000");
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

						for (int i = 0; i < size; i++) {

							if (mMarkars.get(list.get(i).getName()) == null) {

								/**
								 * 设置停车场标记
								 */
								MarkerOptions makerOptions = new MarkerOptions();

								String money = list.get(i).getMoney();

								makerOptions.icon(BitmapDescriptorFactory
										.fromBitmap(showParkIcon(money, list
												.get(i).getState())));

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

						searchPBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
						searchPBar.setVisibility(View.INVISIBLE);
					}
				});

	}

}
