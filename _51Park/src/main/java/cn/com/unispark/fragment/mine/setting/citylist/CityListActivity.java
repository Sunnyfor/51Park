package cn.com.unispark.fragment.mine.setting.citylist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.MainActivity;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.setting.SettingActivity;
import cn.com.unispark.fragment.mine.setting.citylist.CityListEntity.DataObject.CityGroupInfo;
import cn.com.unispark.fragment.mine.setting.citylist.CityListEntity.DataObject.CityGroupInfo.CityInfo;
import cn.com.unispark.fragment.mine.setting.citylist.view.PinnedHeaderListView;
import cn.com.unispark.fragment.mine.setting.citylist.view.sort.SideBar;
import cn.com.unispark.fragment.mine.setting.citylist.view.sort.SideBar.OnTouchingLetterChangedListener;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【城市切换】界面
 * 日期：	2014年12月5日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2014年12月5日
 * </pre>
 */
public class CityListActivity extends BaseActivity {

	private PinnedHeaderListView lstv;
	private CityListAdapter customAdapter;
	// private CityListAdapter adapter;
	// private List<CityListEntity> list;
	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;
	// private boolean isFlag = true;
	// private CityListEntity city;
	public SharedPreferences settings;
	// private String current_city = "";// 当前城市

	private List<CityGroupInfo> mCityGroupInfo = new ArrayList<CityGroupInfo>();
	List<CityItemEntity> data;
	private SideBar sideBar;
	private TextView dialog;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.offline_map_main);
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("选择城市");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		lstv = (PinnedHeaderListView) findViewById(R.id.citylist);
		// 设置城市的单击事件
		lstv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 保存要切换的城市坐标
				// weidu
				// ParkApplication.CityLatitude = data.get(arg2).getmCityInfo()
				// .getLatitude();
				// // jingdu
				// ParkApplication.CityLongitude = data.get(arg2).getmCityInfo()
				// .getLongitude();
				ParkApplication.CurrentCity = data.get(arg2).getmCityInfo()
						.getName();
				finish();
				SettingActivity.mActivity.finish();
				MainActivity.homeRBtn.setChecked(true);
				MainActivity.mViewPager.setCurrentItem(0);
			}
		});

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = customAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lstv.setSelection(position);
				}
			}
		});

		ParseGetCityList();
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}

	};

	private List<CityItemEntity> createTestData() {
		List<CityItemEntity> data = new ArrayList<CityItemEntity>();
		for (int i = 0; i < mCityGroupInfo.size(); i++) {
			for (int j = 0; j < mCityGroupInfo.get(i).getCity().size(); j++) {
				CityItemEntity itemEntity = new CityItemEntity(mCityGroupInfo.get(i)
						.getGroup(), mCityGroupInfo.get(i).getCity().get(j));
				data.add(itemEntity);
			}
		}
		return data;
	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取城市列表
	 * 日期：	2015年11月25日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void ParseGetCityList() {
		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.mUserId);

		LogUtil.showLog(
				3,
				"【获取城市列表URL】" + Constant.GET_CITY_URL
						+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.GET_CITY_URL,
				CityListEntity.class, params, new onResult<CityListEntity>() {
					@Override
					public void onSuccess(CityListEntity result) {
						loadingProgress.hide();
						mCityGroupInfo = result.getData().getList();
						data = createTestData();
						View HeaderView = getLayoutInflater().inflate(
								R.layout.listview_item_header, lstv, false);
						ViewUtil.setViewSize(HeaderView, 50, 0);

						TextView header = (TextView) HeaderView
								.findViewById(R.id.header);
						ViewUtil.setTextSize(header, 40);
						ViewUtil.setPaddingLeft(header, 20);

						lstv.setPinnedHeader(HeaderView);
						customAdapter = new CityListAdapter(getApplication(),
								data);
						lstv.setAdapter(customAdapter);
						lstv.setOnScrollListener(customAdapter);
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.hide();
					}
				});
	}

	public class CityItemEntity {
		private String mTitle;
		private CityInfo mCityInfo;

		public CityItemEntity(String pTitle, CityInfo mCityInfo) {
			mTitle = pTitle;
			this.mCityInfo = mCityInfo;
		}

		public String getTitle() {
			return mTitle;
		}

		public String getmTitle() {
			return mTitle;
		}

		public void setmTitle(String mTitle) {
			this.mTitle = mTitle;
		}

		public CityInfo getmCityInfo() {
			return mCityInfo;
		}

		public void setmCityInfo(CityInfo mCityInfo) {
			this.mCityInfo = mCityInfo;
		}

	}
}
