package cn.com.unispark.fragment.home.map;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.maps2d.model.LatLng;
import com.umeng.analytics.MobclickAgent;

public class MapActivity extends BaseActivity {

	// 返回按钮// 更多选项按钮
	private ImageView moreImgView;
	private LinearLayout backLLayout, moreLLayout;

	// 停车场类型：全部、免费、无忧、包月
	/*private RadioGroup radiogroup;
	private RadioButton all_rbtn, free_rbtn, park_rbtn, month_rbtn;*/

	// 搜索框
	private TextView search_tv;
	// 显示当前位置
//	private TextView location_tv;

	// 是否展示停车列表
	private boolean isShowParkList = false;

	private MapFragment mapFragment;
	public static ParkFragment parkFragment;

//	private Handler handler = new Handler(new Handler.Callback() {
//
//		@Override
//		public boolean handleMessage(Message msg) {
//
//			showFragment(mapFragment);
//			hideFragment(parkFragment);
//
//			mapFragment.setRadioGroup(radiogroup);
//
//			return false;
//		}
//	});

	@Override
	public void setContentLayout() {
		setContentView(R.layout.map_park_main);
	}

	@Override
	public void initView() {
		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 展示更多按钮
		moreImgView = (ImageView) findViewById(R.id.moreImgView);
		moreImgView.setImageResource(R.drawable.btn_see_list);
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);

		// 搜索框
		search_tv = (TextView) findViewById(R.id.search_tv);
		search_tv.setHint("搜索地点");
		search_tv.setOnClickListener(this);

		/*
		 * 停车场类型选项卡
		 */
	/*	radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		ViewUtil.setMargin(radiogroup, 10, 30, 15, 30, ViewUtil.LINEARLAYOUT);

		// 停车场类型：全部
		all_rbtn = (RadioButton) findViewById(R.id.all_rbtn);
		all_rbtn.setOnClickListener(this);
		ViewUtil.setTextSize(all_rbtn, 25);
		ViewUtil.setMarginRight(all_rbtn, 20, ViewUtil.LINEARLAYOUT);

		// 免费
		free_rbtn = (RadioButton) findViewById(R.id.free_rbtn);
		ViewUtil.setTextSize(free_rbtn, 25);
		ViewUtil.setMarginRight(free_rbtn, 20, ViewUtil.LINEARLAYOUT);

		// 无忧
		park_rbtn = (RadioButton) findViewById(R.id.park_rbtn);
		ViewUtil.setTextSize(park_rbtn, 25);
		ViewUtil.setMarginRight(park_rbtn, 20, ViewUtil.LINEARLAYOUT);

		// 包月
		month_rbtn = (RadioButton) findViewById(R.id.month_rbtn);
		ViewUtil.setTextSize(month_rbtn, 25);
		ViewUtil.setMarginRight(month_rbtn, 20, ViewUtil.LINEARLAYOUT);

		// // 当前位置
		// // line_view = findViewById(R.id.line_view);
		// location_tv = (TextView) findViewById(R.id.location_tv);
		// ViewUtil.setViewSize(location_tv, 70, 0);
		// ViewUtil.setTextSize(location_tv, 24);
		// ViewUtil.setPaddingRight(location_tv, 20);
*/
		mapFragment = new MapFragment();
		parkFragment = new ParkFragment();

		
		/*mapFragment.setRadioGroup(radiogroup);*/
		mapFragment.setUpdateLcaoNameListener(new updateLocalName() {
			
			@Override
			public void update(String localName) {
				parkFragment.setLocalName(localName);
			}
		});
		addFragment(mapFragment);
		addFragment(parkFragment);

		showFragment(mapFragment);
		hideFragment(parkFragment);

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout:
			/**
			 * <pre>
			 * 切换地图列表
			 * 1.moreImgView	用来切换显示的图标
			 * 2.location_tv	刷新按钮的展示
			 * </pre>
			 */
			if (isShowParkList) {
				// 显示停车场列表
				showFragment(parkFragment);
				hideFragment(mapFragment);
				/*parkFragment.setRadioGroup(radiogroup);*/
				isShowParkList = false;

				moreImgView.setImageResource(R.drawable.btn_see_map);
				// location_tv.setCompoundDrawables(null, null, getResources()
				// .getDrawable(R.drawable.icon_flush), null);

				MobclickAgent.onEvent(context, "seekParking_listBtn_click");
			} else {
				// 显示地图覆盖物
				showFragment(mapFragment);
				hideFragment(parkFragment);
				/*mapFragment.setRadioGroup(radiogroup);*/
				isShowParkList = true;

				moreImgView.setImageResource(R.drawable.btn_see_list);
				// location_tv.setCompoundDrawables(null, null, null, null);

				MobclickAgent.onEvent(context, "seekParking_listMapBtn_click");
			}

			break;
		case R.id.search_tv:// 搜索
			Intent intent = new Intent(this, SearchActivity.class);
			startActivityForResult(intent, 100);
			MobclickAgent.onEvent(context, "seekParking_search_click");
			break;
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// 初始化推荐停车场
//		if (resultCode == 40) {
//			ParkApplication.isSearch = false;
//			mapFragment.setparklist(((SearchItemEntity) data
//					.getSerializableExtra("SearchItem")).getData().getList());
//			mapFragment.showParkViewPager();
//			handler.sendEmptyMessage(0);
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	interface updateLocalName {
		void update(String localName);
	}

	/**
	 * <pre>
	 * 功能说明：添加某Fragment
	 * 日期：	2015年12月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void addFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container_rl, fragment).commit();
	}

	/**
	 * <pre>
	 * 功能说明：显示某Fragment
	 * 日期：	2015年12月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().show(fragment).commit();
	}

	/**
	 * <pre>
	 * 功能说明：隐藏某Fragment
	 * 日期：	2015年12月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void hideFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().hide(fragment).commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int ResponseCode, Intent arg2) {
		super.onActivityResult(requestCode, ResponseCode, arg2);
		
		if(requestCode == 100 && ResponseCode == 110){
//			ToastUtil.show("woshizhsdhjkd");
			LatLng latlng = arg2.getParcelableExtra("latlng");
			mapFragment.moveMap(latlng);
		}
		
	}
}
 
