package com.uf.dancemarathon;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.uf.dancemarathon.FontSetter.fontName;

public class EventActivity extends AppCompatActivity
{
	private String ACTION_BAR_TITLE = "Event Details";
	private Event event;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		//Get the event
		Bundle b = getIntent().getExtras();
		event = b.getParcelable("event");
		
		//Get the textviews
		TextView title = (TextView) findViewById(R.id.event_page_title);
		TextView desc = (TextView) findViewById(R.id.event_page_desc);
		TextView stime = (TextView) findViewById(R.id.event_page_stime);
		TextView stime_label = (TextView) findViewById(R.id.event_page_stime_label);
		TextView etime = (TextView) findViewById(R.id.event_page_etime);
		TextView etime_label = (TextView) findViewById(R.id.event_page_etime_label);
		TextView location = (TextView) findViewById(R.id.event_page_loc);
		TextView location_label = (TextView) findViewById(R.id.event_page_loc_label);
		
		//Get the button
		Button calButton = (Button) findViewById(R.id.addToCalendar_button);
		calButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addEventToCalendar();
			}
			
		});
		// Set fonts
		FontSetter.setFont(this, fontName.AGBMed, title);
		FontSetter.setFont(this, fontName.AGBReg, desc, stime, etime, location, stime_label, etime_label, location_label);
		
		//Set the textviews
		title.setText(event.getTitle());
		desc.setText(event.getDescription());
		stime.setText(event.getFormattedStartDate("hh:mm aa   MM/dd/yyyy"));
		etime.setText(event.getFormattedEndDate("hh:mm aa   MM/dd/yyyy"));
		location.setText(event.getLocation());

		//Customize action bar
		ActionBar bar = getSupportActionBar();
		TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
		FontSetter.setFont(this, fontName.ALTB, customBar);
	}
	
	/**
	 * Adds the event to the user's calendar
	 */
	private void addEventToCalendar()
	{
		Intent intent = new Intent(Intent.ACTION_INSERT)
        .setData(Events.CONTENT_URI)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartDate().getTime())
        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEndDate().getTime())
        .putExtra(Events.TITLE, event.getTitle())
        .putExtra(Events.DESCRIPTION, event.getDescription())
        .putExtra(Events.EVENT_LOCATION, event.getLocation())
        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        .putExtra(Events.HAS_ALARM, true);
		startActivity(intent);
	}
	
	protected void onStart()
	{
		super.onStart();
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "EventActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.EVENT_ACTIVITY_PREFIX + event.getTitle());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
