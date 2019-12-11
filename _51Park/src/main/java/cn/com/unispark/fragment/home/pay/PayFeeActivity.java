package cn.com.unispark.fragment.home.pay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.uubee.prepay.api.PrepayAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.entity.ParkInfoEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInfoEntity.DataObject.PriceInfo;
import cn.com.unispark.fragment.home.pay.alipay.AliPay;
import cn.com.unispark.fragment.home.pay.entity.OrderEntity;
import cn.com.unispark.fragment.home.pay.entity.UubeeRiskEntity;
import cn.com.unispark.fragment.home.pay.uubeepay.UubeePay;
import cn.com.unispark.fragment.home.pay.wechatpay.Wxpay;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity;
import cn.com.unispark.fragment.mine.coupons.entity.CouponsEntity.DataObject.CouponsInfo;
import cn.com.unispark.fragment.mine.creditcard.CreditPayOneActivity;
import cn.com.unispark.fragment.mine.creditcard.entity.UserCreditCardEntity;
import cn.com.unispark.fragment.mine.setting.SettingActivity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 有未支付订单，点击跳转到【交停车费】界面
 * 日期：	2015年3月19日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 *
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月17日
 * </pre>
 */
public class PayFeeActivity extends BaseActivity {

    // 导航栏标题和返回按钮
    private TextView titleText;
    private LinearLayout backLLayout;

    // 小车动画只执行一次，判断是否为第一次执行
    private boolean isFirstIn = true;

    // 停车费用动画效果
    private ImageView image;

/*	// 包月提示语// 包月图标
    private TextView month_user_tv;
	private ImageView month_user_iv;

	// 停车费用 //停车时间：分，秒
	private TextView park_fee1_tv, parkTimeHourText, parkTimeMinuteText;*/

    // 停车场名称 //车牌号
    private TextView name_tv, plate_tv;
    //	停车时长
    private TextView parkLongText;

    // 已交停车费用// 补交停车费用 //本地优惠//停车券//余额支付//应付金额
    private RelativeLayout coupons_rl, discount_rl;
    private TextView prepayText, parkFee2Text, discount_tv, couponsText,
            remainPayText, shouldPayText;
    private float mParkfeePay, mCouponsPay, mRemainPay, mShouldPay = 0;

    // 停车券提示语
    private String couponsPrompt;

    // 选择支付方式//微信//支付宝//有贝//信用卡
    private View payway_ic;
    // private RadioGroup radioGroup;
    private RadioButton wechat_rbtn, alipay_rbtn;
//	uubee_rbtn, credit_rbtn;

    // 小车在场状态
    private TextView textView_start, textView_middle, textView_end;
    private ImageView imageView_startPoint, imageView_middlePoint,
            imageView_endPoint;
    private ImageView imageView_startline, imageView_endline;
    private TextView textView_time;
    private ImageView imageView_car;
    private RelativeLayout relayout_text, relayout_car;

    private RelativeLayout relayout_time; // 提示语动画布局

    // 确认支付按钮
    private Button sureBtn;

/*	// 联系客服
	private TextView contact_tv;

	// 自动支付
	private TextView auto_pay_tv;
	private LinearLayout auto_pay_ll;*/

    // 停车券Id号(停车券列表接口赋值)
    private String mCouponsId;
    // 账户余额
    private float mUserRemain;

    // 微信支付
    private Wxpay wxpay;
    // 支付宝支付
    private AliPay aliPay;
    // 有贝
    private UubeePay uubeepay;
    // 停车券
    private CouponsInfo couponsInfo;
    // 有贝先付的风控参数
    private String uubeeRiskParam;

    private PullToRefreshScrollView scrollview;

    // 硬件出场提示语
    private String exitnote;
    // private int parktype;

    // 微信支付完成后需要关闭交停车费页面
    public static Activity activity;
    private TranslateAnimation upTransAnim, downTransAnim;

    // 收费详情的对话框
    private Dialog priceDialog;
    private Button close_btn;
    private List<PriceInfo> todayList;
    private List<PriceInfo> nightList;

    private String mOrderNum;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    relayout_time.startAnimation(downTransAnim);
                    break;
                case 1:
                    showPromptAnimation();
                    break;
            }

            return false;
        }
    });

    @Override
    public void setContentLayout() {
        setContentView(R.layout.payfee_main);

        activity = this;

        // 初始化支付方式
        wxpay = new Wxpay(this);
        aliPay = new AliPay(this);
        uubeepay = new UubeePay();

        parseGetFirstCoupons();

    }

    @Override
    public void initView() {

        // 导航栏标题
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("交停车费");

        // 返回按钮
        backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
        backLLayout.setOnClickListener(this);

        // 上拉加载下拉刷新
        scrollview = (PullToRefreshScrollView) findViewById(R.id.scrollview);

        // 上拉监听函数
        scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 执行刷新函数
                isFirstIn = false;
                parseGetOrderInfo();
            }
        });

        // 停车费用动画效果
        image = (ImageView) findViewById(R.id.image);
		/*showParkfeeAnimation();*/

        relayout_time = (RelativeLayout) findViewById(R.id.relayout_time);
        ViewUtil.setMarginTop(relayout_time, 32, ViewUtil.RELATIVELAYOUT);
	
		/*	// 包月提示语// 包月图标
		month_user_tv = (TextView) findViewById(R.id.month_user_tv);
		month_user_tv.setOnClickListener(this);
		month_user_iv = (ImageView) findViewById(R.id.month_user_iv);

		// 停车费用 //停车时间：时，分
		park_fee1_tv = (TextView) findViewById(R.id.park_fee1_tv);
		ViewUtil.setTextSize(park_fee1_tv, 64);

		parkTimeHourText = (TextView) findViewById(R.id.park_time_hour_tv);
		ViewUtil.setTextSize(parkTimeHourText, 28);

		parkTimeMinuteText = (TextView) findViewById(R.id.park_time_minute_tv);
		ViewUtil.setTextSize(parkTimeMinuteText, 28);*/

		/*
		 * 停车账单布局
		 */
        View payfee_bill_ic = findViewById(R.id.payfee_bill_ic);
        ViewUtil.setViewSize(payfee_bill_ic, 0, 610);
        ViewUtil.setMargin(payfee_bill_ic, 24, 20, 0, 20, ViewUtil.LINEARLAYOUT);

        // 呼吸灯文字（停车费用）
		/*TextView test_tv = (TextView) findViewById(R.id.test_tv);
		ViewUtil.setTextSize(test_tv, 24);*/

		/*
		 * 文字（停车费用）
		 */
        TextView test0_tv = (TextView) findViewById(R.id.test0_tv);
        ViewUtil.setTextSize(test0_tv, 30);
        ViewUtil.setMarginLeft(test0_tv, 30, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginBottom(test0_tv, 30, ViewUtil.RELATIVELAYOUT);

        // 补交费用
        parkFee2Text = (TextView) findViewById(R.id.park_fee2_tv);
        ViewUtil.setTextSize(parkFee2Text, 30);
        ViewUtil.setMarginRight(parkFee2Text, 30, ViewUtil.RELATIVELAYOUT);

        // 已交费用
        prepayText = (TextView) findViewById(R.id.park_prepay_tv);
        ViewUtil.setTextSize(prepayText, 22);
        ViewUtil.setMarginTop(prepayText, 5, ViewUtil.RELATIVELAYOUT);

		/*
		 * 文字（本地优惠）
		 */
        TextView local_tv = (TextView) findViewById(R.id.local_tv);
        ViewUtil.setTextSize(local_tv, 30);
        ViewUtil.setMargin(local_tv, 30, ViewUtil.RELATIVELAYOUT);

        discount_tv = (TextView) findViewById(R.id.discount_tv);
        ViewUtil.setTextSize(discount_tv, 30);
        ViewUtil.setMarginRight(discount_tv, 30, ViewUtil.RELATIVELAYOUT);

        discount_rl = (RelativeLayout) findViewById(R.id.discount_rl);

		/*
		 * 文字(停车券)
		 */
        TextView test1_tv = (TextView) findViewById(R.id.test1_tv);
        ViewUtil.setTextSize(test1_tv, 30);

        couponsText = (TextView) findViewById(R.id.coupons_tv);
        ViewUtil.setTextSize(couponsText, 24);

        coupons_rl = (RelativeLayout) findViewById(R.id.coupons_rl);
        coupons_rl.setOnClickListener(this);
        ViewUtil.setMargin(coupons_rl, 30, ViewUtil.RELATIVELAYOUT);

		/*
		 * 文字（余额支付）
		 */
        TextView test2_tv = (TextView) findViewById(R.id.test2_tv);
        ViewUtil.setTextSize(test2_tv, 30);
        ViewUtil.setMargin(test2_tv, 30, ViewUtil.RELATIVELAYOUT);

        remainPayText = (TextView) findViewById(R.id.remain_pay_tv);
        ViewUtil.setTextSize(remainPayText, 30);
        ViewUtil.setMarginRight(remainPayText, 30, ViewUtil.RELATIVELAYOUT);

		/*
		 * 文字(应付金额)
		 */
        TextView test3_tv = (TextView) findViewById(R.id.test3_tv);
        ViewUtil.setTextSize(test3_tv, 30);

        shouldPayText = (TextView) findViewById(R.id.should_pay_tv);
        ViewUtil.setTextSize(shouldPayText, 36);
        ViewUtil.setMargin(shouldPayText, 38, 30, 0, 0, ViewUtil.RELATIVELAYOUT);

		/*
		 * 选择支付方式布局
		 */
        payway_ic = findViewById(R.id.payway_ic);
        ViewUtil.setMarginRight(payway_ic, 30, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginLeft(payway_ic, 30, ViewUtil.RELATIVELAYOUT);

        // 停车场名称(停车场收费详情)
        name_tv = (TextView) findViewById(R.id.name_tv);
		/*name_tv.setOnClickListener(this);*/
        ViewUtil.setTextSize(name_tv, 30);
		/*ViewUtil.setDrawablePadding(name_tv, 20);*/
        ViewUtil.setMarginTop(name_tv, 20, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginLeft(name_tv, 25, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginBottom(name_tv, 15, ViewUtil.RELATIVELAYOUT);

        // 车牌号
        plate_tv = (TextView) findViewById(R.id.plate_tv);
        ViewUtil.setTextSize(plate_tv, 30);
        ViewUtil.setMarginLeft(plate_tv, 25, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginBottom(plate_tv, 15, ViewUtil.RELATIVELAYOUT);

//		停车时长
        parkLongText = (TextView) findViewById(R.id.tv_park_long);
        ViewUtil.setTextSize(parkLongText, 30);
        ViewUtil.setMarginLeft(parkLongText, 25, ViewUtil.RELATIVELAYOUT);
		
		/*
		 * 请选择支付方式（文字）
		 */
        TextView choice_payway_tv = (TextView) findViewById(R.id.choice_payway_tv);
        ViewUtil.setTextSize(choice_payway_tv, 30);
        ViewUtil.setMarginTop(choice_payway_tv, 22, ViewUtil.LINEARLAYOUT);

        // 选择支付方式文字下方的线条
        View line_w_view = findViewById(R.id.line_w_view);
        ViewUtil.setMarginBottom(line_w_view, 10, ViewUtil.LINEARLAYOUT);
        ViewUtil.setMarginTop(line_w_view, 10, ViewUtil.LINEARLAYOUT);

        // 微信
        wechat_rbtn = (RadioButton) findViewById(R.id.wechat_rbtn);
        wechat_rbtn.setOnClickListener(this);
        ViewUtil.setTextSize(wechat_rbtn, 30);
        ViewUtil.setViewSize(wechat_rbtn, 88, 0);

        // 支付宝
        alipay_rbtn = (RadioButton) findViewById(R.id.alipay_rbtn);
        alipay_rbtn.setOnClickListener(this);
        ViewUtil.setTextSize(alipay_rbtn, 30);
        ViewUtil.setViewSize(alipay_rbtn, 88, 0);

/*		// 有贝
		uubee_rbtn = (RadioButton) findViewById(R.id.uubee_rbtn);
		uubee_rbtn.setVisibility(View.VISIBLE);
		uubee_rbtn.setOnClickListener(this);
		ViewUtil.setTextSize(uubee_rbtn, 30);
		ViewUtil.setViewSize(uubee_rbtn, 88, 0);

		// 信用卡
		credit_rbtn = (RadioButton) findViewById(R.id.credit_rbtn);
		credit_rbtn.setVisibility(View.VISIBLE);
		credit_rbtn.setOnClickListener(this);
		ViewUtil.setTextSize(credit_rbtn, 30);
		ViewUtil.setViewSize(credit_rbtn, 88, 0);

		// 有贝支付、信用卡支付下方的线条
		findViewById(R.id.credit_line).setVisibility(View.VISIBLE);
		findViewById(R.id.uubee_line).setVisibility(View.VISIBLE);*/

		/*
		 * 确认支付按钮
		 */
        sureBtn = (Button) findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(this);
        ViewUtil.setMarginTop(sureBtn, 30, ViewUtil.RELATIVELAYOUT);

        ViewUtil.setMarginTop(findViewById(R.id.payfee_car_ic), 30,
                ViewUtil.LINEARLAYOUT);
        ViewUtil.setMarginBottom(findViewById(R.id.payfee_car_ic), 30,
                ViewUtil.LINEARLAYOUT);

/*		// 联系客服
		contact_tv = (TextView) findViewById(R.id.contact_tv);
		contact_tv.setOnClickListener(this);
		ViewUtil.setTextSize(contact_tv, 24);
		ViewUtil.setDrawablePadding(contact_tv, 18);
		ViewUtil.setMargin(contact_tv, 35, 0, 30, 24, ViewUtil.RELATIVELAYOUT);

		
		 * 判断是否开启自动支付功能,“去开启”颜色改为红色，加下划线
		 
		auto_pay_ll = (LinearLayout) findViewById(R.id.auto_pay_ll);
		auto_pay_ll.setOnClickListener(this);
		ViewUtil.setMarginTop(auto_pay_ll, 38, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setPaddingBottom(auto_pay_ll, 25);
		ViewUtil.setMarginRight(auto_pay_ll, 18, ViewUtil.RELATIVELAYOUT);

		// 去开启（文字），红色加下划线
		auto_pay_tv = (TextView) findViewById(R.id.auto_pay_tv);
		auto_pay_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		ViewUtil.setTextSize(auto_pay_tv, 24);*/

        // 自动支付，支付更快捷（文字）
		/*TextView auto_pay = (TextView) findViewById(R.id.auto_pay);
		ViewUtil.setTextSize(auto_pay, 24);
		ViewUtil.setDrawablePadding(auto_pay, 10);*/

        /**
         * 三个停车阶段的TextViw
         */
        relayout_text = (RelativeLayout) findViewById(R.id.relayout_text);
        ViewUtil.setMarginBottom(relayout_text, 15, ViewUtil.RELATIVELAYOUT);

        textView_start = (TextView) findViewById(R.id.textView_start);
        ViewUtil.setTextSize(textView_start, 20);
        ViewUtil.setMarginLeft(textView_start, 25, ViewUtil.RELATIVELAYOUT);

        textView_middle = (TextView) findViewById(R.id.textView_middle);
        ViewUtil.setTextSize(textView_middle, 20);

        textView_end = (TextView) findViewById(R.id.textView_end);
        ViewUtil.setTextSize(textView_end, 20);
        ViewUtil.setMarginRight(textView_end, 25, ViewUtil.RELATIVELAYOUT);

        /**
         * 三个圆点
         */
        imageView_startPoint = (ImageView) findViewById(R.id.imageView_startPoint);
        ViewUtil.setViewSize(imageView_startPoint, 22, 22);

        imageView_middlePoint = (ImageView) findViewById(R.id.imageView_middlePoint);
        ViewUtil.setViewSize(imageView_middlePoint, 22, 22);

        imageView_endPoint = (ImageView) findViewById(R.id.imageView_endPoint);
        ViewUtil.setViewSize(imageView_endPoint, 22, 22);

        /**
         * 俩条线
         */
        imageView_startline = (ImageView) findViewById(R.id.imageView_startline);
        ViewUtil.setViewSize(imageView_startline, 2, 250);

        imageView_endline = (ImageView) findViewById(R.id.imageView_endline);
        ViewUtil.setViewSize(imageView_endline, 2, 250);

        /**
         * 时间
         */
        textView_time = (TextView) findViewById(R.id.textView_time);
        ViewUtil.setTextSize(textView_time, 18);
        ViewUtil.setMarginLeft(textView_time, 25, ViewUtil.RELATIVELAYOUT);
        /**
         * 小汽车
         */

        relayout_car = (RelativeLayout) findViewById(R.id.relayout_car);
        ViewUtil.setMarginTop(relayout_car, 10, ViewUtil.RELATIVELAYOUT);

        imageView_car = (ImageView) findViewById(R.id.imageView_car);
        ViewUtil.setViewSize(imageView_car, 42, 90);

    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.backLLayout:
                finish();
                break;
		/*case R.id.name_tv:// 点停车场名车弹出收费详情
			parsePriceInfo();
			break;*/
            case R.id.close_btn: // 收费详情对话框的关闭按钮
                priceDialog.dismiss();
                MobclickAgent.onEvent(context, "chargeDetails_closeBtn_click");
                break;
            case R.id.coupons_rl:// 停车券
                Intent it = new Intent(activity, PayCouponsActivity.class);
                startActivityForResult(it, 20);
                break;
            case R.id.sure_btn:// 确认支付按钮
                // // 订单号
                // ParkApplication.mOrderNum = result.getData().getInfo()
                // .getOrderno();
                // LogUtil.showLog(2, "【订单号】" + ParkApplication.mOrderNum);
                onClickSurePayBtn();
                break;
            case R.id.contact_tv:// 联系客服
                ToolUtil.IntentPhone(activity, Constant.CONTACT_PHONE);
                MobclickAgent.onEvent(context, "parkingFee_CustomerService_click");
                break;
            case R.id.auto_pay_ll:// 自动支付按钮
                ToolUtil.IntentClass(activity, SettingActivity.class, "isShow",
                        false, false);
                break;
            case R.id.month_user_tv:// 包月用户提醒
                relayout_time.clearAnimation();
                relayout_time.startAnimation(downTransAnim);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        parseGetOrderInfo();
    }

    /**
     * <pre>
     * 功能说明：点击确认支付按钮
     * 日期：	2015年10月29日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void onClickSurePayBtn() {
        // 订单号
        ParkApplication.mOrderNum = mOrderNum;

        String payfee = String.valueOf(ReckonUtil.getMoneyFormat(mShouldPay))
                .toString();
        if (mShouldPay > 0) {
            if (alipay_rbtn.isChecked()) {
                ParkApplication.mPayParkType = 1;
                aliPay.pay(ParkApplication.mOrderNum, Constant.SUBJECT,
                        Constant.BODY_PAYFEE, payfee,
                        Constant.ALIPAY_NOTIFY_URL);
            } else if (wechat_rbtn.isChecked()) {
                ParkApplication.mPayParkType = 2;
                ParkApplication.mNotifyUrlPage = Constant.WEIXIN_NOTIFY_URL;
                wxpay.pay(ParkApplication.mOrderNum,
                        ReckonUtil.StringY2StringF(payfee),
                        1);
            }
		/*	else if (uubee_rbtn.isChecked()) {
				ParkApplication.mPayParkType = 3;
				parseGetUubeeRiskParams(payfee);
			} else if (credit_rbtn.isChecked()) {
				ParkApplication.mPayParkType = 4;
				parseQueryBindCard(payfee);
			}*/
            else {
                ParkApplication.mPayParkType = 5;
                parsePayByRemain(payfee);
            }
        } else if (mShouldPay == 0) {
            ParkApplication.mPayParkType = 6;
            parsePayByZero();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        switch (resultCode) {
            case 20:// 选择停车券
                if (intent != null) {
                    if (intent.getSerializableExtra("CouponsInfo") != null) {
                        couponsInfo = (CouponsInfo) intent
                                .getSerializableExtra("CouponsInfo");
                        parseGetOrderInfo();
                    }
                }
                break;
            case 30:// 未选择停车券
                couponsInfo = null;
                couponsPrompt = "未选择停车券";
                parseGetOrderInfo();
                break;
        }
    }

    /**
     * <pre>
     * 功能说明：展示收费详情的对话框
     * 日期：	2016年1月12日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void showPriceDialog(ParkInfoEntity result) {

        priceDialog = new Dialog(context);
        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setContentView(R.layout.park_info_price);

		/*
		 * 设置dialog样式
		 */
        Window window = priceDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        LayoutParams attributes = priceDialog.getWindow().getAttributes();
        attributes.width = ViewUtil.getWidth(640);
        // attributes.height = ViewUtil.getHeight(800);

        // 关闭按钮
        close_btn = (Button) priceDialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(this);
        ViewUtil.setViewSize(close_btn, 38, 38);
        ViewUtil.setMarginRight(close_btn, 20, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginTop(close_btn, 20, ViewUtil.RELATIVELAYOUT);

        // 标题
        TextView price_tv = (TextView) priceDialog.findViewById(R.id.price_tv);
        ViewUtil.setTextSize(price_tv, 36);
        ViewUtil.setViewSize(price_tv, 96, 0);

        // 虚线
        View line_view = priceDialog.findViewById(R.id.line_view);
        ViewUtil.setViewSize(line_view, 2, 600);
        ViewUtil.setMarginBottom(line_view, 20, ViewUtil.RELATIVELAYOUT);

        // 白天价格
        TextView today_price_tv = (TextView) priceDialog
                .findViewById(R.id.today_price_tv);
        ViewUtil.setViewSize(today_price_tv, 50, 580);
        ViewUtil.setTextSize(today_price_tv, 24);
        today_price_tv.setText(result.getData().getDpricedaytime());

        // 白天价格内容
        LinearLayout today_price_ll = (LinearLayout) priceDialog
                .findViewById(R.id.today_price_ll);
        ViewUtil.setViewSize(today_price_tv, 0, 580);
        ViewUtil.setMarginBottom(today_price_ll, 40, ViewUtil.RELATIVELAYOUT);

        // 夜间价格
        TextView night_price_tv = (TextView) priceDialog
                .findViewById(R.id.night_price_tv);
        ViewUtil.setViewSize(night_price_tv, 50, 580);
        ViewUtil.setTextSize(night_price_tv, 24);
        night_price_tv.setText(result.getData().getDpricenighttime());

        // 夜晚价格内容
        LinearLayout night_price_ll = (LinearLayout) priceDialog
                .findViewById(R.id.night_price_ll);
        ViewUtil.setViewSize(night_price_ll, 0, 580);
        ViewUtil.setMarginBottom(night_price_ll, 40, ViewUtil.RELATIVELAYOUT);

        /**
         * 白天价格所有item
         */
        for (int i = 0; i < todayList.size(); i++) {
            RelativeLayout price_rl = new RelativeLayout(context);

            price_rl.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewUtil.getWidth(580), ViewUtil.getHeight(50));
            price_rl.setLayoutParams(layoutParams);

            // 信息
            TextView info = new TextView(context);
            info.setTextColor(Color.parseColor("#313131"));
            info.setText(todayList.get(i).getInfo());
            ViewUtil.setTextSize(info, 24);
            ViewUtil.setMarginLeft(info, 43, ViewUtil.RELATIVELAYOUT);

            price_rl.addView(info);

            // 价格
            TextView price = new TextView(context);
            price.setTextColor(Color.parseColor("#313131"));
            price.setText(todayList.get(i).getPrice());
            ViewUtil.setTextSize(price, 24);
            ViewUtil.setMarginLeft(price, 460, ViewUtil.RELATIVELAYOUT);

            price_rl.addView(price);
            price_rl.setBackgroundResource(R.drawable.draw_view_border);

            today_price_ll.addView(price_rl);
        }

        /**
         * 晚上天价格所有item
         */
        for (int i = 0; i < nightList.size(); i++) {
            RelativeLayout price_rl = new RelativeLayout(context);

            price_rl.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewUtil.getWidth(580), ViewUtil.getHeight(50));
            price_rl.setLayoutParams(layoutParams);

            // 信息
            TextView info = new TextView(context);
            info.setTextColor(Color.parseColor("#313131"));
            info.setText(nightList.get(i).getInfo());
            ViewUtil.setTextSize(info, 24);
            ViewUtil.setMarginLeft(info, 43, ViewUtil.RELATIVELAYOUT);

            price_rl.addView(info);

            // 价格
            TextView price = new TextView(context);
            price.setTextColor(Color.parseColor("#313131"));
            price.setText(nightList.get(i).getPrice());
            ViewUtil.setTextSize(price, 24);
            ViewUtil.setMarginLeft(price, 460, ViewUtil.RELATIVELAYOUT);

            price_rl.addView(price);
            price_rl.setBackgroundResource(R.drawable.draw_view_border);

            night_price_ll.addView(price_rl);
        }

        priceDialog.show();
    }

    /**
     * <pre>
     * 功能说明：展示停车费用呼吸灯效果
     * 日期：	2015年10月27日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
/*	private void showParkfeeAnimation() {
		AnimationSet animationSet = new AnimationSet(true);
		AlphaAnimation alphaanimation = new AlphaAnimation(1, (float) 0.2);
		alphaanimation.setDuration(2000);
		alphaanimation.setRepeatCount(-1);
		alphaanimation.setRepeatMode(Animation.REVERSE);
		animationSet.addAnimation(alphaanimation);
		image.startAnimation(animationSet);
	}*/

    /**
     * <pre>
     * 功能说明： 硬件出场提示语，用来判断展示具体的跳转界面
     * 日期：	2015年12月7日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    // private void showIntentClass() {
    // if (!TextUtils.isEmpty(exitnote)) {
    // ToolUtil.IntentClass(activity, PayPromptActivity.class, false);
    // } else {
    // ToolUtil.IntentClass(activity, PayResultActivity.class,
    // "order_num", ParkApplication.mOrderNum, true);
    // }
    //

    // int parktype = data.getOrdertype();

    // if (parktype == 2) {// 硬件
    // if (!TextUtils.isEmpty(exitnote)) {
    // ToolUtil.IntentClass(activity, PayPromptActivity.class, false);
    // }
    // } else {// 软件
    // ToolUtil.IntentClass(activity, PayResultActivity.class,
    // "order_num", ParkApplication.mOrderNum, true);
    // }
    //
    // }

    /**
     * <pre>
     * 功能说明：展示计时或计费布局
     * 日期：	2015年11月3日
     * 开发者：	陈丶泳佐
     *
     * @param result
     * @param isPayfee  true:计费，false:计时
     * @param isParking  true:停车中，false:待支付
     * </pre>
     */
    private void showPayfeeResult(OrderEntity result, boolean isPayfee,
                                  boolean isParking) {

		/*
		 * 账单布局展示
		 */
        findViewById(R.id.scrollview).setVisibility(View.VISIBLE);
        findViewById(R.id.payfee_car_ic).setVisibility(View.VISIBLE);

		/*
		 * 停车费用
		 */
		/*park_fee1_tv.setText(Float.toString(result.getData().getInfo()
				.getShparkfee()) == null ? "0.00" : ReckonUtil
				.getMoneyFormat(result.getData().getInfo().getShparkfee()));*/

		/*
		 * 停车时间：时，分
		 */
		/*String time = result.getData().getInfo().getParklength();
		String hour = "000";
		String minute = "00";
		if (time != null) {
			if (time.contains("时")) {
				hour = time.split("时")[0];
				minute = time.split("时")[1].split("分")[0].split("时")[0];
			} else {
				minute = time.split("分")[0];
			}
		}
		parkTimeHourText.setText(hour);
		parkTimeMinuteText.setText(minute);*/

        // 停车开始时间
        textView_time.setText(result.getData().getInfo().getStarttime());

		/*
		 * 小车动画
		 */
        if (isFirstIn) {
            if (isParking) {
                showCarAnimation(isParking);
            } else {
                showCarAnimation(isParking);
                // 小车在场状态展示，"待支付"文字显示，线条和点改为橙色
                textView_end.setVisibility(View.VISIBLE);
                imageView_endline.setBackgroundColor(Color
                        .parseColor("#fda734"));
                imageView_endPoint
                        .setImageResource(R.drawable.icon_circle_yellow);
            }
        }

		/*
		 * 停车场类型?1:软件停车场 2：硬件停车场(包月用户)
		 */
        int parkType = result.getData().getInfo().getOrdertype();
        if (parkType == 2) {
            // 用户类型?0：日卡 1：月卡 2：非日卡月卡(判断包月用户类型前先判断停车场类型)
            int isCardMonth = result.getData().getInfo().getIscardmonth();
            if (isCardMonth != 2) {
                // 包月/计次用户
                if (isFirstIn) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4000);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

			/*	month_user_tv.setVisibility(View.VISIBLE);
				month_user_iv.setVisibility(View.VISIBLE);
				month_user_tv.setText(exitnote);
				park_fee1_tv.setText("0.00");
				if (isCardMonth == 0) { // 计次用户
					month_user_iv
							.setBackgroundResource(R.drawable.label_meter_user);
				} else if (isCardMonth == 1) { // 包月用户
					month_user_iv
							.setBackgroundResource(R.drawable.label_month_user);
				}*/

                return;
            }
        }

		/*
		 * 判断是计时还是计费
		 */
        if (isPayfee) {

            payway_ic.setVisibility(View.GONE);

            // 计费布局展示
            findViewById(R.id.payfee_bill_ic).setVisibility(View.VISIBLE);

			/*// 判断是否开启自动支付功能
			if (ParkApplication.getmUserInfo().getAutopay() == 1) {
				auto_pay_ll.setVisibility(View.VISIBLE);
			} else {
				auto_pay_ll.setVisibility(View.GONE);
			}*/

            // 获取账户余额
            mUserRemain = Float.parseFloat(ParkApplication.getmUserInfo()
                    .getUserscore());

            // 停车费用，负责计算最终费用（减去本地优惠后的停车费用（如无本地优惠则aftershparkfee与shaparkfee字段相同））
            mParkfeePay = result.getData().getInfo().getAftershparkfee();
            LogUtil.e("停车费用：" + result.getData().getInfo().getShparkfee());
            LogUtil.e("实际费用：" + result.getData().getInfo().getAftershparkfee());

            // 已交停车费用
            if (result.getData().getInfo().getPrepayment() != null
                    && !"0.00"
                    .equals(result.getData().getInfo().getPrepayment())) {
                prepayText.setText("(已交￥"
                        + ReckonUtil.getMoneyFormat(result.getData().getInfo()
                        .getPrepayment()) + ",超时需补交)");
                // 补交费用
                parkFee2Text.setText("￥"
                        + ReckonUtil.getMoneyFormat(result.getData().getInfo()
                        .getAfterpay()));
            } else {
                // 停车费用,只负责展示
                parkFee2Text.setText("￥"
                        + ReckonUtil.getMoneyFormat(result.getData().getInfo()
                        .getShparkfee()));
            }

            // 是否有本地优惠（0:无;1:有），本地优惠只负责展示，不参与计算
            if (result.getData().getInfo().getIsdiscount() == 1) {
                discount_rl.setVisibility(View.VISIBLE);
                discount_tv.setText(result.getData().getInfo()
                        .getLocaldiscount());
            }

            /**
             * <pre>
             * 1.选择停车券,判断停车券类型
             * 		1.1停车券够用
             * 			1.1.1余额支付显示0元
             * 		1.2停车券不够用
             * 			1.2.1余额够用
             * 				1.2.1.1应付金额为0元
             * 			1.2.2余额不够用
             * 				1.2.2.1选择第三方支付
             * 2.未选择停车券
             * </pre>
             */
            if (couponsInfo != null) {

                mCouponsId = couponsInfo.getCoupons_id();
                mCouponsPay = Float.parseFloat(couponsInfo.getType_money());

                // 判断停车券类别：1.现金券 2.限定券 3.抵扣券
                if (couponsInfo.getCoupons_type() != 3) {
                    /**
                     * 选择限定券和现金券
                     */
                    mRemainPay = mParkfeePay - mCouponsPay;
                } else {
                    /**
                     * <pre>
                     * 选择抵扣券
                     * 使用方法： 停车需交费用 + 抵扣券的面额（type_money） - 抵扣券抵扣掉的金额（other_amount）
                     * </pre>
                     */
                    // 抵扣券抵扣的金额
                    float dikouQuan = Float.valueOf(couponsInfo
                            .getOther_amount());
                    mRemainPay = mParkfeePay + mCouponsPay - dikouQuan;
                    // 如果最终停车费 < 停车券，就支付停车券的金额
                    mRemainPay = mRemainPay < mCouponsPay ? mCouponsPay
                            : mRemainPay;
                }
            } else {
                // 未选择停车券
                mCouponsId = "";
                mCouponsPay = 0;
                mRemainPay = mParkfeePay;
            }

            /**
             * 计算最终费用
             */
            if (mRemainPay <= 0) {
                // 停车券够用
                mRemainPay = 0;
                mShouldPay = 0;
            } else {
                // 停车券不够用
                if (mUserRemain - mRemainPay >= 0) {
                    // 余额够用
                    mShouldPay = 0;
                } else {
                    // 余额不够用
                    mShouldPay = mRemainPay - mUserRemain;
                    mRemainPay = mUserRemain;
                    // 选择第三方支付
                    payway_ic.setVisibility(View.VISIBLE);
                }
            }

            // ////////////////////////////////显示账单////////////////////////////////////////////

            // 停车券金额
            couponsText.setText(mCouponsPay <= 0 ? couponsPrompt : mCouponsPay
                    + "元" + showCouponsType(couponsInfo.getCoupons_type()));

            // 余额支付
            remainPayText.setText("-￥" + ReckonUtil.getMoneyFormat(mRemainPay));

            // 应交金额
            shouldPayText.setText(ReckonUtil.getMoneyFormat(mShouldPay) + "元");

            ParkApplication.mCouponId = mCouponsId;
            ParkApplication.mCostAfter = mShouldPay + mRemainPay + "";
            ParkApplication.mBalance = mRemainPay + "";
        } else {

            // 计费和自动支付布局隐藏
            findViewById(R.id.payfee_bill_ic).setVisibility(View.GONE);
			/*auto_pay_ll.setVisibility(View.GONE);*/
        }

    }

    /**
     * <pre>
     * 功能说明：判断停车券类别：1.现金券 2.限定券 3.抵扣券
     * 日期：	2015年11月11日
     * 开发者：	陈丶泳佐
     *
     * @param type
     * @return
     * </pre>
     */
    private String showCouponsType(int type) {
        String couponsType = null;
        switch (type) {
            case 1:
                couponsType = "现金券";
                break;
            case 2:
                couponsType = "限定券";
                break;
            case 3:
                couponsType = "抵扣券";
                break;
        }
        return couponsType;
    }

    /**
     * <pre>
     * 功能说明：展示包月提示语动画
     * 日期：	2015年11月20日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void showPromptAnimation() {

        // 从上往下平移动画
        upTransAnim = new TranslateAnimation(0, 0, 0, ViewUtil.getHeight(56));
        upTransAnim.setDuration(2000);
        upTransAnim.setInterpolator(new LinearInterpolator());
        upTransAnim.setFillAfter(true);

        // 从下往上平移动画
        downTransAnim = new TranslateAnimation(0, 0, ViewUtil.getHeight(56), 0);
        downTransAnim.setDuration(2000);
        downTransAnim.setFillAfter(true);
        downTransAnim.setInterpolator(new LinearInterpolator());
        upTransAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // RelativeLayout.LayoutParams layoutParams =
                // (android.widget.RelativeLayout.LayoutParams) scrollview
                // .getLayoutParams();
                // layoutParams.topMargin = ViewUtil.getHeight(88);
                ViewUtil.setMarginTop(relayout_time, 88,
                        ViewUtil.RELATIVELAYOUT);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });

        downTransAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // ViewUtil.setMarginTop(relayout_time, ViewUtil.getHeight(36),
                // type);
                ViewUtil.setMarginTop(relayout_time, 32,
                        ViewUtil.RELATIVELAYOUT);
            }
        });

        relayout_time.startAnimation(upTransAnim);

    }

    /**
     * <pre>
     * 功能说明：展示小车状态动画
     * 日期：	2015年11月20日
     * 开发者：	陈丶泳佐
     *
     * @param isParking true:停车中	false：待支付
     * </pre>
     */
    private void showCarAnimation(final boolean isParking) {

        TranslateAnimation carTransAnim;

        if (isParking) {
            int toX = (getScreenWidth() / 2 - ViewUtil.getWidth(45));
            carTransAnim = new TranslateAnimation(0, toX, 0, 0);
        } else {
            int formX = (getScreenWidth() / 2 - ViewUtil.getWidth(45));
            int toX = getScreenWidth() - ViewUtil.getWidth(90);
            carTransAnim = new TranslateAnimation(formX, toX, 0, 0);
        }

        carTransAnim.setDuration(3000);
        carTransAnim.setFillAfter(true);
        imageView_car.startAnimation(carTransAnim);

    }

    /**
     * <pre>
     * 功能说明：获取账单,判断是否有未支付账单
     * 日期：	2015年6月11日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void parseGetOrderInfo() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("orderno", getIntent().getStringExtra("orderNo"));

        LogUtil.d("【获取账单URL】" + Constant.GET_ORDER_URL + params);

        httpUtil.parse(httpUtil.POST, Constant.GET_ORDER_URL,
                OrderEntity.class, params, new onResult<OrderEntity>() {

                    @Override
                    public void onSuccess(OrderEntity result) {

                        scrollview.onRefreshComplete();

                        // 停车场Id
                        ParkApplication.mParkId = result.getData().getInfo()
                                .getParkid();

                        // 硬件出场提示语，用来判断展示具体的跳转界面
                        exitnote = result.getData().getInfo().getExitnote();

                        mOrderNum = result.getData().getInfo().getOrderno();
                        LogUtil.showLog(2, "【订单号】" + mOrderNum);

                        // 停车场名称
                        name_tv.setText("停车场："
                                + result.getData().getInfo().getParkname());

                        parkLongText.setText("停车时长：" + result.getData().getInfo().getParklength());
                        // 车牌号为空时展示手机号
                        if (!TextUtils.isEmpty(result.getData().getInfo()
                                .getCarno())) {
                            plate_tv.setText("车牌号："
                                    + result.getData().getInfo().getCarno());
                        } else {
                            String phone = ParkApplication.getmUserInfo()
                                    .getUsername();
                            if (!TextUtils.isEmpty(phone)) {
                                phone = phone.substring(0, 3) + "****"
                                        + phone.substring(7, 11);
                                plate_tv.setText("手机号：" + phone);
                            }
                        }

                        switch (result.getData().getMsgcode()) {
                            case 1:
                                LogUtil.e("【1 :软件交费，拉取账单成功[待支付]】");
                                showPayfeeResult(result, true, false);
                                break;
                            case 2:
                                LogUtil.e("【2: 软件交费， 账单仅查看不能交费页面[停车中]】");
                                showPayfeeResult(result, false, true);
                                break;
                            case 3:
                                LogUtil.e("【3: 未拉取到账单[无订单]】");
                                break;
                            case 4:
                                LogUtil.e("【4: 硬件交费，拉取账单成功[停车中]】");
                                showPayfeeResult(result, true, true);
                                break;
                            case 5:
                                LogUtil.e("【5: 硬件交费，硬件费用为0，未支付过[停车中]】");
                                showPayfeeResult(result, false, true);
                                break;
                            case 6:
                                LogUtil.e("【6: 硬件交费，硬件支付过，15分钟内[待出场]】");
                                showPayfeeResult(result, false, false);
                                break;

                        }

                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        ToastUtil.showToast(errMsg);
                    }
                });

    }

    /**
     * <pre>
     * 功能说明：默认进入后选取第一条停车券
     * 日期：	2015年11月9日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void parseGetFirstCoupons() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());// 用户ID号
        params.put("page", "0");// 当前页（默认第一页）
        params.put("perpage", "10");// 每页显示数（默认显示10条记录）
        params.put("type", "2");// 查询场景? 1为停车券列表：2为交费 （默认为1）
        params.put("timeout", "0");// 过期状态标示? 0 为有效: 1为无效: 2为全部（默认为0）
        params.put("used", "0");// 使用标示? 0 为未使用: 1为已使用: 2为全部 （默认为0）

        LogUtil.showLog(3, "【用户获取停车券后选取第一条停车券URL】" + Constant.GET_POUPONS_URL
                + LogUtil.buildUrlParams(params));

        httpUtil.parse(httpUtil.POST, Constant.GET_POUPONS_URL,
                CouponsEntity.class, params, new onResult<CouponsEntity>() {
                    @Override
                    public void onSuccess(CouponsEntity result) {

                        if (result.getData().getCount() != 0) {
                            couponsInfo = result.getData().getList().get(0);
                            coupons_rl.setClickable(true);

                        } else {
                            couponsPrompt = "无可用停车券";
                            coupons_rl.setClickable(false);

                        }

                        parseGetOrderInfo();
                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {

                        ToastUtil.showToast(errMsg);
                        if (errCode == httpUtil.SERVER_REQ_NO) {

                            couponsPrompt = errMsg;
                            ViewUtil.setTextSize(couponsText, 24);

                            coupons_rl.setClickable(false);

                            parseGetOrderInfo();
                        }
                    }
                });

    }

    /**
     * <pre>
     * 功能说明：获取风控参数，然后调用有贝的创建订单的方法
     * 日期：	2015年9月10日
     * 开发者：	陈丶泳佐
     *
     * @param money    停车费用
     * </pre>
     */
    private void parseGetUubeeRiskParams(final String money) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());

        LogUtil.showLog(
                3,
                "【获取有贝的风控参数URL】" + Constant.UBINFO_URL
                        + LogUtil.buildUrlParams(params));

        httpUtil.parse(httpUtil.POST, Constant.UBINFO_URL,
                UubeeRiskEntity.class, params, new onResult<UubeeRiskEntity>() {

                    @Override
                    public void onSuccess(UubeeRiskEntity result) {
                        uubeeRiskParam = result.getData().getUbstr();
                        // 有贝先付设置网络测试环境
                        PrepayAgent.setEnvMode(3);
                        uubeepay.pay(ParkApplication.getmUserInfo().getUid(),
                                ParkApplication.getmUserInfo().getUsername(),
                                ParkApplication.mOrderNum, "", money,
                                uubeeRiskParam, Constant.UUBEE_NOTIFY_URL);

                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        ToastUtil.showToast(money);
                    }
                });

    }

    /**
     * <pre>
     * 功能说明：余额支付
     * 日期：	2015年8月6日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void parsePayByRemain(String remainPay) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("total_score", remainPay);
        params.put("order_num", ParkApplication.mOrderNum);

        LogUtil.showLog(
                3,
                "【余额支付 URL】" + Constant.BALANCE_PAY_URL
                        + LogUtil.buildUrlParams(params));

        httpUtil.parseno(httpUtil.POST, Constant.BALANCE_PAY_URL, params,
                new onResultTo() {

                    @Override
                    public void onResult(int code, String msg, String json) {
                        ToastUtil.showToast(msg);
                        if (code == httpUtil.SERVER_REQ_OK) {
                            // showIntentClass();
                            // if (!TextUtils.isEmpty(exitnote)) {
                            // ToolUtil.IntentClass(activity,
                            // PayPromptActivity.class, false);
                            // } else {
                            ToolUtil.IntentClass(activity,
                                    PayResultActivity.class, true);
                            // }
                        }
                    }
                });

    }

    /**
     * <pre>
     * 功能说明：零元支付
     * 日期：	2015年8月6日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    protected void parsePayByZero() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("order_num", ParkApplication.mOrderNum);
        params.put("total_score", "0");
        params.put("coupon_id", mCouponsId);

        // LogUtil.showLog(
        // 3,
        // "【 零元支付URL】" + Constant.PAY_ZERO_URL
        // + LogUtil.buildUrlParams(params));

        httpUtil.parseno(httpUtil.POST, Constant.PAY_ZERO_URL, params,
                new onResultTo() {

                    @Override
                    public void onResult(int code, String msg, String json) {
                        ToastUtil.showToast(msg);
                        if (code == httpUtil.SERVER_REQ_OK) {
                            ToolUtil.IntentClass(activity,
                                    PayResultActivity.class, true);
                            // if (!TextUtils.isEmpty(exitnote)) {
                            // ToolUtil.IntentClass(activity,
                            // PayPromptActivity.class, false);
                            // } else {
                            // ToolUtil.IntentClass(activity,
                            // PayResultActivity.class, "order_num",
                            // ParkApplication.mOrderNum, true);
                            // }

                            // Intent intent = new Intent(activity,
                            // PayResultActivity.class);
                            // startActivity(intent);
                            // finish();
                            // } else {
                            // ToastUtil.showToast(msg);
                        }
                    }
                });
    }

    /**
     * <pre>
     * 功能说明：【解析】连连用户签约信息查询
     * 日期：	2015年8月26日
     * 开发者：	陈丶泳佐
     *
     * @param money
     * </pre>
     */
    private void parseQueryBindCard(final String money) {

        loadingProgress.show();

        Map<String, String> params = new HashMap<>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());

        LogUtil.showLog(3, "【连连用户签约信息查询 URL】" + Constant.LLPAY_QUERY_URL
                + LogUtil.buildUrlParams(params));

        httpUtil.parse(httpUtil.POST, Constant.LLPAY_QUERY_URL,
                UserCreditCardEntity.class, params,
                new onResult<UserCreditCardEntity>() {
                    @Override
                    public void onSuccess(UserCreditCardEntity result) {
                        loadingProgress.dismiss();
                        if (result.getData().getCount() == 0) {
                            // 未绑定
                            Intent intent = new Intent(activity,
                                    CreditPayOneActivity.class);
                            intent.putExtra("money", money);
                            startActivity(intent);
                        } else {
                            // 已绑定
                            final String card_no = result.getData()
                                    .getAgreement_list().get(0).getCard_no();// 银行卡号后4位
                            final String no_agree = result.getData()
                                    .getAgreement_list().get(0).getNo_agree();// 签约协议号
                            final DialogUtil mDialog = new DialogUtil(context);
                            mDialog.setMessage("您已经绑定尾号为" + card_no
                                    + "的信用卡，确认支付？");
                            mDialog.setPositiveButton("确定",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                            parseWumiPay(no_agree, money);
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        loadingProgress.dismiss();
                        ToastUtil.showToast(errMsg);
                    }
                });
    }

    /**
     * <pre>
     * 功能说明：【解析】信用卡无密支付
     * 日期：	2015年8月26日
     * 开发者：	陈丶泳佐
     *
     * @param money    交易金额
     * @param no_agree 签约协议号
     * </pre>
     */
    private void parseWumiPay(String no_agree, String money) {

        loadingProgress.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("no_order", ParkApplication.mOrderNum);
        params.put("money_order", money);
        params.put("no_agree", no_agree);

        httpUtil.parseno(httpUtil.POST, Constant.LLPAY_WUMI_URL, params,
                new onResultTo() {

                    @Override
                    public void onResult(int code, String msg, String json) {

                        loadingProgress.dismiss();

                        if (code == httpUtil.SERVER_REQ_OK) {
                            ToastUtil.showToast("支付成功！");
                            ToolUtil.IntentClass(activity,
                                    PayResultActivity.class, true);
                            // ToolUtil.IntentClass(activity,
                            // PayResultActivity.class, "order_num",
                            // ParkApplication.mOrderNum, true);

                            // if (!TextUtils.isEmpty(exitnote)) {
                            // ToolUtil.IntentClass(activity,
                            // PayPromptActivity.class, false);
                            // } else {
                            // ToolUtil.IntentClass(activity,
                            // PayResultActivity.class, "order_num",
                            // ParkApplication.mOrderNum, true);
                            // }

                        } else {
                            showToast("支付失败！");
                        }
                    }
                });

    }

    /**
     * <pre>
     * 功能说明：【解析】获取停车场收费详情
     * 日期：	2015年11月13日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void parsePriceInfo() {

        loadingProgress.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("parkid", ParkApplication.mParkId);

        LogUtil.d("【获取停车场详情URL】" + Constant.PARK_DETAIL_URL + params);

        httpUtil.parse(httpUtil.POST, Constant.PARK_DETAIL_URL,
                ParkInfoEntity.class, params, new onResult<ParkInfoEntity>() {

                    @Override
                    public void onSuccess(ParkInfoEntity result) {

                        loadingProgress.dismiss();

                        // 判断有无收费信息
                        if (TextUtils.isEmpty(result.getData().getDpriceday())
                                || TextUtils.isEmpty(result.getData()
                                .getDpricenight())) {

                            ToastUtil.show("暂无收费详情");
                        } else {

                            // 收费详情日间/夜间价格列表
                            todayList = result.getData().getDpricedaylist();
                            nightList = result.getData().getDpricenightlist();
                            showPriceDialog(result);
                        }

                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        loadingProgress.dismiss();
                        ToastUtil.show(errMsg);
                    }
                });

    }
}
