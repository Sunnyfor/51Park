package cn.com.unispark.fragment.treasure.lease;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.treasure.lease.adapter.ChildAdapter;
import cn.com.unispark.fragment.treasure.lease.adapter.GroupAdapter;
import cn.com.unispark.fragment.treasure.lease.adapter.LeaseCarAdapter;
import cn.com.unispark.fragment.treasure.lease.entity.CityEntity;
import cn.com.unispark.fragment.treasure.lease.entity.CityEntity.DataObject.CityItem;
import cn.com.unispark.fragment.treasure.lease.entity.DistrictEntity;
import cn.com.unispark.fragment.treasure.lease.entity.DistrictEntity.DataObject.DistrictItem;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseCarEntity;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseCarEntity.DataObject.LeaseCarInfo;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【车位租赁】界面
 * 日期：	2015年9月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月22日
 * </pre>
 */
public class LeaseCarActivity extends BaseActivity {


	
	private ListView lstv;
	private TextView data_null_tv;
	private LeaseCarAdapter adapter;
	private List<LeaseCarInfo> list;

	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 城市选择和类型选择
	private View city_view, type_view;
	private CheckBox city_cb, type_cb;
	private PopupWindow city_popwin;
	private PopupWindow type_popwin;
	private LinearLayout city_ll, type_ll;

	/*
	 * 类型选择：全部/包月/计次
	 */
	private TextView pop_all_tv, pop_month_tv, pop_meter_tv;

	// 数据源
	private List<CityItem> CityList = new ArrayList<CityItem>();
	private List<DistrictItem> DistrictList = new ArrayList<DistrictItem>();

	// 集合//适配器
	private ListView groupLstv;
	private ListView childLstv;
	private GroupAdapter groupAdapter;
	private ChildAdapter childAdapter;

	// 租赁类型
	private int type = 0;

	// 城市//区域(动态替换城市选择的文字)
	private String city;
	protected String district;
	private String cityid = "";
	private String districid = "";

	@Override
	public void setContentLayout() {
		setContentView(R.layout.lease_car_main);

	}

	@Override
	public void initView() {
		// 导航栏标题“车位租赁”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.che_wei_zu_lin));

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 城市选择
		city_cb = (CheckBox) findViewById(R.id.lease_city_cb);
		city_cb.setOnClickListener(this);

		// 类型选择
		type_cb = (CheckBox) findViewById(R.id.lease_type_cb);
		type_cb.setOnClickListener(this);

		/*
		 * 城市选择PopupWindow
		 */
		city_view = View.inflate(context, R.layout.lease_car_city_popwin, null);
		city_view.setId(R.id.test1_tv);
		city_view.setOnClickListener(this);

		city_ll = (LinearLayout) city_view.findViewById(R.id.lease_city_ll);
		ViewUtil.setViewSize(city_ll, ViewUtil.HEIGHT / 2 - 168, 0);

		groupLstv = (ListView) city_view.findViewById(R.id.listview);
		ViewUtil.setViewSize(groupLstv, 0, ViewUtil.WEIGHT / 2);

		childLstv = (ListView) city_view.findViewById(R.id.listview0);
		ViewUtil.setViewSize(childLstv, 0, ViewUtil.WEIGHT / 2);

		/*
		 * 类型选择PopupWindow
		 */
		type_view = View.inflate(context, R.layout.lease_car_type_popwin, null);
		type_view.setId(R.id.test1_tv);
		type_view.setOnClickListener(this);

		// 设置屏幕的一半
		type_ll = (LinearLayout) type_view.findViewById(R.id.lease_type_ll);
		ViewUtil.setViewSize(type_ll, 0, ViewUtil.WEIGHT / 2);

		// 类型选择：全部
		pop_all_tv = (TextView) type_view.findViewById(R.id.pop_all_tv);
		pop_all_tv.setOnClickListener(this);
		ViewUtil.setTextSize(pop_all_tv, 28);
		ViewUtil.setViewSize(pop_all_tv, 80, 0);

		// 包月
		pop_month_tv = (TextView) type_view.findViewById(R.id.pop_month_tv);
		pop_month_tv.setOnClickListener(this);
		ViewUtil.setTextSize(pop_month_tv, 28);
		ViewUtil.setViewSize(pop_month_tv, 80, 0);

		// 计次
		pop_meter_tv = (TextView) type_view.findViewById(R.id.pop_meter_tv);
		pop_meter_tv.setOnClickListener(this);
		ViewUtil.setTextSize(pop_meter_tv, 28);
		ViewUtil.setViewSize(pop_meter_tv, 80, 0);

		// 数据为空时显示
		data_null_tv = (TextView) findViewById(R.id.data_null_tv);
		data_null_tv.setText("暂时没有车位租赁消息");

		// ListView
		lstv = (ListView) findViewById(R.id.listview);
		lstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (1 == list.get(position).getIssoldout()) {
					// 判断是否可以购买或者续费？0是不可以1是可以
					int isCanReBuy = list.get(position).getIscanrebuy();
					switch (isCanReBuy) {
					case 0:
						ToastUtil.show(list.get(position).getReason());
						break;
					case 1:
						ParkApplication.mParkId = list.get(position)
								.getParkid();
						ParkApplication.mLeaseType = list.get(position)
								.getDefaluttype();
						ToolUtil.IntentClass(activity,
								LeaseDetailActivity.class, false);
						break;
					}
				} else {
					ParkApplication.mParkId = list.get(position)
							.getParkid();
					ParkApplication.mLeaseType = list.get(position)
							.getDefaluttype();
					ToolUtil.IntentClass(activity,
							LeaseDetailActivity.class, false);
//					ToastUtil.show(list.get(position).getReason());
				}
			}
		});

		parseGetCityGroup();

		parseCarLeaseList(type, cityid, districid);
	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.lease_city_cb:
			showCityPopWin(v);
			MobclickAgent.onEvent(context, "lease_cityDropBtn_click ");
			break;
		case R.id.lease_type_cb:
			showTypePopWin(v);
			MobclickAgent.onEvent(context, "lease_typeDrop_click");
			break;
		case R.id.pop_all_tv:
			type = 0;
			parseCarLeaseList(0, cityid, districid);
			type_popwin.dismiss();
			type_cb.setText(getResources().getString(R.string.quan_bu));
			MobclickAgent.onEvent(context, "lease_choiceType_click");
			break;
		case R.id.pop_month_tv:
			type = 1;
			parseCarLeaseList(1, cityid, districid);
			type_popwin.dismiss();
			type_cb.setText(getResources().getString(R.string.bao_yue));
			MobclickAgent.onEvent(context, "lease_choiceType_click");
			break;
		case R.id.pop_meter_tv:
			type = 2;
			parseCarLeaseList(2, cityid, districid);
			type_popwin.dismiss();
			type_cb.setText(getResources().getString(R.string.ji_ci));
			MobclickAgent.onEvent(context, "lease_choiceType_click");
			break;
		case R.id.test1_tv:// 城区选择布局
			city_popwin.dismiss();
			break;
		case R.id.test2_tv:// 类型选择布局
			type_popwin.dismiss();
			type_popwin =null;
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// parseCarLeaseList(0,"");
	}

	/**
	 * <pre>
	 * 功能说明：展示停车场区域选择的弹框
	 * 日期：	2015年11月11日
	 * 开发者：	任建飞
	 * 
	 * </pre>
	 */
	private void showCityPopWin(View v) {

		if (city_popwin == null) {

			city_popwin = new PopupWindow(city_view, getScreenWidth(),
					getScreenHeight());

			city_popwin.setOutsideTouchable(true);
			city_popwin.update();
			city_popwin.setTouchable(true);
			city_popwin.setBackgroundDrawable(getResources().getDrawable(
					R.color.transparent));

//			city_popwin.setFocusable(true);
//			city_popwin.setFocusable(true);

			city_view.setFocusableInTouchMode(true);

			groupAdapter = new GroupAdapter(this, CityList);
			groupLstv.setAdapter(groupAdapter);

			// 默认进入后加载第一个城市所有的区域
			parseGetDistrict(CityList.get(0).getCity_id());
			city = CityList.get(0).getName();

			groupLstv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					groupAdapter.setSelectedPosition(position);
					groupAdapter.notifyDataSetChanged();
					cityid = CityList.get(position).getCity_id();
					parseGetDistrict(cityid);

					city = CityList.get(position).getName();
					MobclickAgent.onEvent(context, "lease_choiceCity_click");
				}
			});

			childLstv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					districid = DistrictList.get(position).getDistrictid();

					LogUtil.e("【列表ID】" + position + "【区域ID】" + districid);
					if (position != 0) {
						parseCarLeaseList(type, cityid, districid);
					} else {
						parseCarLeaseList(type, cityid, null);
					}

					district = DistrictList.get(position).getName();

					// 最多显示7位，超过的用省略号表示
					String citydist = city + "-" + district;
					if (citydist.length() > 7) {
						city_cb.setText(citydist.substring(0, 7) + "..");
					} else {
						city_cb.setText(citydist);
					}

					city_popwin.dismiss();
				}
			});

			if (childAdapter == null) {
				childLstv.setVisibility(View.VISIBLE);
				childAdapter = new ChildAdapter(context, DistrictList);
				childLstv.setAdapter(childAdapter);
			}

			city_popwin.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					city_cb.setChecked(false);
				}
			});

		}

		if (!city_popwin.isShowing()) {
			   if (android.os.Build.VERSION.SDK_INT >=24) {
                   int[] a = new int[2];
                   v.getLocationInWindow(a);
                   city_popwin.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0 , a[1]+v.getHeight());
               } else{
            	   city_popwin.showAsDropDown(v);
               }
//			city_popwin.showAsDropDown(v);
		} else {
			city_popwin.dismiss();
		}

	}

	/**
	 * <pre>
	 * 功能说明：展示租赁停车场类型选择的弹框
	 * 日期：	2015年10月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void showTypePopWin(View v) {

		if (type_popwin == null) {
			type_popwin = new PopupWindow(type_view, getScreenWidth(),

					getScreenHeight());
			
			// 点击popupWindow之外的地方失去焦点
			type_popwin.setOutsideTouchable(true);
			type_popwin.update();
			type_popwin.setTouchable(true);
			// 替换背景颜色
			type_popwin.setBackgroundDrawable(getResources().getDrawable(
					R.color.transparent));
			
			
			// 获取焦点
//			type_popwin.setFocusable(true);
//			type_view.setFocusableInTouchMode(true);
			

//			type_popwin.setFocusable(true);

			// 点击popupWindow之外的地方失去焦点
			type_popwin.setOutsideTouchable(true);
		}

		if (!type_popwin.isShowing()) {	
			   if (android.os.Build.VERSION.SDK_INT >=24) {
                   int[] a = new int[2];
                   v.getLocationInWindow(a);
                   type_popwin.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0 , a[1]+v.getHeight());
               } else{
            	   type_popwin.showAsDropDown(v);
               }
			   
//			type_popwin.showAsDropDown(v);
		} else {
			type_popwin.dismiss();
			type_popwin = null;
		}

		type_popwin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				type_cb.setChecked(false);
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
	private void parseCarLeaseList(int type, String cityid, String districtid) {

		if (!loadingProgress.isShowing()) {
			loadingProgress.show();
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("type", String.valueOf(type));
		if (cityid != null) {
			params.put("city_id", cityid);
		}
		if (districtid != null) {
			params.put("districtid", districtid);// 地区自增id
		}

		LogUtil.showLog(2, "【车位租赁列表 URL】" + Constant.PARKCARD_LIST_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.PARKCARD_LIST_URL,
				LeaseCarEntity.class, params, new onResult<LeaseCarEntity>() {
					@Override
					public void onSuccess(LeaseCarEntity result) {

						if (loadingProgress.isShowing()) {
							loadingProgress.hide();
						}

						list = result.getData().getDatalist();
					
						if (list.size() != 0) {
							lstv.setVisibility(View.VISIBLE);
							data_null_tv.setVisibility(View.GONE);

							adapter = new LeaseCarAdapter(context, list);
							lstv.setAdapter(adapter);
						} else {
							lstv.setVisibility(View.GONE);
							data_null_tv.setVisibility(View.VISIBLE);
						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {

						if (loadingProgress.isShowing()) {
							loadingProgress.hide();
						}

						ToastUtil.show(errMsg);
					}
				});

	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取城市列表
	 * 日期：	2015年11月19日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetCityGroup() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.showLog(3, "【获取城市列表 URL】" + Constant.GET_CITY_LIST_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.GET_CITY_LIST_URL,
				CityEntity.class, params, new onResult<CityEntity>() {

					@Override
					public void onSuccess(CityEntity result) {
						if (CityList.size() > 0) {
							CityList.clear();
						}
						for (CityItem mCityItem : result.getData().getList()) {
							CityList.add(mCityItem);
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.show(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取城市相对应的区域列表
	 * 日期：	2015年11月19日
	 * 开发者：	陈丶泳佐
	 * 
	 */
	private void parseGetDistrict(String cityID) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("city_id", cityID);// 城市列表自增id

		LogUtil.showLog(3, "【获取城市相对应的区域列表 URL】" + Constant.GET_DISTRICT_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.GET_DISTRICT_URL,
				DistrictEntity.class, params, new onResult<DistrictEntity>() {
					@Override
					public void onSuccess(DistrictEntity result) {
						if (DistrictList.size() > 0) {
							DistrictList.clear();
						}
						for (DistrictItem mDistrictItem : result.getData()
								.getList()) {
							DistrictList.add(mDistrictItem);
						}
						childAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.show(errMsg);
					}
				});
	}

}
