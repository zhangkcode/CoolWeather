package com.coolweather.app.service;

import com.coolweather.app.activity.WeatherActivity;
import com.coolweather.app.receiver.UpdateWeatherInfoReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.VWConsts;

import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
/**
 * 后台更新服务
 * @author zhangkcode
 * @createDate 20170503
 *
 */
public class UpdateWeatherInfoService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updateWeatherInfo();
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//启动广播接收器
		Intent myIntent = new Intent(this,UpdateWeatherInfoReceiver.class);
		PendingIntent pPintent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		long triggerTime = System.currentTimeMillis() + 60*1000*3;//三分钟
		manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pPintent);
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * 更新天气信息
	 * @param address
	 */
	private void updateWeatherInfo(){
		SharedPreferences preFer = PreferenceManager.getDefaultSharedPreferences(UpdateWeatherInfoService.this);
		String weatherId = preFer.getString("weatherId", "");
		Log.d("UpdateWeatherInfoService", weatherId);
		String address = VWConsts.WEATHER_ADDRESS+weatherId+VWConsts.WEATHER_KEY;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.d("UpdateWeatherInfoService", "后天数据更新中");
				Utility.parseWeatherData(response, UpdateWeatherInfoService.this);
			}
			
			@Override
			public void onError(Exception e) {
				Log.d("UpdateWeatherInfoService", "后台更新服务失败");
			}
		});
	}

}
