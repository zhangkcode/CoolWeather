package com.coolweather.app.util;
/**
 * �ص��ӿ�
 * @author zhangkcode
 * @createDate 20170428
 *
 */
public interface HttpCallbackListener {
	public void onFinish(String response);
	public void onError(Exception e);
}
