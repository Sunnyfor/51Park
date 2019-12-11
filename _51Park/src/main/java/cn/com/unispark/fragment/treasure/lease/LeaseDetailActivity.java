package cn.com.unispark.fragment.treasure.lease;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.alipay.AliPay;
import cn.com.unispark.fragment.home.pay.wechatpay.Wxpay;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.fragment.treasure.lease.calendar.CalendarActivity;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseCarBuyEntity;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseDetailEntity;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseDetailEntity.DataObject.LeaseDetailInfo;
import cn.com.unispark.fragment.treasure.lease.entity.LeaseDetailEntity.DataObject.TimeslistDetailInfo;
import cn.com.unispark.login.LoginActivity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.ImageUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 【租赁详情】页面
 * 日期：	2015年9月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class LeaseDetailActivity extends BaseActivity {

    // 导航栏标题以及返回按钮//右侧提示按钮
    private TextView titleText;
    private ImageView moreImgView;
    private LinearLayout backLLayout, moreLLayout;

    // 微信支付// 支付宝支付
    private Wxpay wxpay;
    private AliPay aliPay;

    // 微信支付完成后需要关闭详情页
    public static Activity activity;

    /**
     * 原始价格
     */
    private TextView origPriceText;

    /**
     * 优惠后的价格
     */
    private TextView realPriceText;

    // 租期布局
    private LinearLayout lease_date_ll;

    /**
     * 计次的默认值
     */
    private int origCount;

    // 计次的实际值
    private int realCount = 0;

    // 计次的最大值
    private int maxCount = 99;

    /**
     * 计算后的价格
     */
    private String payfee;

    /**
     * 计次的单价
     */
    private String oncePayfee;

    /**
     * 停车场Id号
     */
    private String parkId;

    /**
     * 停车场类型
     */
    private int parkType;
    /**
     * 月份的类型
     */
    private int monthType;

    /**
     * 月卡开始的日期
     */
    private String monthStartDate;
    /**
     * 选中的日期
     */
    private String selectDate;

    /**
     *
     */
    private PeriodView lastBtn;
    /**
     * 具体时长
     */
    private int longStr;

    private String dateStartStr;

    private RelativeLayout start_date_rl;

    private Button finish_btn;

    private RadioButton wechat_rbtn, alipay_rbtn;

    private TextView startDateTextView;


    // 包月或计次切换查看详情的布局
    private LinearLayout month_ll;
    /*
     * 购买须知对话框
     */
    private Dialog dialog;
    private LinearLayout dialog_ll;// 须知主体框
    private ImageView clear_iv;// 关闭按钮
    private TextView month_tv, meter_tv;// 规则提醒
    private String monthdesc, meterdesc;// 规则提醒文字
    private ImageView desc_iv;
    private TextView name_tv;
    private TextView address_tv;
    // 总车位数//空车位数
    private TextView count_tv, empty_count_tv;
    private RelativeLayout carlease_buy_rl;
    private TextView lease_date_tv;
    // // 包月或计次切换按钮
    private RadioGroup radiogroup;
    private RadioButton left_rb;
    private RadioButton right_rb;
    private RelativeLayout title_ic;
    private TextView choice_payway_tv;
    private TextView start_date_tv;
    private TextView tv_date_desc;
    private CheckBox isread_checkbox;
    private LinearLayout isread_ll;
    private TextView left_tv;

    //购买须知按钮
    private TextView right_tv;
    // 购买须知的URL
    private String buyUrl;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.lease_detail_main);
        activity = this;
        wxpay = new Wxpay(this);
        aliPay = new AliPay(this);
    }

    @Override
    public void initView() {

        title_ic = (RelativeLayout) findViewById(R.id.title_ic);
        ViewUtil.setViewSize(title_ic, 88, 0);

        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        left_rb = (RadioButton) findViewById(R.id.left_rb);
        ViewUtil.setViewSize(left_rb, 60, 150);
        ViewUtil.setTextSize(left_rb, 26);

        left_rb.setText("包月服务");
        right_rb = (RadioButton) findViewById(R.id.right_rb);
        ViewUtil.setViewSize(right_rb, 60, 150);
        ViewUtil.setTextSize(right_rb, 26);
        right_rb.setText("计次服务");
        // 导航栏标题
        titleText = (TextView) findViewById(R.id.titleText);
        // titleText.setText("包月服务");
        // 返回按钮
        backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
        backLLayout.setOnClickListener(this);

        // choice_payway_tv = (TextView) findViewById(R.id.choice_payway_tv);
        // choice_payway_tv.setBackgroundColor(getResources().getColor(
        // R.color.white));
        /*
		 * 停车场描述图片
		 */
        desc_iv = (ImageView) findViewById(R.id.desc_iv);
        desc_iv.setScaleType(ScaleType.FIT_XY);
        desc_iv.setOnClickListener(this);
        ViewUtil.setViewSize(desc_iv, 176, 235);
        ViewUtil.setMargin(desc_iv, 20, ViewUtil.RELATIVELAYOUT);

        // 停车场名称
        name_tv = (TextView) findViewById(R.id.name_tv);
        ViewUtil.setTextSize(name_tv, 28);
        ViewUtil.setMarginTop(name_tv, 20, ViewUtil.RELATIVELAYOUT);

        // 地址
        address_tv = (TextView) findViewById(R.id.address_tv);
        ViewUtil.setTextSize(address_tv, 24);
        ViewUtil.setMarginTop(address_tv, 20, ViewUtil.RELATIVELAYOUT);

        // 总车位数
        count_tv = (TextView) findViewById(R.id.count_tv);
        ViewUtil.setTextSize(count_tv, 24);
        ViewUtil.setMarginTop(count_tv, 22, ViewUtil.RELATIVELAYOUT);

        // 空车位数
        empty_count_tv = (TextView) findViewById(R.id.empty_count_tv);
        ViewUtil.setTextSize(empty_count_tv, 24);
        ViewUtil.setMarginLeft(empty_count_tv, 10, ViewUtil.RELATIVELAYOUT);

        // 右侧详情按钮
        moreImgView = (ImageView) findViewById(R.id.moreImgView);
        moreImgView.setImageResource(R.drawable.btn_prompt);
        moreImgView.setVisibility(View.VISIBLE);
        moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
        ViewUtil.setViewSize(moreLLayout, 88, 88);
        moreLLayout.setOnClickListener(this);
        // 最底部布局 购买
        carlease_buy_rl = (RelativeLayout) findViewById(R.id.carlease_buy_rl);
        ViewUtil.setViewSize(carlease_buy_rl, 150, 0);
        // 租期
        lease_date_tv = (TextView) findViewById(R.id.lease_date_tv);
        ViewUtil.setTextSize(lease_date_tv, 30);
        ViewUtil.setViewSize(lease_date_tv, 70, ViewUtil.WEIGHT);
        ViewUtil.setPaddingLeft(lease_date_tv, 20);

		/*
		 * 包月或计次切换查看详情的布局
		 */
        month_ll = (LinearLayout) findViewById(R.id.month_ll);

        // 租期装盛放的容器
        lease_date_ll = (LinearLayout) findViewById(R.id.lease_date_ll);
        ViewUtil.setViewSize(lease_date_ll, 148, 0);

        // ViewUtil.setMargin(lease_date_ll, 0, 10, 0, 10,
        // ViewUtil.LINEARLAYOUT);

        // 原始价格//优惠后的价格
        origPriceText = (TextView) findViewById(R.id.orig_price_tv);
        ViewUtil.setMarginLeft(origPriceText, 8, ViewUtil.LINEARLAYOUT);
        // ViewUtil.setMarginTop(origPriceText, 84, ViewUtil.LINEARLAYOUT);
        ViewUtil.setTextSize(origPriceText, 24);

        realPriceText = (TextView) findViewById(R.id.real_price_tv);
        ViewUtil.setMarginLeft(realPriceText, 38, ViewUtil.LINEARLAYOUT);
        // ViewUtil.setMarginTop(realPriceText, 90, ViewUtil.LINEARLAYOUT);
        ViewUtil.setTextSize(realPriceText, 36);

        // 购买或续费按钮
        finish_btn = (Button) findViewById(R.id.finish_btn);
        finish_btn.setText("立即购买");
        ViewUtil.setViewSize(finish_btn, 70, 220);
        ViewUtil.setMarginRight(finish_btn, 26, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginTop(finish_btn, 10, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setTextSize(finish_btn, 30);
        finish_btn.setOnClickListener(this);
		/*
		 * 请选择支付方式（文字）
		 */
        TextView choice_payway_tv = (TextView) findViewById(R.id.choice_payway_tv);
        choice_payway_tv.setTextColor(getResources()
                .getColor(R.color.gray_font));
        choice_payway_tv.setBackgroundColor(getResources().getColor(
                R.color.white));
        ViewUtil.setTextSize(choice_payway_tv, 30);
        ViewUtil.setViewSize(choice_payway_tv, 70, ViewUtil.WEIGHT);
        ViewUtil.setPaddingLeft(choice_payway_tv, 20);

        // 选择支付方式文字下方的线条
        // findViewById(R.id.line_w_view).setVisibility(View.VISIBLE);
        // 微信
        wechat_rbtn = (RadioButton) findViewById(R.id.wechat_rbtn);
        wechat_rbtn.setOnClickListener(this);
        ViewUtil.setTextSize(wechat_rbtn, 30);
        ViewUtil.setViewSize(wechat_rbtn, 88, 0);
        ViewUtil.setPaddingLeft(wechat_rbtn, 20);
        ViewUtil.setPaddingRight(wechat_rbtn, 20);
        // 支付宝
        alipay_rbtn = (RadioButton) findViewById(R.id.alipay_rbtn);
        alipay_rbtn.setOnClickListener(this);
        ViewUtil.setTextSize(alipay_rbtn, 30);
        ViewUtil.setViewSize(alipay_rbtn, 88, 0);
        ViewUtil.setPaddingLeft(alipay_rbtn, 20);
        ViewUtil.setPaddingRight(alipay_rbtn, 20);
        // 日期
        startDateTextView = (TextView) findViewById(R.id.start_date_tv);
        startDateTextView.setOnClickListener(this);
        ViewUtil.setTextSize(startDateTextView, 24);
        ViewUtil.setMarginTop(startDateTextView, 8, ViewUtil.RELATIVELAYOUT);
        ViewUtil.setMarginRight(startDateTextView, 26, ViewUtil.RELATIVELAYOUT);
        tv_date_desc = (TextView) findViewById(R.id.tv_date_desc);
        ViewUtil.setTextSize(tv_date_desc, 24);
        ViewUtil.setMarginTop(tv_date_desc, 8, ViewUtil.RELATIVELAYOUT);

        isread_checkbox = (CheckBox) findViewById(R.id.isread_checkbox);
        ViewUtil.setPaddingLeft(isread_checkbox, 20);
        isread_ll = (LinearLayout) findViewById(R.id.isread_ll);
        ViewUtil.setPaddingTop(isread_ll, 14);

        left_tv = (TextView) findViewById(R.id.left_tv);
        ViewUtil.setTextSize(left_tv, 24);
        ViewUtil.setPaddingLeft(left_tv, 8);
        right_tv = (TextView) findViewById(R.id.right_tv);
        right_tv.setOnClickListener(this);
        ViewUtil.setTextSize(left_tv, 24);

		/*
		 * 根据停车类型来判断是否展示包月+计次的选项
		 */
        switch (ParkApplication.mLeaseType) {
            case 0:// 包月+计次:展示选项按钮(默认隐藏)
                radiogroup.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.GONE);
                parseLeaseDetailList(1);
                break;
            case 1:// 包月
                radiogroup.setVisibility(View.GONE);
                titleText.setVisibility(View.VISIBLE);
                titleText.setText("包月服务");
                parseLeaseDetailList(1);
                break;
            case 2:// 计次
                radiogroup.setVisibility(View.GONE);
                titleText.setVisibility(View.VISIBLE);
                titleText.setText("计次服务");
                parseLeaseDetailList(2);
                break;
        }
        // 切换viewpagerItem
        radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (left_rb.getId() == checkedId) {
                    ParkApplication.mLeaseType = 1;
                    parseLeaseDetailList(1);
                    MobclickAgent.onEvent(context, "DownLoadFragment_click");
                }
                if (right_rb.getId() == checkedId) {
                    ParkApplication.mLeaseType = 2;
                    parseLeaseDetailList(2);
                    MobclickAgent.onEvent(context, "DownLoadFragment_click");
                }
            }
        });
        isread_checkbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            finish_btn
                                    .setBackgroundResource(R.drawable.btn_common_selected);
                            finish_btn.setEnabled(true);
                        } else {
                            finish_btn
                                    .setBackgroundResource(R.drawable.btn_common_buy_noselected);
                            finish_btn.setEnabled(false);
                        }
                    }
                });

    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.start_date_tv:
                Intent intent = new Intent(context, CalendarActivity.class);
                intent.putExtra("startDareStr", monthStartDate);
                intent.putExtra("dateStartStr", dateStartStr);
                intent.putExtra("longStr", longStr + "");
                startActivityForResult(intent, 20);
                MobclickAgent.onEvent(context, "lease_startDate_click");
                break;
            case R.id.backLLayout:
                finish();
                break;
            case R.id.moreLLayout:
                showPromptDialog();
                MobclickAgent.onEvent(context, "lease_purchaseNotesBtn_click");
                break;
            case R.id.clear_iv:// 购买须知关闭按钮
//			dialog.cancel();
                dialog.hide();
                MobclickAgent.onEvent(context, "lease_purchaseCloseBtn_click");
                break;
            case R.id.finish_btn:
                if (ShareUtil.getSharedBoolean("islogin")) {
                    parseLeaseCarBuy();
                } else {
                    ToolUtil.IntentClass(activity, LoginActivity.class, false);
                }
                MobclickAgent.onEvent(context, "lease_buyBtn_click");
                break;
            case R.id.right_tv:
                Intent in = new Intent(context, WebActiveActivity.class);
                in.putExtra("url", buyUrl);
                in.putExtra("title", "购买须知");
                startActivity(in);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 30:
                String dateStr = data.getStringExtra("dateStr");
                selectDate = dateStr;
                monthStartDate = dateStr;
                startDateTextView.setText(dateStr);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * <pre>
     * 功能说明：租赁详情提示的对话框
     * 日期：	2015年11月23日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void showPromptDialog() {
        dialog = new Dialog(context, R.style.Dialog_Lease_Detail_Style);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);// 全屏展示
        dialog.setCancelable(false);

        View view = View.inflate(context, R.layout.lease_detail_dialog, null);

        // 关闭按钮
        clear_iv = (ImageView) view.findViewById(R.id.clear_iv);
        clear_iv.setOnClickListener(this);
        ViewUtil.setMarginTop(clear_iv, 18, ViewUtil.LINEARLAYOUT);

        // 须知主体框
        dialog_ll = (LinearLayout) view.findViewById(R.id.dialog_ll);
        ViewUtil.setViewSize(dialog_ll, 680, 600);
        ViewUtil.setPaddingLeft(dialog_ll, 50);
        ViewUtil.setPaddingRight(dialog_ll, 50);

        // 规则提醒:包月
        month_tv = (TextView) view.findViewById(R.id.month_tv);
        if (!TextUtils.isEmpty(monthdesc)) {
            month_tv.setText("规则提醒：" + monthdesc);
        }

        // 计次
        meter_tv = (TextView) view.findViewById(R.id.meter_tv);
        if (!TextUtils.isEmpty(meterdesc)) {
            meter_tv.setText("规则提醒：" + meterdesc);
        }

        dialog.setContentView(view);
        dialog.show();

    }

    /**
     * <pre>
     * 功能说明：动态日期布局
     * 日期：	2015年10月20日
     * 开发者：	任建飞
     *
     * @param entitylist 根据类获取相关信息
     * </pre>
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setMontyView(List<LeaseDetailInfo> entitylist) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        lease_date_ll.removeAllViews();
        for (int i = 0; i < entitylist.size(); i++) {
            LeaseDetailInfo mLeaseMonthEntity = entitylist.get(i);
            final PeriodView bt = new PeriodView(context, null);
            bt.setmTopStr(mLeaseMonthEntity.getName());
            bt.setmDetailStr(mLeaseMonthEntity.getPricename());
            bt.setTag(mLeaseMonthEntity);
            if (2 == mLeaseMonthEntity.getIsbuy()) {
                bt.setEnabled(false);
            } else {
                bt.setEnabled(true);
            }
            switch (i) {
                case 0:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_one_icon);
                    bt.setLineVisibility(true);
                    break;
                case 1:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_two_icon);
                    bt.setLineVisibility(true);
                    break;
                case 2:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_three_icon);
                    bt.setLineVisibility(true);
                    break;
                case 3:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_four_icon);
                    bt.setLineVisibility(false);
                    break;
            }
            // 为Button添加长高设置
            LinearLayout.LayoutParams layoutParams_bt = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams_bt.gravity = Gravity.CENTER;
            bt.setLayoutParams(layoutParams_bt);
            if (i == 0) {
                monthType = Integer.valueOf(entitylist.get(i).getType())
                        .intValue();
                monthStartDate = entitylist.get(i).getStart();
                dateStartStr = entitylist.get(i).getStart();
                longStr = entitylist.get(i).getLongtime();
                if (TextUtils.isEmpty(selectDate)) {
                    startDateTextView.setText(monthStartDate);
                } else {
                    monthStartDate = selectDate;
                    startDateTextView.setText(selectDate);
                }
                // 显示优惠
                if (mLeaseMonthEntity.getDiscounttype() == 1
                        && mLeaseMonthEntity.getShow_old_price() == 1) {
                    // 显示优惠
                    showDiscount(bt, mLeaseMonthEntity);
                } else if (mLeaseMonthEntity.getDiscounttype() == 1
                        && mLeaseMonthEntity.getShow_old_price() == 0) {// 有优惠，显示买送。不显示价格
                    showRealPrice(bt, mLeaseMonthEntity);
                } else {
                    origPriceText.setVisibility(View.GONE);
                    realPriceText.setText("￥" + mLeaseMonthEntity.getPrice());
                    payfee = mLeaseMonthEntity.getPrice();
                }
                bt.setIsChecked(true);
                lastBtn = bt;
            }
            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!lastBtn.getTag().equals(bt.getTag())) {
                        Log.e("slx", "no same");
                        showMonthInfo(bt);
                    }
                }
            });
            // 添加到主布局
            lease_date_ll.addView(bt);
        }
    }

    /**
     * <pre>
     * 功能说明：动态日期布局
     * 日期：	2015年10月20日
     * 开发者：	任建飞
     *
     * @param entitylist 根据类获取相关信息
     * </pre>
     */
    private void setMeterView(List<TimeslistDetailInfo> entitylist) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        lease_date_ll.removeAllViews();
        Log.e("slx", "entitylist" + entitylist.size());
        for (int i = 0; i < entitylist.size(); i++) {
            TimeslistDetailInfo mLeaseMonthEntity = entitylist.get(i);
            final PeriodView bt = new PeriodView(context, null);
            bt.setmTopStr(mLeaseMonthEntity.getName());
            bt.setmDetailStr(mLeaseMonthEntity.getPricename());
            bt.setTag(mLeaseMonthEntity);
            if (2 == mLeaseMonthEntity.getIsbuy()) {
                bt.setEnabled(false);
            } else {
                bt.setEnabled(true);
            }
            switch (i) {
                case 0:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_one_icon);
                    bt.setLineVisibility(true);
                    break;
                case 1:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_two_icon);
                    bt.setLineVisibility(true);
                    break;
                case 2:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_three_icon);
                    bt.setLineVisibility(true);
                    break;
                case 3:
                    bt.setmTopBg(2 == mLeaseMonthEntity.getIsbuy() ? R.drawable.lease_preiod_default_icon
                            : R.drawable.lease_preiod_four_icon);
                    bt.setLineVisibility(false);
                    break;
            }
            // 为Button添加长高设置
            LinearLayout.LayoutParams layoutParams_bt = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams_bt.gravity = Gravity.CENTER;
            bt.setLayoutParams(layoutParams_bt);
            if (i == 0) {
                monthType = Integer.valueOf(entitylist.get(i).getType())
                        .intValue();
                monthStartDate = entitylist.get(i).getStart();
                dateStartStr = entitylist.get(i).getStart();
                if (TextUtils.isEmpty(selectDate)) {
                    startDateTextView.setText(monthStartDate);
                } else {
                    monthStartDate = selectDate;
                    startDateTextView.setText(selectDate);
                }
                // 显示优惠
                origPriceText.setVisibility(View.GONE);
                double num = (double) mLeaseMonthEntity.getNum();
                double oneaddTemp = Double.valueOf(
                        mLeaseMonthEntity.getRealprice()).doubleValue();
                payfee = Double.toString(ReckonUtil.mul(num, oneaddTemp));
                SpannableString noYhText = new SpannableString("￥" + payfee);
                // noYhText.setSpan(
                // new TextAppearanceSpan(this, R.style.RMB_style), 0, 1,
                // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                realPriceText.setText(noYhText, TextView.BufferType.SPANNABLE);
                bt.setIsChecked(true);
                lastBtn = bt;
            }
            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!lastBtn.getTag().equals(bt.getTag())) {
                        Log.e("slx", "no same");
                        showMeterInfo(bt);
                    }
                }
            });
            // 添加到主布局
            lease_date_ll.addView(bt);
        }
    }

    /**
     * <pre>
     * 功能说明：展示月份图标
     * 日期：	2015年12月2日
     * 开发者：	陈丶泳佐
     *
     * @param bt
     * </pre>
     */
    private void showMeterInfo(PeriodView bt) {
        TimeslistDetailInfo mLeaseMonthEntity = (TimeslistDetailInfo) bt
                .getTag();
        // 显示包月金额
        monthType = Integer.valueOf(mLeaseMonthEntity.getType()).intValue();
        monthStartDate = mLeaseMonthEntity.getStart();
        dateStartStr = mLeaseMonthEntity.getStart();
        if (TextUtils.isEmpty(selectDate)) {
            startDateTextView.setText(monthStartDate);
        } else {
            monthStartDate = selectDate;
            startDateTextView.setText(selectDate);
        }
        double num = (double) mLeaseMonthEntity.getNum();
        double oneaddTemp = Double.valueOf(mLeaseMonthEntity.getRealprice())
                .doubleValue();
        payfee = Double.toString(ReckonUtil.mul(num, oneaddTemp));
        SpannableString noYhText = new SpannableString("￥" + payfee);
        // noYhText.setSpan(new TextAppearanceSpan(this, R.style.RMB_style), 0,
        // 1,
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        realPriceText.setText(noYhText, TextView.BufferType.SPANNABLE);
        // 把当前button复值给上lastBtn，保存点击过的button上一个btn背景切换
        TimeslistDetailInfo lastLeaseMonthEntity = (TimeslistDetailInfo) lastBtn
                .getTag();
        lastBtn.setIsChecked(false);
        bt.setIsChecked(true);
        lastBtn = bt;
    }

    /**
     * <pre>
     * 功能说明：展示月份图标
     * 日期：	2015年12月2日
     * 开发者：	陈丶泳佐
     *
     * @param bt
     * </pre>
     */
    private void showMonthInfo(PeriodView bt) {
        LeaseDetailInfo mLeaseMonthEntity = (LeaseDetailInfo) bt.getTag();
        // 显示包月金额
        monthType = Integer.valueOf(mLeaseMonthEntity.getType()).intValue();
        monthStartDate = mLeaseMonthEntity.getStart();
        dateStartStr = mLeaseMonthEntity.getStart();
        longStr = mLeaseMonthEntity.getLongtime();
        if (TextUtils.isEmpty(selectDate)) {
            startDateTextView.setText(monthStartDate);
        } else {
            monthStartDate = selectDate;
            startDateTextView.setText(selectDate);
        }
        if (mLeaseMonthEntity.getDiscounttype() == 1
                && mLeaseMonthEntity.getShow_old_price() == 1) {// 有优惠
            // 显示实际金额
            showDiscount(bt, mLeaseMonthEntity);
        } else if (mLeaseMonthEntity.getDiscounttype() == 1
                && mLeaseMonthEntity.getShow_old_price() == 0) {// 有优惠，显示买送。不显示价格
            showRealPrice(bt, mLeaseMonthEntity);
        } else {// 无优惠
            // 显示包月金额
            showRealPrice(bt, mLeaseMonthEntity);
        }
        // 把当前button复值给上lastBtn，保存点击过的button上一个btn背景切换
        LeaseDetailInfo lastLeaseMonthEntity = (LeaseDetailInfo) lastBtn
                .getTag();
        lastBtn.setIsChecked(false);
        bt.setIsChecked(true);
        lastBtn = bt;
    }

    /**
     * <pre>
     * 功能说明：展示抵扣布局
     * 日期：	2015年12月2日
     * 开发者：	陈丶泳佐
     *
     * @param bt
     * @param mLeaseMonthEntity
     * </pre>
     */
    private void showDiscount(PeriodView bt, LeaseDetailInfo mLeaseMonthEntity) {
        origPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        origPriceText.setText("￥" + mLeaseMonthEntity.getPrice());
        origPriceText.setVisibility(View.VISIBLE);
        // 显示优惠价格
        realPriceText.setText("￥" + mLeaseMonthEntity.getRealprice());
        // 要支付的金额
        payfee = mLeaseMonthEntity.getRealprice();
    }

    /**
     * <pre>
     * 功能说明：展示真实价格
     * 日期：	2015年12月2日
     * 开发者：	陈丶泳佐
     *
     * @param bt
     * @param mLeaseMonthEntity
     * </pre>
     */
    private void showRealPrice(PeriodView bt, LeaseDetailInfo mLeaseMonthEntity) {
        origPriceText.setVisibility(View.GONE);
        realPriceText.setText("￥" + mLeaseMonthEntity.getPrice());
        // 要支付的金额
        payfee = mLeaseMonthEntity.getPrice();
    }

    /**
     * <pre>
     * 功能说明：展示租赁详情介绍
     * 日期：	2015年9月23日
     * 开发者：	陈丶泳佐
     *
     * @param data
     * </pre>
     */
    public void showLeaseDetail(LeaseDetailEntity data) {
        // 停车场ID
        parkId = data.getData().getParkid();
        if (!TextUtils.isEmpty(data.getData().getPic())) {
            ImageUtil.loadImage(context, desc_iv, data.getData().getPic());
        }
        name_tv.setText(data.getData().getParkname());
        address_tv.setText(data.getData().getParkaddr());
        // count_tv.setText(data.getData().getNum());
        // 空车位数
        count_tv.setText("总车位: " + data.getData().getTotalcount());
        empty_count_tv.setText("空车位: " + data.getData().getFreecount());

//		if (!ShareUtil.getSharedString(parkId).equals(parkId)) {
//			showPromptDialog();
//			ShareUtil.setSharedString(parkId, parkId);
//		}

        // 是否为续费（0:否 1：是）
        finish_btn.setText(data.getData().getIsrebuy() == 0 ? "立即购买" : "续费");
        // 日期是否可点击（0:否 1：是）
        // startDateRLayout
        // .setClickable("0".equals(data.getData().getIsrebuy()) ? true
        // : false);
        // 卡类型（1：按月 2：按次）
        parkType = data.getData().getType();
        switch (parkType) {
            case 1:
                List<LeaseDetailInfo> monthList = data.getData().getMonth();
                if ("2".equals(data.getData().getIsmonthsoldout())) {
                    isSoldOut();
                } else {
                    isCanSale();
                }
                setMontyView(monthList);
                break;
            case 2:
                if ("2".equals(data.getData().getIstimessoldout())) {
                    isSoldOut();
                } else {
                    isCanSale();
                }
                List<TimeslistDetailInfo> monthList2 = data.getData()
                        .getTimeslist();
                setMeterView(monthList2);
                break;
        }
    }

    /**
     * 未售完
     */
    private void isCanSale() {
        isread_checkbox.setEnabled(true);
        realPriceText.setTextColor(getResources().getColor(R.color.red_font));
        startDateTextView.setVisibility(View.VISIBLE);
        tv_date_desc.setVisibility(View.VISIBLE);
        finish_btn.setText("立即购买");
        finish_btn.setBackgroundResource(R.drawable.btn_common_selected);
        finish_btn.setEnabled(true);
    }

    /**
     * 已售完
     */
    private void isSoldOut() {
        isread_checkbox.setEnabled(false);
        realPriceText
                .setTextColor(getResources().getColor(R.color.gray_fontbb));
        startDateTextView.setVisibility(View.GONE);
        tv_date_desc.setVisibility(View.GONE);
        finish_btn.setText("已售罄");
        finish_btn.setBackgroundResource(R.drawable.btn_common_buy_noselected);
        finish_btn.setEnabled(false);
    }

    /**
     * <pre>
     * 功能说明：【解析】车位租赁详情
     * 日期：	2015年9月22日
     * 开发者：	陈丶泳佐
     *
     * @param type 1为包月  2为计次
     * </pre>
     */
    private void parseLeaseDetailList(int type) {
        loadingProgress.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("parkid", ParkApplication.mParkId);
        params.put("type", String.valueOf(type));
        LogUtil.showLog(3, "【车位租赁详情 URL】" + Constant.PARKCARD_INFO_URL
                + LogUtil.buildUrlParams(params));
        httpUtil.parse(httpUtil.POST, Constant.PARKCARD_INFO_NEW_URL,
                LeaseDetailEntity.class, params,
                new onResult<LeaseDetailEntity>() {
                    @Override
                    public void onSuccess(LeaseDetailEntity result) {
                        loadingProgress.dismiss();

                        // 购买须知的Url
                        buyUrl = result.getData().getBuyrule();

                        findViewById(R.id.scrollview).setVisibility(
                                View.VISIBLE);
                        monthdesc = result.getData().getMonthdesc();
                        meterdesc = result.getData().getMeterdesc();
                        showLeaseDetail(result);
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
     * 功能说明：【解析】车位租赁购买
     * 日期：	2015年9月28日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void parseLeaseCarBuy() {

        loadingProgress.show();

        if (ParkApplication.mLeaseType == 0) {
            ParkApplication.mLeaseType = 1;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());
        params.put("price", payfee);
        params.put("parkid", parkId);
        params.put("type", ParkApplication.mLeaseType + "");

        switch (ParkApplication.mLeaseType) {
            case 1:// 包月
                ParkApplication.monthStartDate = monthStartDate;
                params.put("date", monthStartDate);// 开始日期,Type=1 时有效
                params.put("monthtype", monthType + "");// 月分类型,Type=1时有效
                break;
            case 2:// 计次
                params.put("num", realCount + "");// 购买个数,Type=2时有效
                break;
        }

        LogUtil.showLog(
                3,
                "【车位租赁购买 URL】" + Constant.PARKCARD_PAY_URL
                        + LogUtil.buildUrlParams(params));

        httpUtil.parse(httpUtil.POST, Constant.PARKCARD_PAY_URL,
                LeaseCarBuyEntity.class, params,
                new onResult<LeaseCarBuyEntity>() {
                    @Override
                    public void onSuccess(LeaseCarBuyEntity result) {

                        // 1成功 2失败
                        if (result.getData().getResult() == 1) {

                            ParkApplication.mLeaseOrderNum = result.getData()
                                    .getOrderno();

                            if (wechat_rbtn.isChecked()) {
                                // 微信支付
                                String payStr = ReckonUtil
                                        .StringY2StringF(payfee);
                                ParkApplication.mNotifyUrlPage = Constant.WEIXIN_PARKCARD_URL;
                                wxpay.pay(ParkApplication.mLeaseOrderNum, payStr, 0);
                                MobclickAgent.onEvent(context,
                                        "lease_WeChatPay_click");
                            } else {
                                // 支付宝支付
                                aliPay.pay(ParkApplication.mLeaseOrderNum,
                                        Constant.BODY_BUY, Constant.SUBJECT,
                                        payfee, Constant.ALIPAY_PARKCARD_URL);
                                MobclickAgent.onEvent(context,
                                        "lease_AlipayPay_click");
                            }
                        }
                        loadingProgress.dismiss();
                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        ToastUtil.showToast(errMsg);
                        loadingProgress.dismiss();
                    }
                });

    }

}
