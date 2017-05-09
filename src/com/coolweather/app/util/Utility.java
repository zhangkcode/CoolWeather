package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDBUtil;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
 * 解析从服务器读取的数据
 * @author zhangkcode
 * @createDate 20170428
 *
 */
public class Utility {
	/**
	 * 解析省数据
	 * @param response
	 * @param db
	 */
	public static boolean parsePronvinceData(String response,CoolWeatherDBUtil db){
		if(!TextUtils.isEmpty(response)){
			try {
				JSONArray allProvinces = new JSONArray(response);
				if(allProvinces != null && allProvinces.length() > 0){
					for(int i = 0;i<allProvinces.length();i++){
						JSONObject obj = allProvinces.getJSONObject(i);
						Province province = new Province();
						province.setCode(obj.getInt("id"));
						province.setName(obj.getString("name"));
						db.saveProvince(province);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	/**
	 * 解析市数据
	 */
	public static boolean parseCityData(String response,CoolWeatherDBUtil db,int provinceId){
		if(!TextUtils.isEmpty(response)){
			try {
				JSONArray allCities = new JSONArray(response);
				if(allCities != null && allCities.length() > 0){
					for(int i = 0; i < allCities.length(); i++){
						JSONObject obj = allCities.getJSONObject(i);
						City city = new City();
						city.setCode(obj.getInt("id"));
						city.setName(obj.getString("name"));
						city.setProvince_code(provinceId);
						db.saveCity(city);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return true;
		}
		return false;
	}
	/**
	 * 解析县数据
	 */
	public static boolean parseCountyData(String response,CoolWeatherDBUtil db,int cityId){
		if(!TextUtils.isEmpty(response)){
			try {
				JSONArray allCounties = new JSONArray(response);
				if(allCounties != null && allCounties.length() > 0){
					for(int i =0;i<allCounties.length();i++){
						JSONObject obj = allCounties.getJSONObject(i);
						County county = new County();
						county.setCode(obj.getInt("id"));
						county.setName(obj.getString("name"));
						county.setCity_id(cityId);
						county.setWeather_id(obj.getString("weather_id"));
						db.saveCounty(county);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	/**
	 * 根据json数据解析天气数据
	 */
	public static boolean parseWeatherData(String response,Context context){
		if(!TextUtils.isEmpty(response)){
			try {
				JSONObject jsonObject = new JSONObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
				JSONObject allJsonObject = jsonArray.getJSONObject(0);
				String status = allJsonObject.getString("status");
				if("ok".equals(status)){
					JSONArray weatherInfoArray = allJsonObject.getJSONArray("daily_forecast");
					JSONObject basicObject = allJsonObject.getJSONObject("basic");
					String cityName = basicObject.getString("city");
					JSONObject publishTimeObject = basicObject.getJSONObject("update");
					String publishTime = publishTimeObject.getString("loc").substring(11, 16);
					JSONObject weatherInfoObject = weatherInfoArray.getJSONObject(0);
//					JSONObject astroObject = weatherInfoObject.getJSONObject("astro");
//					String publishTime = astroObject.getString("mr");
					JSONObject tmpObject =  weatherInfoObject.getJSONObject("tmp");
					String maxTmp = tmpObject.getString("max");
					String minTmp = tmpObject.getString("min");
					JSONObject condObject = weatherInfoObject.getJSONObject("cond");
					String cond_day = condObject.getString("txt_d");
					JSONObject windObject = weatherInfoObject.getJSONObject("wind");
					String windDir = windObject.getString("dir");
					String windSc = windObject.getString("sc");
					savaWeatherInfo(context,cityName,maxTmp,minTmp,windDir+windSc,cond_day,publishTime);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	/**
	 * 将天气信息保存至SharedPreferences中
	 */
	 public static boolean savaWeatherInfo(Context context,String cityName,String maxTmp,
			       String minTmp,String wind,String weatherCond,String publishTime){
		 SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 editor.putString("maxTmp", maxTmp);
		 editor.putString("minTmp", minTmp);
		 editor.putString("wind", wind);
		 editor.putString("weatherCond", weatherCond);
		 editor.putString("current_time", sdf.format(new Date()));
         editor.putString("cityName", cityName);	
         editor.putString("publishTime", publishTime);
		 editor.commit();
		 return true;
	 }

}
