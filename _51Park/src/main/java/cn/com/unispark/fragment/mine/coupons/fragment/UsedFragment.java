package cn.com.unispark.fragment.mine.coupons.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.coupons.adapter.CouponsOutOrUsedAdapter;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity.DataObject.CouponsInfo;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ViewUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * <pre>
 * 功能说明： 已使用的优惠券
 * 日期：	2015年10月20日
 * 开发者：	任建飞
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class UsedFragment extends Fragment implements OnScrollListener  {
	private PullToRefreshListView lstv;
	/**
	 * "正在加载..."进度条
	 */
	private TextView data_null_tv;
	private Context mContext;
	private View view;
	private boolean isNextPage = false;
	private int page = 1;
	private List<CouponsInfo> dataList = new ArrayList<CouponsInfo>();
	// @SuppressLint("HandlerLeak")
	// private Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// if (!isNextPage) {
	// if (dataList.size() != 0) {
	// dataList.clear();
	// }
	// }
	// for (CouponsInfo parkRecode : ((CouponsEntity) msg.obj)
	// .getData()) {
	// dataList.add(parkRecode);
	// }
	// mCouponsAdapter.notifyDataSetChanged();
	// // 上拉加载下拉刷新结束
	// lstv.onRefreshComplete();
	// nullDataText.setVisibility(View.GONE);
	// if (isNextPage) {
	// // 移动到listview尾部
	// lstv.getRefreshableView().setSelection(lstv.getBottom());
	// isNextPage = false;
	// } else {
	// ToastUtil.show("刷新完成");
	// }
	// break;
	// }
	// }
	// };
	private int mCouponsCount;
	private int sum;
	private CouponsOutOrUsedAdapter mCouponsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		view = View.inflate(mContext, R.layout.coupons_fragment, null);
		initView();
		getYouHuiQuanList();
		return view;
	}

	@Override
	public void onDestroy() {
		Log.e("slx", "UsedFragment onDestroy");
		super.onDestroy();
	}

	public void initView() {

		// 数据为空时展示
		data_null_tv = (TextView) view.findViewById(R.id.data_null_tv);
		ViewUtil.setTextSize(data_null_tv, 30);

		lstv = (PullToRefreshListView) view.findViewById(R.id.listview);
		lstv.setMode(Mode.BOTH);// 上拉加载和下拉刷新
		lstv.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isNextPage = false;
				getYouHuiQuanList();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Log.e("slx", "onPullUp");
				isNextPage = true;
				loadMore();
			}
		});
		data_null_tv = (TextView) view.findViewById(R.id.data_null_tv);
		data_null_tv.setText("您还没有优惠券");
		lstv.setOnScrollListener(this);
	}

	int mLastFirstVisibleItem = 0;
	boolean mIsScrollingUp;
	private boolean mIsUp;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (view.getId() == lstv.getRefreshableView().getId()) {
			final int currentFirstVisibleItem = lstv.getRefreshableView()
					.getFirstVisiblePosition();
			if (currentFirstVisibleItem > mLastFirstVisibleItem) {
				mIsUp = true;
			} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
				mIsUp = false;
			}
			// Log.e("slx", "mIsUp2"+mIsUp);
			mLastFirstVisibleItem = currentFirstVisibleItem;
		}
	}
//
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 设置刷新文本说明(展开刷新栏前)
		Log.e("slx", "mIsUp" + mIsUp);
		if (mIsUp) {
			lstv.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
			lstv.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
			lstv.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
		} else {
			lstv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
			lstv.getLoadingLayoutProxy().setPullLabel("下拉刷新");
			lstv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
		}
	}

	/**
	 * 上拉加载更多
	 */
	private void loadMore() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mCouponsCount % 8 == 0) {
					sum = mCouponsCount / 8;
				} else {
					sum = mCouponsCount / 8 + 1;
				}
				if (page >= sum) {
					Toast.makeText(mContext, "已全部加载 ", Toast.LENGTH_SHORT)
							.show();
					lstv.onRefreshComplete();
				} else {
					page++;
					getYouHuiQuanList();
				}
			}
		}, 2000);
	}

	private void getYouHuiQuanList() {
		if (!isNextPage) {
			page = 1;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("page", page + "");
		params.put("perpage", "8");
		params.put("type", "1");
		params.put("timeout", "2");
		params.put("used", "1");
		HttpUtil httpUtils = new HttpUtil(mContext);
		httpUtils.parse(httpUtils.POST, Constant.GET_POUPONS_URL,
				CouponsEntity.class, params, new onResult<CouponsEntity>() {
					@Override
					public void onSuccess(CouponsEntity result) {
						mCouponsCount = result.getData().getCount();

						if (mCouponsCount != 0) {
							lstv.setVisibility(View.VISIBLE);

							if (!isNextPage) {
								if (dataList.size() != 0) {
									dataList.clear();
								}
							}
							dataList = result.getData().getList();
							mCouponsAdapter = new CouponsOutOrUsedAdapter(
									mContext, dataList);
							lstv.setAdapter(mCouponsAdapter);
							// 上拉加载下拉刷新结束
							lstv.onRefreshComplete();
							data_null_tv.setVisibility(View.GONE);
							if (isNextPage) {
								// 移动到listview尾部
								lstv.getRefreshableView().setSelection(
										lstv.getBottom());
								isNextPage = false;
							} else {
								// ToastUtil.show("刷新完成");
							}

						} else {
							lstv.setVisibility(View.GONE);
							data_null_tv.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {

					}
				});
	}
}