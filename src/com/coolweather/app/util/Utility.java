package com.coolweather.app.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

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

}
