package com.gaonic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
import org.osmdroid.views.MapView;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ExpandableListView;


public class SensorInfoWindow extends DefaultInfoWindow {
	SensorsAdapter listAdapter;
	ExpandableListView expListView;
		
	public SensorInfoWindow(MainActivity act,MapView mapView, SensorOverlayItem item) {
		super(R.layout.sensor_info_window, mapView);
		
		expListView = (ExpandableListView) mView.findViewById(R.id.expandableListView1);
		List<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
		data.add(item.getSensorData());
		listAdapter = new SensorsAdapter(act,data);
		listAdapter.setBubbleMode(true);
		
		// setting list adapter
		expListView.setAdapter(listAdapter);
		mView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				close();
			}
		});
		expListView.expandGroup(0);
	}	

}
