package cn.com.unispark.fragment.home.pay.uubeepay;

/**
 * 额度查询结果对象
 */
public class ResultPay extends ResultBase {
	public String partner_sign;
	public String sign_type;
	public String oid_partner;
	public String dt_order;
	public String no_order;
	public String oid_paybill;
	public String result_pay;
	public String money_order;
	public String settle_date;
	public String info_order;

	@Override
	public String toString() {
		return "ResultPay [partner_sign=" + partner_sign + ", sign_type="
				+ sign_type + ", oid_partner=" + oid_partner + ", dt_order="
				+ dt_order + ", no_order=" + no_order + ", oid_paybill="
				+ oid_paybill + ", result_pay=" + result_pay + ", money_order="
				+ money_order + ", settle_date=" + settle_date
				+ ", info_order=" + info_order + ",result_code="+ret_code+"]";
	}

}
