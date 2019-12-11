package cn.com.unispark.fragment.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.CycleViewPager;
import cn.com.unispark.fragment.home.map.MapActivity;
import cn.com.unispark.fragment.home.map.entity.ParkCountEntity;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.fragment.home.pay.adapter.OrderListAdapter;
import cn.com.unispark.fragment.home.pay.entity.OrderEntity.DataObject.InfoObject;
import cn.com.unispark.fragment.home.pay.entity.OrderListEntity;
import cn.com.unispark.fragment.home.pay.entity.OrderListEntity.DataObject.OrderList;
import cn.com.unispark.fragment.home.viewpager.AdPagerAdapter;
import cn.com.unispark.fragment.home.viewpager.entity.AdPagerEntity;
import cn.com.unispark.fragment.home.viewpager.entity.AdPagerEntity.Data.ImageInfo;
import cn.com.unispark.fragment.mine.parkrecord.ParkRecordActivity;
import cn.com.unispark.fragment.mine.vipcard.PhotoActivity;
import cn.com.unispark.fragment.mine.vipcard.UserQrActivity;
import cn.com.unispark.login.LoginActivity;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【首页】主界面
 * 日期：	2015年3月14日
 * 开发者：	陈泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月10日
 * </pre>
 */
@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends Fragment implements OnClickListener {
	/**
	 * 无订单
	 */
	public final int F_NO_ORDER = 0;
	/**
	 * 展示账单信息
	 */
	public final int F_SHOW_ORDER = 1;
	/**
	 * 支付成功
	 */
	public final int F_PAY_SUCCESS = 2;

	// 成员View
	private View view;

	// 导航栏标题// 返回按钮//二维码按钮
	private TextView titleText;
	private ImageView backImgView, moreImgView;
	private LinearLayout moreLLayout;

	// 轮播图
	private CycleViewPager viewpager;

	// 轮播页集合
	public List<ImageInfo> adpagerList;
	// 存放轮播图页数的点
	private LinearLayout pointLLayout;
	// 轮播页滑动到最后一个点
	private int lastPosition;
	// 找车位//记录车位（改为缴车费）
	private RelativeLayout find_car_rl, record_car_rl;
	// 附近的停车场数量
	private TextView count_tv;
	// 记录车位(座驾位置)文字切换、图片切换
	private TextView record_car_tv;
	private ImageView find_car_iv, record_car_iv;

	// PopupWindow类型选择：扫一扫，我的二维码
	private View contentView;
	private PopupWindow popupWindow;
	private TextView scanText, qrcodeText;

	// 退出时间
	public long mExitTime;

	// 网络请求
	private HttpUtil httpUtil;
	private PullToRefreshScrollView sv_content;

	// 硬件出场提示语
	private String exitnote;

	// 解析数据
	private InfoObject data;

	private Activity mActivity;
/*	*//**
	 * 记录车位
	 *//*
	private final int FLAG_RECORD_CAR = 1;
	*//**
	 * 座驾位置
	 *//*
	private final int FLAG_CAR_LOCATION = 2;*/

	/*
	 * 车辆在场状态：无订单，待支付，待出场，停车中。。
	 */
	private ImageView order_no_iv;
	
	
	/*private View order_state_ic;*/

	/*// 订单状态栏可滑动布局
	private HorizontalScrollView order_scrollView;
	// 存放订单状态的容器
	private LinearLayout order_ll;*/
	
	
	// 存放订单列表的容器
	private ViewPager order_viewPager;
	// 订单列表的集合
	private List<OrderList> orderList;
	// 订单数量
	private int orderCount;
	// 订单列表适配器
	private OrderListAdapter orderAdapter;
	// 订单状态栏显示更多的按钮// 订单状态栏与订单布局之间的分割线
	private ImageView more_iv, line_iv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		view = View.inflate(mActivity, R.layout.home_main, null);

		httpUtil = new HttpUtil(mActivity);
		initView();

		return view;
	}

	public void initView() {

		//
		sv_content = (PullToRefreshScrollView) view
				.findViewById(R.id.sv_content);
		// 上拉、下拉设定
		sv_content.setMode(Mode.PULL_FROM_START);
		// 上拉监听函数
		sv_content.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 执行刷新函数
				parseGetOrderInfo();
				MobclickAgent.onEvent(mActivity, "home_refres_click");
			}
		});

		// 标题栏
		titleText = (TextView) view.findViewById(R.id.titleText);
		titleText.setText("");
		titleText.setBackgroundResource(R.drawable.icon_home_title);

		// 隐藏返回按钮
		backImgView = (ImageView) view.findViewById(R.id.backImgView);
		backImgView.setVisibility(View.GONE);

		// 显示导航栏右侧的二维码图标
/*		moreImgView = (ImageView) view.findViewById(R.id.moreImgView);
		moreImgView.setImageResource(R.drawable.btn_home_scan);
		moreImgView.setVisibility(View.VISIBLE);
		moreLLayout = (LinearLayout) view.findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);*/

/*		// PopupWindow布局，类型选择：扫一扫，我的二维码
		contentView = View.inflate(mActivity, R.layout.home_scan_popwin, null);
		contentView.setId(520);
		contentView.setOnClickListener(this);

		// PopupWindow子条目：扫一扫
		scanText = (TextView) contentView.findViewById(R.id.pop_scan_tv);
		scanText.setOnClickListener(this);
		// PopupWindow子条目：我的二维码
		qrcodeText = (TextView) contentView.findViewById(R.id.pop_qrcode_tv);
		qrcodeText.setOnClickListener(this);*/
		// 初始化ViewPager
		viewpager = (CycleViewPager) view.findViewById(R.id.viewpager);
		// 存放轮播图页数的点
		pointLLayout = (LinearLayout) view.findViewById(R.id.point_ll);

		/*
		 * 找车位和记录车位选项卡
		 */
		LinearLayout center_ll = (LinearLayout) view
				.findViewById(R.id.center_ll);
		ViewUtil.setViewSize(center_ll, 190, 0);
		ViewUtil.setPadding(center_ll, 14, 10);

		// 找车位布局
		find_car_rl = (RelativeLayout) view.findViewById(R.id.find_car_rl);
		find_car_rl.setOnClickListener(this);
		ViewUtil.setViewSize(find_car_rl, 0, 305);

		// 找车位文字
		TextView find_car_tv = (TextView) view.findViewById(R.id.find_car_tv);
		ViewUtil.setTextSize(find_car_tv, 30);
		ViewUtil.setMarginTop(find_car_tv, 46, ViewUtil.RELATIVELAYOUT);

		// 找车位图标
		find_car_iv = (ImageView) view.findViewById(R.id.find_car_iv);
		ViewUtil.setMarginLeft(find_car_iv, 30, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(find_car_iv, 22, ViewUtil.RELATIVELAYOUT);

		// 附近的停车场数量
		count_tv = (TextView) view.findViewById(R.id.count_tv);
		ViewUtil.setTextSize(count_tv, 20);
		ViewUtil.setMarginTop(count_tv, 8, ViewUtil.RELATIVELAYOUT);

		// 记录车位布局
		record_car_rl = (RelativeLayout) view.findViewById(R.id.record_car_rl);
		record_car_rl.setOnClickListener(this);
		ViewUtil.setViewSize(record_car_rl, 0, 305);
		ViewUtil.setMarginLeft(record_car_rl, 10, ViewUtil.LINEARLAYOUT);

		// 记录车位(座驾位置)文字,字体颜色
		record_car_tv = (TextView) view.findViewById(R.id.record_car_tv);
		ViewUtil.setTextSize(record_car_tv, 30);
		ViewUtil.setMarginTop(record_car_tv, 46, ViewUtil.RELATIVELAYOUT);

		// 记录车位(座驾位置)图标
		record_car_iv = (ImageView) view.findViewById(R.id.record_car_iv);
		ViewUtil.setMarginLeft(record_car_iv, 52, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginRight(record_car_iv, 15, ViewUtil.RELATIVELAYOUT);

		/*
		 * 无订单// 有订单
		 */
		order_no_iv = (ImageView) view.findViewById(R.id.order_no_iv);
		ViewUtil.setViewSize(order_no_iv, 400, 0);
		ViewUtil.setMargin(order_no_iv, 0,10, ViewUtil.LINEARLAYOUT);
		
		order_viewPager = (ViewPager) view.findViewById(R.id.order_viewPager);
		ViewUtil.setViewSize(order_viewPager, 400, 0);
		ViewUtil.setMargin(order_viewPager, 0,10, ViewUtil.LINEARLAYOUT);
		
	/*	order_state_ic = view.findViewById(R.id.order_state_ic);
		// order_state_ic.setVisibility(View.VISIBLE);
		// order_state_ic.setOnClickListener(this);

		// 订单状态栏与订单布局之间的分割线
		line_iv = (ImageView) view.findViewById(R.id.line_iv);
		ViewUtil.setViewSize(line_iv, 14, 0);
		ViewUtil.setMarginTop(line_iv, -14, ViewUtil.RELATIVELAYOUT);

		// 订单状态栏显示更多的按钮
		// more_iv = (ImageView) view.findViewById(R.id.more_iv);

		// 首页订单布局
		order_ll = (LinearLayout) view.findViewById(R.id.order_ll);
		order_viewPager = (ViewPager) view.findViewById(R.id.order_viewPager);
		ViewUtil.setViewSize(order_viewPager, 330, 0);

		// 订单可滑动状态
		order_scrollView = (HorizontalScrollView) view
				.findViewById(R.id.order_scrollView);
		ViewUtil.setViewSize(order_scrollView, 80, 0);
		ViewUtil.setMargin(order_scrollView, 0, 60, ViewUtil.RELATIVELAYOUT);
		order_scrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});*/

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find_car_rl:// 找车位
			ToolUtil.IntentClass(mActivity, MapActivity.class, false);
			MobclickAgent.onEvent(mActivity, "home_SeekParking_click");
			break;
		case R.id.record_car_rl:// 记录车位
			/*ToolUtil.IntentClass(mActivity, RecordCarActivity.class, false);*/
//			交车费
			ToolUtil.IntentClassLogin(mActivity, ParkRecordActivity.class, false);
//			ToolUtil.IntentClass(mActivity, TestActivity.class, false);
			/**
			 * <pre>
			 * 判断是从记录车位还是座驾位置进入的此页面
			 * 记录车位：调用系统的相机
			 * 座驾位置：展示记录车位的照片
			 * </pre>
			 */
			// int flagCar = ShareUtil.getSharedInteger("flagCar") == 0 ? 1
			// : ShareUtil.getSharedInteger("flagCar");
			// switch (flagCar) {
			// case FLAG_RECORD_CAR:
			 ToolUtil.IntentClass(mActivity, PhotoActivity.class, false);
			// break;
			// case FLAG_CAR_LOCATION:
			// ToolUtil.IntentClass(mActivity, RecordCarActivity.class, false);
			// break;
			// }
			MobclickAgent.onEvent(mActivity, "home_RecordCar_click");
			break;
		/*case R.id.order_state_ic:// 订单状态
			showIntentClass();
			break;*/
		case R.id.moreLLayout:// 标题栏右侧扫描按钮
			if (ShareUtil.getSharedBoolean("islogin")) {// 已登录
				showPopupWindow(v);
			} else {// 未登录
				ToolUtil.IntentClass(mActivity, LoginActivity.class, false);
			}
			break;
//		case R.id.pop_scan_tv:// 扫一扫
//			showPopupWindow(v);
//			ToolUtil.IntentClass(mActivity, QrScanActivity.class, false);
//			MobclickAgent.onEvent(mActivity, "home_ScanQRcode_click");
//			break;
		case R.id.pop_qrcode_tv:
			showPopupWindow(v);// 我的二维码
			ToolUtil.IntentClass(mActivity, UserQrActivity.class, false);
			MobclickAgent.onEvent(mActivity, "home_myQRcode_click");
			break;
		case 520:
			popupWindow.dismiss();
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		parseInitUserInfo();

	}

	/**
	 * <pre>
	 * 功能说明：展示订单列表的ViewPager
	 * 日期：	2016年1月8日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	private void showOrderViewPager() {

		/*final int mWidth = ViewUtil.getWidth(520) / 3; // TextView宽度

		FrameLayout.LayoutParams llayout_params = (FrameLayout.LayoutParams) order_ll
				.getLayoutParams();
		order_ll.setPadding(mWidth, 0, mWidth, 0);
		llayout_params.width = mWidth * (orderCount + 2);

		// if (orderCount >= 3) {
		// more_iv.setVisibility(View.VISIBLE);
		// ViewUtil.setMargin(more_iv, 25, 20, 25, 20, ViewUtil.RELATIVELAYOUT);
		// }

		
		 * 初始化订单状态TextView组件
		 
		order_ll.removeAllViews();
		for (int i = 0; i < orderCount; i++) {

			HomeStateLayout stateLayout = new HomeStateLayout(getActivity());
			stateLayout.setTag(i);
			stateLayout.setText(orderList.get(i).getStatusnote());
			stateLayout.setGravity(Gravity.CENTER);
			stateLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					order_viewPager.setCurrentItem((Integer) v.getTag());
					changeState((Integer) v.getTag());
				}
			});

			stateLayout.setViewSize(0, mWidth);
			order_ll.addView(stateLayout);
		}*/

		orderAdapter = new OrderListAdapter(getActivity(), getActivity(),
				orderList);
		order_viewPager.setAdapter(orderAdapter);
		/*
		 * ViewPager页数改变监听
		 */
		order_viewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageSelected(int position) {
						
						/*order_scrollView.smoothScrollTo(position * mWidth, 0);
						changeState(position);*/
						
						// if (position >= orderCount - 2) {
						// more_iv.setVisibility(View.GONE);
						// } else {
						// more_iv.setVisibility(View.VISIBLE);
						// }

						MobclickAgent.onEvent(mActivity,
								"home_ParkingFeeSlide_click");
					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

		/*changeState(0);
		order_scrollView.scrollTo(0, 0);*/
		order_viewPager.setCurrentItem(0);

	}

	/*private void changeState(int positon) {
		for (int i = 0, size = order_ll.getChildCount(); i < size; i++) {
			if (positon == i) {
				((HomeStateLayout) order_ll.getChildAt(i)).setChecked(true);
			} else {
				((HomeStateLayout) order_ll.getChildAt(i)).setChecked(false);
			}
		}
	}*/

	/**
	 * <pre>
	 * 功能说明：判断订单状态展示哪部分
	 * 日期：	2015年12月7日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	// private void showOrderState(boolean isNoOrder, boolean isShowBill,
	// boolean isPaySuccess) {
	//
	// // 无订单
	// ImageView order_no_iv = (ImageView) view.findViewById(R.id.order_no_iv);
	// // 展示账单
	// RelativeLayout order_bill_rl = (RelativeLayout) view
	// .findViewById(R.id.order_bill_rl);
	// // 支付成功
	// RelativeLayout order_pay_rl = (RelativeLayout) view
	// .findViewById(R.id.order_pay_rl);
	//
	// if (isNoOrder) {
	// order_no_iv.setVisibility(View.VISIBLE);
	// order_state_ic.setClickable(false);
	// } else {
	// order_no_iv.setVisibility(View.GONE);
	// }
	//
	// if (isShowBill) {
	// order_bill_rl.setVisibility(View.VISIBLE);
	// order_state_ic.setClickable(true);
	// } else {
	// order_bill_rl.setVisibility(View.GONE);
	// }
	//
	// if (isPaySuccess) {
	// order_pay_rl.setVisibility(View.VISIBLE);
	// order_state_ic.setClickable(true);
	// } else {
	// order_pay_rl.setVisibility(View.GONE);
	// }
	// }

	/**
	 * <pre>
	 * 功能说明：判断是记录车位还是座驾位置：设置图标、文字、字体颜色
	 * 日期：	2015年10月27日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showRecordOrLocation() {
		/**
		 * flagCar：1--记录车位 2--座驾位置
		 */
		int flagCar = ShareUtil.getSharedInteger("flagCar") == 0 ? 1
				: ShareUtil.getSharedInteger("flagCar");
		switch (flagCar) {
		case 1:
			// 记录车位：橙字
			record_car_tv.setText(R.string.ji_lu_che_wei);
			record_car_tv.setTextColor(this.getResources().getColor(
					R.color.orange_font));
			record_car_iv.setImageResource(R.drawable.icon_home_recordcar);
			break;
		case 2:
			// 座驾位置：红字
			record_car_tv.setText(R.string.zuo_jia_wei_zhi);
			record_car_tv.setTextColor(this.getResources().getColor(
					R.color.red_font));
			record_car_iv.setImageResource(R.drawable.icon_home_car_location);
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：扫一扫和我的二维码选择
	 * 日期：	2015年10月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showPopupWindow(View v) {

		DisplayMetrics metric = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = metric.heightPixels;

		if (popupWindow == null) {
			popupWindow = new PopupWindow(contentView, width, height, true);
		}

		// 替换背景颜色
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.transparent));
		// 获取焦点
		popupWindow.setFocusable(true);
		// 点击popupWindow之外的地方失去焦点
		popupWindow.setOutsideTouchable(false);

		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, 0, 0);
		}

	}

	/**
	 * <pre>
	 * 功能说明：展示ViewPager页数的点
	 * 日期：	2015年10月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showViewPagerPoint() {
		int size = adpagerList.size();
		if (size == 1) {
			pointLLayout.setVisibility(View.GONE);
		}
		if (size != 0) {
			pointLLayout.removeAllViewsInLayout();
			for (int i = 0; i < size; i++) {
				// 添加指示点
				ImageView point = new ImageView(mActivity);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.rightMargin = 20;
				point.setLayoutParams(params);
				point.setBackgroundResource(R.drawable.draw_point_bg);
				if (i == 0) {
					point.setEnabled(true);
				} else {
					point.setEnabled(false);
				}
				pointLLayout.addView(point);
			}

			viewpager.setAdapter(new AdPagerAdapter(mActivity, adpagerList));
			viewpager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					if (position >= adpagerList.size()) {
						position = 0;
					}
					if (position < 0) {
						position = adpagerList.size() - 1;
					}

					pointLLayout.getChildAt(position).setEnabled(true);
					pointLLayout.getChildAt(lastPosition).setEnabled(false);
					lastPosition = position;
				}

				@Override
				/**
				 * 页面正在滑动的时候，回调
				 */
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
				}

				@Override
				/**
				 * 当页面状态发生变化的时候，回调
				 */
				public void onPageScrollStateChanged(int state) {

				}
			});
		} else {
			Log.e("slx", "cn.com.unispark.Constants.islocation=false;");
		}
	}

	/**
	 * <pre>
	 * 功能说明： 硬件出场提示语，用来判断展示具体的跳转界面
	 * 日期：	2015年12月7日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void showIntentClass() {

		if (ParkApplication.mOrderState == 6) {
			ToolUtil.IntentClassLogin(mActivity, PayResultActivity.class, false);
		} else {
			ToolUtil.IntentClassLogin(mActivity, PayFeeActivity.class, false);
		}

		// int parkType = data.getOrdertype();
		// String exitnote = data.getExitnote();
		//
		// if (parkType == 2) {// 硬件
		// if (!TextUtils.isEmpty(exitnote)) {
		// ToolUtil.IntentClass(activity, PayResultActivity.class, false);
		// }else {
		// ToolUtil.IntentClass(activity, PayFeeActivity.class, false);
		// }
		// } else {// 软件
		// ToolUtil.IntentClass(activity, PayFeeActivity.class, false);
		// }

	}

	/**
	 * <pre>
	 * 功能说明：展示解析结果
	 * 日期：	2015年11月4日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param orderType 账单类型
	 * @param result 解析结果
	 * </pre>
	 */
	// private void showParseResult(int orderType) {
	// switch (orderType) {
	// case F_SHOW_ORDER:
	//
	// // 展示账单布局
	// showOrderState(false, true, false);
	//
	// // 状态展示的字体颜色
	// state_ll.setVisibility(View.VISIBLE);
	// state_tv.setTextColor(getResources().getColor(R.color.orange_font));
	// /*
	// * 展示账单信息
	// */
	//
	// // 停车场名称
	// int length = 0;
	// if (data.getParkname() != null) {
	// length = data.getParkname().length();
	// }
	// if (length <= 10) {
	// name_tv.setText(data.getParkname().substring(0, length));
	// } else {
	// name_tv.setText(data.getParkname().substring(0, 10) + "...");
	// }
	//
	// // 车牌号
	// car_plate_tv.setText(data.getCarno().equals("") ? "未绑定车牌号" : data
	// .getCarno());
	//
	// // 停车时长
	// long_tv.setText(data.getParklength());
	//
	// /**
	// * <pre>
	// * 1> 停车费用(判断包月用户类型前先判断停车场类型)
	// * 2> 停车场类型? 1:软件停车场 2：硬件停车场(包月用户)
	// * 3> 用户类型? 0：日卡 1：月卡 2：非日卡月卡
	// * </pre>
	// */
	// int parkType = data.getOrdertype();
	// int isCardMonth = data.getIscardmonth();
	//
	// String cardnoteStr = data.getCardnote();
	// if (parkType == 1) {
	// // 软件停车场
	// park_fee_tv
	// .setText(Float.toString(data.getShparkfee()) == null ? "0.00"
	// : ReckonUtil.getMoneyFormat(data.getShparkfee()));
	// } else if (parkType == 2) {
	// // 硬件停车场
	// if (isCardMonth != 2) {
	// // 包月用户
	// park_fee_tv.setText("0.00");
	// // 包月或计次过期提醒
	// if (!TextUtils.isEmpty(cardnoteStr)) {
	// final DialogUtil dialog = new DialogUtil(mActivity);
	// dialog.setMessage(cardnoteStr);
	// dialog.setPositiveButton("确定", new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// }
	// });
	// }
	// } else {
	// // 非包月用户
	// park_fee_tv
	// .setText(Float.toString(data.getShparkfee()) == null ? "0.00"
	// : ReckonUtil.getMoneyFormat(data
	// .getShparkfee()));
	// }
	// }
	//
	// break;
	// case F_NO_ORDER:
	//
	// // 展示无订单布局
	// showOrderState(true, false, false);
	//
	// // 状态展示的字体颜色,无订单时禁用点击事件
	// // order_state_tv.setTextColor(getResources().getColor(
	// // R.color.gray_font));
	// state_ll.setVisibility(View.GONE);
	//
	// break;
	// case F_PAY_SUCCESS:
	//
	// // 展示支付成功布局
	// showOrderState(false, false, true);
	//
	// // 状态展示的字体颜色
	// state_ll.setVisibility(View.VISIBLE);
	// state_tv.setTextColor(getResources().getColor(R.color.orange_font));
	//
	// // 动态将出场时间改为红色放大效果
	// if (!TextUtils.isEmpty(exitnote)) {
	// SpannableStringBuilder style = new SpannableStringBuilder(
	// exitnote);
	// style.setSpan(
	// new ForegroundColorSpan(Color.parseColor("#ec4f39")),
	// 6, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	// style.setSpan(
	// new AbsoluteSizeSpan(ViewUtil.getWidth(24), false), 6,
	// 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	//
	// leave_time_tv.setText(style);
	// }
	//
	// break;
	// }
	// }

	/**
	 * <pre>
	 * 功能说明：【解析】获取附近停车场的个数
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void parseGetNearParkCount() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("latitude", Double.toString(ParkApplication.mLat));
		params.put("longitude", Double.toString(ParkApplication.mLon));

		
		LogUtil.showLog(3, "【获取附近停车场的个数URL】" + Constant.PARK_LIST_COUNT_URL
				+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.PARK_LIST_COUNT_URL,
				ParkCountEntity.class, params,
				new HttpUtil.onResult<ParkCountEntity>() {

					@Override
					public void onSuccess(ParkCountEntity result) {
						int count = result.getData().getCount();
						count_tv.setText("附近有" + count + "个停车场");
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】活动专区列表数据轮播图
	 * 日期：	2015年10月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void parseAdViewPager() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.d("【活动专区列表数据URL】" + Constant.ACTIVITY_LIST_URL, params);

		httpUtil.parse(httpUtil.POST, Constant.ACTIVITY_LIST_URL,
				AdPagerEntity.class, params,
				new HttpUtil.onResult<AdPagerEntity>() {

					@Override
					public void onSuccess(AdPagerEntity result) {
						adpagerList = result.getData().getList();
						showViewPagerPoint();
					}

					@Override
					public void onFailed(int errCode, String errMsg) {

					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：获取账单,判断是否有未支付账单
	 * 日期：	2015年6月11日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param uid
	 * </pre>
	 */
	private void parseGetOrderInfo() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());

		LogUtil.d("【首页获取账单URL】" + Constant.GET_CURRENTLIST_URL, params);
		System.out.println("【首页获取账单URL】" + Constant.GET_CURRENTLIST_URL+ LogUtil.buildUrlParams(params));

		httpUtil.parse(httpUtil.POST, Constant.GET_CURRENTLIST_URL,
				OrderListEntity.class, params, new onResult<OrderListEntity>() {

					@Override
					public void onSuccess(OrderListEntity result) {
						sv_content.onRefreshComplete();

						orderList = result.getData().getList();
						orderCount = result.getData().getCount();

						if (orderCount == 0) {
							order_no_iv.setVisibility(View.VISIBLE);
							order_viewPager.setVisibility(View.GONE);
						} else {
							order_no_iv.setVisibility(View.GONE);
							order_viewPager.setVisibility(View.VISIBLE);
							showOrderViewPager();
						}

						// ParkApplication.mOrderState = result.getData()
						// .getMsgcode();
						//
						// data = result.getData().getInfo();
						//
						// exitnote = "提示：" + data.getExitnote();
						//
						// switch (ParkApplication.mOrderState) {
						// case 1:
						// LogUtil.e("【1 :软件交费，拉取账单成功[待支付]】");
						// state_tv.setText(R.string.dai_zhi_fu);
						// // orderState = F_SHOW_ORDER;
						// showParseResult(F_SHOW_ORDER);
						// break;
						// case 2:
						// LogUtil.e("【2: 软件交费， 账单仅查看不能交费页面[停车中]】");
						// state_tv.setText(R.string.ting_che_zhong);
						// // orderState = F_SHOW_ORDER;
						// showParseResult(F_SHOW_ORDER);
						// break;
						// case 3:
						// LogUtil.e("【3: 未拉取到账单[无订单]】");
						// // order_state_tv.setText(R.string.wu_ding_dan);
						// // orderState = F_NO_ORDER;
						// showParseResult(F_NO_ORDER);
						// break;
						// case 4:
						// LogUtil.e("【4: 硬件交费，拉取账单成功[停车中]】");
						// state_tv.setText(R.string.ting_che_zhong);
						// // orderState = F_SHOW_ORDER;
						// showParseResult(F_SHOW_ORDER);
						// break;
						// case 5:
						// LogUtil.e("【5: 硬件交费，硬件费用为0，未支付过[停车中]】");
						// state_tv.setText(R.string.ting_che_zhong);
						// // orderState = F_SHOW_ORDER;
						// showParseResult(F_SHOW_ORDER);
						// break;
						// case 6:
						// LogUtil.e("【6: 硬件交费，硬件支付过，15分钟内[待出场]】");
						// // orderState = F_PAY_SUCCESS;
						// state_tv.setText(R.string.dai_chu_chang);
						// showParseResult(F_PAY_SUCCESS);
						// break;
						// }

					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						sv_content.onRefreshComplete();
						ToastUtil.showToast(errMsg);
					}
				});

	}

	public static Animation getAlphaAnimation(float fromAlpha, float toAlpha,
			long durationMillis) {
		AlphaAnimation alpha = new AlphaAnimation(fromAlpha, toAlpha);
		alpha.setDuration(durationMillis);
		alpha.setFillAfter(true);
		return alpha;
	}

	public static Animation getTranslateAnimation(float fromXDelta,
			float toXDelta, float fromYDelta, float toYDelta,
			long durationMillis) {
		TranslateAnimation translate = new TranslateAnimation(fromXDelta,
				toXDelta, fromYDelta, toYDelta);
		translate.setDuration(durationMillis);
		translate.setFillAfter(true);
		return translate;
	}

	/**
	 * <pre>
	 * 功能说明： 网络连接改变的监听
	 * 日期：	2015年6月11日
	 * 开发者：	陈丶泳佐
	 * 版本信息：V4.3.0
	 * 版权声明：版权所有@北京百会易泊科技有限公司
	 * 
	 * 历史记录
	 *    修改内容：
	 *    修改人员：
	 *    修改日期： 2015年6月11日
	 * </pre>
	 */
	class NetConnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				ToastUtil.showToastNetError();
			} else {
				ToastUtil.showToast("网络状态良好");
				// ToolUtil.showLog(2, "【用户帐号】" +
				// ToolUtil.getSharedString("phone"));
				// parseUserInfo(getSharedString("phone"));
				// parseShowWebView();o
			}
		}
	}
	

	/**
	 * <pre>
	 * 功能说明：【解析】初始化用户信息
	 * 日期：	2015年8月21日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param phone
	 * </pre>
	 */
	public void parseInitUserInfo() {
		if(ParkApplication.getmUserInfo().getName() == null && ShareUtil.getSharedBoolean("islogin")){
			Map<String, String> params = new HashMap<String, String>();
			params.put("uid", ShareUtil.getSharedString("uid"));
			params.put("phone", ShareUtil.getSharedString("phone"));

			LogUtil.d("【初始化用户信息URL】" + Constant.USER_INFO_URL, params);

			httpUtil.parse(httpUtil.POST, Constant.USER_INFO_URL, UserEntity.class,
					params, new onResult<UserEntity>() {
						@Override
						public void onSuccess(UserEntity result) {
							LogUtil.d("【初始化用户信息URL】" + result.toString());
							ParkApplication.setmUserInfo(result.getData());
							parseAdViewPager();
							parseGetNearParkCount();
							showRecordOrLocation();
							parseGetOrderInfo();
						}

						@Override
						public void onFailed(int errCode, String errMsg) {
							ShareUtil.setSharedBoolean("islogin", false);
							ToastUtil.showToast(errMsg);
						}
					});
		}else{
			parseAdViewPager();
			parseGetNearParkCount();
			showRecordOrLocation();
			parseGetOrderInfo();
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		httpUtil.close();
	}

}
