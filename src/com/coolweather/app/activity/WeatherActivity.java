package com.coolweather.app.activity;

import java.util.Date;

import com.coolweather.app.R;
import com.coolweather.app.service.UpdateWeatherInfoService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.VWConsts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 天气Activity
 * @author zhangkcode
 * @createDate 20170502
 *
 */
public class WeatherActivity extends Activity implements android.view.View.OnClickListener{
	private TextView city_name;
	private TextView publish_text;
	private LinearLayout weather_info_layout;
	private TextView current_date ;
	private TextView weather_desp;
	private TextView temp1;
	private TextView temp2;
	private Button switchCityBtn;
	private Button refreshWeatherBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		initComponent();
		String weatherId = getIntent().getStringExtra("weatherId");
		if(!TextUtils.isEmpty(weatherId)){
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
			editor.putString("weatherId", weatherId);
			editor.commit();
			queryWeatherDataFromServer(VWConsts.WEATHER_ADDRESS+weatherId+VWConsts.WEATHER_KEY);
		}else{
			ShowWeatherInfo();
		}
	}
	/**
	 * 初始化各控件
	 */
	private void initComponent(){
		city_name = (TextView) findViewById(R.id.city_name);
		publish_text = (TextView) findViewById(R.id.publish_text);
		weather_info_layout = (LinearLayout) findViewById(R.id.weather_info_layout);
		current_date = (TextView) findViewById(R.id.current_date);
		weather_desp = (TextView) findViewById(R.id.weather_desp);
		temp1 = (TextView) findViewById(R.id.temp1);
		temp2 = (TextView) findViewById(R.id.temp2);
		switchCityBtn = (Button) findViewById(R.id.switch_city);
		refreshWeatherBtn = (Button) findViewById(R.id.refresh_weather);
		
		switchCityBtn.setOnClickListener(this);
		refreshWeatherBtn.setOnClickListener(this);
	}
	/**
	 * 从服务器获取数据
	 */
	private void queryWeatherDataFromServer(String address){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Utility.parseWeatherData(response, WeatherActivity.this);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ShowWeatherInfo();
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						weather_info_layout.setVisibility(View.INVISIBLE);
						publish_text.setText("更新失败！");
					}
				});
			}
		});
	}
	/**
	 * 展示天气信息
	 */
	private void ShowWeatherInfo(){
		weather_info_layout.setVisibility(View.VISIBLE);
		SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
		String maxTmp = prefer.getString("maxTmp", "");
		String minTmp = prefer.getString("minTmp", "");
		String wind = prefer.getString("wind", "");
		String weatherCond = prefer.getString("weatherCond", "");
		String current_time = prefer.getString("current_time", "");
		String cityName = prefer.getString("cityName", "");
		String publishTime = prefer.getString("publishTime", "");
		
		temp1.setText(minTmp+"℃");
		temp2.setText(maxTmp+"℃");
		weather_desp.setText(weatherCond+wind+"级");
		current_date.setText(current_time);
		city_name.setText(cityName);
		publish_text.setText("今天"+publishTime+"发布");
		
		//启动后台服务
		Intent myIntent = new Intent(this,UpdateWeatherInfoService.class);
		startService(myIntent);
	}
	@Override
	/**
	 * 切换城市按钮和刷新天气按钮操作
	 */
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.switch_city:
				Intent fromWeatherIntent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
				fromWeatherIntent.putExtra("from_weatherActivity", true);
				startActivity(fromWeatherIntent);
				finish();
				break;
			case R.id.refresh_weather:
				publish_text.setText("更新中......");
				SharedPreferences preFer = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
				String weatherId = preFer.getString("weatherId", "");
				if(!TextUtils.isEmpty(weatherId)){
					queryWeatherDataFromServer(VWConsts.WEATHER_ADDRESS+weatherId+VWConsts.WEATHER_KEY);
				}else{
					ShowWeatherInfo();
				}
				break;
			default :
				break;
		}
		
	}
	

}
