package cn.com.unispark.application;

/**
 * <pre>
 * 功能说明： 保存一些常量以及URL,运用的全局变量，在整个项目中调用
 * 日期：	2015年6月2日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月2日
 * </pre>
 */
public class Constant {

	/*
	 * CSA
	 */
//	private static final String HOST = "http://alphaapi.51park.cn/memv2";

	/*
	 * CSB
	 */
	 private static final String HOST = "http://betaapi.51park.cn/memv2";

	/*
	 * ZS
	 */
//	 private static final String HOST = "http://api.51park.com.cn/memv2";

	/**
	 * APPID和APPKEY
	 */
	public static final String APP_ID = "2013033101";
	public static final String APP_KEY = "4a9a404261c36c2051f8eb704920d5cd";
	
	/**
	 * 联系客服，客服电话
	 */
	public static final String CONTACT_PHONE = "4007095151";

	/**
	 * 用户二维码展示
	 */
	public static final String QRCODE_URL = "http://interface.51park.com.cn/index.php?s=/User/Index/bind/CardID/";

	/**
	 * 关于我们
	 */
	public static final String ABOUT_US_URL = "http://www.51park.com.cn/download/51Park/";

	/**
	 * 支付时必填项subject="无忧停车手机自助交费"
	 */
	public static final String SUBJECT = "无忧停车手机自助交费";

	/**
	 * 支付时必填项body="移动终端自助交停车费"
	 */
	public static final String BODY_PAYFEE = "移动终端自助交停车费";

	/**
	 * 支付时必填项body="无忧停车账户手机充值"
	 */
	public static final String BODY_RECHARGE = "无忧停车账户手机充值";

	/**
	 * 支付时必填项body="无忧停车账户手机购买"
	 */
	public static final String BODY_BUY = "无忧停车账户手机购买";

	/**
	 * 缓存JSON的列名
	 */
	public static final String ID = "id";

	/**
	 * 创建数据库名称
	 */
	public static final String DB_MSG_NAME = "message.db";// 消息推送
	public static final String DB_SEARCH_HISTORY = "search.db";// 历史搜索记录
	// public static final String DB_SEARCH_HISTORY = "searchhistory.db";//
	// 历史搜索记录

	/**
	 * 创建数据表名称
	 */
	public static final String TABLE_HISTORY_NAME = "HistoryListDB";
	public static final String TABLE_MSG_NAME = "MSGListDB";

	// /////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 用户登录的URL
	 */
	public static final String LOGIN_URL = HOST + "/user/login.php";

	/**
	 * 短信获取验证码的URL
	 */
	public static final String SENDSMS_URL = HOST + "/user/sms.php";

	/**
	 * 百宝箱列表
	 */
	public static final String TREASURE_LIST_URL = HOST
			+ "/other/treasurelist.php";

	/**
	 * 活动专区列表数据的URL
	 */
	public static final String ACTIVITY_LIST_URL = HOST
			+ "/other/activitylist.php";

	/**
	 * 获取账单(点击交停车费)的URL
	 */
	public static final String GET_ORDER_URL = HOST + "/order/getorder.php";

	/**
	 * 获取当前账单列表(首页账单展示)（new）
	 */
	public static final String GET_CURRENTLIST_URL = HOST
			+ "/order/getcurrentlist.php";

	/**
	 * 获取订单详情的URL
	 */
	public static final String INFO_ORDER_URL = HOST + "/order/info.php";

	/**
	 * 车位租赁列表页的URL
	 */
	public static final String PARKCARD_LIST_URL = HOST
			+ "/monthly/parkcardlist.php";
	/**
	 * 设置默认车牌的URL
	 */
	public static final String PLATE_DEFAULT_URL = HOST
			+ "/user/platedefault.php";
	/**
	 * 车位租赁详情的URL
	 */
	public static final String PARKCARD_INFO_URL = HOST
			+ "/monthly/parkcardinfo.php";
	/**
	 * 车位租赁详情的URL
	 */
	public static final String PARKCARD_INFO_NEW_URL = HOST
			+ "/monthly/parkcardinfonew.php";
	/**
	 * 我的租赁列表页的URL
	 */
	public static final String MY_PARKCARD_URL = HOST
			+ "/monthly/myparkcard.php";

	/**
	 * 我的租赁列表(过期)页的URL
	 */
	public static final String MY_EXPIRECARD_URL = HOST
			+ "/monthly/myexpirecard.php";

	/**
	 * 车位租赁购买的URL
	 */
	public static final String PARKCARD_PAY_URL = HOST
			+ "/monthly/parkcardpay.php";
	/**
	 * 车牌管理列表的URL
	 */
	public static final String PLATELIST_URL = HOST + "/user/platelist.php";

	/**
	 * 车牌管理的URL（添加或删除）
	 */
	public static final String PLATEMANAGE_URL = HOST + "/user/platemanage.php";

	/**
	 * 车牌管理的URL修改
	 */
	public static final String PLATEMODIFY_URL = HOST + "/user/platemodify.php";

	/**
	 * 车牌找回的URL修改
	 */
	public static final String PLATEBACK_URL = HOST + "/user/plateback.php";

	/**
	 * 购买日卡月卡支付宝回调的URL
	 */
	public static final String ALIPAY_PARKCARD_URL = HOST
			+ "/monthly/alipay_parkcard.php";

	/**
	 * 购买日卡月卡微信回调的URL
	 */
	public static final String WEIXIN_PARKCARD_URL = HOST
			+ "/monthly/weixin_parkcard.php";

	/**
	 * 消息推送列表的URL
	 */
	public static final String MSG_LIST_URL = HOST + "/notice/noticelist.php";

	/**
	 * 消息通知详情的URL
	 */
	public static final String MSG_DETAIL_URL = HOST
			+ "/notice/noticedetail.php";

	/**
	 * 车秘页面的URL
	 */
	public static final String CHEMI_URL = HOST + "/other/chemi.php";
	
	/**
	 * 车行易的URL
	 */
	public static final String CHEXINGYI_URL = HOST + "/pay/parkeasy.php";

	/**
	 * 摇一摇的URL
	 */
	public static final String SHAKE_URL = "http://www.51park.cn/wap99/a8/n_13.html";
	
	/**
	 * 注册协议/服务协议
	 */
	public static final String PARK_SERVER_URL = HOST + "/other/park_server.html";

	/**
	 * 设置别名的URL
	 */
	public static final String ALIAS_URL = HOST + "/notice/noticealias.php";

	/**
	 * 退出登录的URL
	 */
	public static final String UNLOGIN_URL = HOST + "/user/logout.php";

	/**
	 * 停车记录的URL
	 */
	// public static final String RECORDS_URL = HOST +
	// "/usertest/map/getrecord.php";
	// public static final String RECORDS_URL = HOST + "/order/records.php";
	public static final String RECORDS_URL = HOST + "/order/getrecords.php";

	/**
	 * 余额明细列表的URL
	 */
	public static final String BALANCECHARGE_URL = HOST
			+ "/pay/balancecharge.php";

	// alpha测试地址：http://alphaapi.51park.cn/memv2/pay/balancecharge.php

	/**
	 * 用户反馈的URL
	 */
	public static final String FEEDBACK_URL = HOST + "/other/feedback.php";

	/**
	 * 活动中心列表的URL
	 */
	public static final String ACTION_CENTER_URL = HOST
			+ "/other/activitycenter.php";
	/**
	 * 用户基本信息的URL
	 */
	public static final String USER_INFO_URL = HOST + "/user/info.php";

	/**
	 * 用户获取优惠券的URL
	 */
	public static final String GET_POUPONS_URL = HOST + "/user/coupons.php";
	/**
	 * 优惠券兑换码的URL
	 */
	public static final String COUPONCODE_URL = HOST + "/user/couponcode.php";
	/**
	 * 有贝先付获取风控参数的URL
	 */
	public static final String UBINFO_URL = HOST + "/pay/ubinfo.php";

	/**
	 * 修改个人资料的URL
	 */
	public static final String MODIFY_INFO_URL = HOST + "/user/modify.php";

	/**
	 * 绑定无忧会员卡的URL
	 */
	public static final String BIND_URL = HOST + "/user/bind.php";

	/**
	 * 余额支付的URL
	 */
	public static final String BALANCE_PAY_URL = HOST + "/order/balancepay.php";

	/**
	 * 零元支付的URL--[优惠券的金额]>=交费金额时或是没产生交费金额时
	 */
	public static final String PAY_ZERO_URL = HOST + "/order/payzero.php";

	/**
	 * 会员卡解绑的URL
	 */
	public static final String UNBIND_URL = HOST + "/user/unbind.php";

	/**
	 * 余额明细--充值记录列表的URL
	 */
	public static final String RECHARGE_LIST_URL = HOST + "/recharge/list.php";
	/**
	 * 获取城市列表(设置界面)[城市ID信息，注：IOS APP适配]的URL
	 */
	public static final String GET_CITY_URL = HOST + "/map/getcity2.php";
	/**
	 * 余额明细--余额消费列表的URL
	 */
	public static final String BALANCE_URL = HOST + "/pay/balance.php";
	/**
	 * 余额明细详情--余额消费列表的URL
	 */
	public static final String CONSUM_BCINFO_URL = HOST + "/order/bcinfo.php";
	/**
	 * 充值配置的URL(获取充值信息)
	 */
	public static final String RECHARGE_CONFIG_URL = HOST
			+ "/recharge/config.php";
	/**
	 * 自动支付的URL
	 */
	public static final String AUTO_PAY_CONFIG_URL = HOST + "/user/config.php";
	/**
	 * 充值订单确定的URL
	 */
	public static final String RECHARGE_CONFIRM_URL = HOST
			+ "/recharge/confirm.php";

	/**
	 * 支付宝充值回调的URL
	 */
	public static final String ALIPAY_RECHARGE_URL = HOST
			+ "/recharge/alipay.php";

	/**
	 * 微信充值回调的URL
	 */
	public static final String WEIXIN_RECHARGE_URL = HOST
			+ "/recharge/weixin.php";

	/**
	 * 支付成功后回调的URL
	 */
	public static final String RETURN_URL = HOST + "/order/returnurl.php";

	/**
	 * 支付宝支付回调的URL
	 */
	public static final String ALIPAY_NOTIFY_URL = HOST + "/pay/alipay.php";

	/**
	 * 微信支付回调的URL
	 */
	public static final String WEIXIN_NOTIFY_URL = HOST + "/pay/weixin.php";

	/**
	 * 有贝支付回调的URL
	 */
	public static final String UUBEE_NOTIFY_URL = HOST + "/pay/uubee.php";

	/**
	 * 连连支付回调的URL
	 */
	public static final String LLPAY_NOTIFY_URL = HOST + "/llpay/return.php";

	/**
	 * 连连用户签约信息查询的URL
	 */
	public static final String LLPAY_QUERY_URL = HOST + "/llpay/bankcard.php";

	/**
	 * 连连银行卡卡BIN查询的URL
	 */
	public static final String LLPAY_BIN_URL = HOST + "/llpay/binquery.php";

	/**
	 * 连连信用卡签约申请的URL[绑卡申请]
	 */
	public static final String LLPAY_BIND_URL = HOST + "/llpay/llbind.php";

	/**
	 * 连连银行卡签约验证的URL[绑卡验证]
	 */
	public static final String LLPAY_VERIFY_URL = HOST
			+ "/llpay/bindverify.php";

	/**
	 * 连连信用卡解绑的URL
	 */
	public static final String LLPAY_UNBIND_URL = HOST
			+ "/llpay/bankcardunbind.php";

	/**
	 * 连连银行卡支付申请的URL
	 */
	public static final String LLPAY_PREPAY_URL = HOST
			+ "/llpay/bankcardprepay.php";

	/**
	 * 连连银行卡支付接口（已绑定银行卡）的URL[无密支付]
	 */
	public static final String LLPAY_WUMI_URL = HOST + "/llpay/bankcardpay.php";

	/**
	 * 附近停车场的URL
	 */
	public static final String PARK_LIST_URL = HOST + "/map/parklist.php";

	/**
	 * 附近停车场数量的URL
	 */
	public static final String PARK_LIST_COUNT_URL = HOST
			+ "/map/parklistcount.php";

	/**
	 * 附近停车场推荐列表的URL
	 */
	public static final String PARK_RECOMMEND_URL = HOST
			+ "/map/parkrecommend.php";

	/**
	 * 停车场详情的URL
	 */
	public static final String PARK_DETAIL_URL = HOST + "/map/parkdetail.php";

	/**
	 * 根据城市查询停车场的URL
	 */
	public static final String CITY_PARK_URL = HOST + "/map/citypark.php";

	// /**
	// * 获取城市列表(设置界面)[城市ID信息，注：IOS APP适配]的URL
	// */
	// public static final String GET_CITY_URL = HOST + "/map/getcity2.php";

	/**
	 * 获取城市列表(租赁城市选择)[注：无索引]的URL
	 */
	public static final String GET_CITY_LIST_URL = HOST
			+ "/map/getcitylist.php";

	/**
	 * 获取区域信息的URL
	 */
	public static final String GET_DISTRICT_URL = HOST + "/map/getdistrict.php";

	/**
	 * 停车场出入口的URL
	 */
	public static final String ENEXIT_URL = HOST + "/map/enexit.php";


	/**
	 * 微信支付统一下单
	 */
	public static final String WEIXIN_PAY_URL = HOST + "/pay/weixinpay.php";

}
