package cn.com.unispark;


import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.map_main);
         
	        MapView mapView = (MapView) findViewById(R.id.mapview);
	        mapView.onCreate(savedInstanceState);// 此方法必须重写
	        AMap aMap = mapView.getMap();
	         
	        aMap.setTrafficEnabled(true);// 显示实时交通状况
	        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
	        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
	}
	
	
}
