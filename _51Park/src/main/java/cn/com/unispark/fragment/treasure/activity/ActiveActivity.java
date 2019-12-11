package cn.com.unispark.fragment.treasure.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.fragment.treasure.activity.ActiveEntity.DataObject.ActionCenterInfo;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ToastUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

public class ActiveActivity extends BaseActivity implements OnScrollListener {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	private PullToRefreshListView lstv;
	private ArrayList<ActionCenterInfo> list;
	private ActiveAdapter adapter;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.action_main);

	}

	@Override
	public void initView() {

		// 导航栏标题“活动中心”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.huo_dong_zhong_xin));

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		lstv = (PullToRefreshListView) findViewById(R.id.listview);
		lstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				String srcUrl = list.get(paramInt - 1).getSrc();
				if (1 == list.get(paramInt - 1).getIsexpire()) {

					if (!"".equals(srcUrl)) {
						Intent intent = null;
						intent = new Intent(context, WebActiveActivity.class);
						intent.putExtra("url", srcUrl);
						context.startActivity(intent);
						MobclickAgent.onEvent(context, "activityBtn_details_click");
					}
				} else {
					ToastUtil.show("该活动已过期！");
				}

			}
		});
		// 下拉刷新和上拉加载监听事件
		lstv.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				parseGetActiveList();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
			}
		});
		
		list = new ArrayList<ActionCenterInfo>();
		adapter = new ActiveAdapter(context, list);
		lstv.setAdapter(adapter);
		lstv.setMode(Mode.PULL_FROM_START);// 上拉加载和下拉刷新
		lstv.setOnScrollListener(this);

		
		// 数据为空时战士
		data_null_tv = (TextView) findViewById(R.id.data_null_tv);
		data_null_tv.setText("暂时没有活动");
		parseGetActiveList();
	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}

	public Handler mHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				if (list.size() != 0) {
					list.clear();
				}
				for (ActionCenterInfo parkRecode : ((ActiveEntity) msg.obj)
						.getData().getList()) {
					list.add(parkRecode);
				}
				adapter.notifyDataSetChanged();
				// 上拉加载下拉刷新结束
				lstv.onRefreshComplete();
				data_null_tv.setVisibility(View.GONE);
				// if (isNextPage) {
				// recordListView.getRefreshableView().setSelection(
				// recordListView.getBottom());// 移动到listview尾部
				// isNextPage = false;
				// } else {
				// ToastUtil.showToast("刷新完成");
				// }
				break;
			}

			return false;
		}
	});
	private TextView data_null_tv;

	/**
	 * <pre>
	 * 功能说明：【解析】获取活动中心列表
	 * 日期：	2015年11月30日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	protected void parseGetActiveList() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		httpUtil.parse(httpUtil.POST, Constant.ACTION_CENTER_URL,
				ActiveEntity.class, params, new onResult<ActiveEntity>() {
					@Override
					public void onSuccess(ActiveEntity result) {

						loadingProgress.dismiss();

						int count = result.getData().getList().size();

						if (count != 0) {
							Message message = new Message();
							message.obj = (Object) result;
							message.what = 0;
							mHandler.sendMessage(message);
						} else {
							data_null_tv.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.show(errMsg);
					}
				});
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

}
