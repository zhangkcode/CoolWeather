package com.coolweather.app.receiver;

import com.coolweather.app.service.UpdateWeatherInfoService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 启动后台更新服务的广播接收器
 * @author zhangkcode
 * @createDate 20170503
 *
 */
public class UpdateWeatherInfoReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent toServiceIntent = new Intent(context,UpdateWeatherInfoService.class);
		context.startService(toServiceIntent);
	}

}
