package cn.com.unispark.fragment.mine.msgpush.util;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.com.unispark.MainActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.MapFragment;
import cn.com.unispark.fragment.home.pay.OrderDetailsActivity;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.fragment.mine.coupons.CouponsFragmentActivity;
import cn.com.unispark.fragment.mine.creditcard.CreditBindOneActivity;
import cn.com.unispark.fragment.mine.msgpush.MsgActivity;
import cn.com.unispark.fragment.mine.msgpush.MsgDetailActivity;
import cn.com.unispark.fragment.mine.parkrecord.ParkRecordActivity;
import cn.com.unispark.fragment.mine.vipcard.UserQrActivity;
import cn.com.unispark.fragment.mine.vipcard.VipCardActivity;
import cn.com.unispark.login.SplashActivity;
import cn.jpush.android.api.JPushInterface;

public class MsgReceiver extends BroadcastReceiver {
	public static final String MESSAGE_RECEIVED_ACTION = "cn.com.unispark.main.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("rjf ", "rjf " + intent.getAction() + ", extras: "
				+ printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
//			processCustomMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d("rjf ", "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d("slx", "用户点击打开了通知");
//			String title = bundle
//					.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			if (!extras.isEmpty()) {
				extarJson(extras, context);
			} else {
				Intent spintent = new Intent(context, SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(spintent);
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.e(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 解析服务器发送过来的附加字段，json
	@SuppressWarnings("unused")
	private void extarJson(String extras, Context context) {
		try {
			JSONObject jsonObject = new JSONObject(extras); // 将string转化为JSON对象的作用
			Log.e("slx", jsonObject.toString() + "-----------------");
			if (jsonObject != null) {
				String type = jsonObject.getString("type");
				if (type == null || type.equals("")) {
					Intent intent = new Intent(context, SplashActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} else { // 不等于空的时候，根据type的状态值，跳转到相应的页面
					if (type.equals("1001")) {
						// 活动页面type:1001，url:该活动链接地址
						Intent intent = new Intent(context,
								WebActiveActivity.class);
						intent.putExtra("url", jsonObject.getString("url")
								+ "?uid=" + ParkApplication.getmUserInfo().getUid());
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 在非Activity的页面启动一个Activity,不存在Activity的栈，所以说要创建一个
						context.startActivity(intent);
					} else if (type.equals("1002")) {
						// 交停车费拉取到账单页面（交停车费）
						Intent intent = new Intent(context,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1003")) {
						// 找停车位页面type:1003
						Intent intent = new Intent(context, MapFragment.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1004")) {
						// 会员卡页面（绑定会员卡页面）type:1004
						Intent intent = new Intent(context,
								VipCardActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1005")) {
						// 信用卡页面（绑定信用卡页面）type:1005
						Intent intent = new Intent(context,
								CreditBindOneActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1006")) {
						// 停车券页面type:1006
						Intent intent = new Intent(context,
								CouponsFragmentActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1007")) {
						// 停车纪录页面type:1007
						Intent intent = new Intent(context,
								ParkRecordActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1008")) {
						// 我的二维码页面type:1008：
						Intent intent = new Intent(context,
								UserQrActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1009")) {
						// 消息中心页面 type:1009
						Intent intent = new Intent(context,
								MsgActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1011")) { // 消息详情页面 type:1011
														// id:1【id为信息详情id号】
						Intent intent = new Intent(context,
								MsgDetailActivity.class);
						intent.putExtra("id", jsonObject.getString("id"));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1012")) { // 停车纪录详情type:1012，id:1501927456【id为订单号】
						Intent intent = new Intent(context,
								OrderDetailsActivity.class);
						intent.putExtra("id", jsonObject.getString("id"));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else if (type.equals("1013")) { // 车秘页面type:1013
						// 车秘页面
//						doGetCheMiUrl(context);
					} else {
						Intent intent = new Intent(context,
								SplashActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				}
			} else {
				Intent intent = new Intent(context, SplashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * 跳转到chemi界面
//	 */
//	private void doGetCheMiUrl(final Context context) {
//		AQuery aQuery = new AQuery(context);
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("appid", Constant.APP_ID);
//		params.put("appkey", Constant.APP_KEY);
//		params.put("uid", Integer.valueOf(ParkApplication.getmUserInfo().getUid()).intValue());
//		aQuery.ajax(Constant.CHEMI_URL, params, JSONObject.class,
//				new AjaxCallback<JSONObject>() {
//					@Override
//					public void callback(String url, JSONObject json,
//							AjaxStatus status) {
//						Log.e("slx", "json" + json);
//						try {
//							if (json != null) {
//								String ChkApi = json.getString("ChkApi");
//								if (ChkApi.equals("2")) {
//									String chemiUrl = json.getString("url");
//									Intent intent = new Intent(context,
//											WebActiveActivity.class);
//									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//									intent.putExtra("url", chemiUrl);
//									intent.putExtra("titleStr", "汽车专家");
//									context.startActivity(intent);
//								}
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				});
//	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MapActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
//	}
}
