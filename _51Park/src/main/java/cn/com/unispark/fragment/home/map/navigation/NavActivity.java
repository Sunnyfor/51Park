package cn.com.unispark.fragment.home.map.navigation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.entity.InOutEntity;
import cn.com.unispark.util.ReckonUtil;

import com.amap.api.maps.MapView;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明：【驾车方案】界面[高德语音导航]
 * 日期：	2015年6月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月25日
 * </pre>
 */
public class NavActivity extends BaseActivity implements AMapNaviListener,
		AMapNaviViewListener {

	// 到达目的地的时间和距离
	private TextView time_distance_tv;

	// 开始导航
	private RelativeLayout nav_rl;

	// 起点终点坐标
	private NaviLatLng mNaviStart;
	private NaviLatLng mNaviEnd;

	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

	// 规划线路
	private RouteOverLay mRouteOverLay;
	private com.amap.api.maps.AMap mAMap;
	public static List<AMapNaviGuide> naviGuideList;

	// 地图展示视图
	private MapView mapView;
	private AMapNavi mAMapNavi;
	// 语音导航视图
	private AMapNaviView naviview;
	private NavVoiceTTSController ttsManager;

	// 保存出入口坐标的集合
	private List<InOutEntity> inOutList;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.nav_voice_main);

		inOutList = new ArrayList<InOutEntity>();
		inOutList.add(new InOutEntity(116.325125, 39.966937));
		inOutList.add(new InOutEntity(116.322851, 39.966863));
		inOutList.add(new InOutEntity(116.322035, 39.965445));

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mapView = (MapView) findViewById(R.id.mapview);

		mapView.onCreate(savedInstanceState);

		mAMap = mapView.getMap();

		naviview = (AMapNaviView) findViewById(R.id.amapnaviview);
		naviview.onCreate(savedInstanceState);
		naviview.setAMapNaviViewListener(this);

		mRouteOverLay = new RouteOverLay(mAMap, null);

	}

	@Override
	public void initView() {

		// 到达目的地的时间和距离
		time_distance_tv = (TextView) findViewById(R.id.time_distance_tv);

		// 开始导航
		nav_rl = (RelativeLayout) findViewById(R.id.nav_rl);
		nav_rl.setOnClickListener(this);

		// 地图
		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);

		mNaviStart = new NaviLatLng(
				ParkApplication.mCurrentPosition.target.latitude,
				ParkApplication.mCurrentPosition.target.longitude);

		mNaviEnd = new NaviLatLng(ParkApplication.mLatEnd,
				ParkApplication.mLonEnd);

		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);

		// 修改
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (!isSuccess) {
			showToast("路线计算失败,检查参数情况");
		}

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		/*
		 * case R.id.moreLLayout: ToolUtil.IntentClass(activity,
		 * NavDetailActivity.class, false); MobclickAgent.onEvent(context,
		 * "driveCase_detailsBtn_click"); break;
		 */
		case R.id.nav_rl:
			// 开启导航
			naviview.setVisibility(View.VISIBLE);
			mapView.setVisibility(View.GONE);
			mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
			ttsManager.startSpeaking();
			MobclickAgent.onEvent(context, "driveCase_IVRbtn_click");
			break;
		}
	}

	// ------------------生命周期重写函数---------------------------
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		// 删除监听
		AMapNavi.getInstance(this).removeAMapNaviListener(this);

		// 销毁导航
		AMapNavi.getInstance(this).destroy();
		NavVoiceTTSController.getInstance(this).stopSpeaking();
		NavVoiceTTSController.getInstance(this).destroy();

		if (naviGuideList != null) {
			naviGuideList.clear();
			naviGuideList = null;
		}

	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		showToast("路径规划出错" + arg0);
	}

	@Override
	public void onCalculateRouteSuccess() {

		// ==================================================================
		// AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		// for (int i = 0; i < inOutList.size(); i++) {
		// System.out.println("距离：");
		// mNaviEnd = new NaviLatLng(inOutList.get(i).getLatitude(), inOutList
		// .get(i).getLongitude());
		// mEndPoints.add(mNaviEnd);
		//
		// boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
		// mEndPoints, null, AMapNavi.DrivingDefault);
		//
		//
		// int a = naviPath.getAllLength();
		// System.out.println("距离：" + a);
		// }
		//
		// // int in[] = new int[] { 1, 2, 3 };

		// ==================================================================

		// 修改
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();

		if (naviPath == null) {
			return;
		}

		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);

		// 添加驾车/步行路线添加到地图上显示。
		mRouteOverLay.addToMap();

		// 移动镜头到当前的视角。
		mRouteOverLay.zoomToSpan();

		naviGuideList = mAMapNavi.getNaviGuideList();

		time_distance_tv.setText(naviPath.getAllTime() / 60 + "分钟 \t( "
				+ naviPath.getAllLength() + ")");

	}

	@Override
	public void onInitNaviFailure() {
		naviview.setVisibility(View.GONE);
		mapView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onNaviCancel() {
		naviview.setVisibility(View.GONE);
		mapView.setVisibility(View.VISIBLE);
		ttsManager.stopSpeaking();
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLockMap(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviSetting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub

	}

}
