package cn.com.unispark.fragment.home.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.fragment.mine.coupons.adapter.CouponsAdapter;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity.DataObject.CouponsInfo;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * <pre>
 * 功能说明： 支付时选择停车券界面
 * 日期：	2015年11月4日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月4日
 * </pre>
 */
public class PayCouponsActivity extends BaseActivity {

	private CouponsAdapter adapter;
	private PullToRefreshListView lstv;
	private List<CouponsInfo> list = new ArrayList<CouponsInfo>();

	// 导航栏标题
	private TextView titleText;
	// 导航栏返回按钮
	private LinearLayout backLLayout;
	// 导航栏右侧使用规则介绍
	private ImageView moreImgView;
	// 优惠券使用规则url
	private String couponruleUrl;

	// 不选择停车券
	private Button noselect_coupons_btn;
	// 数据为空时提示语
	private TextView dataNullText;

	// 是否加载下一页
	private boolean isNextPage = false;
	// 停车券分页加载
	private int page = 1;

	// 停车券总条数
	private int mCouponsCount;
	private int sum;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.coupons_fragment);
		parseGetCoupons();
	}

	@Override
	public void initView() {
		// 显示头部
		findViewById(R.id.title_ic).setVisibility(View.VISIBLE);

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("使用停车券");

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 导航栏右侧使用规则介绍
		moreImgView = (ImageView) findViewById(R.id.moreImgView);
		moreImgView.setImageResource(R.drawable.btn_prompt);
		moreImgView.setVisibility(View.VISIBLE);
		moreImgView.setOnClickListener(this);

		// 不选择停车券
		noselect_coupons_btn = (Button) findViewById(R.id.noselect_coupons_btn);
		noselect_coupons_btn.setVisibility(View.VISIBLE);
		noselect_coupons_btn.setOnClickListener(this);
		ViewUtil.setTextSize(noselect_coupons_btn, 30);

		dataNullText = (TextView) findViewById(R.id.data_null_tv);

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
				loadMore();
			}
		});

		lstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent it = getIntent();
				it.putExtra("CouponsInfo", list.get(position - 1));
				setResult(20, it);
				finish();
			}
		});

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreImgView:
			if (couponruleUrl != null) {
				Intent it = new Intent(activity, WebActiveActivity.class);
				it.putExtra("url", couponruleUrl);
				it.putExtra("title", "停车券使用规则");
				startActivity(it);
			}
			break;
		case R.id.noselect_coupons_btn:
			Intent intent = new Intent();
			setResult(30, intent);
			finish();
			break;
		}
	}

	private void loadMore() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mCouponsCount % 10 == 0) {
					sum = mCouponsCount / 10;
				} else {
					sum = mCouponsCount / 10 + 1;
				}
				if (page >= sum) {
					ToastUtil.showToast("停车券已全部加载");
					lstv.onRefreshComplete();
				} else {
					page++;
					parseGetCoupons();
				}
			}
		}, 1000);

	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取未使用的停车券
	 * 日期：	2015年11月5日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetCoupons() {

		if (!isNextPage) {
			page = 1;
		}

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("page", page + "");
		params.put("perpage", "10");// 默认加载10条
		params.put("type", "2");// 查询场景（1：停车券列表，2：交费，默认为1）
		params.put("timeout", "0");
		params.put("used", "0");

		LogUtil.showLog(3, "【获取未使用的停车券URL】" + Constant.GET_POUPONS_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.GET_POUPONS_URL,
				CouponsEntity.class, params, new onResult<CouponsEntity>() {
					@Override
					public void onSuccess(CouponsEntity result) {

						loadingProgress.dismiss();

						couponruleUrl = result.getData().getCouponrule();
						
						mCouponsCount = result.getData().getCount();
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
							lstv.onRefreshComplete();
							if (isNextPage) {
								// 移动到ListView尾部
								lstv.getRefreshableView().setSelection(
										lstv.getBottom());
								isNextPage = false;
							}
						} else {
							dataNullText.setVisibility(View.VISIBLE);
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
