package cn.com.unispark.fragment.mine.parkrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.OrderDetailsActivity;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.fragment.home.pay.adapter.OrderListAdapter;
import cn.com.unispark.fragment.home.pay.entity.OrderListEntity;
import cn.com.unispark.fragment.home.pay.entity.OrderListEntity.DataObject.OrderList;
import cn.com.unispark.fragment.mine.parkrecord.ParkRecodeEntity.DataObject.ParkRecodeInfo;
import cn.com.unispark.fragment.mine.setting.offlinemap.view.PinnedHeaderListView;
import cn.com.unispark.task.CustomHttpClient;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ListViewUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 停车记录界面
 * 日期：	2015年3月19日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class ParkRecordActivity extends BaseActivity {
	/**
	 * 停车记录展示条数
	 */
	public int parkCount = 0;

	private TextView titleText;// 导航栏消费充值标题

	/**
	 * 历史订单集合和适配器（即原先的停车记录数据）
	 */
	private List<ParkRecodeInfo> list;
	private ParkRecordAdapter adapter;
	private PinnedHeaderListView lstv;

	/**
	 * 当前订单集合和适配器
	 */
	private List<OrderList> orderList = new ArrayList<OrderList>();
	private CurrentRecordAdapter orderAdapter;
	private ListView orderLstv;

	int num_list_ye = 1;// 控制停车场显示的第几页
	int zuidazhi;
	Button btn_shangye = null;
	Button btn_xiaye = null;
	ProgressBar search_xiaofeijilu = null;

	CustomHttpClient httpClient = null;
	// String leixing = null;
	// private Context mContext;
	private Handler handler = new Handler();

	private boolean isNextPage = false;// 是否要加载下一页，true加载下一页，false不加载
	private LinearLayout backLLayout;
	private TextView data_null_tv;

	private PullToRefreshScrollView sv_content;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.park_record_main);

	}

	/**
	 * 初始化view和基本事件
	 */
	@Override
	public void initView() {
		// 导航栏消费充值标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("停车记录");

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		/*
		 * 当前订单、历史订单切换按钮
		 */
		RadioGroup orderRGrp = (RadioGroup) findViewById(R.id.order_rgrp);
		ViewUtil.setMargin(orderRGrp, 10, 0, ViewUtil.LINEARLAYOUT);

		currentRBtn = (RadioButton) findViewById(R.id.current_rbtn);
		currentRBtn.setOnClickListener(this);
		ViewUtil.setViewSize(currentRBtn, 60, 150);
		ViewUtil.setTextSize(currentRBtn, 26);

		RadioButton historyRBtn = (RadioButton) findViewById(R.id.history_rbtn);
		historyRBtn.setOnClickListener(this);
		ViewUtil.setViewSize(historyRBtn, 60, 150);
		ViewUtil.setTextSize(historyRBtn, 26);

		// 在MineFragment中传入，进入后默认选中历史订单。
		if ("yes".equals(getIntent().getStringExtra("historyOrder"))) {
			historyRBtn.setChecked(true);
		} else {
			// HomeFragment中无需传入，进入选中当前订单即可。
			currentRBtn.setChecked(true);
		}

		// 数据为空时展示
		data_null_tv = (TextView) findViewById(R.id.data_null_tv);
		data_null_tv.setText(currentRBtn.isChecked() ? "当前无订单" : "还没有停车记录");

		// 上拉、下拉设定
		sv_content = (PullToRefreshScrollView) findViewById(R.id.sv_content);
		sv_content.setMode(Mode.BOTH);
		// ViewUtil.setMarginTop(sv_content, 88, ViewUtil.RELATIVELAYOUT);

		orderLstv = (ListView) findViewById(R.id.lv_current_order);
		// ViewUtil.setViewSize(orderLstv, 960, 640);
		// setListViewHeightBasedOnChildren(listView);
		orderLstv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ParkApplication.mOrderNum = orderList.get(position)
						.getOrderno();
				String orderNo=orderList.get(position).getOrderno();
				
				switch (orderList.get(position).getStatus()) {
				case 1:
					ToolUtil.IntentClass(activity, PayFeeActivity.class,"orderNo",orderNo, false);
					break;
				case 2:
					ToolUtil.IntentClass(activity, PayFeeActivity.class, "orderNo",orderNo, false);
					break;
				case 3:
					ToolUtil.IntentClass(activity, PayResultActivity.class,
							false);
					break;
				case 4:
					ToolUtil.IntentClass(activity, PayFeeActivity.class,"orderNo",orderNo,  false);
					// orderLstv.setClickable(false);
					break;
				}

				// ToolUtil.IntentClass(activity, PayFeeActivity.class, false);
			}
		});

		lstv = (PinnedHeaderListView) findViewById(R.id.listview);
		lstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				ParkRecodeInfo recodeEntity = list.get(paramInt);
				if (1 == recodeEntity.getIspay()) {
					// // 已交费
					ToolUtil.IntentClass(activity, OrderDetailsActivity.class,
							"order_num", recodeEntity.getOrderno(), false);
					MobclickAgent
							.onEvent(context, "my_parkRecordDetails_click");
				} else {
					// 未交费
					if (ParkApplication.mOrderState == 6) {
						ToolUtil.IntentClassLogin(activity,
								PayResultActivity.class, false);
					} else {
						ToolUtil.IntentClassLogin(activity,
								PayFeeActivity.class, false);
					}

				}
			}
		});

		// 左郁
		// 下拉刷新和上拉加载监听事件
		sv_content
				.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						isNextPage = false;
						// parseGetParkRecord();
						if (currentRBtn.isChecked()) {
							parseGetCurrentOrder();
						} else {
							parseGetParkRecord();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						isNextPage = true;
						loadMore();
					}
				});

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.current_rbtn:
			parseGetCurrentOrder();
			break;
		case R.id.history_rbtn:
			parseGetParkRecord();
			break;
		}

	}

	/**
	 * 上拉加载更多
	 */
	private void loadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (parkCount % 10 == 0) {
					zuidazhi = parkCount / 10;
				} else {
					zuidazhi = parkCount / 10 + 1;
				}
				if (num_list_ye >= zuidazhi) {
					ToastUtil.show("停车记录已全部加载 ");
					sv_content.onRefreshComplete();
				} else {
					num_list_ye++;
					// parseGetParkRecord();
					if (currentRBtn.isChecked()) {
						parseGetCurrentOrder();
					} else {
						parseGetParkRecord();
					}
				}
			}
		}, 2000);
	}

	// public Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// if (!isNextPage) {
	// if (list.size() != 0) {
	// list.clear();
	// } else {
	// lstv.setVisibility(View.INVISIBLE);
	// data_null_tv.setVisibility(View.GONE);
	// }
	// }
	// for (ParkRecodeInfo parkRecode : ((ParkRecodeEntity) msg.obj)
	// .getData().getList()) {
	// list.add(parkRecode);
	// }
	// adapter.notifyDataSetChanged();
	// // 上拉加载下拉刷新结束
	// // lstv.onRefreshComplete();
	// lstv.setVisibility(View.VISIBLE);
	// data_null_tv.setVisibility(View.GONE);
	// if (isNextPage) {
	// // lstv.getRefreshableView().setSelection(lstv.getBottom());//
	// // 移动到listview尾部
	// isNextPage = false;
	// } else {
	// // ToastUtil.show("刷新完成");
	// }
	// break;
	// }
	// };
	// };

	public Handler mHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (!isNextPage) {
					if (list.size() != 0) {
						list.clear();
					} else {
						lstv.setVisibility(View.INVISIBLE);

						data_null_tv.setVisibility(View.GONE);
					}
				}
				// for (ParkRecodeInfo parkRecode : ((ParkRecodeEntity) msg.obj)
				// .getData().getList()) {
				// list.add(parkRecode);
				// }

				list.addAll(((ParkRecodeEntity) msg.obj).getData().getList());

				adapter.notifyDataSetChanged();
				// 上拉加载下拉刷新结束
				sv_content.onRefreshComplete();
				lstv.setVisibility(View.VISIBLE);
				data_null_tv.setVisibility(View.GONE);
				if (isNextPage) {
					// lstv.getRefreshableView().setSelection(lstv.getBottom());//
					// 移动到listview尾部
					isNextPage = false;
				} else {
					ToastUtil.show("刷新完成");
				}
				break;

			}
			return false;
		}
	});

	private boolean mIsUp;

	/**
	 * <pre>
	 * 功能说明：【解析】获取停车记录
	 * 日期：	2015年11月18日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetParkRecord() {

		lstv.setVisibility(View.VISIBLE);
		orderLstv.setVisibility(View.GONE);

		if (!isNextPage) {
			num_list_ye = 1;
		}

//		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("page", num_list_ye + "");
		params.put("perpage", "10");
		// Constant.RECORDS_URL
		// "http://alphaapi.51park.cn/memv2/usertest/map/getrecord.php"
		httpUtil.parse(httpUtil.POST, Constant.RECORDS_URL,
				ParkRecodeEntity.class, params,
				new onResult<ParkRecodeEntity>() {
					@Override
					public void onSuccess(ParkRecodeEntity result) {
						loadingProgress.hide();
						parkCount = result.getData().getCount();

						if (parkCount > 0) {
							View HeaderView = LayoutInflater
									.from(context)
									.inflate(
											R.layout.listview_parkrecode_item_header,
											lstv, false);
							ViewUtil.setViewSize(HeaderView, 50, 0);
							list = new ArrayList<ParkRecodeInfo>();
							adapter = new ParkRecordAdapter(context, list);
							lstv.setAdapter(adapter);
							lstv.setPinnedHeader(HeaderView);
							lstv.setOnScrollListener(adapter);

							Message message = new Message();
							message.obj = (Object) result;
							message.what = 0;
							mHandler.sendMessage(message);
						} else {
							lstv.setVisibility(View.GONE);
							data_null_tv.setVisibility(View.VISIBLE);
							data_null_tv.setText("还没有停车记录");
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.hide();
						ToastUtil.show(errMsg);
					}
				});
	}

	/**
	 * 
	 * <pre>
	 * 功能说明：获取当前订单列表
	 * 日期：	2016年12月15日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	private void parseGetCurrentOrder() {

		lstv.setVisibility(View.GONE);
		orderLstv.setVisibility(View.VISIBLE);

		if (!isNextPage) {
			num_list_ye = 1;
		}

		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.d("【首页获取账单URL】" + Constant.GET_CURRENTLIST_URL, params);
		System.out.println("【首页获取账单URL】" + Constant.GET_CURRENTLIST_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.GET_CURRENTLIST_URL,
				OrderListEntity.class, params, new onResult<OrderListEntity>() {

					@Override
					public void onSuccess(OrderListEntity result) {
						sv_content.onRefreshComplete();
						loadingProgress.hide();

						parkCount = result.getData().getCount();

						if (parkCount > 0) {
							orderList = result.getData().getList();
							orderAdapter = new CurrentRecordAdapter(context,
									orderList);
							orderLstv.setAdapter(orderAdapter);
							orderAdapter.notifyDataSetChanged();
							ListViewUtil
									.setListViewHeightBasedOnChildren(orderLstv);
						} else {
							orderLstv.setVisibility(View.GONE);
							data_null_tv.setVisibility(View.VISIBLE);
							data_null_tv.setText("当前无订单");

						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						sv_content.onRefreshComplete();
						loadingProgress.hide();
						ToastUtil.showToast(errMsg);
					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
		if (currentRBtn.isChecked()) {
			parseGetCurrentOrder();
		} else {
			parseGetParkRecord();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return super.onKeyDown(keyCode, event);
	}

	int mLastFirstVisibleItem = 0;
	boolean mIsScrollingUp;

	private RadioButton currentRBtn;

	/**
	 * scrollview嵌套listview显示不全解决
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		OrderListAdapter listAdapter = (OrderListAdapter) listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = ((Adapter) listAdapter).getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	// if (view.getId() == lstv.getRefreshableView().getId()) {
	// final int currentFirstVisibleItem = lstv.getRefreshableView()
	// .getFirstVisiblePosition();
	// if (currentFirstVisibleItem > mLastFirstVisibleItem) {
	// mIsUp = true;
	// } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
	// mIsUp = false;
	// }
	// // Log.e("slx", "mIsUp2"+mIsUp);
	// mLastFirstVisibleItem = currentFirstVisibleItem;
	// }
	// }
	//
	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem,
	// int visibleItemCount, int totalItemCount) {
	// // 设置刷新文本说明(展开刷新栏前)
	// Log.e("slx", "mIsUp" + mIsUp);
	// if (mIsUp) {
	// lstv.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
	// lstv.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
	// lstv.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
	// } else {
	// lstv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
	// lstv.getLoadingLayoutProxy().setPullLabel("下拉刷新");
	// lstv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
	// }
	// }

}
