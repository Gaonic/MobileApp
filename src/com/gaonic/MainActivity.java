package com.gaonic;

import nmvictors.myapplynx.ui.dialogs.MyAppDialog;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnCloseListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation.AnimationListener;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends SherlockFragmentActivity implements Constants {
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	private ThingsMapFragment fragment;

	private boolean fullScreen;

	private int abHeight;

	private int pos;

	private MenuItem searchMenuItem;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_ACTION_BAR);
	    	   
	        setContentView(R.layout.activity_main);
	        
	        getSupportActionBar().setDisplayShowTitleEnabled(true);
	        getSupportActionBar().setDisplayUseLogoEnabled  (true);
	        abHeight = getSupportActionBar().getHeight();   
	        // use getActionBar().getThemedContext() to ensure
	        // that the text color is always appropriate for the action bar
	        // background rather than the activity background.
	        navigateTo(getIntent().getIntExtra("nav", 0));
	      }

	    public void navigateTo(int pos) {
	    	 fragment = ThingsMapFragment.newInstance(pos == 0 ? GAONIC_SENSORTYPE_MY_SENSORS : GAONIC_SENSORTYPE_NEARBY_SENSORS);
		        getSupportFragmentManager().beginTransaction()
		            .replace(android.R.id.content, fragment).commit();
		    this.pos = pos;
	    }
	    
	    public int selectedNav() {
	    	return pos;
	    }
	    
	    public boolean toggleFullscreen() {
	    	return toggleFullscreen(null);
	    }
	    public boolean toggleFullscreen(final AnimationListener al) {
	    	if (fullScreen) {
	    		/*UIUtils.expand(getSupportActionBar(), new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
					if (al != null)
						al.onAnimationEnd(animation);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						if (al != null)
							al.onAnimationRepeat(animation);
					}

					@Override
					public void onAnimationStart(Animation animation) {
						if (al != null)
							al.onAnimationStart(animation);
						getSupportActionBar().show();
			    		
					}
	    			
	    		}, abHeight);*/
	    		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
	    	    //        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	    		getSupportActionBar().show();
	    		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	    			
	            fullScreen = false;
	    		return false;
	    	} else {
	    		/*UIUtils.collapse(getSupportActionBar(), new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
							 if (al != null)
								al.onAnimationEnd(animation);
							
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						if (al != null)
							al.onAnimationRepeat(animation);
					}

					@Override
					public void onAnimationStart(Animation animation) {
						if (al != null)
							al.onAnimationStart(animation);
					}
	    			
	    		});*/
	    		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    		 getSupportActionBar().hide();
	    		 //requestWindowFeature(Window.fe);
	    	 	 fullScreen = true;
	    	 	  return true;
	    	}
	    }
	      @Override
	      public void onRestoreInstanceState(Bundle savedInstanceState) {
	        // Restore the previously serialized current dropdown position.
	        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
	        	try {
	        		getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
	        	} catch(Exception e) {
	        		
	        	}
	        }
	      }

	      @Override
	      public void onSaveInstanceState(Bundle outState) {
	        // Serialize the current dropdown position.
	        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, pos);
	      }
	      
	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()) {
			case R.id.action_attach_thing:
				new AttachSensorDialog(this,null).show();
				break;
			case R.id.action_about:
				startActivity(new Intent(this, AboutActivity.class));
				break;
			case R.id.action_quit:
				GaonicDialog dialog = new GaonicDialog(this,null);
				dialog.setPositiveButton("Quit", new MyAppDialog.OnClickListener() {
					
					@Override
					public void onClick(MyAppDialog dialog, int which) {
						finish();
					}
				});
				dialog.setIcon(R.drawable.ic_launcher);
				dialog.setTitle("Confirm Quit");
				dialog.setMessage("Are you sure you want to log out and close Gaonic?");
				dialog.setNegativeButton("Cance", null);
				dialog.show();
				break;
				
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getSupportMenuInflater().inflate(R.menu.main, menu);
			searchMenuItem = menu.findItem(R.id.action_search_things);
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView) searchMenuItem.getActionView();
	        if (null != searchView )
	        {
	            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	            searchView.setIconifiedByDefault(false);   
	        }

	        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
	        {
	            public boolean onQueryTextChange(String newText) 
	            {
	                // this is your adapter that will be filtered
	            	if (TextUtils.isEmpty(newText)) {
	            		fragment.resetList();
	            	}
	            	fragment.getAdapter().getFilter().filter(newText);
	                return true;
	            }

	            public boolean onQueryTextSubmit(String query) 
	            {
	                // this is your adapter that will be filtered
	            	if (TextUtils.isEmpty(query)) {
	            		fragment.resetList();
	            	}
	                fragment.getAdapter().getFilter().filter(query);
	                return true;
	            }
	        };
	        if (null != searchView )
	        {
	        	searchView.setOnQueryTextListener(queryTextListener);
	        	searchView.setOnCloseListener(new OnCloseListener() {

					@Override
					public boolean onClose() {
						fragment.resetList();
						return false;
					}});
	        }

			return true;
		}

		public void prepareSearch() {
			searchMenuItem.expandActionView();
			
		}
	
		public void closeSearch() {
			if (searchMenuItem.isActionViewExpanded() ) {
				searchMenuItem.collapseActionView();
				fragment.resetList();
			}
		}
}
