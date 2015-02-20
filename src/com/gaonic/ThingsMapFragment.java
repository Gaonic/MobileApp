package com.gaonic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import nmvictors.myapplynx.ui.dialogs.MyAppDialog;

import org.json.JSONArray;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem.HotspotPlace;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.gaonic.SensorsAdapter.OnGroupExpandedListener;
import com.gaonic.tasks.JSONParserTask;
import com.gaonic.tasks.TaskListener;
import com.gaonic.utils.ResUtils;
import com.gaonic.utils.Utils;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThingsMapFragment extends SherlockFragment implements Constants{
	private SensorOverlayItem poiItem;
	private SensorItemizedOverlayWithBubble poiMarkers;
	
	private MapView mapView;
	public String type;
	private AQuery mHttpTask;
	private View mProgressView;
	private IMapController myMapController;
	private volatile int completed;
	private IcsSpinner overlayMenu;
	private ImageButton toggleFullScreen;
	private IcsSpinner overlayNavigation;
	private View overlayMapMenu;
	private boolean enteringFull;
	private DrawerLayout mDrawerLayout;
	private ExpandableListView listView;

	private ActionBarHelper mActionBar;

	private SherlockActionBarDrawerToggle mDrawerToggle;
	private ResUtils resUtils;
	private ArrayList<SensorOverlayItem> poiItems;
	private SensorsAdapter adapter;
	private List<HashMap<String, Object>> sensorData;
	private HashMap<String, Object> contextMenuData;
	private Location location;
	protected boolean fullscreenMode;
	
	private ThingsMapFragment() {
		
	}
	public static ThingsMapFragment newInstance(String sensor_type) {
		ThingsMapFragment fragment = new ThingsMapFragment();
        fragment.type = sensor_type;
        return fragment;
    }
	
	/**
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 *      android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo info=
	            (ExpandableListView.ExpandableListContextMenuInfo)menuInfo;
	    int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
	    int child = ExpandableListView.getPackedPositionChild(info.packedPosition);
		if (group != -1 && child != -1) {
			contextMenuData = (HashMap<String, Object>) adapter.getChild(group, child);
			menu.setHeaderTitle(contextMenuData.get("name").toString());
			menu.add(Menu.NONE, 0, Menu.NONE, "Locate On the Map");
		} else if (group != -1) {
			/*menu.setHeaderTitle(((DataHolder<String>) expListAdapter.getGroup(group)).getData());
			menu.add(Menu.NONE, 0, Menu.NONE, R.string.clear);
*/		
		}
	}
	
	/**
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */

	public boolean onContextItemSelected(android.view.MenuItem item) {
		mapView.getController().animateTo(new GeoPoint(Double.parseDouble(contextMenuData.get("latitude").toString()), 
				Double.parseDouble(contextMenuData.get("longitude").toString())));
		mDrawerLayout.closeDrawer(listView);
			
		return true;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		showProgress(show,null);
	}
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show,String msg) {
		if(getActivity() == null) {
			return;
		}
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getActivity().getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mProgressView.setVisibility(View.VISIBLE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
			if (msg != null) {
				((TextView)getActivity().findViewById(R.id.loadmap_status_message)).setText(msg);
			}
			overlayMapMenu.setVisibility(View.VISIBLE);
			overlayMapMenu.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					getActivity().findViewById(R.id.overlay_map_menu).setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
			mapView.setVisibility(View.VISIBLE);
			mapView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mapView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mapView.setVisibility(show ? View.GONE : View.VISIBLE);
			overlayMapMenu.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 Context context  = getSherlockActivity().getSupportActionBar().getThemedContext();
		 TypedArray logos     = getResources().obtainTypedArray(R.array.map_overlay_menu_logos);
	     TypedArray titles    = getResources().obtainTypedArray(R.array.map_overlay_menu_titles);
	     TypedArray subtitles = getResources().obtainTypedArray(R.array.map_overlay_menu_subtitles);
	     
		overlayMenu = (IcsSpinner) getActivity().findViewById(R.id.menu);
	    overlayMenu.setAdapter(new MapOverlayMenuAdapter(context, logos, titles, subtitles));
	    overlayMenu.setOnItemSelectedListener(new IcsAdapterView.OnItemSelectedListener() {

			@Override
		public void onItemSelected(IcsAdapterView<?> parent, View view,
				final int position, long id) {
				if (!fullscreenMode) {
					return;
				}
				toggleFullScreen.performClick();
				switch(position) {
				case 0:
					new AttachSensorDialog((MainActivity)getActivity(), null).show();
					break;
				case 1:
					mDrawerLayout.openDrawer(listView);
					((MainActivity)getActivity()).prepareSearch();
					break;
				case 2:
					startActivity(new Intent(getActivity(),AboutActivity.class));
					break;
				case 4:
					GaonicDialog dialog = new GaonicDialog(getActivity(),null);
					dialog.setPositiveButton("Quit", new MyAppDialog.OnClickListener() {
						
						@Override
						public void onClick(MyAppDialog dialog, int which) {
							getActivity().finish();
						}
					});
					dialog.setIcon(R.drawable.ic_launcher);
					dialog.setTitle("Confirm Quit");
					dialog.setMessage("Are you sure you want to log out and close Gaonic?");
					dialog.setNegativeButton("Cance", null);
					dialog.show();
					break;
					
				}
			}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
	    });
	    logos     = getResources().obtainTypedArray(R.array.gaonic_logos);
	    titles    = getResources().obtainTypedArray(R.array.gaonic_titles);
	    overlayNavigation = (IcsSpinner) getActivity().findViewById(R.id.navigation);
	    overlayNavigation.setAdapter(new MapOverlayMenuAdapter(context, logos, titles, subtitles).withDynamicIcon(true));
	    overlayNavigation.setOnItemSelectedListener(new IcsAdapterView.OnItemSelectedListener() {

				@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					final int position, long id) {
				if(enteringFull) {
					enteringFull = false;
					return;
				}
				Intent intent = getActivity().getIntent();
				intent.putExtra("nav", position);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent);
				}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});	
	    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        	location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        	if (GAONIC_SENSORTYPE_NEARBY_SENSORS.equals(type)) {
        		showGPSDisabledAlertToUser();
        		//Toast.makeText(getActivity(), "Warning: GPS Not enabled!", Toast.LENGTH_LONG).show();
        		
        	}
        }else{
        	location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        toggleFullScreen = (ImageButton) getActivity().findViewById(R.id.toggleFullscreen);
	    toggleFullScreen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fullscreenMode = ((MainActivity)getActivity()).toggleFullscreen();
				
				toggleFullScreen.setImageResource(fullscreenMode ? R.drawable.ic_action_fullscreen_exit : R.drawable.ic_action_fullscreen);
				overlayMenu.setVisibility(fullscreenMode ? View.VISIBLE : View.INVISIBLE);
						
			}
		});
	    overlayMenu.setVisibility(View.INVISIBLE);
	    enteringFull = true;
		overlayNavigation.setSelection(((MainActivity)getActivity()).selectedNav());
				
		mProgressView = getActivity().findViewById(R.id.loadmap_status);
		overlayMapMenu =  getActivity().findViewById(R.id.overlay_map_menu);
		mapView = (MapView) getActivity().findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        String userID = getActivity().getIntent().getExtras().getString(GAONIC_KEY_USER_ID);
        resUtils = new ResUtils(getActivity().getResources());	
        
        poiItems = new ArrayList<SensorOverlayItem>();
        poiMarkers = new SensorItemizedOverlayWithBubble((MainActivity)getActivity(), 
                                        poiItems, mapView);
        mapView.getOverlays().add(poiMarkers);
        myMapController = mapView.getController();
        myMapController.setZoom(15);
        getActivity().findViewById(R.id.overlay_map_menu).setVisibility(View.INVISIBLE);
       /* if (GAONIC_SENSORTYPE_ALL.equals(type)) {
        	completed = 2;
        	mHttpTask = new AQuery(getActivity());
        	mHttpTask.ajax(Uri.parse(GAONIC_URL_USER_SENSORS).buildUpon()
        			.appendQueryParameter("user_id", userID+"")
        			.appendQueryParameter("type", GAONIC_SENSORTYPE_MY_SENSORS).build().toString(), JSONArray.class, loadSensorsListener);
        	mHttpTask.ajax(Uri.parse(GAONIC_URL_USER_SENSORS).buildUpon()
        			.appendQueryParameter("user_id", userID+"")
        			.appendQueryParameter("type", GAONIC_SENSORTYPE_NEARBY_SENSORS).build().toString(), JSONArray.class, loadSensorsListener);
        } else {
        	*/
        	completed = 1;
        	mHttpTask = new AQuery(getActivity());
        	mHttpTask.ajax(Uri.parse(GAONIC_URL_USER_SENSORS).buildUpon()
        			.appendQueryParameter("user_id", userID+"")
        			.appendQueryParameter("type", type).build().toString(), JSONArray.class, loadSensorsListener);
        //}
		showProgress(true);
       
	}
	
	
	  private void showGPSDisabledAlertToUser(){
	        GaonicDialog alertDialogBuilder = new GaonicDialog(getActivity(),null);
	        alertDialogBuilder.setTitle("GPS Is Disabled!");
	        alertDialogBuilder.setMessage("GPS is disabled in your device. To retreive your precise current location for use in this app, "
	        		+ "it is recommened that you enable GPS.Would you like to enable it?");
	        alertDialogBuilder.setCancelable(false);
	        alertDialogBuilder .setIcon(R.drawable.ic_action_gps_off_light);
	        alertDialogBuilder.setPositiveButton("Goto Settings",
	                new MyAppDialog.OnClickListener(){
	           
				@Override
				public void onClick(MyAppDialog dialog, int which) {
					 Intent callGPSSettingIntent = new Intent(
		                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		                startActivity(callGPSSettingIntent);
				}
	        });
	        alertDialogBuilder.setNegativeButton("Cancel",
	                new MyAppDialog.OnClickListener(){
	           
				@Override
				public void onClick(MyAppDialog dialog, int which) {
					Toast.makeText(getActivity().getApplicationContext(), "Network Service will be used to retreive your location.", Toast.LENGTH_LONG).show();
					 dialog.cancel ();
				}
	        });
	        alertDialogBuilder.show();
	    }
	
	public void onCompleted() {
		completed-=1;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	public String loadJSONFromAsset() {
	    String json = null;
	    try {

	        InputStream is = getActivity().getAssets().open("nearby.json");

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	    Log.e("ERROR", ex.getMessage());
	        return null;
	    }
	    return json;

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_mapview, null);
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		listView = (ExpandableListView) view.findViewById(R.id.left_drawer);
		listView.setOnCreateContextMenuListener(this);
		mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		sensorData = new ArrayList<HashMap<String,Object>>();
		adapter = new SensorsAdapter((MainActivity)this.getActivity(),sensorData);
		adapter.setOnGroupExpanded(new OnGroupExpandedListener() {

			private int lastExpandedGroupPosition;

			@Override
			public void onGroupExpanded(int groupPosition) {
				  //collapse the old expanded group, if not the same
		        //as new group to expand
			    if(groupPosition != lastExpandedGroupPosition){
		           listView.collapseGroup(lastExpandedGroupPosition);
		        }

		         lastExpandedGroupPosition = groupPosition;
			}
			
		});
		adapter.setOnNaviageButtonClicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawer(listView);
				int pos = listView.getPositionForView(v);
				double lat = Double.parseDouble((String)adapter.getData().get(pos).get("latitude"));
				double lon = Double.parseDouble((String)adapter.getData().get(pos).get("longitude"));
				mapView.getController().animateTo(new GeoPoint(lat,lon));
				
			}
		});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int gPos, int cPos, long arg4) {
				HashMap<String,Object> data = (HashMap<String, Object>) adapter.getChild(gPos, cPos);
				mapView.getController().animateTo(new GeoPoint(Double.parseDouble(data.get("latitude").toString()), 
						Double.parseDouble(data.get("longitude").toString())));
				mDrawerLayout.closeDrawer(arg0);
				return true;
			}});
		mActionBar = createActionBarHelper();
		mActionBar.init();

		// ActionBarDrawerToggle provides convenient helpers for tying together
		// the
		// prescribed interactions between a top-level sliding drawer and the
		// action bar.
		mDrawerToggle = new SherlockActionBarDrawerToggle(this.getActivity(), mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close);
		mDrawerToggle.syncState();
		return view;
	}

	private AjaxCallback<JSONArray> loadSensorsListener =  new AjaxCallback<JSONArray>() {

		public void callback(String url, JSONArray object, com.androidquery.callback.AjaxStatus status) {
			onCompleted();
			if (completed == 0)
				ThingsMapFragment.this.showProgress(false);
			if (object != null) {
				Log.v(TAG, object.toString());					if (completed <= 0)
						ThingsMapFragment.this.showProgress(true,"Parsing response");
					new JSONParserTask(parserTaskListener)
					.execute(object.toString());;
						
					
			} else {
				if(getActivity() != null)
					Utils.showError(getActivity(), status.getError());
			}
		}
		
		/*@Override
		public void onCancelled() {
			onCompleted();
			if (completed == 0)
				showProgress(false);
		}*/
		
	};
	
	private TaskListener<List<HashMap<String, Object>>> parserTaskListener =  new TaskListener<List<HashMap<String, Object>>> () {

		@Override
		public void onResult(List<HashMap<String, Object>> result) {
			if (result != null) {
				if (GAONIC_SENSORTYPE_MY_SENSORS.equals(type))
					 sensorData.clear();
					
				for (int i=0; i<result.size(); i++) {
					String name =  result.get(i).get("name").toString();

					GeoPoint point = new GeoPoint(Double.parseDouble((String) result.get(i).get("latitude")), 
							Double.parseDouble((String) result.get(i).get("longitude")));
					poiItem = new SensorOverlayItem(
					                name, name, 
					                point, mapView.getContext(),result.get(i));
					poiItem.setMarker(resUtils.getMarker((String) result.get(i).get("sensor_type")));
					poiItem.setMarkerHotspot(HotspotPlace.CENTER);
					poiMarkers.addItem(poiItem);
					if (!GAONIC_SENSORTYPE_NEARBY_SENSORS.equals(type))
						myMapController.setCenter(point);
					sensorData.add(result.get(i));
					adapter.notifyDataSetChanged();
			}
			}
				
			showProgress(false);
			if (location != null)
			if (GAONIC_SENSORTYPE_NEARBY_SENSORS.equals(type)) {
				GeoPoint point = new GeoPoint(location.getLatitude(),location.getLongitude());
				poiItem = new SensorOverlayItem(
				                "Your Are Here!", "Your Current Location", 
				                point, mapView.getContext(),null);
				poiItem.setMarker(resUtils.getMarker("current"));
				poiItem.setMarkerHotspot(HotspotPlace.CENTER);
				poiMarkers.addItem(poiItem);
				myMapController.animateTo(point);
			}
			
	   
		}

		
		@Override
		public void onCancelled() {
			showProgress(false);
		}
		
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up action should open or close the drawer.
		 * mDrawerToggle will take care of this.
		 */
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * This list item click listener implements very simple view switching by
	 * changing the primary content text. The drawer is closed when a selection
	 * is made.
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//TODO mContent.setText(Shakespeare.DIALOGUE[position]);
			//TODO mActionBar.setTitle(Shakespeare.TITLES[position]);
			mDrawerLayout.closeDrawer(listView);
		}
	}

	/**
	 * A drawer listener can be used to respond to drawer events such as
	 * becoming fully opened or closed. You should always prefer to perform
	 * expensive operations such as drastic relayout when no animation is
	 * currently in progress, either before or after the drawer animates.
	 * 
	 * When using ActionBarDrawerToggle, all DrawerLayout listener methods
	 * should be forwarded if the ActionBarDrawerToggle is not used as the
	 * DrawerLayout listener directly.
	 */
	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}

	

	private class ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		private ActionBarHelper() {
			mActionBar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		}

		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
			mTitle = mDrawerTitle = getActivity().getTitle();
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
			((MainActivity)getActivity()).closeSearch();
		}

		/**
		 * When the drawer is open we set the action bar to a generic title. The
		 * action bar should only contain data relevant at the top level of the
		 * nav hierarchy represented by the drawer, as the rest of your content
		 * will be dimmed down and non-interactive.
		 */
		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}
	}

	public SensorsAdapter getAdapter() {
		// TODO Auto-generated method stub
		return adapter;
	}
	public void resetList() {
		adapter = new SensorsAdapter((MainActivity)this.getActivity(),sensorData);
		adapter.setOnGroupExpanded(new OnGroupExpandedListener() {

			private int lastExpandedGroupPosition;

			@Override
			public void onGroupExpanded(int groupPosition) {
				  //collapse the old expanded group, if not the same
		        //as new group to expand
			    if(groupPosition != lastExpandedGroupPosition){
		           listView.collapseGroup(lastExpandedGroupPosition);
		        }

		         lastExpandedGroupPosition = groupPosition;
			}
			
		});
		adapter.setOnNaviageButtonClicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawer(listView);
				int pos = listView.getPositionForView(v);
				double lat = Double.parseDouble((String)adapter.getData().get(pos).get("latitude"));
				double lon = Double.parseDouble((String)adapter.getData().get(pos).get("longitude"));
				mapView.getController().animateTo(new GeoPoint(lat,lon));
				
			}
		});
		listView.setAdapter(adapter);
		
	}

	
}
