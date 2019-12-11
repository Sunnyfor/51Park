package cn.com.unispark.fragment.mine.recharge.childfrag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import cn.com.unispark.fragment.mine.recharge.adapter.RemainConsumeAdapter;
import cn.com.unispark.fragment.mine.recharge.entity.RemainConsumeEntity;
import cn.com.unispark.fragment.mine.recharge.entity.RemainConsumeEntity.DataObject.RemainConsumeInfo;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * <pre>
 * 功能说明：【余额消费】界面
 * 日期：	2015年7月30日
 * 开发者：	任建飞
 * 版本信息：V4.3.5
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class RemainConsumeFragment extends Fragment {
	
	private PullToRefreshListView lstv;
	private RemainConsumeAdapter adapter;
	private List<RemainConsumeInfo> list;

	private int page = 1; // 当前页（默认第一页）
	private View view;
	private int sum;
	public int count = 0;
	
	
	private boolean isNextPage = false;// 是否要加载下一页，true加载下一页，false不加载
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (!isNextPage) {
					if (list.size() != 0) {
						list.clear();
					}
				}
				for (RemainConsumeInfo parkRecode : ((RemainConsumeEntity) msg.obj)
						.getData().getList()) {
					list.add(parkRecode);
				}
				adapter.notifyDataSetChanged();
				// 上拉加载下拉刷新结束
				lstv.onRefreshComplete();
				lstv.setVisibility(View.VISIBLE);
				data_null_tv.setVisibility(View.GONE);
				if (isNextPage) {
					// 移动到listview尾部
					lstv.getRefreshableView()
							.setSelection(
									lstv.getBottom());
					isNextPage = false;
				} else {
					ToastUtil.show("刷新完成");
				}
				break;
			}
		}
	};
	private Context mContext;
	private TextView data_null_tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContext = getActivity();
		
		view = View.inflate(mContext, R.layout.remain_fragment, null);
		
		initView();
		getBalanceConsumptionEntityList();
		return view;
	}

	private void initView() {
		lstv = (PullToRefreshListView) view
				.findViewById(R.id.lv_balancerecode);
		lstv.setMode(Mode.BOTH);// 上拉加载和下拉刷新
		lstv
				.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isNextPage = false;
						getBalanceConsumptionEntityList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isNextPage = true;
						loadMore();
					}
				});
		list = new ArrayList<RemainConsumeInfo>();
		adapter = new RemainConsumeAdapter(list,
				mContext);
		lstv.setAdapter(adapter);
		
		data_null_tv = (TextView) view.findViewById(R.id.data_null_tv);
		data_null_tv.setText("您还没有消费记录");
		ViewUtil.setTextSize(data_null_tv, 30);
		
//		lstv.setOnScrollListener(this);
	}

	int mLastFirstVisibleItem = 0;
	boolean mIsScrollingUp;
	private boolean mIsUp;

//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		if (view.getId() == lstv.getRefreshableView().getId()) {
//			final int currentFirstVisibleItem = lstv.getRefreshableView()
//					.getFirstVisiblePosition();
//			if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//				mIsUp = true;
//			} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//				mIsUp = false;
//			}
//			// Log.e("slx", "mIsUp2"+mIsUp);
//			mLastFirstVisibleItem = currentFirstVisibleItem;
//		}
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		// 设置刷新文本说明(展开刷新栏前)
//		Log.e("slx", "mIsUp" + mIsUp);
//		if (mIsUp) {
//			lstv.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
//			lstv.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
//			lstv.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
//		} else {
//			lstv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
//			lstv.getLoadingLayoutProxy().setPullLabel("下拉刷新");
//			lstv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
//		}
//	}


	/**
	 * 上拉加载更多
	 */
	private void loadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (count % 8 == 0) {
					sum = count / 8;
				} else {
					sum = count / 8 + 1;
				}
				if (page >= sum) {
					Toast.makeText(mContext, "消费记录已全部加载 ", Toast.LENGTH_SHORT)
							.show();
					lstv.onRefreshComplete();
				} else {
					page++;
					getBalanceConsumptionEntityList();
				}
			}
		}, 2000);
	}

	/**
	 * <pre>
	 * 功能说明：加载充值记录
	 * 日期：	2015年7月30日
	 * 开发者：	任建飞
	 * </pre>
	 */
	public void getBalanceConsumptionEntityList() {
		if (!isNextPage) {
			page = 1;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("page", page + "");
		params.put("perpage", "8");

		LogUtil.showLog(3,
				"【余额明细--余额消费列表 URL】" + Constant.BALANCE_URL + LogUtil.buildUrlParams(params));

		HttpUtil httpUtils = new HttpUtil(mContext);
		httpUtils.parse(httpUtils.POST, Constant.BALANCE_URL,
				RemainConsumeEntity.class, params,
				new onResult<RemainConsumeEntity>() {
					@Override
					public void onSuccess(RemainConsumeEntity result) {
						count = result.getData().getCount();
						if (count>0) {
							Message message = new Message();
							message.obj = (Object) result;
							message.what = 0;
							handler.sendMessage(message);
						}else {
							lstv.onRefreshComplete();
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