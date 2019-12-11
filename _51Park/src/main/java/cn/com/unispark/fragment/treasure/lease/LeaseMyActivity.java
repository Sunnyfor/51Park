package cn.com.unispark.fragment.treasure.lease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.DisplayFullListView;
import cn.com.unispark.fragment.treasure.lease.adapter.LeaseMyAdapter;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseMyEntity;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseMyEntity.DataObject.LeaseMyInfo;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;

/**
 * <pre>
 * 功能说明： 【我的租赁】界面
 * 日期：	2015年9月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月22日
 * </pre>
 */
public class LeaseMyActivity extends BaseActivity {

	private DisplayFullListView lstv;
	private DisplayFullListView lstv0;// 过期停车位
	private LeaseMyAdapter adapter;
	private List<LeaseMyInfo> list;
	private List<LeaseMyInfo> list0;// 过期停车位集合

	// 导航栏标题以及返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 查看过期车位
	private TextView see_park_tv;

	// 没有租赁列表时,去租赁颜色改为蓝色
	private LinearLayout data_null_ll;
	private TextView data_null_tv;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.lease_my_main);
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("我的停车位");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 没有租赁列表时,“去租赁”颜色改为蓝色
		data_null_ll = (LinearLayout) findViewById(R.id.data_null_ll);
		data_null_tv = (TextView) findViewById(R.id.data_null_tv);
		data_null_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		data_null_tv.setOnClickListener(this);

		// 查看过期车位
		see_park_tv = (TextView) findViewById(R.id.see_park_tv);
		see_park_tv.setOnClickListener(this);

		// 未过期列表
		lstv = (DisplayFullListView) findViewById(R.id.listview);
		lstv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 判断是否可以续费
				if (list.get(position).getIscanrebuy() == 0) {
					ToastUtil.show(list.get(position).getReason());
				} else {
					ParkApplication.mParkId = list.get(position).getParkid();
					ParkApplication.mLeaseType = list.get(position).getType();
					ToolUtil.IntentClass(activity, LeaseDetailActivity.class,
							false);
					MobclickAgent.onEvent(context, "my_myParkingDTLeaseBtn_click");

				}

			}
		});

		// 过期列表
		lstv0 = (DisplayFullListView) findViewById(R.id.listview0);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.data_null_tv://去租赁按钮
			onIntentClass(activity, LeaseCarActivity.class, false);
			MobclickAgent.onEvent(context, "my_myParkingLeaseBtn_click");
			break;
		case R.id.see_park_tv:
			parseDateLeaseList(false);
			MobclickAgent.onEvent(context, "my_myParkingCheckPastBtn_click");
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		parseMyLeaseList();
		parseDateLeaseList(true);
	}

	/**
	 * <pre>
	 * 功能说明：【解析】我的租赁列表
	 * 日期：	2015年9月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseMyLeaseList() {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.showLog(
				3,
				"【我的租赁列表 URL】" + Constant.MY_PARKCARD_URL
						+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.MY_PARKCARD_URL,
				LeaseMyEntity.class, params, new onResult<LeaseMyEntity>() {

					@Override
					public void onSuccess(LeaseMyEntity result) {
						list = result.getData().getDatalist();
						if (list.size() != 0) {
							lstv.setVisibility(View.VISIBLE);
							adapter = new LeaseMyAdapter(context, list, true);
							lstv.setAdapter(adapter);
						} else {
							data_null_ll.setVisibility(View.VISIBLE);
							findViewById(R.id.scrollview).setVisibility(
									View.GONE);
						}

						loadingProgress.dismiss();
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.show(errMsg);
						loadingProgress.dismiss();
					}
				});

	}

	/**
	 * <pre>
	 * 功能说明：【解析】过期租赁列表
	 * 日期：	2015年9月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param isOnlySee 用来判断过期列表是否展示
	 * 		注：默认进入后是不显示过期列表的，点击【查看过期列表】显示过期列表
	 * </pre>
	 */
	private void parseDateLeaseList(final boolean isOnlySee) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.showLog(3, "【过期租赁列表 URL】" + Constant.MY_EXPIRECARD_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.MY_EXPIRECARD_URL,
				LeaseMyEntity.class, params, new onResult<LeaseMyEntity>() {

					@Override
					public void onSuccess(LeaseMyEntity result) {
						list0 = result.getData().getDatalist();
						if (list0.size() != 0) {
							if (!isOnlySee) {
								lstv0.setVisibility(View.VISIBLE);
								adapter = new LeaseMyAdapter(context, list0,
										false);
								lstv0.setAdapter(adapter);
							} else {
								see_park_tv.setVisibility(View.VISIBLE);
							}
						} else {
							see_park_tv.setVisibility(View.GONE);

						}

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.show(errMsg);
						loadingProgress.dismiss();
					}
				});

	}

}
