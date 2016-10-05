package com.uf.dancemarathon;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.uf.dancemarathon.FontSetter.fontName;


/**
 * This activity displays basic contact information
 * @author Chris Whitten
 *
 */
public class ContactUsActivity extends ActionBarActivity {

    private String ACTION_BAR_TITLE = "Contact Us";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);

		//Customize action bar
		ActionBar bar = getSupportActionBar();
		TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
        FontSetter.setFont(this, fontName.ALTB, customBar);

		
		// Set font of textviews
		
		TextView cu_label1 = (TextView) findViewById(R.id.cu_label1);
		TextView cu_content1 = (TextView) findViewById(R.id.cu_content1);
		TextView cu_label2 = (TextView) findViewById(R.id.cu_label2);
		TextView cu_content2 = (TextView) findViewById(R.id.cu_content2);
		TextView cu_label3 = (TextView) findViewById(R.id.cu_label3);
		TextView cu_content3 = (TextView) findViewById(R.id.cu_content3);
		TextView cu_label4 = (TextView) findViewById(R.id.cu_label4);
		TextView cu_content4 = (TextView) findViewById(R.id.cu_content4);
		
		FontSetter.setFont(this, fontName.ALTB, cu_label1, cu_label2, cu_label3, cu_label4);
		FontSetter.setFont(this, fontName.ALTR, cu_content1, cu_content2, cu_content3, cu_content4);
		
	}
	
	protected void onStart()
	{
		super.onStart();
		
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "ContactUsActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.CONTACTUS_ACTIVITY_NAME);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
}
