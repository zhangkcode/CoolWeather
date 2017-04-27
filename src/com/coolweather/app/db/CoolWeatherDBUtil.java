package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.VWConsts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �������ݿ⹤����
 * @author zhangkcode
 * @createDate 20170427
 *
 */
public class CoolWeatherDBUtil {
	
	private SQLiteDatabase db;
	private static CoolWeatherDBUtil coolWeatherDB;
	
	/**
	 * ˽�л����췽��
	 * @param context
	 */
	private CoolWeatherDBUtil(Context context){
		db = new CoolWeatherHelper(context,VWConsts.DB_NAME,null,VWConsts.DB_VERSION).getWritableDatabase();
	}
	/**
	 * ʵ������̬����
	 * @param context
	 * @return CoolWeather
	 */
	public static CoolWeatherDBUtil getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDBUtil(context);
		}
		return coolWeatherDB;
	}
	/**
	 * ����ʡ��¼
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put(VWConsts.PROVINCE_CODE, province.getCode());
			values.put(VWConsts.PROVINCE_NAME, province.getName());
			db.insert(VWConsts.PROVINCE_TABLE, null, values);
		}
	}
	/**
	 * ����ʡ��¼
	 */
	public List<Province> queryProvinceList(){
		List<Province> provinceList = new ArrayList<Province>();
		Cursor cursor = db.query(VWConsts.PROVINCE_TABLE, null, null, null, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()){
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex(VWConsts.PROVINCE_ID)));
				province.setCode(cursor.getInt(cursor.getColumnIndex(VWConsts.PROVINCE_CODE)));
				province.setName(cursor.getString(cursor.getColumnIndex(VWConsts.PROVINCE_NAME)));
				provinceList.add(province);
			}
		}
		return provinceList;
	}
	/**
	 * �����м�¼
	 */
	public void saveCity(City city){
       if(city != null){
    	   ContentValues values = new ContentValues();
    	   values.put(VWConsts.CITY_CODE, city.getCode());
    	   values.put(VWConsts.CITY_NAME, city.getName());
    	   values.put(VWConsts.CITY_PROVINCE_ID, city.getProvince_code());
    	   db.insert(VWConsts.CITY_TABLE, null, values);
       }
	}
	/**
	 * �����м�¼
	 */
	public List<City> queryCityList(int provinceId){
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query(VWConsts.CITY_TABLE, null, VWConsts.CITY_PROVINCE_ID +"= ?",
				new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()){
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex(VWConsts.CITY_ID)));
				city.setCode(cursor.getInt(cursor.getColumnIndex(VWConsts.CITY_CODE)));
				city.setName(cursor.getString(cursor.getColumnIndex(VWConsts.CITY_NAME)));
				city.setProvince_code(cursor.getInt(cursor.getColumnIndex(VWConsts.CITY_PROVINCE_ID)));
				cityList.add(city);
			}
		}
		return cityList;
	}
	/**
	 * �����ؼ�¼
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues values = new ContentValues();
			values.put(VWConsts.COUNTY_CODE,county.getCode());
			values.put(VWConsts.COUNTY_NAME, county.getName());
			values.put(VWConsts.COUNTY_CITY_ID, county.getCity_id());
			db.insert(VWConsts.COUNTY_TABLE, null, values);
		}
	}
	/**
	 * �����ؼ�¼
	 */
	public List<County> queryCountyList(int cityId){
		List<County> countyList = new ArrayList<County>();
		Cursor cursor = db.query(VWConsts.COUNTY_TABLE, null, VWConsts.COUNTY_CITY_ID + " = ?",
				new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor != null){
			County county = new County();
			county.setId(cursor.getInt(cursor.getColumnIndex(VWConsts.COUNTY_ID)));
			county.setCode(cursor.getInt(cursor.getColumnIndex(VWConsts.COUNTY_CODE)));
			county.setName(cursor.getString(cursor.getColumnIndex(VWConsts.COUNTY_NAME)));
			county.setCity_id(cursor.getInt(cursor.getColumnIndex(VWConsts.COUNTY_CITY_ID)));
			countyList.add(county);
		}
		return countyList;
	}
	

}
