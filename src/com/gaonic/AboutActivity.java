package com.gaonic;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class AboutActivity extends SherlockFragmentActivity {

	 private static final String[] CONTENT = new String[] { "GAONIC", "CONTACT US", };
	    private static final int[] ICONS = new int[] {
	            R.drawable.ic_action_about,
	            R.drawable.ic_action_contact_us,
	    };

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_about);

	        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

	        ViewPager pager = (ViewPager)findViewById(R.id.pager);
	        pager.setAdapter(adapter);

	        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
	        indicator.setViewPager(pager);
	    }

	    class GoogleMusicAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	        public GoogleMusicAdapter(FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public Fragment getItem(int position) {
	        	if (position == 0) 
	        		return new AboutGaonicFragment();
	        	return new ContactUsFragment();
	        	
	        
	           
	        }

	        @Override
	        public CharSequence getPageTitle(int position) {
	            return CONTENT[position % CONTENT.length].toUpperCase();
	        }

	        @Override public int getIconResId(int index) {
	          return ICONS[index];
	        }

	      @Override
	        public int getCount() {
	          return CONTENT.length;
	        }
	    }
	}