package cn.com.unispark.fragment.home.pay.lianlianpay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.ToolUtil;

/**
 * 
 * <pre>
 * 功能说明： 连连支付工具类（去除卡前置模式）
 * 日期：	2015年3月28日
 * 开发者：	陈丶泳佐
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年3月28日
 * </pre>
 */
public class LianlianPay {
	private PayOrder order;
	private Activity mActivity;
	private Handler mHandler;

	public LianlianPay(Activity mActivity) {
		this.mActivity = mActivity;
	}

	/**
	 * <pre>
	 * 功能说明：用心痛卡支付：单笔支付完成绑卡操作
	 * 日期：	2015年4月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param goods 商品名称
	 * @param money	商品单价
	 * </pre>
	 */
	public void payOrder(String goods, String money) {
		order = constructPayOrder(goods, money);
		init();
	}

	/**
	 * <pre>
	 * 功能说明：绑定信用卡
	 * 日期：	2015年4月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param id_no	身份证
	 * @param name	姓名
	 * @param card_no	卡号
	 * </pre>
	 */
	public void bindCard(String id_no, String name, String card_no) {
		order = constructBindCard(id_no, name, card_no);
		init();
	}

	/**
	 * <pre>
	 * 功能说明：初始化方法
	 * 日期：	2015年4月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * </pre>
	 */
	private void init() {
		String content4Pay = BaseHelper.toJSONString(order);
		// 关键 content4Pay 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的技术支持
		MobileSecurePayer msp = new MobileSecurePayer();
		mHandler = createHandler();
		msp.pay(content4Pay, mHandler, Constants.RQF_PAY, mActivity, false);
	}

	@SuppressLint("HandlerLeak")
	public Handler createHandler() {
		return new Handler() {
			public void handleMessage(Message msg) {
				String strRet = (String) msg.obj;
				Log.e("slx", "11111111111" + "strRet:" + strRet + " msg.wat:"
						+ msg.what);
				switch (msg.what) {
				case Constants.RQF_PAY: {
					JSONObject objContent = BaseHelper.string2JSON(strRet);
					String retCode = objContent.optString("ret_code");
					String retMsg = objContent.optString("ret_msg");
					Log.e("slx", "11111111111" + "retCode" + retCode
							+ " retMsg:" + retMsg);
					// 先判断状态码，状态码为 成功或处理中 的需要 验签
					if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
						// TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
						String resulPay = objContent.optString("result_pay");
						if (Constants.RESULT_PAY_SUCCESS
								.equalsIgnoreCase(resulPay)) {
							// TODO 支付成功后续处理
							if (!ParkApplication.mOrderNum.isEmpty()) {
								ToolUtil.IntentClass(mActivity,
										PayResultActivity.class, true);
							}

						} else {
							final DialogUtil mDialog = new DialogUtil(mActivity);
							mDialog.setMessage(retMsg);
							mDialog.setPositiveButton("确定",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mDialog.dismiss();
										}
									});

						}
					} else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
						String resulPay = objContent.optString("result_pay");
						if (Constants.RESULT_PAY_PROCESSING
								.equalsIgnoreCase(resulPay)) {
							final DialogUtil mDialog = new DialogUtil(mActivity);
							mDialog.setMessage(retMsg);
							mDialog.setPositiveButton("确定",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mDialog.dismiss();
										}
									});
						}
					} else {
						final DialogUtil mDialog = new DialogUtil(mActivity);
						mDialog.setMessage(retMsg);
						mDialog.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(View v) {
								mDialog.dismiss();
							}
						});
					}
				}
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	/**
	 * <pre>
	 * 功能说明：封装支付方法，返回订单
	 * 日期：	2015年4月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param goods	商品名称
	 * @param money	商品价格
	 * @return
	 * </pre>
	 */
	private PayOrder constructPayOrder(String goods, String money) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String timeString = dataFormat.format(date);

		PayOrder order = new PayOrder();
		// TODO busi_partner 是指商户的业务类型，"101001"为虚拟商品销售，详情请参考接口说明书
		order.setBusi_partner("101001");
		// TODO 商户订单，Demo采用时间戳模拟订单号
		order.setNo_order(ParkApplication.mOrderNum);
		order.setDt_order(timeString);
		order.setName_goods(goods == null ? "停车费" : goods);
		order.setNotify_url(Constant.LLPAY_NOTIFY_URL);
		order.setSign_type(PayOrder.SIGN_TYPE_MD5);// MD5 签名方式
		// order.setSign_type(PayOrder.SIGN_TYPE_RSA);// RSA 签名方式

		// 订单参数
		order.setValid_order("100");
		order.setUser_id(ParkApplication.getmUserInfo().getUid());
		order.setMoney_order(money);
		order.setFlag_modify("0");
		// 风险控制参数
		order.setRisk_item(constructRiskItem());
		String sign = "";
		order.setOid_partner(EnvConstants.PARTNER);
		// TODO 对签名原串进行排序，并剔除不需要签名的串。
		String content = BaseHelper.sortParam(order);
		// MD5 签名方式, 签名方式包括两种，一种是MD5，一种是RSA 这个在商户站管理里有对验签方式和签名Key的配置。
		sign = Md5Algorithm.getInstance().sign(content, EnvConstants.MD5_KEY);
		// sign = Rsa.sign(content, EnvConstants.RSA_PRIVATE);// RSA 签名方式
		order.setSign(sign);
		return order;
	}

	/**
	 * <pre>
	 * 功能说明：封装绑卡操作，返回订单
	 * 日期：	2015年4月2日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param idCard	身份证
	 * @param name	姓名	
	 * @param cardNum	信用卡号
	 * @return
	 * </pre>
	 */
	private PayOrder constructBindCard(String idCard, String name,
			String cardNum) {

		PayOrder order = new PayOrder();
		order.setSign_type(PayOrder.SIGN_TYPE_MD5);// MD5 签名方式
		// order.setSign_type(PayOrder.SIGN_TYPE_RSA);// RSA 签名方式

		order.setUser_id("123456");
		order.setId_no(idCard);
		order.setAcct_name(name);
		order.setCard_no(cardNum);

		String sign = "";
		order.setOid_partner(EnvConstants.PARTNER);
		String content = BaseHelper.sortParam(order);
		// MD5 签名方式
		sign = Md5Algorithm.getInstance().sign(content, EnvConstants.MD5_KEY);
		// RSA 签名方式
		// sign = Rsa.sign(content, EnvConstants.RSA_PRIVATE);
		order.setSign(sign);
		return order;
	}

	/**
	 * <pre>
	 * 功能说明：风险控制参数生成例子，请根据文档动态填写。最后返回时必须调用.toString()
	 * 日期：	2015年3月26日
	 * 开发者：	陈丶泳佐
	 * 
	 * @return
	 * </pre>
	 */
	private String constructRiskItem() {
		JSONObject mRiskItem = new JSONObject();
		try {
			mRiskItem.put("frms_ware_category", "1999");// 商品类目
			mRiskItem.put("user_info_dt_register",
					ParkApplication.getmUserInfo().getRegdate());// 注册时间
			mRiskItem.put("user_info_bind_phone",
					ParkApplication.getmUserInfo().getUsername());// 绑定的手机号
			mRiskItem.put("user_info_mercht_userno", ParkApplication.getmUserInfo().getUid());// 商户用户唯一标识
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return mRiskItem.toString();
	}

}
