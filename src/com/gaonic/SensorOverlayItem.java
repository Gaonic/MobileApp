package com.gaonic;

import java.util.HashMap;

import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.util.GeoPoint;

import android.content.Context;

public class SensorOverlayItem extends ExtendedOverlayItem{

	private HashMap<String,Object> data;

	public SensorOverlayItem(String aTitle, String aDescription,
			GeoPoint aGeoPoint, Context context, HashMap<String,Object> sData) {
		super(aTitle, aDescription, aGeoPoint, context);
		data = sData;
	}

	public HashMap<String,Object> getSensorData() {
		return data;
	}
}
