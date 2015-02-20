package com.gaonic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils implements com.gaonic.Constants{
	public static void showError(Context context,String error) {
		Log.e(TAG, "Error: "+error);
		show(context,"Error: "+error);
	}
	
	public static void show(Context context,String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromStream(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder response = new StringBuilder();
		String strLine = null;
		while ((strLine = br.readLine()) != null) {
			response.append(strLine);
		}
		br.close();
		return response.toString();
	}

	/**
	 * Makes a conection to given URL and returns an {@link java.io.InputStream InputStream} 
	 * of that connection/
	 */
	public static InputStream getConnectionStream(String url_)
			throws Exception {
		URL url = new URL(url_);
		HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
		//Log.i(TAG+"/DirectAccess", "Status Code: "+httpconn.getResponseCode());
		return httpconn.getInputStream();
	}
}
