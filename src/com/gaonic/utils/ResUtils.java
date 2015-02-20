package com.gaonic.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.gaonic.R;

public class ResUtils {
	private Drawable markerAll;
	private Drawable markerWireless;
	private Drawable markerWind;
	private Drawable markerWeather;
	private Drawable markerWaterQuality;
	private Drawable markerUnknown;
	private Drawable markerTrafficJam;
	private Drawable markerTemperature;
	private Drawable markerRecycleBin;
	private Drawable markerSmartphone;
	private Drawable markerRadiation;
	private Drawable markerPowerMeter;
	private Drawable markerParking;
	private Drawable markerNoise;
	private Drawable markerHumidity;
	private Drawable markerBicycleStation;
	private Drawable markerLiquid;
	private Drawable markerLight;
	private Drawable markerBinTrash;
	private Drawable markerAirPollution;
	private Resources res;
	private Drawable currentLoc;

	public ResUtils(Resources  res) {
		this.res = res;
		initMarkers();
		
	}
	
	public  Drawable getMarker(String type) {
		if ("power_meter".equals(type)) {
			return markerParking;
		} else if ("noise".equals(type)) {
			return markerNoise;
		}  else if ("light".equals(type)) {
			return markerLight;
		}  else if ("traffic_jam".equals(type)) {
			return markerTrafficJam;
		}  else if ("liquid".equals(type)) {
			return markerLiquid;
		}  else if ("parking".equals(type)) {
			return markerParking;
		}  else if ("humidity".equals(type)) {
			return markerHumidity;
		}  else if ("bin_trash".equals(type)) {
			return markerBinTrash;
		}  else if ("air_pollution".equals(type)) {
			return markerAirPollution;
		}  else if ("smartphone".equals(type)) {
			return markerSmartphone;
		}  else if ("water_quality".equals(type)) {
			return markerWaterQuality;
		}  else if ("unknown".equals(type)) {
			return markerUnknown;
		}  else if ("temperature".equals(type)) {
			return markerTemperature;
		}  else if ("wind".equals(type)) {
			return markerWind;
		}  else if ("wireless".equals(type)) {
			return markerWireless;
		}  else if ("weather".equals(type)) {
			return markerWeather;
		}  else if ("radiation".equals(type)) {
			return markerRadiation;
		} else if ("current".equals(type)) {
			return currentLoc;
		}
		return markerAll;
	}
	private void initMarkers() {
		markerAll = getResources().getDrawable(R.drawable.sensor_all);
		int markerWidth = markerAll.getIntrinsicWidth();
		int markerHeight = markerAll.getIntrinsicHeight();
		markerAll.setBounds(0, markerHeight, markerWidth, 0);
		
		markerWind = getResources().getDrawable(R.drawable.sensor_wind);
		markerWidth = markerWind.getIntrinsicWidth();
		markerHeight = markerWind.getIntrinsicHeight();
		markerWind.setBounds(0, markerHeight, markerWidth, 0);
		
		markerAirPollution = getResources().getDrawable(R.drawable.sensor_air_pollution);
		markerWidth = markerAirPollution.getIntrinsicWidth();
		markerHeight = markerAirPollution.getIntrinsicHeight();
		markerAirPollution.setBounds(0, markerHeight, markerWidth, 0);
		
		markerBinTrash = getResources().getDrawable(R.drawable.sensor_bin_trash);
		markerWidth = markerBinTrash.getIntrinsicWidth();
		markerHeight = markerBinTrash.getIntrinsicHeight();
		markerBinTrash.setBounds(0, markerHeight, markerWidth, 0);
		
		markerLight = getResources().getDrawable(R.drawable.sensor_light);
		markerWidth = markerLight.getIntrinsicWidth();
		markerHeight = markerLight.getIntrinsicHeight();
		markerLight.setBounds(0, markerHeight, markerWidth, 0);
		
		markerLiquid = getResources().getDrawable(R.drawable.sensor_liquid);
		markerWidth = markerLiquid.getIntrinsicWidth();
		markerHeight = markerLiquid.getIntrinsicHeight();
		markerLiquid.setBounds(0, markerHeight, markerWidth, 0);
		
		markerBicycleStation = getResources().getDrawable(R.drawable.sensor_bicycles_station);
		markerWidth = markerBicycleStation.getIntrinsicWidth();
		markerHeight = markerBicycleStation.getIntrinsicHeight();
		markerBicycleStation.setBounds(0, markerHeight, markerWidth, 0);
		
		markerHumidity = getResources().getDrawable(R.drawable.sensor_humidity);
		markerWidth = markerHumidity.getIntrinsicWidth();
		markerHeight = markerHumidity.getIntrinsicHeight();
		markerHumidity.setBounds(0, markerHeight, markerWidth, 0);
		
		markerNoise = getResources().getDrawable(R.drawable.sensor_noise);
		markerWidth = markerNoise.getIntrinsicWidth();
		markerHeight = markerNoise.getIntrinsicHeight();
		markerNoise.setBounds(0, markerHeight, markerWidth, 0);
		
		markerParking = getResources().getDrawable(R.drawable.sensor_parking);
		markerWidth = markerParking.getIntrinsicWidth();
		markerHeight = markerParking.getIntrinsicHeight();
		markerParking.setBounds(0, markerHeight, markerWidth, 0);
		
		markerPowerMeter = getResources().getDrawable(R.drawable.sensor_power_meter);
		markerWidth = markerPowerMeter.getIntrinsicWidth();
		markerHeight = markerPowerMeter.getIntrinsicHeight();
		markerPowerMeter.setBounds(0, markerHeight, markerWidth, 0);
		
		markerRadiation = getResources().getDrawable(R.drawable.sensor_radiation);
		markerWidth = markerRadiation.getIntrinsicWidth();
		markerHeight = markerRadiation.getIntrinsicHeight();
		markerRadiation.setBounds(0, markerHeight, markerWidth, 0);
		
		markerSmartphone = getResources().getDrawable(R.drawable.sensor_smartphone);
		markerWidth = markerSmartphone.getIntrinsicWidth();
		markerHeight = markerSmartphone.getIntrinsicHeight();
		markerSmartphone.setBounds(0, markerHeight, markerWidth, 0);
		
		markerRecycleBin = getResources().getDrawable(R.drawable.sensor_recycle_bin_trash);
		markerWidth = markerRecycleBin.getIntrinsicWidth();
		markerHeight = markerRecycleBin.getIntrinsicHeight();
		markerRecycleBin.setBounds(0, markerHeight, markerWidth, 0);
		
		markerTemperature = getResources().getDrawable(R.drawable.sensor_temprature);
		markerWidth = markerTemperature.getIntrinsicWidth();
		markerHeight = markerTemperature.getIntrinsicHeight();
		markerTemperature.setBounds(0, markerHeight, markerWidth, 0);
		
		markerTrafficJam= getResources().getDrawable(R.drawable.sensor_traffic_jam);
		markerWidth = markerTrafficJam.getIntrinsicWidth();
		markerHeight = markerTrafficJam.getIntrinsicHeight();
		markerTrafficJam.setBounds(0, markerHeight, markerWidth, 0);
		
		markerUnknown = getResources().getDrawable(R.drawable.sensor_unknown);
		markerWidth = markerUnknown.getIntrinsicWidth();
		markerHeight = markerUnknown.getIntrinsicHeight();
		markerUnknown.setBounds(0, markerHeight, markerWidth, 0);
		
		markerWaterQuality = getResources().getDrawable(R.drawable.sensor_water_quality);
		markerWidth = markerWaterQuality.getIntrinsicWidth();
		markerHeight = markerWaterQuality.getIntrinsicHeight();
		markerWaterQuality.setBounds(0, markerHeight, markerWidth, 0);
		
		markerWeather = getResources().getDrawable(R.drawable.sensor_weather);
		markerWidth = markerWeather.getIntrinsicWidth();
		markerHeight = markerWeather.getIntrinsicHeight();
		markerWeather.setBounds(0, markerHeight, markerWidth, 0);
		
		markerWind = getResources().getDrawable(R.drawable.sensor_wind);
		markerWidth = markerWind.getIntrinsicWidth();
		markerHeight = markerWind.getIntrinsicHeight();
		markerWind.setBounds(0, markerHeight, markerWidth, 0);
		
		markerWireless = getResources().getDrawable(R.drawable.sensor_wireless);
		markerWidth = markerWireless.getIntrinsicWidth();
		markerHeight = markerWireless.getIntrinsicHeight();
		markerWireless.setBounds(0, markerHeight, markerWidth, 0);
		
		currentLoc = getResources().getDrawable(R.drawable.ic_location_place);
		markerWidth = currentLoc.getIntrinsicWidth();
		markerHeight = currentLoc.getIntrinsicHeight();
		currentLoc.setBounds(0, markerHeight, markerWidth, 0);
	}

	private Resources getResources() {
		return res;
	}
}
