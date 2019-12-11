package cn.com.unispark.fragment.unknown.db;

import cn.com.unispark.application.Constant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MsgDBOpenHelper extends SQLiteOpenHelper {

	public MsgDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.JSON_TABLENAME +
		// " ("
		// + Constant.ID + " integer primary key autoincrement,"
		// + Constant.USERNAME + " varchar(500), "+ Constant.PASSWORD +
		// " varchar(500), " + Constant.TIME
		// + " varchar(500)) ");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.TABLE_MSG_NAME + " (" + Constant.ID + " integer primary key autoincrement,"
				+ "key" + " varchar(500), " + "value" + " varchar(500))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
