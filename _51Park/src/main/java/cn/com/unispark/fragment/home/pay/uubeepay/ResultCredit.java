package cn.com.unispark.fragment.home.pay.uubeepay;

/**
 * 额度查询结果对象
 */
public class ResultCredit extends ResultBase {
    public String oid_partner;
    public String user_id;
    public String mob_user;
    public String bal_credit;
    public String acct_balance;
    public String partner_sign;
    public String sign_type;
    public String total_credit;
    
	@Override
	public String toString() {
		return "ResultCredit [oid_partner=" + oid_partner + ", user_id="
				+ user_id + ", mob_user=" + mob_user + ", bal_credit="
				+ bal_credit + ", acct_balance=" + acct_balance
				+ ", partner_sign=" + partner_sign + ", sign_type=" + sign_type
				+ ", total_credit=" + total_credit + "]";
	}
    
    
}
