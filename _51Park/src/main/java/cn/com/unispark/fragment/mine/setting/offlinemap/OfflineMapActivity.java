//package cn.com.unispark.fragment.mine.setting.offlinemap;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.os.Handler.Callback;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import cn.com.unispark.R;
//import cn.com.unispark.application.BaseActivity;
//import cn.com.unispark.application.Constant;
//import cn.com.unispark.application.ParkApplication;
//import cn.com.unispark.fragment.mine.setting.offlinemap.CityListEntity.DataObject.CityGroupInfo;
//import cn.com.unispark.fragment.mine.setting.offlinemap.CityListEntity.DataObject.CityGroupInfo.CityInfo;
//import cn.com.unispark.fragment.mine.setting.offlinemap.view.PinnedHeaderListView;
//import cn.com.unispark.fragment.mine.setting.offlinemap.view.sort.SideBar;
//import cn.com.unispark.fragment.mine.setting.offlinemap.view.sort.SideBar.OnTouchingLetterChangedListener;
//import cn.com.unispark.util.HttpUtil.onResult;
//import cn.com.unispark.util.LogUtil;
//import cn.com.unispark.util.ToastUtil;
//import cn.com.unispark.util.ViewUtil;
//
//import com.amap.api.maps.AMapException;
//import com.amap.api.maps.offlinemap.OfflineMapCity;
//import com.amap.api.maps.offlinemap.OfflineMapManager;
//import com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
//import com.amap.api.maps.offlinemap.OfflineMapProvince;
//import com.amap.api.maps.offlinemap.OfflineMapStatus;
//import com.umeng.analytics.MobclickAgent;
//
///**
// * <pre>
// * 功能说明： 【离线地图】界面
// * 日期：	2014年12月5日
// * 开发者：	任建飞
// * 版本信息：V5.1.0
// * 版权声明：版权所有@北京百会易泊科技有限公司
// * 
// * 历史记录
// *    修改内容：
// *    修改人员：
// *    修改日期： 2015年12月10日
// * </pre>
// */
//public class OfflineMapActivity extends BaseActivity implements
//		OfflineMapDownloadListener {
//
//	// 导航栏标题以及返回按钮
//	private TextView titleText;
//	private LinearLayout backLLayout;
//
//	private PinnedHeaderListView lstv;
//	private OfflineMapAdapter customAdapter;
//	public SharedPreferences settings;
//	private List<CityGroupInfo> mCityGroupInfo = new ArrayList<CityGroupInfo>();
//	List<CityMapItemEntity> data = null;
//	private SideBar sideBar;
//	private TextView dialog;
//	// private MapView mapView;
//	private OfflineMapManager mOfflineMapManager = null;// 离线地图下载控制器
//	private ArrayList<OfflineMapCity> mOfflineCityList = new ArrayList<OfflineMapCity>();
//	private final static int UPDATE_LIST = 0;
//	private final static int DISMISS_INIT_DIALOG = 1;
//	private final static int SHOW_INIT_DIALOG = 2;
//	private List<OfflineMapProvince> provinceList = new ArrayList<OfflineMapProvince>();
//	private OfflineMapCity mMapCity;
//	private Handler handler = new Handler(new Callback() {
//
//		@Override
//		public boolean handleMessage(Message msg) {
//			switch (msg.what) {
//			case UPDATE_LIST:
//				if (customAdapter != null) {
//					customAdapter.notifyDataSetChanged();
//				}
//				break;
//			case DISMISS_INIT_DIALOG:
//				loadingProgress.dismiss();
//				handler.sendEmptyMessage(UPDATE_LIST);
//				break;
//			// case SHOW_INIT_DIALOG:
//			// if (initDialog != null) {
//			// initDialog.show();
//			// }
//			// break;
//			}
//
//			return false;
//		}
//	});
//
//	@Override
//	public void setContentLayout() {
//		setContentView(R.layout.offline_map_main);
//	}
//
//	@Override
//	public void initView() {
//		mOfflineMapManager = new OfflineMapManager(this, this);
//		titleText = (TextView) findViewById(R.id.titleText);
//		titleText.setText("离线地图");
//		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
//		backLLayout.setOnClickListener(this);
//		lstv = (PinnedHeaderListView) findViewById(R.id.citylist);
//		lstv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				try {
//					String name = data.get(arg2).getmOfflineMapCity().getCity();
//					// String url = mOfflineMapManager.getItemByCityName(name)
//					// .getUrl();
//					mOfflineMapManager.downloadByCityName(name);
//					MobclickAgent.onEvent(context,
//							"offlinemap_downloadBtn_click");
//				} catch (AMapException e) {
//					LogUtil.e("离线地图下载抛出异常" + e.getErrorMessage());
//				}
//				handler.sendEmptyMessage(UPDATE_LIST);
//			}
//		});
//		sideBar = (SideBar) findViewById(R.id.sidrbar);
//		sideBar.setVisibility(View.GONE);
//		dialog = (TextView) findViewById(R.id.dialog);
//		sideBar.setTextView(dialog);
//		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
//			@Override
//			public void onTouchingLetterChanged(String s) {
//				int position = customAdapter.getPositionForSection(s.charAt(0));
//				if (position != -1) {
//					lstv.setSelection(position);
//				}
//			}
//		});
//		initDialog();
//	}
//
//	/**
//	 * 初始化如果已下载的城市多的话，会比较耗时
//	 */
//	private void initDialog() {
//		loadingProgress.show();
//		handler.sendEmptyMessage(SHOW_INIT_DIALOG);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				final Handler handler1 = new Handler();
//				handler1.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						init();
//						handler.sendEmptyMessage(DISMISS_INIT_DIALOG);
//						handler.removeCallbacks(this);
//						Looper.myLooper().quit();
//					}
//
//				}, 10);
//
//				Looper.loop();
//
//			}
//		}).start();
//	}
//
//	private void init() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
//		provinceList = mOfflineMapManager.getOfflineMapProvinceList();
//		for (int i = 0; i < provinceList.size(); i++) {
//			OfflineMapProvince offlineMapProvince = provinceList.get(i);
//			for (OfflineMapCity offlineMapCity : offlineMapProvince
//					.getCityList()) {
//				mOfflineCityList.add(offlineMapCity);
//			}
//		}
//		ParseGetCityList();
//	}
//
//	/**
//	 * 把一个省的对象转化为一个市的对象
//	 */
//	public OfflineMapCity getCicy(OfflineMapProvince aMapProvince) {
//		OfflineMapCity aMapCity = new OfflineMapCity();
//		aMapCity.setCity(aMapProvince.getProvinceName());
//		aMapCity.setSize(aMapProvince.getSize());
//		aMapCity.setCompleteCode(aMapProvince.getcompleteCode());
//		aMapCity.setState(aMapProvince.getState());
//		aMapCity.setUrl(aMapProvince.getUrl());
//		return aMapCity;
//	}
//
//	@Override
//	public void onClickEvent(View view) {
//		switch (view.getId()) {
//		case R.id.backLLayout:
//			finish();
//			break;
//		}
//
//	};
//
//	private List<CityMapItemEntity> createTestData() {
//		List<CityMapItemEntity> data = new ArrayList<CityMapItemEntity>();
//		for (int i = 0; i < mCityGroupInfo.size(); i++) {
//			for (int j = 0; j < mCityGroupInfo.get(i).getCity().size(); j++) {
//				CityMapItemEntity itemEntity = new CityMapItemEntity(
//						mCityGroupInfo.get(i).getGroup(), mCityGroupInfo.get(i)
//								.getCity().get(j));
//				data.add(itemEntity);
//			}
//		}
//		for (int i = 0; i < mOfflineCityList.size(); i++) {
//			mMapCity = mOfflineMapManager.getItemByCityName(mOfflineCityList
//					.get(i).getCity());
//			Log.e("slx", "mMapCity" + mMapCity.getCity());
//			for (int j = 0; j < data.size(); j++) {
//				if ((mOfflineCityList.get(i).getCity()).equals(data.get(j)
//						.getmCityInfo().getName().toString())) {
//					data.get(j).setmOfflineMapCity(mOfflineCityList.get(i));
//					Log.e("slx", "mOfflineCityList.get(i)"
//							+ mOfflineCityList.get(i).getCity());
//				}
//			}
//		}
//		return data;
//	}
//
//	/**
//	 * <pre>
//	 * 功能说明：【解析】获取城市列表
//	 * 日期：	2015年11月25日
//	 * 开发者：	陈丶泳佐
//	 * 
//	 * </pre>
//	 */
//	public void ParseGetCityList() {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("uid", ParkApplication.mUserId);
//		httpUtil.parse(httpUtil.POST, Constant.GET_CITY_URL,
//				CityListEntity.class, params, new onResult<CityListEntity>() {
//					@Override
//					public void onSuccess(CityListEntity result) {
//						mCityGroupInfo = result.getData().getList();
//						// 处理地图大小数据
//						View HeaderView = getLayoutInflater().inflate(
//								R.layout.listview_item_header, lstv, false);
//						ViewUtil.setViewSize(HeaderView, 50, 0);
//						TextView header = (TextView) HeaderView
//								.findViewById(R.id.header);
//						ViewUtil.setTextSize(header, 40);
//						ViewUtil.setPaddingLeft(header, 20);
//						lstv.setPinnedHeader(HeaderView);
//						data = createTestData();
//						customAdapter = new OfflineMapAdapter(context, data,
//								mOfflineMapManager);
//						lstv.setAdapter(customAdapter);
//						lstv.setOnScrollListener(customAdapter);
//					}
//
//					@Override
//					public void onFailed(int errCode, String errMsg) {
//						loadingProgress.hide();
//					}
//				});
//	}
//
//	public class CityMapItemEntity implements Serializable {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//		private String mTitle;
//		private CityInfo mCityInfo;
//		private OfflineMapCity mOfflineMapCity;
//
//		public OfflineMapCity getmOfflineMapCity() {
//			return mOfflineMapCity;
//		}
//
//		public void setmOfflineMapCity(OfflineMapCity mOfflineMapCity) {
//			this.mOfflineMapCity = mOfflineMapCity;
//		}
//
//		private String mCityMapSize;
//
//		public String getmCityMapSize() {
//			return mCityMapSize;
//		}
//
//		public void setmCityMapSize(String mCityMapSize) {
//			this.mCityMapSize = mCityMapSize;
//		}
//
//		public CityMapItemEntity(String pTitle, CityInfo mCityInfo) {
//			mTitle = pTitle;
//			this.mCityInfo = mCityInfo;
//		}
//
//		public String getTitle() {
//			return mTitle;
//		}
//
//		public String getmTitle() {
//			return mTitle;
//		}
//
//		public void setmTitle(String mTitle) {
//			this.mTitle = mTitle;
//		}
//
//		public CityInfo getmCityInfo() {
//			return mCityInfo;
//		}
//
//		public void setmCityInfo(CityInfo mCityInfo) {
//			this.mCityInfo = mCityInfo;
//		}
//
//	}
//
//	@Override
//	public void onDownload(int status, int completeCode, String downName) {
//
//		switch (status) {
//		case OfflineMapStatus.SUCCESS:
//			break;
//		case OfflineMapStatus.LOADING:
//			LogUtil.d("download: " + completeCode + "%" + "," + downName);
//			break;
//		case OfflineMapStatus.UNZIP:
//			LogUtil.d("unzip: " + completeCode + "%" + "," + downName);
//			break;
//		case OfflineMapStatus.WAITING:
//			break;
//		case OfflineMapStatus.PAUSE:
//			LogUtil.d("pause: " + completeCode + "%" + "," + downName);
//			break;
//		case OfflineMapStatus.STOP:
//			break;
//		case OfflineMapStatus.ERROR:
//			LogUtil.e("download: " + " ERROR " + downName);
//			break;
//		case OfflineMapStatus.EXCEPTION_AMAP:
//			LogUtil.e("download: " + " EXCEPTION_AMAP " + downName);
//			break;
//		case OfflineMapStatus.EXCEPTION_NETWORK_LOADING:
//			LogUtil.e("download: " + " EXCEPTION_NETWORK_LOADING " + downName);
//			ToastUtil.show("网络异常");
//			mOfflineMapManager.pause();
//			break;
//		case OfflineMapStatus.EXCEPTION_SDCARD:
//			LogUtil.e("download: " + " EXCEPTION_SDCARD " + downName);
//			break;
//		default:
//			break;
//		}
//		handler.sendEmptyMessage(UPDATE_LIST);
//
//	}
//
//	@Override
//	public void onCheckUpdate(boolean arg0, String arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onRemove(boolean arg0, String arg1, String arg2) {
//		// TODO Auto-generated method stub
//		handler.sendEmptyMessage(UPDATE_LIST);
//	}
//
//	// 一些可能会用到的方法
//	/**
//	 * 暂停所有下载和等待
//	 */
//	private void stopAll() {
//		if (mOfflineMapManager != null) {
//			mOfflineMapManager.stop();
//		}
//	}
//
//	/**
//	 * 继续下载所有暂停中
//	 */
//	private void startAllInPause() {
//		if (mOfflineMapManager == null) {
//			return;
//		}
//		for (OfflineMapCity mapCity : mOfflineMapManager
//				.getDownloadingCityList()) {
//			if (mapCity.getState() == OfflineMapStatus.PAUSE) {
//				try {
//					mOfflineMapManager.downloadByCityName(mapCity.getCity());
//				} catch (AMapException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	/**
//	 * 取消所有<br>
//	 * 即：删除下载列表中除了已完成的所有<br>
//	 * 会在OfflineMapDownloadListener.onRemove接口中回调是否取消（删除）成功
//	 */
//	private void cancelAll() {
//		if (mOfflineMapManager == null) {
//			return;
//		}
//		for (OfflineMapCity mapCity : mOfflineMapManager
//				.getDownloadingCityList()) {
//			if (mapCity.getState() == OfflineMapStatus.PAUSE) {
//				mOfflineMapManager.remove(mapCity.getCity());
//			}
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (mOfflineMapManager != null) {
//			mOfflineMapManager.destroy();
//		}
//	}
//}
