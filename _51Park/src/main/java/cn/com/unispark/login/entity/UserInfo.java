package cn.com.unispark.login.entity;

import java.io.Serializable;

/**
 * <pre>
 * 功能说明： 用户信息实体类
 * 日期：	2015年10月26日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月17日
 * </pre>
 */
public class UserInfo implements Serializable {

	public UserInfo() {
		super();
	}

	private static final long serialVersionUID = 1L;
	private String uid;// 用户uid
	private String token;// 获取到的凭证
	private String userid;// 用户uid
	private int sex;// 性别('0'未设置,'1'男,'2'女)
	private String username;// 用户名(手机号)
	private String userscore;// 用户余额
	private String cardno;// 车牌号
	private String qr;// 用户二维码信息
	private String name;// 用户姓名
	private String card_no_qr;// 会员卡号
	private String binddate;// 绑卡日期（例如：2015-03-16 18:24:54）
	private String regdate;// 注册时间（例如：20150316182454，连连支付使用）
	private String noagree;// 用户在连连支付签约号
	private int autopay;// 1：关闭自动支付;2：开启自动支付

	public String getUserid() {
		
		if(userid == null){
			userid = uid;
		}
		
		return userid;
	}

	public void setUserid(String userid) {
		this.uid = userid;
		this.userid = userid;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserscore() {
		return userscore;
	}

	public void setUserscore(String userscore) {
		this.userscore = userscore;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCard_no_qr() {
		return card_no_qr;
	}

	public void setCard_no_qr(String card_no_qr) {
		this.card_no_qr = card_no_qr;
	}

	public String getBinddate() {
		return binddate;
	}

	public void setBinddate(String binddate) {
		this.binddate = binddate;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getNoagree() {
		return noagree;
	}

	public void setNoagree(String noagree) {
		this.noagree = noagree;
	}

	public int getAutopay() {
		return autopay;
	}

	public void setAutopay(int autopay) {
		this.autopay = autopay;
	}

	public String getUid() {
		
		if(uid == null){
			uid = userid;
		}
		
		return uid;
	}

	public void setUid(String uid) {
		this.userid = uid;
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserInfo [uid=" + uid + ", token=" + token + ", userid="
				+ userid + ", sex=" + sex + ", username=" + username
				+ ", userscore=" + userscore + ", cardno=" + cardno + ", qr="
				+ qr + ", name=" + name + ", card_no_qr=" + card_no_qr
				+ ", binddate=" + binddate + ", regdate=" + regdate
				+ ", noagree=" + noagree + ", autopay=" + autopay + "]";
	}

}
