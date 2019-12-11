package cn.com.unispark.fragment.mine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.autolayout.AutoLayout;
import cn.com.unispark.fragment.mine.conversation.ConversationActivity;
import cn.com.unispark.fragment.mine.coupons.CouponsActivity;
import cn.com.unispark.fragment.mine.coupons.CouponsFragmentActivity;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity;
import cn.com.unispark.fragment.mine.msgpush.MsgActivity;
import cn.com.unispark.fragment.mine.parkrecord.ParkRecordActivity;
import cn.com.unispark.fragment.mine.personinfo.PersonInfoActivity;
import cn.com.unispark.fragment.mine.plate.PlateManagerActivity;
import cn.com.unispark.fragment.mine.recharge.RemainActivity;
import cn.com.unispark.fragment.mine.setting.SettingActivity;
import cn.com.unispark.fragment.mine.vipcard.UserQrActivity;
import cn.com.unispark.fragment.mine.wallet.WalletActivity;
import cn.com.unispark.fragment.treasure.lease.LeaseMyActivity;
import cn.com.unispark.fragment.unknown.db.MsgDBOpenHelper;
import cn.com.unispark.login.LoginActivity;
import cn.com.unispark.login.entity.UserEntity;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;

import com.jauker.widget.BadgeView;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 【个人中心】界面
 * 日期：	2014年11月24日
 * 开发者：	陈丶泳佐
 * 历史记录
 *    修改内容：限行模块和日期模块添加
 *    修改人员：rjf
 *    修改日期： 2015年1月28日
 * </pre>
 */
public class MineFragment extends Fragment implements OnClickListener {

	private View view;

	// 导航栏标题// 左侧消息按钮//右侧设置按钮
	private TextView titleText;
	private ImageView backImgView, moreImgView;
	private LinearLayout backLLayout, moreLLayout;

	// 优惠券的数量
	private TextView couponsCountText;
	// 头部
	private TextView nameText, phoneText;// 用户姓名和手机号

	// 主体选项卡（文字）
	private TextView moneyText;// 当前余额显示

	// 主体选项卡（按钮）
	private TextView parkRecordText;// 代金券、停车记录

	private String sex = "";
	private String SDCardRoot;
	private MsgDBOpenHelper dbHelper;
	private RelativeLayout myWalletRelativeLayout;
	private TextView myLeaseTextView;
	private RelativeLayout feedbackRelativeLayout;
	/*private Button qrcodeBtn;*/
	private RelativeLayout remainRLayout, couponsRLayout;
	
	private LinearLayout personinfo_ll;
	private TextView plateManagerText;

	private boolean isred_flag;
	private Button loginBtn;
	SharedPreferences sp;
	private ImageView navImageView;
	protected int ouponsCount;
	protected int couponsCount;

	private Context context;
	private Activity activity;

	private BadgeView mBadgeView;

	private ImageView iv_red;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		activity = getActivity();

		view = View.inflate(context, R.layout.mine_main, null);

		AutoLayout.getInstance().auto(activity, true);

		initView();
		initDB();
		return view;
	}

	public void initView() {

		// 标题栏
		titleText = (TextView) view.findViewById(R.id.titleText);
		titleText.setText("我的");

		// 导航栏左侧 消息图标
		backImgView = (ImageView) view.findViewById(R.id.backImgView);
		backImgView.setImageResource(R.drawable.btn_mine_message);
		backLLayout = (LinearLayout) view.findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 导航栏右侧 设置图标
		moreImgView = (ImageView) view.findViewById(R.id.moreImgView);
		moreImgView.setVisibility(View.VISIBLE);
		moreImgView.setImageResource(R.drawable.btn_mine_setting);
		moreLLayout = (LinearLayout) view.findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);

		// 个人资料布局
		personinfo_ll = (LinearLayout) view.findViewById(R.id.personinfo_ll);
		personinfo_ll.setOnClickListener(this);

		// 用户姓名和手机号
		nameText = (TextView) view.findViewById(R.id.nameText);
		phoneText = (TextView) view.findViewById(R.id.phoneText);

		// 我的二维码(头部快捷点击)
		/*qrcodeBtn = (Button) view.findViewById(R.id.btn_qrcode);
		qrcodeBtn.setOnClickListener(this);*/

		// 我的钱包
/*		myWalletRelativeLayout = (RelativeLayout) view
				.findViewById(R.id.rl_my_wallet);
		myWalletRelativeLayout.setOnClickListener(this);*/

		// 我的停车位
		myLeaseTextView = (TextView) view.findViewById(R.id.tv_lease_mine);
		myLeaseTextView.setOnClickListener(this);

		// 账户余额显示
		moneyText = (TextView) view.findViewById(R.id.moneyText);
		couponsCountText = (TextView) view.findViewById(R.id.count_tv);

		// 停车记录
		parkRecordText = (TextView) view.findViewById(R.id.parkRecordText);
		parkRecordText.setOnClickListener(this);
		// 优惠券红点
		iv_red = (ImageView) view.findViewById(R.id.iv_red);
		iv_red.setVisibility(View.GONE);

		// 车牌管理
		plateManagerText = (TextView) view.findViewById(R.id.plateManagerText);
		plateManagerText.setOnClickListener(this);
		feedbackRelativeLayout = (RelativeLayout) view
				.findViewById(R.id.feedback_rl);
		feedbackRelativeLayout.setOnClickListener(this);
		remainRLayout = (RelativeLayout) view.findViewById(R.id.remain_ll);
		remainRLayout.setOnClickListener(this);
		// 优惠券布局
		couponsRLayout = (RelativeLayout) view.findViewById(R.id.coupons_ll);
		couponsRLayout.setOnClickListener(this);
		loginBtn = (Button) view.findViewById(R.id.btn_login);
		navImageView = (ImageView) view.findViewById(R.id.iv_nav);
		loginBtn.setOnClickListener(this);
		sp = context.getSharedPreferences("51Park", Context.MODE_PRIVATE);
		// 优惠券红点显示
		mBadgeView = new BadgeView(context);
		// mBadgeView.setTargetView(couponsCountText);
		mBadgeView.setTargetView(couponsRLayout);
		mBadgeView.setTextSize(12);
		mBadgeView.setBadgeCount(0);
		mBadgeView.setBadgeMargin(-18, 10, 18, 0);
		mBadgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
		mBadgeView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:// 消息
			ParkApplication.isRed = "";
			ToolUtil.IntentClass(activity, MsgActivity.class, false);
			MobclickAgent.onEvent(context, "my_announcement_click");
			break;
		case R.id.moreLLayout:// 设置
			ToolUtil.IntentClass(activity, SettingActivity.class, false);
			MobclickAgent.onEvent(context, "my_settings_click");
			break;
		case R.id.btn_login:// 登录
			ToolUtil.IntentClass(activity, LoginActivity.class, false);
			MobclickAgent.onEvent(context, "my_login_click");
			break;
		case R.id.personinfo_ll:// 个人资料
			ToolUtil.IntentClassLogin(activity, PersonInfoActivity.class, false);
			MobclickAgent.onEvent(context, "my_drawerData_click");
			break;
		/*case R.id.btn_qrcode:// 个人二维码
			ToolUtil.IntentClassLogin(activity, UserQrActivity.class, false);
			MobclickAgent.onEvent(context, "my_QRcode_click");
			break;*/
		/*case R.id.rl_my_wallet:// 我的钱包
			ToolUtil.IntentClassLogin(activity, WalletActivity.class,
					"couponsCount", couponsCount + "", false);
			MobclickAgent.onEvent(context, "my_myWallet_click");
			break;*/
		case R.id.remain_ll:// 账户余额
			ToolUtil.IntentClassLogin(activity, RemainActivity.class, false);
			MobclickAgent.onEvent(context, "my_BalanceOfAccount_click");
			break;
		case R.id.coupons_ll:// 我的停车券
			ShareUtil.setSharedBoolean("couponRed", false);
			iv_red.setVisibility(View.GONE);
			ToolUtil.IntentClassLogin(activity, CouponsActivity.class, false);
			MobclickAgent.onEvent(context, "my_parkTicket_click");
			break;
		case R.id.plateManagerText:// 车牌管理
			ToolUtil.IntentClassLogin(activity, PlateManagerActivity.class,
					false);
			break;
		case R.id.parkRecordText:// 停车记录
			ToolUtil.IntentClassLogin(activity, ParkRecordActivity.class, "historyOrder","yes",false);
			MobclickAgent.onEvent(context, "my_parkRecord_click");
			break;
		case R.id.tv_lease_mine:// 我的停车位
			ToolUtil.IntentClassLogin(activity, LeaseMyActivity.class, false);
			MobclickAgent.onEvent(context, "my_parkingSpace_click");
			break;
		case R.id.feedback_rl:// 意见反馈
			ToolUtil.IntentClass(activity, ConversationActivity.class, false);
			MobclickAgent.onEvent(context, "my_feedback_click");
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// loadUserInfo();
		initData();
	}

	private void initData() {

		isred_flag = sp.getBoolean("islogin", false);
		if (isred_flag) {// 已登录
			String remain = ParkApplication.getmUserInfo().getUserscore();
			if (!TextUtils.isEmpty(remain)) {
				moneyText.setText(remain);
			}
			isred_flag = sp.getBoolean("islogin", true);
			String nameStr = ParkApplication.getmUserInfo().getName();
			if (!TextUtils.isEmpty(nameStr)) {
				switch (ParkApplication.getmUserInfo().getSex()) {
				case 1:
					sex = "(先生)";
					break;
				case 2:
					sex = "(女士)";
					break;
				}
				nameText.setText(nameStr + sex);
			}
			String userName = ParkApplication.getmUserInfo().getUsername();
			if (!TextUtils.isEmpty(userName)) {
				String userNameStr = userName.substring(0, 3) + "****"
						+ userName.substring(7, 11);
				phoneText.setText(userNameStr);
			}
			// String cardNo = ParkApplication.getmUserInfo().getCardno();
			// if (!TextUtils.isEmpty(cardNo)) {
			// tv_chepai.setText(cardNo);
			// } else {
			// tv_chepai.setText("未绑定车牌号");
			// }
			personinfo_ll.setVisibility(View.VISIBLE);
			loginBtn.setVisibility(View.GONE);
			/*qrcodeBtn.setVisibility(View.VISIBLE);*/
			navImageView.setVisibility(View.VISIBLE);
			// 消息处理
			if (ParkApplication.isRed.length() > 0) {
				// redIcon.setVisibility(View.VISIBLE);
			} else {
				// redIcon.setVisibility(View.INVISIBLE);
			}
			parseGetCouponsCount();
		} else {// 未登录
			moneyText.setText("0.00");
			couponsCountText.setText("0");
			personinfo_ll.setVisibility(View.GONE);
			loginBtn.setVisibility(View.VISIBLE);
			/*qrcodeBtn.setVisibility(View.GONE);*/
			navImageView.setVisibility(View.GONE);
		}
	}

	private void initDB() {
		SDCardRoot = Environment.getExternalStorageDirectory().toString()
				+ "/51Park/maps/";
		File file = new File(SDCardRoot);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		dbHelper = new MsgDBOpenHelper(context, SDCardRoot
				+ Constant.DB_MSG_NAME, null, 1);
	}

	/**
	 * <pre>
	 * 功能说明：初始化用户信息
	 * @param phone
	 * </pre>
	 */
	public void loadUserInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("phone", ParkApplication.getmUserInfo().getUsername());

		HttpUtil httpUtil = new HttpUtil(context);
		httpUtil.parse(httpUtil.POST, Constant.USER_INFO_URL, UserEntity.class,
				params, new onResult<UserEntity>() {
					@Override
					public void onSuccess(UserEntity result) {
						ParkApplication.getmUserInfo().setUserscore(
								result.getData().getUserscore());
						// String remainStr = result.getData().getUserscore();
						// if (!TextUtils.isEmpty(remainStr)) {
						// moneyText.setText(remainStr);
						// } else {
						// moneyText.setText("0");
						// }
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】获取优惠券的条数
	 * 日期：	2015年11月5日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void parseGetCouponsCount() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("perpage", "8");
		params.put("type", "1");
		params.put("timeout", "0");
		params.put("used", "0");
		LogUtil.showLog(
				3,
				"【获取优惠券的条数URL】" + Constant.GET_POUPONS_URL
						+ LogUtil.buildUrlParams(params));

		HttpUtil httpUtil = new HttpUtil(context);
		httpUtil.parse(httpUtil.POST, Constant.GET_POUPONS_URL,
				CouponsEntity.class, params, new onResult<CouponsEntity>() {
					@Override
					public void onSuccess(CouponsEntity result) {
						couponsCount = result.getData().getCount();
						int tempCount = ShareUtil
								.getSharedInteger("couponsCount");
						boolean couponRed = ShareUtil
								.getSharedBoolean("couponRed");
						if (tempCount != 0) {
							if (couponsCount - tempCount > 0 || couponRed) {
								ShareUtil.setSharedBoolean("couponRed", true);
								ShareUtil.setSharedInteger("couponsCount",
										couponsCount);
								iv_red.setVisibility(View.VISIBLE);
							} else {
								iv_red.setVisibility(View.GONE);
							}
						}
						couponsCountText.setText(String.valueOf(couponsCount));
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.showToast(errMsg);
					}
				});
	}

}
