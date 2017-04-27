package com.coolweather.app.db;

import com.coolweather.app.util.VWConsts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * DBµÄHelperÀà
 * @author zhangkcode
 * @createDate 20170427
 *
 */
public class CoolWeatherHelper extends SQLiteOpenHelper {

	public CoolWeatherHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(VWConsts.CREATE_PROVINCE);
		db.execSQL(VWConsts.CREATE_CITY);
		db.execSQL(VWConsts.CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
