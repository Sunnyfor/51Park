package cn.com.unispark.fragment.unknown.db;
//package cn.com.unispark.mine;
//
//import cn.com.unispark.application.Constant;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class MyDBOpenHelper extends SQLiteOpenHelper {
//
//	public MyDBOpenHelper(Context context, String name, CursorFactory factory,
//			int version) {
//		super(context, name, factory, version);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.JSON_TABLENAME + " ("
//				+ Constant.ID + " integer primary key autoincrement,"
//				+ Constant.USERNAME + " varchar(500), "+ Constant.PASSWORD + " varchar(500), " + Constant.TIME
//				+ " varchar(500)) ");
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		onCreate(db);
//	}
//
//}
