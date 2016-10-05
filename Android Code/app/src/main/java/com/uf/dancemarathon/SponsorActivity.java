package com.uf.dancemarathon;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SponsorActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sponsor);
		
		//Hide actionbar
		ActionBar bar = this.getSupportActionBar();
		bar.hide();
		
		//Add fragment
		FragmentManager manager = getSupportFragmentManager();
		
		//Prevents stacking fragments on top of each other
		if(savedInstanceState != null)
			return;
		
		manager.beginTransaction().add(R.id.sponsor_activity, new ConstructionFragment()).commit();
	}
	
	protected void onStart()
	{
		super.onStart();
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "SponsorActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.SPONSOR_ACTIVITY_NAME);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sponsor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

