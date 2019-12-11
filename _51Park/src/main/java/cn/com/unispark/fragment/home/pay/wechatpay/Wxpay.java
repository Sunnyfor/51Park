package cn.com.unispark.fragment.home.pay.wechatpay;

import android.annotation.SuppressLint;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.message.BasicNameValuePair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.MD5Util;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.wxapi.WexinPayEntity;

/**
 * <pre>
 * 功能说明： 微信支付工具类
 * 日期：	2015年1月6日
 * 开发者：	陈丶泳佐
 *
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年1月6日
 * </pre>
 */
@SuppressLint("DefaultLocale")
public class Wxpay {

	private IWXAPI api;
	private BaseActivity activity;

	private LoadingProgress loadingProgress;

	private String order_num, total_fee;

	public Wxpay(BaseActivity activity) {
		super();
		this.activity = activity;
	}

	/**
	 * <pre>
	 * 功能说明：微信支付封装类
	 * 日期：	2015年9月28日
	 * 开发者：	陈丶泳佐
	 *
	 * @param orderNum  订单号
	 * @param totalFee  最终费用
	 * @param type     1：支付
	 *                 2：充值
	 * </pre>
	 */
	public void pay(String orderNum, String totalFee, int type) {

		order_num = orderNum;
		total_fee = totalFee;

		loadingProgress = new LoadingProgress(activity, "正在获取支付信息..");

		api = WXAPIFactory.createWXAPI(activity, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);

		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;

		if (isPaySupported) {

			parseOrderInfo(orderNum, totalFee, type);

			api.unregisterApp();
		} else {
			loadingProgress.dismiss();
			ToastUtil.show("您手机上未安装微信或微信版本过低！");
		}

	}


	/**
	 * 发送请求
	 *
	 * @param result 从后台服务器获取请求支付的参数
	 */
	private void sendPayReq(WexinPayEntity.Weixin result) {

//        应用ID	            appid		是	wx8888888888888888	微信开放平台审核通过的应用APPID
//        商户号	            partnerid	是	1900000109	微信支付分配的商户号
//        预支付交易会话ID	prepayid	是	WX1217752501201407033233368018	微信返回的支付交易会话ID
//        随机字符串	        noncestr	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
//        时间戳	            timestamp	是	1412000000	时间戳，请见接口规则-参数规定
//        签名	            sign		是	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
//        扩展字段	        package		是	Sign=WXPay	暂填写固定值Sign=WXPay

		PayReq req = new PayReq();
		req.appId = Constants.APP_ID;
		req.nonceStr = result.getNonce_str();
		req.packageValue = "Sign=WXPay";
		req.partnerId = Constants.PARTNER_ID;
		req.prepayId = result.getPrepay_id();
		req.timeStamp = result.getTimestamp();

		//开始将6个字段进行数据封装
		List<BasicNameValuePair> list = new LinkedList<>();
		list.add(new BasicNameValuePair("appid", req.appId));
		list.add(new BasicNameValuePair("noncestr", req.nonceStr));
		list.add(new BasicNameValuePair("package", req.packageValue));
		list.add(new BasicNameValuePair("partnerid", req.partnerId));
		list.add(new BasicNameValuePair("prepayid", req.prepayId));
		list.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(list);

		LogUtil.e("【微信req签名】" + req.sign);
		LogUtil.e("【微信result签名】" + result.getSign());


		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
	}


	/**
	 * 生成签名
	 */
	private String genAppSign(List<BasicNameValuePair> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getName());
			sb.append('=');
			sb.append(list.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.PARTNER_KEY);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		LogUtil.e("【微信生成签名】" + sign);
		return sign;
	}


	/**
	 * 【解析】获取预定单参数
	 *
	 * @param orderNum 订单号
	 * @param totalFee 费用
	 * @param type     1：支付
	 *                 2：充值
	 */
	private void parseOrderInfo(String orderNum, String totalFee, int type) {

		Map<String, String> params = new HashMap<>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("out_trade_no", orderNum);
		params.put("total_fee", totalFee);
		params.put("type", String.valueOf(type));

		activity.httpUtil.parse(activity.httpUtil.POST, Constant.WEIXIN_PAY_URL, WexinPayEntity.class, params,
				new HttpUtil.onResult<WexinPayEntity>() {
					@Override
					public void onSuccess(WexinPayEntity result) {
						LogUtil.e("【result】:" + result.getData());
						sendPayReq(result.getData());
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						ToastUtil.show(errMsg);
						LogUtil.e("【result】:" + errMsg);
					}
				});


	}
}
