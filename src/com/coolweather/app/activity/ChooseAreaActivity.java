package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDBUtil;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.VWConsts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 选择地区Activity
 * @author zhangkcode
 * @createDate 20170428
 */
public class ChooseAreaActivity extends Activity{
	private TextView textView = null;
	private ListView listView = null;
	ArrayAdapter<String> adapter = null;
	private List<String> dataList = new ArrayList<String>();
	//0省 1市 2县
	private int currentLevel = 0;
	private CoolWeatherDBUtil DBUtil;
	private ProgressDialog dialog = null;
	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		initComponent();
	}
	/**
	 * 初始化控件
	 */
	private void initComponent(){
		textView = (TextView) findViewById(R.id.title_text);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		queryAreaData(currentLevel,-1);
		listView.setAdapter(adapter);
		//点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(currentLevel == 0){
					selectedProvince =  (Province) provinceList.get(position);
					queryAreaData(1,selectedProvince.getId());
				}else if(currentLevel == 1){
					selectedCity = (City) cityList.get(position);
					queryAreaData(2,selectedCity.getId());
				}else if(currentLevel == 2){
					selectedCounty = (County)countyList.get(position);
					Intent intent = new Intent (ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("weatherId", selectedCounty.getWeather_id());
					startActivity(intent);
					finish();
				}
				
			}
		});
	}
	/**
	 * 查询地域数据当本地数据库无数据时从服务器中获取
	 */
	private void queryAreaData(int levelFlag,int parentId){
		DBUtil = CoolWeatherDBUtil.getInstance(this);
		switch(levelFlag){
		 //当前为省级
		 case 0:
			 textView.setText("中国");
			 provinceList = DBUtil.queryProvinceList();
			 if(provinceList != null && provinceList.size()>0){
				dataList.clear();
				for(Province province :provinceList){
					dataList.add(province.getName());
				}
				adapter.notifyDataSetChanged();
			 }else{
				 queryAreaDataFromServer(levelFlag,-1);
			 }
			 break;
	     //当前为市级
		 case 1:
			 currentLevel = 1;
			 textView.setText(selectedProvince.getName());
			 cityList = DBUtil.queryCityList(parentId);
			 if(cityList != null && cityList.size()>0){
				dataList.clear();
				for(City city :cityList){
					dataList.add(city.getName());
				}
				adapter.notifyDataSetChanged();
			 }else{
				 queryAreaDataFromServer(levelFlag,parentId);
			 }
			 break;
	     //当前为县级
		 case 2:
			 currentLevel = 2;
			 textView.setText(selectedCity.getName());
             countyList = DBUtil.queryCountyList(parentId);	
             if(countyList != null && countyList.size() > 0){
            	 dataList.clear();
            	 for(County county : countyList){
            		 dataList.add(county.getName());
            	 }
            	 adapter.notifyDataSetChanged();
             }else{
            	 queryAreaDataFromServer(levelFlag, parentId);
             }
			 break;
	     default:
	    	 break;
		}
		
	}
	/**
	 * 从服务器查询区域数据
	 */
	private void queryAreaDataFromServer(final int levelFlag,final int parentId){
		showProgressDialog();
		switch(levelFlag){
		//查询省级数据
	     case 0:
			   HttpUtil.sendHttpRequest(VWConsts.NET_ADDRESS, new HttpCallbackListener() {
					
					@Override
					public void onFinish(String response) {
						if(Utility.parsePronvinceData(response, DBUtil)){
							runOnUiThread(new Runnable() {
								public void run() {
									queryAreaData(levelFlag,-1);
								}
							});
							closeProgressDialog();
						}
					}
					
					@Override
					public void onError(Exception e) {
						closeProgressDialog();
						e.printStackTrace();
					}
				});
	    	 break;
	    //查询市级数据
	     case 1:
			   HttpUtil.sendHttpRequest(VWConsts.NET_ADDRESS+"/"+selectedProvince.getCode(), new HttpCallbackListener() {
					
					@Override
					public void onFinish(String response) {
						if(Utility.parseCityData(response, DBUtil, parentId)){
							runOnUiThread(new Runnable() {
								public void run() {
									queryAreaData(levelFlag,parentId);
								}
							});
							closeProgressDialog();
						}
					}
					
					@Override
					public void onError(Exception e) {
						closeProgressDialog();
						e.printStackTrace();
					}
				});
	    	   break;
	     //查询县级数据
	     case 2:
  			   HttpUtil.sendHttpRequest(VWConsts.NET_ADDRESS+"/"+selectedProvince.getCode()+"/"+selectedCity.getCode(), new HttpCallbackListener() {
  					
  					@Override
  					public void onFinish(String response) {
  						if(Utility.parseCountyData(response, DBUtil, parentId)){
  							runOnUiThread(new Runnable() {
								public void run() {
									queryAreaData(levelFlag,parentId);
								}
							});
  							closeProgressDialog();
  						}
  					}
  					
  					@Override
  					public void onError(Exception e) {
  						closeProgressDialog();
  						e.printStackTrace();
  						Toast.makeText(ChooseAreaActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
  					}
  				});
	    	 break;
	     default:
				break;
		}
	}
	/**
	 * 展示ProgressDialog
	 */
   private void showProgressDialog(){
		dialog = new ProgressDialog(ChooseAreaActivity.this);
		dialog.setMessage("正在加载中...");
		dialog.show();
   }
   /**
    * 关闭ProgressDialog
    */
   private void closeProgressDialog(){
	   if(dialog != null){
		   dialog.dismiss();
	   }
   }
   /**
    * 重写onBackPressed方法
    */
   @Override
public void onBackPressed() {
   switch(currentLevel){
	   case 0:
		    finish();
		    break;
	   case 1:
		   currentLevel = 0;
		   queryAreaData(currentLevel,-1);
		   break;
	   case 2:
		   currentLevel = 1;
		   queryAreaData(currentLevel, selectedProvince.getCode());
		   break;
	   default:
		   finish();
		   break;
   }
}
   
}
