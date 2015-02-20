package com.gaonic;


import java.util.List;

import org.osmdroid.bonuspack.overlays.ItemizedOverlayWithBubble;
import org.osmdroid.views.MapView;
import android.content.Context;

public class SensorItemizedOverlayWithBubble extends ItemizedOverlayWithBubble<SensorOverlayItem> {

	private MainActivity act;

	
	@Override
	protected boolean onSingleTapUpHelper(int index, SensorOverlayItem item,
			MapView mapView) {
		mBubble =  new SensorInfoWindow(act,mapView,item);
		return super.onSingleTapUpHelper(index, item, mapView);
	}

	@Override
	public void hideBubble() {
		// TODO Auto-generated method stub
		super.hideBubble();
	}

	public SensorItemizedOverlayWithBubble(MainActivity context, List<SensorOverlayItem> aList,
			MapView mapView) {
		super(context, aList, mapView);
		this.act = context;
	}

	
	
 

}