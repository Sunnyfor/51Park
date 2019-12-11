package cn.com.unispark.fragment.unknown.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.com.unispark.application.Constant;
import cn.com.unispark.fragment.mine.msgpush.util.MsgStatus;

/**
 * <pre>
 * 功能说明： 保存消息推送的数据库
 * 日期：	2015年7月2日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年7月2日
 * </pre>
 */
public class MsgDBUtils {
	public static long insert(String key, String value, SQLiteDatabase db) {
		boolean isUpdate = false;
		MsgStatus[] msgStatuses = queryAllKey(db);
		for (int i = 0; i < msgStatuses.length; i++) {
			if (key.equals(msgStatuses[i].getKey())) {
				isUpdate = true;
				break;
			}
		}
		long id = -1;
		if (!isUpdate) {
			Log.e("slx", "db--->" + db);
			if (db != null) {
				ContentValues values = new ContentValues();
				values.put("key", key);
				values.put("value", value);
				id = db.insert(Constant.TABLE_MSG_NAME, null, values);
			}
		}
		db.close();
		return id;
	}

	public static void delAllMsgStatus(SQLiteDatabase db) {
		String DELETE_DATA = "DELETE FROM " + Constant.TABLE_MSG_NAME + ";";
		db.execSQL(DELETE_DATA);
	}

	public static MsgStatus[] queryAllKey(SQLiteDatabase db) {
		if (db != null) {
			// Log.e("slx", "select * from MSGListDB");
			Cursor cursor = db.rawQuery("select * from MSGListDB", null);
			int count = cursor.getCount();
			Log.e("slx", "count" + count);
			MsgStatus[] msgStatuses = new MsgStatus[count];
			String key = null;
			String value = null;
			MsgStatus msgStatus = null;
			if (count > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					key = cursor.getString(cursor.getColumnIndex("key"));
					value = cursor.getString(cursor.getColumnIndex("value"));
					msgStatus = new MsgStatus();
					msgStatus.setKey(key);
					msgStatus.setValue(value);
					msgStatuses[i] = msgStatus;
					cursor.moveToNext();
				}
			}
			cursor.close();
			return msgStatuses;
		} else {
			return new MsgStatus[0];
		}
	}

	public static long update(String key, String status, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("key", key);
		values.put("value", status);
		long id = db.update(Constant.TABLE_MSG_NAME, values, "key" + " = '"
				+ key + "'", null);
		Log.e("slx", "更新的id---->" + id);
		return id;

	}

}
