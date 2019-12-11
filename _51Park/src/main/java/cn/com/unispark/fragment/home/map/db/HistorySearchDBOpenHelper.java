package cn.com.unispark.fragment.home.map.db;

import cn.com.unispark.application.Constant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 * 功能说明： 历史搜索记录保存的数据库
 * 日期：	2015年11月5日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月5日
 * </pre>
 */
public class HistorySearchDBOpenHelper extends SQLiteOpenHelper {

	public HistorySearchDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.TABLE_HISTORY_NAME
				+ " (" + Constant.ID + " varchar(20) primary key ," + "key"
				+ " varchar(500), " + "value" + " varchar(500), "
				+ "latitude integer," + "longitude integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
