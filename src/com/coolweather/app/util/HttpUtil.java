package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 从服务器获取数据的工具类
 * @author zhangkcode
 * @createDate 20170428
 *
 */
public class HttpUtil {
	
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL Url = new URL(address);
					connection = (HttpURLConnection) Url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.connect();
					int responseCode = connection.getResponseCode();
					if(responseCode == 200){
						InputStream in = connection.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						StringBuilder response = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null){
							response.append(line);
						}
						
						in.close();
						reader.close();
						
						if(listener != null){
							listener.onFinish(response.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(listener != null){
						listener.onError(e);
					}
				}finally{
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
