package cn.com.unispark.fragment.mine.msgpush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.msgpush.adapter.MsgAdapter;
import cn.com.unispark.fragment.mine.msgpush.entity.MsgEntity;
import cn.com.unispark.fragment.mine.msgpush.entity.MsgEntity.DataObject.MsgPushInfo;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 消息中心页面
 * 日期：	2015年6月23日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：rjf
 *    修改日期： 2015年11月25日
 * </pre>
 */
public class MsgActivity extends BaseActivity implements OnScrollListener {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	private PullToRefreshListView lstv;
	private List<MsgPushInfo> list;
	private MsgAdapter adapter;
	private TextView data_null_tv;

	private int num_list_ye;
	private boolean isNextPage = false;

	protected int count = 0;
	protected int zuidazhi;

	int mLastFirstVisibleItem = 0;
	boolean mIsScrollingUp;
	private boolean mIsUp;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (!isNextPage) {
					if (list.size() != 0) {
						list.clear();
					}
				}
				for (MsgPushInfo parkRecode : ((MsgEntity) msg.obj).getData()
						.getList()) {
					list.add(parkRecode);
				}
				adapter.notifyDataSetChanged();
				// 上拉加载下拉刷新结束
				lstv.onRefreshComplete();
				lstv.setVisibility(View.VISIBLE);
				data_null_tv.setVisibility(View.GONE);
				if (isNextPage) {
					lstv.getRefreshableView().setSelection(lstv.getBottom());// 移动到listview尾部
					isNextPage = false;
				}
				break;
			}
		}
	};

	@Override
	public void setContentLayout() {
		setContentView(R.layout.msg_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("无忧公告");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 消息中心无数据所显示
		data_null_tv = (TextView) findViewById(R.id.data_null_tv);
		data_null_tv.setText("您还没有系统通知");

		lstv = (PullToRefreshListView) findViewById(R.id.listview);
		lstv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MsgPushInfo entity = list.get(position - 1); // 得到listView指定位置的的实体类

				// 用来判断是否已经读取过
				ShareUtil.setSharedString(entity.getId(), entity.getId());

				// 跳转至消息详情页
				ToolUtil.IntentClass(activity, MsgDetailActivity.class, "id",
						entity.getId(), false);
				
				MobclickAgent.onEvent(context, "announcement_details_click");

			}
		});

		list = new ArrayList<MsgPushInfo>();
		adapter = new MsgAdapter(context, list);
		lstv.setAdapter(adapter);

		// 下拉刷新和上拉加载监听事件
		lstv.setMode(Mode.BOTH);
		lstv.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isNextPage = false;
				parseGetMsgRecord();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isNextPage = true;
				loadMore();
			}
		});

		loadingProgress.show();
		parseGetMsgRecord();
		lstv.setOnScrollListener(this);
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout: // 返回按钮
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		if (ParkApplication.isMsgDetail) {
			adapter.notifyDataSetChanged();
			ParkApplication.isMsgDetail = false;
		}
		super.onResume();
	}

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
			mLastFirstVisibleItem = currentFirstVisibleItem;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 设置刷新文本说明(展开刷新栏前)
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
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (count % 10 == 0) {
					zuidazhi = count / 10;
				} else {
					zuidazhi = count / 10 + 1;
				}
				if (num_list_ye >= zuidazhi) {
					ToastUtil.showToast("消息已全部加载 ");
					lstv.onRefreshComplete();
				} else {
					num_list_ye++;
					parseGetMsgRecord();
				}
			}
		}, 2000);
	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取消息推送列表
	 * 日期：	2015年11月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetMsgRecord() {

		if (!isNextPage) {
			num_list_ye = 1;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "1");
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("page", num_list_ye + "");
		params.put("perpage", "10");
		httpUtil.parse(httpUtil.POST, Constant.MSG_LIST_URL, MsgEntity.class,
				params, new onResult<MsgEntity>() {
					@Override
					public void onSuccess(MsgEntity result) {
						loadingProgress.dismiss();
						count = result.getData().getCount();
						if (count != 0) {
							Message message = new Message();
							message.obj = (Object) result;
							message.what = 0;
							handler.sendMessage(message);
						} else {
							lstv.setVisibility(View.GONE);
							data_null_tv.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.showToast(errMsg);
					}
				});
	}

}
