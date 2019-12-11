package cn.com.unispark.fragment.mine.plate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.plate.adapter.PlateAdapter;
import cn.com.unispark.fragment.mine.plate.entity.PlateEntity;
import cn.com.unispark.fragment.mine.plate.entity.PlateModifyEntity;
import cn.com.unispark.fragment.mine.plate.entity.PlateEntity.DataObject.PlateInfo;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenu;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenuCreator;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenuItem;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenuListView;
import cn.com.unispark.fragment.mine.plate.view.SwipeMenuListView.OnMenuItemClickListener;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 车牌管理界面,取消设置默认车牌的功能
 * 日期：	2015年11月19日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class PlateManagerActivity extends BaseActivity {
	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;
	// 添加车牌
	private LinearLayout plate_ll;
	private SwipeMenuListView lstv;
	private ArrayList<PlateInfo> list;
	private PlateAdapter adapter;
	private Activity mActivity;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.plate_main);
		mActivity = this;
	}

	@Override
	public void initView() {
		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("车牌管理");
		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		// 添加车牌
		plate_ll = (LinearLayout) findViewById(R.id.plate_ll);
		plate_ll.setOnClickListener(this);
		ViewUtil.setViewSize(plate_ll, 98, 0);
		lstv = (SwipeMenuListView) findViewById(R.id.listview);
		list = new ArrayList<PlateInfo>();
		adapter = new PlateAdapter(context, list);
		lstv.setAdapter(adapter);
		// initMenuListView();
		// parseGetPlateList();
	}

	@Override
	public void onResume() {
		parseGetPlateList();
		super.onResume();
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.plate_ll:// 添加车牌
			ToolUtil.IntentClassForResult(mActivity, PlateAddActivity.class,
					"platecard", "", 30, false);
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明： 【解析】 获取车牌列表
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void parseGetPlateList() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.d("获取车牌列表【URL】" + Constant.PLATELIST_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.PLATELIST_URL,
				PlateEntity.class, params, new onResult<PlateEntity>() {
					@Override
					public void onSuccess(PlateEntity result) {
						if (loadingProgress.isShowing()) {
							loadingProgress.hide();
						}
						if (list.size() > 0) {
							list.clear();
						}
						for (PlateInfo parkRecode : result.getData().getList()) {
							list.add(parkRecode);
						}
						if (list.size() > 0) {
							if (list.size() == 3) {
								plate_ll.setVisibility(View.GONE);
							} else {
								plate_ll.setVisibility(View.VISIBLE);
							}
						}
						initMenuListView(list);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						if (loadingProgress.isShowing()) {
							loadingProgress.hide();
						}
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 初始化侧滑菜单
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void initMenuListView(final ArrayList<PlateInfo> list) {
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				createMenu2(menu);
//				switch (menu.getViewType()) {
//				case 0:
//					createMenu1(menu);
//					break;
//				case 1:
//					createMenu2(menu);
//					break;
//				case 2:
//					break;
//				}

			}

		};
		lstv.setMenuCreator(creator);
		lstv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				int id = menu.getMenuItem(index).getId();
				Log.e("rjf", "id---->>" + id);
				switch (id) {
//				case 10:// 设置为默认
//					defaultPlate(list.get(position).getPlate());
//					break;
				case 20:// 修改车牌号
					ToolUtil.IntentClassForResult(mActivity,
							PlateAddActivity.class, "platecard",
							list.get(position).getPlate(), 30, false);
					break;
				case 30:// 删除当前列的车牌号
					deletePlate(list.get(position).getPlate());
					break;
				}
				return false;
			}
		});
		// 通过动画插值器设置弹簧效果
		lstv.setCloseInterpolator(new BounceInterpolator());
		// lstv.setOpenInterpolator(new AccelerateInterpolator());
	}

	protected void createMenu2(SwipeMenu menu) {
		SwipeMenuItem openItem = new SwipeMenuItem(getParent());
		openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
		openItem.setWidth(dp2px(80));
		openItem.setTitle("修改");
		openItem.setTitleSize(18);
		openItem.setId(20);
		openItem.setTitleColor(Color.WHITE);
		menu.addMenuItem(openItem);
		SwipeMenuItem delItem = new SwipeMenuItem(getParent());
		delItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
		delItem.setWidth(dp2px(80));
		delItem.setTitle("删除");
		delItem.setId(30);
		delItem.setTitleSize(18);
		delItem.setTitleColor(Color.WHITE);
		menu.addMenuItem(delItem);
	}

	/*protected void createMenu1(SwipeMenu menu) {
		SwipeMenuItem defaultItem = new SwipeMenuItem(getParent());
		
		defaultItem.setBackground(new ColorDrawable(Color
				.parseColor("#ff529de8")));
		defaultItem.setWidth(dp2px(80));
		defaultItem.setId(10);
		defaultItem.setTitle("设为默认");// 529de8
		defaultItem.setTitleSize(18);
		defaultItem.setTitleColor(Color.WHITE);
		menu.addMenuItem(defaultItem);
		
		SwipeMenuItem openItem = new SwipeMenuItem(getParent());
		openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
		openItem.setWidth(dp2px(80));
		openItem.setId(20);
		openItem.setTitle("修改");
		openItem.setTitleSize(18);
		openItem.setTitleColor(Color.WHITE);
		menu.addMenuItem(openItem);
		
		SwipeMenuItem delItem = new SwipeMenuItem(getParent());
		delItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
		delItem.setWidth(dp2px(80));
		delItem.setId(30);
		delItem.setTitle("删除");
		delItem.setTitleSize(18);
		delItem.setTitleColor(Color.WHITE);
		menu.addMenuItem(delItem);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (20 == arg1) {
			parseGetPlateList();
		}
		if (40 == arg1) {
			parseGetPlateList();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
*/
	/**
	 * <pre>
	 * 功能说明：
	 * 删除车牌号
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	protected void defaultPlate(String platecard) {
		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("plate", platecard);
		httpUtil.parseno(httpUtil.POST, Constant.PLATE_DEFAULT_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						if (code == httpUtil.SERVER_REQ_OK) {
							loadingProgress.hide();
							parseGetPlateList();
							ToastUtil.show(msg);
						} else {
							ToastUtil.show(msg);
						}
					}
				});
		// httpUtil.parse(httpUtil.POST, Constant.PLATE_DEFAULT_URL,
		// PlateModifyEntity.class, params,
		// new onResult<PlateModifyEntity>() {
		// @Override
		// public void onSuccess(PlateModifyEntity result) {
		// loadingProgress.hide();
		// // 1：操作成功；2：车牌已被绑定(type=1时有效)；3：操作失败
		// if ("200".equals(result.getCode())) {
		// if (1 == result.getData().getRes()) {
		// parseGetPlateList();
		// ToastUtil.showToast("删除成功");
		// } else if (2 == result.getData().getRes()) {
		// ToastUtil.showToast("车牌已被绑定");
		// } else {
		// ToastUtil.showToast("删除失败");
		// }
		// }
		// }
		//
		// @Override
		// public void onFailed(int errCode, String errMsg) {
		// ToastUtil.showToast("删除失败");
		// loadingProgress.hide();
		// }
		// });
	}

	/**
	 * <pre>
	 * 功能说明：
	 * 删除车牌号
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	protected void deletePlate(String platecard) {
		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("plate", platecard);
		params.put("type", "2");
		httpUtil.parse(httpUtil.POST, Constant.PLATEMANAGE_URL,
				PlateModifyEntity.class, params,
				new onResult<PlateModifyEntity>() {
					@Override
					public void onSuccess(PlateModifyEntity result) {
						loadingProgress.hide();
						// 1：操作成功；2：车牌已被绑定(type=1时有效)；3：操作失败
						if ("200".equals(result.getCode())) {
							if (1 == result.getData().getRes()) {
								parseGetPlateList();
								ToastUtil.showToast("删除成功");
							} else if (2 == result.getData().getRes()) {
								ToastUtil.showToast("车牌已被绑定");
							} else {
								ToastUtil.showToast("删除失败");
							}
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast("删除失败");
						loadingProgress.hide();
					}
				});
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
