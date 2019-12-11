package cn.com.unispark.fragment.home.pay.uubeepay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;

import com.google.gson.Gson;
import com.uubee.prepay.api.OnResultListener;
import com.uubee.prepay.api.PrepayAgent;

/**
 * <pre>
 * 功能说明： 有贝先付工具类
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
public class UubeePay {

	/**
	 * 有贝先付
	 */
	private String BASE_PAYINFO_PAY = "{\"valid_order\":\"10080\",\"notify_url\":\"http://payhttp"
			+ ""
			+ ".xiaofubao.com/zhifu/back.shtml\",\"no_order\":\"7234567994\",\"busi_partner\":\"101001\"}";

	/**
	 * 有贝正式私钥
	 */
	// private String RSA_PRIVATE_KEY =
	// "MIICXgIBAAKBgQDG+OCv7dcCdchCohSJWzgYN+TJJC2INtQNeLdBDUV4+KuHnmpKhbOEEuHbPWW+ozEGcZY+ome1Ak+4/VUVMshDJYsGO2AEY7o9zXJ0rHKGyn4o/NQMNJWs70mwjHRDYm9+p2OCx4TFaZsikJS8hCPXHAEH2J5Lo8RO12KzetUHEwIDAQABAoGBAJROk6Nug1HZGvDXwLj1HiWnG8f1SF78hyiXbvh0/PCDYc9Pe+iaKclrDaAN7pWfK/6ikZuUD/rygxve8BlB5dQeZyUwOvp/xnwmTbpZX9knTNjt5t798ufUHF/aXC3qloJxdsKG/do8p7geXIjJIQXJQGZ9GklGdi/0NKNB2+8pAkEA4gfTWxy1cDxQcpou1XJQtIAUaChFoyziONr+2HHTN9rVjCa8IP4UCUgwW/ueKRi5sRemQZuds9SmX3q/Yr4WLQJBAOFam1ynY+uUIH0n6qhM6m5m7xR9whaUa9y5hStq1eIUV+XIJUWDwcWFX2uC6HETeWOwZq5f2c1/XAa/QJQhGj8CQQCrp7vIpw3X1cZWsmc87JsD6X0V+Pehy5VmDlKaOB9rfMLOtCANgkffILaxNG7R0Rg8uLQngTIL/0P2oPa125xlAkEAtRGKV64fuP+yAx35i/URQFfwrbRGKMJk0Fs4RyR2aksSPQzN9cbiAQUjIE102rZdCN6KAn39kQJOPRWam+rlHQJAGNl/UzwXIv0oyX7IQprAxu12JB2B/gJtXUN/+AV/GGeXNBPC7e8Y96MqZC49/FpbhuMXtjgestc566FT4sHOzQ==";
	// private static String OID_PARTNER = "2015051800001010";

	/**
	 * 有贝测试私钥
	 */
	// private String RSA_PRIVATE_KEY =
	// "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALQdPTwH5g1lxuC1LZG6ux3HBQr0Le1kBPVGaFaGagfb+movNb6lGXYNJ6fx4LAAtcuIi+qSeNJ0u879O7LY8+iydLTBeJkDeQ5Wo7wAaHth9pitu4qHb63A4cqOu/ks03T5f8NY3jq9vP3G4OorpAdL1hpZ9oLKNFQpaZwHFNvrAgMBAAECgYEAqUk2DZ8q5Xq8YuVG8Ep+pi0xmysHo7C3TEUhuflZ3QsvoO/AZvBMLv2MQTiAw0vXknZDc3S5pKzc/F5c5ussyvwc3VQhOWF9j7m8drNJzGjTNMYGlFb0kvyY4Gbhts3eNjG5donvzeMMGX0smy7zPxhE7cV9rQZRMBjkdHAYQnkCQQDhMRIBlKXKcvXLtBeCdVVT4WQAv+7IQtBrQiiPrlbmix/kpdxrwtbTSQJFTSZiVyBbQLL1SsxLxsZRzIpafsF/AkEAzMFpmYxxQYxQgs5b7r86eajXuYp7wnaKADi2ZBpXGPknvk+t3b2cHmD2T1naWwJ1dO/dPYgKFI5RkRdp0lZDlQJAViSdTLl+Sll7qTb1H1SfFX1M8fR59WWJsN4YAg6g84QY6TCBhz97SkvNcJE4s7m8iOvRCYBZcktRpgzmiAOhJQJAVLIooUhTzN5iq0j7BC373Tk3Stt6x/zKpr7Oy2eN4Zmmk/VXoSJ+gp5vJHGxffXtahqSNJKiQNCB8vdxYX0nZQJAGcwuxUTixkdMLAWpJhHHAQ7SJjp9QeYAiv54uCjfzr8BWM0GNjhIu5aJzpW5voFtedIoKRi8cytvZlOJkjdy0w==";
	// private static String OID_PARTNER = "2015042800022589";
	// private static String OID_PARTNER = "2015052000041939";

	/**
	 * 无忧正式私钥
	 */
	private String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMAyAkctiBvpGHPEqRAWr+VEDGe2i2BAG0xTJ0Kl977VgV3iZKlODb5sIzNEsrZWj4JcjioiPMwydsmAjkXHgkiwlotyyJEA0Z5Zx2N2QZFU1GZY9eqAxm+poi99vWCuoNS7TYWCjynD3gULQgtk/ioLT+qi7pTGLUgQR1oSkVznAgMBAAECgYB+NVjBnOiwS1U/UHkkk4FWMGVlBb5oqjBC4qf5YDtcGaAmIrCvbQ4haamD1Sz2B8mddWH10R3TiZ/vmnfS4rxuE0bkasEVGzbCUcnnvfnPRQ8J2Sezj7w7QrRj9xPuR5bVE2bp2EUQ8zpW6b7k1AwW5k/nTaOuzsguIDQd5YQ1gQJBAO70sd75iPCAvKcPBAjd54TaopRDywJn70wChej3Yx3iyn1ipRJ4yiQ3ET8BXNqU95be0Cc5+aMcVtvt25MTc3ECQQDN53kbSHQKDxT+q19C7nhmIEUcjtULorN7TAKxZfMfZ526UBC4hd138bNQxlFMU6t0/wZ2mIbBLv0ZZsI623nXAkEAlWkm9rasa0TzsLlw2mJ16toSIgapnx2BwNMrC9nzfbJazj+p23zvV+mevPiLKJlQnmM/X+eeMeD8ZpO5YaMd4QJABwgGjLX2sHk/YEr9381A80va0FTYVaNiua0o0mIG4WWqbzhYudRocbYhR3reP9sDmeUzlU00HNi77+ggbd4c1QJBAIgKHAB65Qbr0kjUl8wUrp1ZvUUmRhGKBOjXZY5gWZi5BsqkESY+ViZ2K5ReEVn3ody6XCCQ81r/meZ9OWHVVzM=";
	private String OID_PARTNER = "201507280000005002";

	/**
	 * <pre>
	 * 功能说明：有贝先付【额度查询】
	 * 日期：	2015年9月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param userId	用户id号
	 * @param phone		用户手机号或者账号
	 * </pre>
	 */
	public void creditQuery(String userId, String phone, final TextView view) {
		JSONObject queryJson = new JSONObject();
		try {
			queryJson.put("oid_partner", OID_PARTNER);
			queryJson.put("user_id", userId);
			queryJson.put("mob_user", phone);

			OnResultListener listener = new OnResultListener() {
				@Override
				public void onResult(String response) {
					Gson gson = new Gson();
					ResultCredit result = gson.fromJson(response,
							ResultCredit.class);
					LogUtil.showLog(2, "查询额度 : " + response);
					if (result.isSuccess()) {
						if (result.acct_balance == null) {
							view.setText("首单抢5元红包");
						} else {
							view.setText("可用余额：" + result.acct_balance);
						}
					} else {
						if (result.acct_balance == null) {
							view.setText("首单抢5元红包");
						} else {
							view.setText("可用余额：" + result.acct_balance);
						}
					}

				}
			};

			queryJson.put("sign_type", SignUtils.SIGN_TYPE_RSA);
			JSONObject signJson = SignUtils.addRSASignature(
					queryJson.toString(), RSA_PRIVATE_KEY);
			PrepayAgent.creditQuery(ParkApplication.applicationContext,
					signJson.toString(), listener);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <pre>
	 * 功能说明：有贝先付【创建支付账单】
	 * 日期：	2015年9月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param userId	用户id号（不可为空）
	 * @param phone		用户手机号或者是用户账号（不可为空）
	 * @param orderNum	商品唯一订单号（不可为空）
	 * @param productName	商品名称	（可为空）
	 * @param productPrice	商品价格（不可为空）
	 * @param risk		风控参数（不可为空）
	 * @param notifyUrl	 	异步通知的URL
	 * </pre>
	 */
	public void pay(String userId, String phone, String orderNum,
			String productName, String productPrice, String risk,
			String notifyUrl) {

		try {

			JSONObject payJson = new JSONObject(BASE_PAYINFO_PAY);

			// 商户编号
			payJson.put("oid_partner", OID_PARTNER);

			// 商户用户ID
			payJson.put("user_id", userId);

			// 签名方式：目前仅支持RSA
			payJson.put("sign_type", SignUtils.SIGN_TYPE_RSA);

			// 风控参数
			payJson.put("risk_item", risk);

			// 手机号码
			payJson.put("mob_user", phone);

			// 商户业务类型: 虚拟商品销售--101001, 实物商品销售--109001 , 外部账户充值--108001
			payJson.put("busi_partner", "109001");

			// 商户唯一订单号
			payJson.put("no_order", orderNum);

			// 商户订单时间
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.SIMPLIFIED_CHINESE);
			String orderStr = mFormat.format(date);
			payJson.put("dt_order", orderStr);

			// 订单金额
			payJson.put("money_order", productPrice);

			// 异步通知的URL
			payJson.put("notify_url", notifyUrl);

			// 订单有效时间
			payJson.put("valid_order", "4320");

			// 商品名称
			if (productName.isEmpty()) {
				payJson.put("name_goods", "交停车费");
			} else {
				payJson.put("name_goods", productName);
			}

			// 订单描述
			// payJson.put("info_order", "");

			OnResultListener listener = new OnResultListener() {
				@Override
				public void onResult(String response) {
					ResultPay result = new Gson().fromJson(response,
							ResultPay.class);

					ToastUtil.showToast(result.ret_msg);

					if (result.isSuccess()) {
						if (SignUtils.verifyRSASignature(response,
								PayFeeActivity.activity)) {

							ToastUtil.showToast("交易成功");
							ToolUtil.IntentClass(PayFeeActivity.activity,
									PayResultActivity.class, true);
						} else {
							ToastUtil.showToast("签名验证失败！");
						}

					} else {
						ToastUtil.showToast("交易取消");
					}
				}
			};

			// 签名串
			// payJson.put("sign", "");

			// 签名字段
			// payJson.put("sign_ﬁeld", "");

			JSONObject signedJson = SignUtils.addRSASignature(
					payJson.toString(), RSA_PRIVATE_KEY);

			// 第三个参数：false是非卡前置，true是卡前置
			PrepayAgent.createOrder(ParkApplication.applicationContext,
					signedJson.toString(), listener, true, false);
			LogUtil.showLog(3, "【有贝提交账单】" + signedJson.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 功能说明：有贝先付【账单查询】
	 * 日期：	2015年9月9日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param userId	用户id号
	 * @param phone		用户手机号或者账号
	 * </pre>
	 */
	public void queryBill(String userId, String phone) {
		try {
			final JSONObject json = new JSONObject();
			json.put("oid_partner", OID_PARTNER);
			json.put("user_id", userId);
			json.put("mob_user", phone);

			OnResultListener listener = new OnResultListener() {
				@Override
				public void onResult(String result) {
					ResultBase payResult = new Gson().fromJson(result,
							ResultBase.class);
					ToastUtil.showToast(payResult.ret_msg);
					if (ResultBase.RES_SUCCESS.equals(payResult.ret_code)) {

					}
				}
			};
			PrepayAgent.billQuery(BaseActivity.activity, json.toString(),
					listener);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
