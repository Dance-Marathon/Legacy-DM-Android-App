package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.uf.dancemarathon.FontSetter.fontName;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity displays the About Us information in a scrollable textview
 * @author Chris Whitten
 *
 */
public class AboutActivity extends AppCompatActivity {

	private String ACTION_BAR_TITLE = "Our Story";
    private String CONTENT_FILENAME = "about.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		try {
			String aboutContent = parseJSONData(CONTENT_FILENAME);
			
			//Set content
			TextView contentView = (TextView) findViewById(R.id.aboutus_content);
			FontSetter.setFont(this, fontName.ALTMO, contentView);
			contentView.setText(aboutContent);
			contentView.setMovementMethod(new ScrollingMovementMethod());
			
			/*//Set title
			TextView aboutTitle = (TextView) findViewById(R.id.aboutus_title);
			FontSetter.setFont(this, fontName.ALTB, aboutTitle);*/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			displayErrorToast();
			//e.printStackTrace();
		}

        //Customize action bar
        ActionBar bar = getSupportActionBar();
        TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
        FontSetter.setFont(this, fontName.ALTB, customBar);

	}
	
	protected void onStart()
	{
		super.onStart();
		
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			////Log.d("Tracking", "AboutActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.ABOUT_ACTIVITY_NAME);
		}
	}
	
	/**
	 * This method displays an error toast
	 */
	private void displayErrorToast()
	{
		Toast toast = Toast.makeText(this, "Could not display About Page Content", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * Parses the input filename looking for the about content
	 * @param fileName The file to use
	 * @return A string containing the content
	 * @throws IOException
	 * @throws JSONException
	 */
	private String parseJSONData(String fileName) throws IOException, JSONException
	{
		String json="";
		String next="";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getAssets().open(fileName)));
		while((next=reader.readLine()) != null)
			json+=next;
		reader.close();
		
		JSONObject o = new JSONObject(json);
		return o.getString("About");
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
