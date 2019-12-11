package cn.com.unispark.fragment.unknown.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.com.unispark.application.Constant;
import cn.com.unispark.fragment.home.map.entity.HistoryEntity;

/**
 * <pre>
 * 功能说明：保存搜索历史记录的数据库
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
public class SearchDBUtils {
	public static long insert(String key, String value, Double WD, Double JD,
			SQLiteDatabase db) {
		boolean isUpdate = false;
		HistoryEntity[] histories = queryAllKey(db);
		for (int i = 0; i < histories.length; i++) {
			if (key.equals(histories[i].getKey())) {
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
				values.put("WD", WD);
				values.put("JD", JD);
				id = db.insert(Constant.TABLE_HISTORY_NAME, null, values);
			}
		}
		db.close();
		return id;
	}

	public static void delAllHistoryData(SQLiteDatabase db) {
		String DELETE_DATA = "DELETE FROM " + Constant.TABLE_HISTORY_NAME + ";";
		db.execSQL(DELETE_DATA);
	}

	public static HistoryEntity[] queryAllKey(SQLiteDatabase db) {
		if (db != null) {
			Cursor cursor = db.rawQuery("select * from HistoryListDB", null);
			int count = cursor.getCount();
			Log.e("slx", "count--->" + count);
			HistoryEntity[] histories = new HistoryEntity[count];
			String key = null;
			String value = null;
			Double WD;
			Double JD;
			HistoryEntity history = null;
			if (count > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					key = cursor.getString(cursor.getColumnIndex("key"));
					value = cursor.getString(cursor.getColumnIndex("value"));
					WD = cursor.getDouble(cursor.getColumnIndex("latitude"));
					JD = cursor.getDouble(cursor.getColumnIndex("longitude"));
					history = new HistoryEntity();
					history.setKey(key);
					history.setValue(value);
					history.setJD(JD);
					history.setWD(WD);
					history.setValue(value);
					histories[i] = history;
					cursor.moveToNext();
				}
			}
			cursor.close();
			return histories;
		} else {
			return new HistoryEntity[0];
		}
	}

//
//	private static long update(String userName, String passWord, String time,
//			SQLiteDatabase db) {
//		ContentValues values = new ContentValues();
//		values.put(Constant.USERNAME, userName);
//		values.put(Constant.TIME, time);
//		values.put(Constant.PASSWORD, passWord);
//		long id = db.update(Constant.JSON_TABLENAME, values, Constant.USERNAME
//				+ " = '" + userName + "'", null);
//		Log.e("slx", "更新的id---->" + id);
//		return id;
//
//	}


}
