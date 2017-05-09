package com.coolweather.app.util;
/**
 * 常量集合类
 * @author zhangkcode
 * @createDate 20170427
 *
 */
public class VWConsts {
	
	public static final String DB_NAME = "db_coolweather";
	public static final int DB_VERSION = 1;
	//省建表语句
	public static final String CREATE_PROVINCE = "create table d_province ("+
                         "id integer primary key autoincrement,"+
			             "province_code integer,"+
                         "province_name text)";
	public static final String PROVINCE_TABLE = "d_province";
	public static final String PROVINCE_ID = "id";
	public static final String PROVINCE_CODE = "province_code";
	public static final String PROVINCE_NAME = "province_name";
	
	//市建表语句
	public static final String CREATE_CITY = "create table d_city ("+
	                    "id integer primary key autoincrement,"+
			            "city_code integer,"+
	                    "city_name text,"+
			            "province_id integer)";
	public static final String CITY_TABLE = "d_city";
	public static final String CITY_ID = "id";
	public static final String CITY_CODE = "city_code";
	public static final String CITY_NAME = "city_name";
	public static final String CITY_PROVINCE_ID = "province_id";
	
	//县建表语句
	public static final String CREATE_COUNTY = "create table d_county ("+
	                    "id integer primary key autoincrement,"+
			            "county_code integer,"+
	                    "county_name text,"+
			            "city_id integer," +
			            "weather_id text)";
	public static final String COUNTY_TABLE = "d_county";
	public static final String COUNTY_ID = "id";
	public static final String COUNTY_CODE = "county_code";
	public static final String COUNTY_NAME = "county_name";
	public static final String COUNTY_CITY_ID = "city_id";
	public static final String COUNTY_WEATHER_ID = "weather_id";
	
	public static final String NET_ADDRESS = "http://guolin.tech/api/china";
	public static final String WEATHER_ADDRESS = "http://guolin.tech/api/weather?cityid=";
	public static final String WEATHER_KEY = "&key=bc0418b57b2d4918819d3974ac1285d9";

}
