package cn.com.unispark.fragment.home.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.fragment.home.map.adapter.HistoryAdapter;
import cn.com.unispark.fragment.home.map.adapter.SearchAdapter;
import cn.com.unispark.fragment.home.map.db.HistorySearchDBOpenHelper;
import cn.com.unispark.fragment.home.map.entity.HistoryEntity;
import cn.com.unispark.fragment.home.map.entity.SearchEntity;
import cn.com.unispark.fragment.unknown.util.SearchDBUtils;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 地图搜索功能
 * 日期：	2015年5月12日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年5月19日
 * </pre>
 */
public class SearchActivity extends BaseActivity {
	// 标题栏返回按钮 // 搜索按钮
	private LinearLayout back_ll, search_ll;

	/**
	 * 搜索记录
	 */
	private List<SearchEntity> searchList = new ArrayList<SearchEntity>();
	private SearchAdapter searchAdapter;
	private ListView searchLstv;
	/**
	 * 历史搜索记录
	 */
	private HistoryEntity[] historyEntity;
	private HistoryAdapter historyAdapter;
	private View historyFooter;

	// 历史记录数据库
	private HistorySearchDBOpenHelper dbHelper;
	private SQLiteDatabase db = null;

	// 搜索结果框
	private ClearEditText searchEdit;

	private Marker searchMarker;

	private PoiSearch.Query query;
	private PoiSearch search;
	private String SDCardRoot;
	private TextView data_null_tv;
	private Button clearBtn;

	private Timer timer = new Timer();

	@Override
	public void setContentLayout() {
		setContentView(R.layout.search_main);

		initDB();
	}

	@Override
	public void initView() {

		//标题栏高度
		RelativeLayout title_rl = (RelativeLayout) findViewById(R.id.title_rl);
		ViewUtil.setViewSize(title_rl, 88, 0);
		
		// 标题栏返回按钮
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		ViewUtil.setPaddingLeft(back_ll, 20);

		// 搜索按钮
		search_ll = (LinearLayout) findViewById(R.id.search_ll);
		search_ll.setOnClickListener(this);
		ViewUtil.setPadding(search_ll, 0, 20);

		data_null_tv = (TextView) findViewById(R.id.data_null_tv);
		historyFooter = View.inflate(context, R.layout.search_history_foot,
				null);

		clearBtn = (Button) historyFooter.findViewById(R.id.close_btn);
		clearBtn.setOnClickListener(this);

		searchLstv = (ListView) findViewById(R.id.search_lv);
		initHistory2();

		// 监听搜索框的文本变化
		searchEdit = (ClearEditText) findViewById(R.id.search_et);
		searchEdit.setHint("搜索地点");
		searchEdit.setFocusable(true);
		searchEdit.setFocusableInTouchMode(true);
		searchEdit.requestFocus();
		ViewUtil.setViewSize(searchEdit, 60, 490);


		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) searchEdit
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(searchEdit, 0);
			}
		}, 100);

		searchEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (!TextUtils.isEmpty(searchEdit.getText())) {
					searchLstv.removeFooterView(historyFooter);
				} else {
					initHistory2();
				}
				if (s.length() <= 0) {
					searchList.clear();
					searchAdapter.notifyDataSetChanged();
					return;
				}
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				Inputtips inputTips = new Inputtips(SearchActivity.this,
						new InputtipsListener() {
							@Override
							public void onGetInputtips(List<Tip> list, int arg1) {
								
								if(list == null){
									return;
								}
								
								loadingProgress.dismiss();
								// 重置集合
								searchList.clear();
								if(searchAdapter != null){
									searchAdapter.notifyDataSetChanged();
								}
								// 遍历搜索结果
								
								if (list.size() > 0) {
									
									for (int i = 0; i < list.size(); i++) {
										searchList.add(new SearchEntity(list.get(i)
												.getName(), list.get(i)
												.getDistrict()));
									}
									
									data_null_tv.setVisibility(View.GONE);
									searchLstv.setVisibility(View.VISIBLE);
									searchLstv.setTag(true);
									searchAdapter = new SearchAdapter(context,
											searchList);
									searchLstv.setAdapter(searchAdapter);
								}

							}
						});
				try {
					inputTips.requestInputtips(s.toString().trim(),
							ParkApplication.CurrentCity.toString());
				} catch (AMapException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		// 点击item推荐停车场
		searchLstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (searchList.size() != 0) {
					doSearch(position, (Boolean) searchLstv.getTag());
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("searchName", searchList.get(position).getName());
					map.put("searchDistrict", searchList.get(position)
							.getDistrict());
					MobclickAgent.onEvent(context, "search_barBtn_click", map);
				}else{
					if(historyEntity.length != 0){
						doSearch(position, (Boolean) false);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("searchName", historyEntity[position].getKey());
						map.put("searchDistrict", historyEntity[position].getValue());
						MobclickAgent.onEvent(context, "search_barBtn_click", map);
					}
				}

			}

		});

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.back_ll:// 返回按钮
			finish();
			break;
		case R.id.search_ll:// 搜索按钮
			if (searchList.size() != 0) {
				doSearch(0, (Boolean) searchLstv.getTag());
			}

			break;
		case R.id.close_btn:
			final DialogUtil dialog = new DialogUtil(context);
			dialog.setMessage("确认要清除所有的历史记录");
			dialog.setPositiveButton("清除", new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					db = dbHelper.getWritableDatabase();
					SearchDBUtils.delAllHistoryData(db);
					db.close();
					initHistory2();

				}
			});
			dialog.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			break;
		// case R.id.search:
		// if (!TextUtils.isEmpty(searchEdit.getText())
		// && searchList.size() != 0) {
		// doSearch(0);
		// } else {
		// ToastUtil.show("附近没有推荐停车场！");
		// }
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
		// break;
		}

	}

	private void initDB() {
		SDCardRoot = Environment.getExternalStorageDirectory().toString()
				+ "/51Park/maps/";
		File file = new File(SDCardRoot);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			Toast.makeText(context, "sd卡不可用", Toast.LENGTH_LONG).show();
		}
		dbHelper = new HistorySearchDBOpenHelper(context, SDCardRoot
				+ Constant.DB_SEARCH_HISTORY, null, 2);
	}

	private void doSearch(int position, boolean flag) {
		loadingProgress.show();
		String name;

		if (flag) {
			name = searchList.get(position).getName();
		} else {
			name = historyEntity[position].getKey();
		}

		query = new PoiSearch.Query(name, "", ParkApplication.CityCode);
		query.setPageNum(0);
		query.setPageSize(10);
		search = new PoiSearch(SearchActivity.this, query);
		search.setOnPoiSearchListener(new OnPoiSearchListener() {
			@Override
			public void onPoiSearched(PoiResult arg0, int arg1) {

				final ArrayList<PoiItem> pois = arg0.getPois();
				if(pois.size() != 0){
					ParkApplication.desLongitude = pois.get(arg1).getLatLonPoint()
							.getLongitude();
					ParkApplication.desLatitude = pois.get(arg1).getLatLonPoint()
							.getLatitude();
					LatLng markerlatlng = new LatLng(pois.get(0).getLatLonPoint()
							.getLatitude(), pois.get(0).getLatLonPoint()
							.getLongitude());
					
					/*parseSearchPark(markerlatlng);*/

					ContentValues values = new ContentValues();
					values.put(Constant.ID, pois.get(0).getPoiId());
					values.put("key", pois.get(0).getTitle());
					values.put("value", pois.get(0).getCityName()
							+ pois.get(0).getAdName());
					values.put("latitude", pois.get(0).getLatLonPoint()
							.getLatitude());
					values.put("longitude", pois.get(0).getLatLonPoint()
							.getLongitude());
					dbHelper.getWritableDatabase().insert(
							Constant.TABLE_HISTORY_NAME, "key", values);
					
					getIntent().putExtra("latlng", markerlatlng);
					setResult(110, getIntent());
					finish();
					
				}
			}

			@Override
			public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

			}
		});
		search.searchPOIAsyn();
	}

	/**
	 * <pre>
	 * 功能说明：初始化历史记录
	 * 日期：	2015-6-15
	 * </pre>
	 */
	public void initHistory2() {
		db = dbHelper.getWritableDatabase();
		historyEntity = SearchDBUtils.queryAllKey(db);
		db.close();
		if (historyEntity.length == 0) {
			searchLstv.setVisibility(View.GONE);
			data_null_tv.setVisibility(View.VISIBLE);
			searchLstv.removeFooterView(historyFooter);
		} else {
			searchLstv.setVisibility(View.VISIBLE);
			data_null_tv.setVisibility(View.GONE);
			searchLstv.addFooterView(historyFooter);
		}

		historyAdapter = new HistoryAdapter(context, historyEntity);
		searchLstv.setAdapter(historyAdapter);
		searchLstv.setTag(false);

	}

	/**
	 * <pre>
	 * 功能说明：【解析】附近停车场推荐列表
	 * 日期：	2015年11月5日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param markerlatlng
	 * </pre>
	 */
	/*private void parseSearchPark(final LatLng markerlatlng) {
		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("longitude", String.valueOf(markerlatlng.longitude));
		params.put("latitude", String.valueOf(markerlatlng.latitude));

		httpUtil.parse(httpUtil.POST, Constant.PARK_RECOMMEND_URL,
				SearchItemEntity.class, params,
				new HttpUtil.onResult<SearchItemEntity>() {

					@Override
					public void onSuccess(SearchItemEntity result) {
						loadingProgress.dismiss();

						// MapFragment.mAMap.moveCamera(CameraUpdateFactory
						// .newCameraPosition(new CameraPosition(
						// markerlatlng,
						// ParkApplication.mMapZoomLevel, 0f, 0f)));

						if (searchMarker != null) {
							searchMarker.destroy();
						}
						//
						// MarkerOptions marker = new MarkerOptions();
						// marker.icon(BitmapDescriptorFactory
						// .fromResource(R.drawable.icon_location_arrows));
						// marker.position(markerlatlng);
						// searchMarker = MapFragment.mAMap.addMarker(marker);

						Intent it = new Intent(SearchActivity.this,
								TuiJianActivity.class);
						it.putExtra("searchItem", result);
						it.putExtra("latlng", markerlatlng);
						startActivity(it);

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});

	}*/

}
