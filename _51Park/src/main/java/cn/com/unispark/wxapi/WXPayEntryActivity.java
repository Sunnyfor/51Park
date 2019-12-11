package cn.com.unispark.wxapi;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.fragment.home.pay.wechatpay.Constants;
import cn.com.unispark.fragment.mine.recharge.RechargeActivity;
import cn.com.unispark.fragment.treasure.lease.LeaseDetailActivity;
import cn.com.unispark.fragment.treasure.lease.LeaseResultActivity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;

/**
 * <pre>
 * 功能说明： 微信支付结果处理
 * 日期：	2015年1月13日
 * 开发者：	任建飞
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 *
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年8月12日
 * </pre>
 */
public class WXPayEntryActivity extends BaseActivity implements
        IWXAPIEventHandler {

    private Activity context;
    private IWXAPI api;
    private ProgressBar pbar;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.wechat_pay_result_main);
        context = this;
        pbar = (ProgressBar) findViewById(R.id.pbar);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void onClickEvent(View view) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.i("微信支付回调结果：" + resp.errStr);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            /*
			 * resp.errCode==  0 ：表示支付成功
			 * resp.errCode== -1 ：表示支付失败，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
			 * resp.errCode== -2 ：表示取消支付，无需处理。发生场景：用户不支付了，点击取消，返回APP。
			 */

            switch (resp.errCode) {
                case 0:// 支付成功


                    if (ParkApplication.mNotifyUrlPage
                            .equals(Constant.WEIXIN_NOTIFY_URL)) {
					/*
					 * 微信支付回调
					 */
                        if (!ParkApplication.mOrderNum.isEmpty()) {
                            Intent intent = new Intent(WXPayEntryActivity.this,
                                    PayResultActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            pbar.setVisibility(View.GONE);
                            Builder tDialog = new Builder(
                                    WXPayEntryActivity.this);
                            tDialog.setTitle("提示");
                            tDialog.setMessage("支付成功！");
                            tDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            finish();
                                        }
                                    });
                            tDialog.show();
                        }

                    } else if (ParkApplication.mNotifyUrlPage
                            .equals(Constant.WEIXIN_RECHARGE_URL)) {
					/*
					 * 微信充值回调
					 */
                        ToastUtil.show("充值成功!");
                        RechargeActivity.activity.finish();
                        finish();
                    } else if (ParkApplication.mNotifyUrlPage
                            .equals(Constant.WEIXIN_PARKCARD_URL)) {
					/*
					 * 微信租赁回调
					 */
                        if (!ParkApplication.mLeaseOrderNum.isEmpty()) {
                            Intent intent = new Intent(WXPayEntryActivity.this,
                                    LeaseResultActivity.class);
                            startActivity(intent);
                            finish();
                            LeaseDetailActivity.activity.finish();
                        }
                    }

                    break;
                case -1:// 支付失败

                    pbar.setVisibility(View.GONE);
                    final DialogUtil mLogoutDialog = new DialogUtil(context);
                    mLogoutDialog.setMessage("支付失败！");
                    mLogoutDialog.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLogoutDialog.dismiss();
                            ParkApplication.isSearch = false;
                            finish();
                        }
                    });
                    break;
                case -2:// 支付取消

                    pbar.setVisibility(View.GONE);
                    final DialogUtil mDialog = new DialogUtil(context);
                    mDialog.setMessage("支付取消！");
                    mDialog.setPositiveButton("确定", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            finish();
                        }
                    });
                    break;
            }

        }
    }

}