package cn.com.unispark.fragment.home.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.fragment.home.map.adapter.ParkListAdapter;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity;
import cn.com.unispark.fragment.home.map.entity.ParkItemEntity.DataObject.ParkItemInfo;
import cn.com.unispark.fragment.treasure.lease.adapter.LeaseCarAdapter;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseCarEntity;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseCarEntity.DataObject.LeaseCarInfo;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.maps2d.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * <pre>
 * 功能说明： 停车场【车位列表】展示
 * 日期：	2015年10月30日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月30日
 * </pre>
 */
public class ParkFragment extends Fragment {

	private PullToRefreshListView lstv;
	private TextView data_null_tv;
	private ParkListAdapter parkListAdapter;// 全部/免费//无忧
	private LeaseCarAdapter leaseCarAdapter;// 包月
	private List<ParkItemInfo> parkItemList;
	private List<LeaseCarInfo> leaseCarList;
	private double lon = ParkApplication.mLon;
	private double lan = ParkApplication.mLat;

	private int typeId = 1;

	private View view;

	private Context context;
	private HttpUtil httpUtil;
	private LoadingProgress loadingProgress;

	// 当前位置
	private TextView location_tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		view = inflater.inflate(R.layout.park_list_main, container, false);

		httpUtil = new HttpUtil(context);
		loadingProgress = new LoadingProgress(context);

		initView();
		parseLoadPark();

		return view;
	}

	public void initView() {

		// 数据为空时显示
		data_null_tv = (TextView) view.findViewById(R.id.data_null_tv);
		data_null_tv.setText("当前位置无停车场");

		/*
		 * 当前位置
		 */
		location_tv = (TextView) view.findViewById(R.id.location_tv);
		ViewUtil.setTextSize(location_tv, 24);
		ViewUtil.setViewSize(location_tv, 70, 0);
		ViewUtil.setPaddingRight(location_tv, 30);

		location_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadingProgress.show();
				new Thread(new Runnable() {

					@Override
					public void run() {

						SystemClock.sleep(2000);
						loadingProgress.dismiss();
					}
				}).start();

			}
		});

		/*
		 * 车位列表
		 */
		lstv = (PullToRefreshListView) view.findViewById(R.id.listview);
		lstv.setMode(Mode.PULL_FROM_START);
		ViewUtil.setMarginBottom(lstv, 70, ViewUtil.RELATIVELAYOUT);// 显示底部当前位置

		lstv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (typeId == 4) {
					ParkApplication.mParkId = leaseCarList.get(position - 1)
							.getParkid();
				} else {
					ParkApplication.mParkId = parkItemList.get(position - 1)
							.getParkid();
				}

				LatLng latlng = new LatLng(parkItemList.get(position - 1)
						.getLatitude(), parkItemList.get(position - 1)
						.getLongitude());

				Intent intent = new Intent(getActivity(),
						ParkInfoActivity.class);
				intent.putExtra("latlng", latlng);
				startActivity(intent);

				// ToolUtil.IntentClass(getActivity(), ParkInfoActivity.class,
				// false);

			}
		});

		lstv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (typeId == 4) {
					parseCarLeaseList();
				} else {
					parseLoadPark();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

			}
		});

	}

	/**
	 * <pre>
	 * 功能说明：【解析】加载停车场
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param id 
	 * 1--全部
	 * 2--无忧
	 * 3--免费
	 * 4--包月
	 * </pre>
	 */
	public void parseLoadPark() {

		// loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("type", Integer.toString(typeId));
		// params.put("distance", "5000");
		params.put("longitude", Double.toString(lon));
		params.put("latitude", Double.toString(lan));

		httpUtil.parse(httpUtil.POST, Constant.PARK_LIST_URL,
				ParkItemEntity.class, params,
				new HttpUtil.onResult<ParkItemEntity>() {

					@Override
					public void onSuccess(ParkItemEntity result) {
						loadingProgress.dismiss();
						lstv.onRefreshComplete();

						// list赋值展示数据
						parkItemList = result.getData().getList();
						if (parkItemList.size() != 0) {
							lstv.setVisibility(View.VISIBLE);
							data_null_tv.setVisibility(View.GONE);

							parkListAdapter = new ParkListAdapter(
									getActivity(), parkItemList);
							lstv.setAdapter(parkListAdapter);
						} else {
							data_null_tv.setVisibility(View.VISIBLE);
							lstv.setVisibility(View.GONE);

						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						lstv.onRefreshComplete();
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】车位租赁列表
	 * 日期：	2015年9月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param type	卡类型? 0全部 : 1月卡 : 2日卡  (默认为0)
	 * </pre>
	 */
	private void parseCarLeaseList() {

		if (!loadingProgress.isShowing()) {
			loadingProgress.show();
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.showLog(3, "【车位租赁列表 URL】" + Constant.PARKCARD_LIST_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.PARKCARD_LIST_URL,
				LeaseCarEntity.class, params, new onResult<LeaseCarEntity>() {
					@Override
					public void onSuccess(LeaseCarEntity result) {

						lstv.onRefreshComplete();

						if (loadingProgress.isShowing()) {
							loadingProgress.dismiss();
						}

						leaseCarList = result.getData().getDatalist();
						if (leaseCarList.size() != 0) {
							lstv.setVisibility(View.VISIBLE);
							data_null_tv.setVisibility(View.GONE);

							leaseCarAdapter = new LeaseCarAdapter(
									getActivity(), leaseCarList);
							lstv.setAdapter(leaseCarAdapter);
						} else {
							lstv.setVisibility(View.GONE);
							data_null_tv.setVisibility(View.VISIBLE);
						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {

						lstv.onRefreshComplete();

						if (loadingProgress.isShowing()) {
							loadingProgress.dismiss();
						}

						ToastUtil.showToast(errMsg);
					}
				});

	}

	/*
	 * public void setRadioGroup(RadioGroup group) {
	 * group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	 * 
	 * @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
	 * // TODO Auto-generated method stub switch (checkedId) { case
	 * R.id.all_rbtn: typeId = 1; parseLoadPark(); break; case R.id.park_rbtn:
	 * typeId = 2; parseLoadPark(); break; case R.id.free_rbtn: typeId = 3;
	 * parseLoadPark(); break; case R.id.month_rbtn: typeId = 4;
	 * parseCarLeaseList(); break; } } }); }
	 */

	public void setLatlon(double lan, double lon) {
		this.lan = lan;
		this.lon = lon;
		parseLoadPark();

	}

	public void setLocalName(String name) {
		location_tv.setText("当前位置：" + name);
	}
}
