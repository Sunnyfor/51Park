package cn.com.unispark.fragment.mine.coupons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.mine.coupons.adapter.CouponsAdapter;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity.DataObject.CouponsInfo;
import cn.com.unispark.fragment.mine.coupons.fragment.CanBeUsedFragment;
import cn.com.unispark.fragment.mine.coupons.fragment.OutDateFragment;
import cn.com.unispark.fragment.mine.coupons.fragment.UsedFragment;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

/**
 * <pre>
 * 功能说明： 优惠券界面（选择、查看）
 * 日期：	2015年10月29日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月29日
 * </pre>
 */
public class CouponsActivity extends BaseActivity implements OnScrollListener{
	
	private PullToRefreshListView lstv;

	private CouponsAdapter adapter;
	private List<CouponsInfo> list = new ArrayList<CouponsInfo>();

	private TextView data_null_tv;

	// 是否加载下一页
	private boolean isNextPage = false;
	// 优惠券分页加载
	private int page = 1;

	// 优惠券总条数
	private int mCouponsCount;
	private int sum;



@Override
public void setContentLayout() {
	setContentView(R.layout.coupons_fragment);
	parseGetCoupons();
}

@Override
public void initView() {

//	// 数据为空时展示
//	data_null_tv = (TextView) view.findViewById(R.id.data_null_tv);
//	ViewUtil.setTextSize(data_null_tv, 30);


	// 导航栏标题
	TextView titleText = (TextView) findViewById(R.id.titleText);
	titleText.setText(getResources().getString(R.string.ting_che_quan));

	// 返回按钮
	LinearLayout backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
	backLLayout.setOnClickListener(this);
	
	lstv = (PullToRefreshListView) findViewById(R.id.listview);
	lstv.setMode(Mode.BOTH);// 上拉加载和下拉刷新
	lstv.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			isNextPage = false;
			parseGetCoupons();
		}

		@Override
		public void onPullUpToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			isNextPage = true;
			loadMoreData();
		}
	});

	data_null_tv = (TextView) findViewById(R.id.data_null_tv);
	data_null_tv.setText("您还没有停车券");
	lstv.setOnScrollListener(this);
}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
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
 * <pre>
 * 功能说明：分页加载更多数据
 * 日期：	2015年11月17日
 * 开发者：	陈丶泳佐
 * 
 * </pre>
 */
private void loadMoreData() {
	new Handler().postDelayed(new Runnable() {
		@Override
		public void run() {
			if (mCouponsCount % 10 == 0) {
				sum = mCouponsCount / 10;
			} else {
				sum = mCouponsCount / 10 + 1;
			}
			if (page >= sum) {
				ToastUtil.showToast("优惠券已全部加载");
				lstv.onRefreshComplete();
			} else {
				page++;
				parseGetCoupons();
			}
		}
	}, 1000);

}

	private void parseGetCoupons() {

		if (!isNextPage) {
			page = 1;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("page", page + "");
		params.put("perpage", "10");// 默认加载10条
		params.put("type", "1");// 优惠券界面
		params.put("timeout", "0");
		params.put("used", "0");

	
		httpUtil.parse(httpUtil.POST, Constant.GET_POUPONS_URL,
			CouponsEntity.class, params, new onResult<CouponsEntity>() {
				@Override
				public void onSuccess(CouponsEntity result) {
					mCouponsCount = result.getData().getCount();
					lstv.onRefreshComplete();
					if (mCouponsCount != 0) {
						lstv.setVisibility(View.VISIBLE);
						if (!isNextPage) {
							if (list.size() != 0) {
								list.clear();
							}
						}
						list = result.getData().getList();
						adapter = new CouponsAdapter(context, list);
						lstv.setAdapter(adapter);

						// 上拉加载下拉刷新结束
						data_null_tv.setVisibility(View.GONE);
						if (isNextPage) {
							// 移动到ListView尾部
							lstv.getRefreshableView().setSelection(
									lstv.getBottom());
							isNextPage = false;
						}
					} else {
						lstv.setVisibility(View.GONE);
						data_null_tv.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onFailed(int errCode, String errMsg) {
					ToastUtil.showToast(errMsg);
				}
			});
		}

}