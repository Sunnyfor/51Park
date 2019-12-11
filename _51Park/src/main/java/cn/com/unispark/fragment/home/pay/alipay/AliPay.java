package cn.com.unispark.fragment.home.pay.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.fragment.treasure.lease.LeaseResultActivity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;

import com.alipay.sdk.app.PayTask;

/**
 * <pre>
 * 功能说明： 支付宝支付工具类
 * 日期：	2015年9月8日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.5.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年9月8日
 * </pre>
 */
public class AliPay {
	/**
	 * 
	 * <pre>
	 * // 紫光百会: 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	 * public static final String PARTNER = &quot;2088701505040794&quot;;
	 * // 商户收款的支付宝账号
	 * public static final String SELLER = &quot;unisparkzhifubao@163.com&quot;;
	 * // 商户（RSA）私钥
	 * public static final String RSA_PRIVATE = &quot;MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMTnI+PByTnOhM3Ag3Q4DpczhlrElsXnGvOY5PgukvUvj1cPZkpW61fy8pASvAOp/rM58FsdNUCnBEify97pATZy9LtUw8EtZoyYYTpRS3Yerbs0pFBT12m18MCSh3g02gYtlNRj2hlkx188JYeJDdyPwDlaYANRbWF+TRL/b2NzAgMBAAECgYBQ0RoO+lPwUQ57MMADrJp4fuJvUO8dbVzHjQK1xs/0qL+pmR1/96sC3yml7rqSumxa/bXkf9mCnsqIUGYqKPKgwB5J//+XTYR/X8LwbTBDjKf2VxdrpUGh/NkFVv72XHlQuwF7T8cCXVuNfDj+8pRAbs5M8fl7f5l+3qEsOhQtoQJBAPrv8zOeEilTp6B0fOEoZ5YAVpHDxzDcS+CFy2ejTnNfo1hyxPMcVtWdQBREJL0P7LwqQV2s+3wR23PxH1mb3PECQQDI4Bx+5PSCNKIs08ZflIWcxcQyXlDiXebKs5SsdqWKUfSfJm9mD14cyf274rAqTeij++FG18WEgox2myfzhRajAkBkEMd0r/YiEL/plkouJ0SXZm8fFWkbLN39HJ1uKIsiKuMiZDHxmD1wVZK2Ai58ThtjNZ53UoG+ighvBZowy5yhAkB42XvLS+teOlQnK/F4p14LB2AV/XAj/+JNqOSVLXxCTzQgW3tBp5UzNMKWOrdJfD2uCmdpu351TiU7ZfUPKM6JAkBJZx3bqduaL7YEKXKn5AZjcm0WNL2kZFuTfeVybVQEjvnknQ0NfN+ZO9BvZQAAh9zR6tP/siyRTZhI2hkDzuQP&quot;;
	 * // 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	 * public static final String RSA_ALIPAY_PUBLIC = &quot;MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+4RKRHjNaTjqZDkjV8CEAdUfhfVPPVcp9lzzY6hwQnEMkBbs5wQXbsPMF8Fhog1EEigBn+NjcMPSMddM6UWyYdq+yn1TPUF/CihXwgJmrJqMHzSO6tGSuoyFztVFrZl1DrnEuTi4Six+Px/nG1SHJHMfp4u8yyFvjurRGkH2lwQIDAQAB&quot;;
	 * </pre>
	 * 
	 */

	// 百会易泊: 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String PARTNER = "2088211505932945";

	// 商户收款的支付宝账号
	public static final String SELLER = "baihuiyibo@163.com";

	// 商户（RSA）私钥
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPPTykbq3PGbAORBebi33T98vQMCw7Ya2lZowWy+5mGVuxnOkw7W0EXGgFTYt9gb3fS3/gPFqZlz2MXgaRO9eilBxV/i0uIJxXdLIhafZDgMncxD3Bmn0PZqKGEzltt0pjsmSOJ3kJJEhfF4Ue6jIG7OW07ZH3YT0E01PK9z27ctAgMBAAECgYB/WtUhm5UR4StjDcv9LpQyj/3Nr8j3uijmz0qQeVmuCN40SZDIRG1+tILdTmEgUNXq95uG7tx6GWdVW7SmqeY+12qyUqckIkBtPUWG48et03rWTJLKtUIQZWo5GLeAbEoP5nDB7ojJPPWoADm2CW4mbP7G9TNhZGKGzRg1Zg0gIQJBAPuGzyWvYTsFSdRdnpSOh+O3AsN7515vSZnYpq6Wp1SKBmpnhoykrXCH54p9DfsWR1LKUbJo1j077cmd9aWX9RkCQQD4Ke0oIa4SmY/Y4YeQy24wmXmY1mxsx4styhE0VvB7qNrI9oNjNZrKtlG9TC6FxXT1vDsBxD0MMbxvQSoxM+E1AkEA826xpUNr0vd3UPWauSSJGDbTkealMpx6m083Ytmj8Qn0056GdjAbPEIMMdh+Y3hYZyQwlX8pefI6XVtnl3DOYQJBAN0pUEehrGa7IC1i4NQz2CHLxUhmX/6Z3vmRyb9hc7RhCYpr1zAFR8w8q6AOUq+5B8EwXU33u4gwkwR7iMSMvbkCQQCkfsvzJ+xUBqoIUjZbRMd1lfpZv/zW88YheZLaMmNpDQmo3pLTXTLWae9mtPvjtJSJ9HSTOc7fuYs7R8R+AgXd";

	// 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDjRvJqDiwLkDtaT6FH8Z+qUXnjwwvVlOJpJISlkl/PdicGHrfMPzQktQnl+j8hPDSMEoBzxKBacFsY/D5UNztGqvqRw1L4LLAL8Uc9/2X9g2VmonmpK/ywQ/y1T+xMRuBf6hROgRO4eBMFtC3mih/sQgPJtfel5SmwvDpyvBZI7QIDAQAB";

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	private Activity mActivity;
	/**
	 * 异步通知，用来判断支付成功后跳转到那个界面
	 */
	private String mNotifyUrl;

	/**
	 * Handler
	 */
	private Handler mHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.what) {
			case SDK_PAY_FLAG:

				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				String resultStatus = payResult.getResultStatus();

				LogUtil.showLog(2, "【支付宝resultInfo】" + resultInfo);
				LogUtil.showLog(2, "【支付宝resultStatus】" + resultStatus);

				if (mNotifyUrl.equals(Constant.ALIPAY_NOTIFY_URL)) {
					/*
					 * 支付宝支付回调
					 */
					if (TextUtils.equals(resultStatus, "9000")) {
						ToastUtil.showToast("支付成功");
						if (!ParkApplication.mOrderNum.isEmpty()) {
							Intent intent = new Intent(mActivity,
									PayResultActivity.class);
							mActivity.startActivity(intent);
							mActivity.finish();
						}
					} else {
						if (TextUtils.equals(resultStatus, "8000")) {
							ToastUtil.showToast("支付结果确认中");
						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							ToastUtil.showToast("支付失败");
						}
					}
					// //////////////////////////////////////////////////////////////////////////////////////
				} else if (mNotifyUrl.equals(Constant.ALIPAY_RECHARGE_URL)) {
					/*
					 * 支付宝充值回调
					 */
					ParkApplication.isRecharge = false;
					if (TextUtils.equals(resultStatus, "9000")) {
						ToastUtil.showToast("充值成功");
						if (!ParkApplication.rechargeOrderNum.isEmpty()) {
							mActivity.finish();
						}
					} else {

						if (TextUtils.equals(resultStatus, "8000")) {
							ToastUtil.showToast("充值结果确认中");
						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							ToastUtil.showToast("充值失败");
						}
					}
					// //////////////////////////////////////////////////////////////////////////////////////
				} else if (mNotifyUrl.equals(Constant.ALIPAY_PARKCARD_URL)) {
					/*
					 * 支付宝租赁回调
					 */
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT)
								.show();
						if (!ParkApplication.mLeaseOrderNum.isEmpty()) {
							Intent intent = new Intent(mActivity,
									LeaseResultActivity.class);
							mActivity.startActivity(intent);
							mActivity.finish();
						}
					} else {
						if (TextUtils.equals(resultStatus, "8000")) {
							ToastUtil.showToast("支付结果确认中");
						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							// ToastUtil.showToast("支付失败");
							final DialogUtil mLogoutDialog = new DialogUtil(
									mActivity);
							mLogoutDialog.setMessage("支付失败，是否继续支付？");
							mLogoutDialog.setPositiveButton("是",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mLogoutDialog.dismiss();
										}
									});
							mLogoutDialog.setNegativeButton("否",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mLogoutDialog.dismiss();
											mActivity.finish();
										}
									});
						}
					}
					// //////////////////////////////////////////////////////////////////////////////////////
				}

				break;
			case SDK_CHECK_FLAG:
				ToastUtil.showToast("检查结果为：" + msg.obj);
				break;
			}

			return false;
		}
	});

	public AliPay(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	/**
	 * <pre>
	 * 功能说明：创建订单信息
	 * 日期：	2015年9月21日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param orderNum	商户网站唯一订单号
	 * @param subject	商品名称
	 * @param body	商品详情
	 * @param price	商品金额
	 * @param notify_url 服务器异步通知页面路径
	 * </pre>
	 */
	public void pay(String orderNum, String subject, String body, String price,
			String notifyUrl) {

		mNotifyUrl = notifyUrl;

		// check();
		String orderInfo = getOrderInfo(orderNum, subject, body, price,
				notifyUrl);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		LogUtil.showLog(2, "【支付宝提交参数】" + payInfo);
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mActivity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * <pre>
	 * 功能说明：查询终端设备是否存在支付宝认证账户
	 * 日期：	2015年11月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(mActivity);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}

	/**
	 * 创建订单
	 */
	public String getOrderInfo(String orderNum, String subject, String body,
			String price, String notifyUrl) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNum + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";
		// orderInfo += "&notify_url=" + "\"" +
		// "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		/**
		 * <pre>
		 *  设置未付款交易的超时时间
		 *  默认30分钟，一旦超时，该笔交易就会自动被关闭。
		 *  取值范围：1m～15d。
		 *  m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		 *  该参数数值不接受小数点，如1.5h，可转换为90m。
		 * </pre>
		 */
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
