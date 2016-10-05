package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;



public class UserActivity extends AppCompatActivity
{
	private KinteraUser user;
	private UserLoader loader;
    private String userPassword;
	private final String USER_WEBSERVICE_PATH = "http://dev.floridadm.org/app/kintera.php";
	private final String ACTION_BAR_TITLE = "Fundraising Progress";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		//Get user from intent
		KinteraUser user = getIntent().getExtras().getParcelable("user");
		this.user = user;
        this.userPassword = getIntent().getExtras().getString("password");
		
		//Populate all of the necessary fields
		setFields(user);
		
		//Instantiate loader to prevent null
		loader = new UserLoader();

		//Customize action bar
		ActionBar bar = getSupportActionBar();
		TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
		FontSetter.setFont(this, FontSetter.fontName.ALTB, customBar);
	}
	
	protected void onStop()
	{
		super.onStop();
		//If a loader exists, cancel its execution
		if(loader != null)
			loader.cancel(true);
	}
	
	protected void onStart()
	{
		super.onStart();
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "UserActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.USER_ACTIVITY_NAME);
		}
	}
	
	/**
	 * Set all the important data fields for this view
	 * @param user The user
	 */
	private void setFields(final KinteraUser user)
	{
		//Set textviews
		TextView name = (TextView) findViewById(R.id.user_name);
		TextView goal = (TextView) findViewById(R.id.user_goal);
		TextView raised = (TextView) findViewById(R.id.user_raised);
		TextView progress = (TextView) findViewById(R.id.user_progress);
		
		name.setText(user.realName);
		goal.setText("$" + Integer.toString((int) user.fundGoal));
		raised.setText("$" + Integer.toString((int) user.fundRaised));
		int percentRaised = (int) ((user.fundRaised / user.fundGoal) * 100);
		progress.setText(Integer.toString(percentRaised)+ "%");
		
		//Set listener for kintera page button
		Button pageButton = (Button) findViewById(R.id.user_page_button);
		pageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				openKinteraPage(user.pageURL);
			}
		});
	}
	
	/**
	 * Clears the user cache file and exits this activity
	 * @param v The //Logout button view
	 */
	public void logout(View v)
	{
		CacheManager.clearCacheFile(this, CacheManager.USER_FILENAME);
		this.finish();
	}
	
	/**
	 * Refresh the user information
	 * @param username The username to use
	 * @param password The password to use
	 */
	public void refreshUser(String username, String password)
	{
		findViewById(R.id.user_loading_overlay).setVisibility(View.VISIBLE);
		loader = new UserLoader();
		loader.execute(username, password);
	}
	
	/**
	 * Open the user's kintera page in the browser
	 * @param url
	 */
	public void openKinteraPage(String url)
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);


        return false; //return false to hide the menu
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh)
		{
			refreshUser(user.userName, userPassword);
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This method shows a toast with the given message
	 * @param message The message to show
	 */
	private void makeToast(String message)
	{
		Toast toast = Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.show();
	}
	
	
	/**
	 * @author Chris Whitten
	 * This class is responsible for loading all the kintera information about the user.
	 * The onProgressUpdate() method is used to display any error messages as toasts
	 *
	 */
	private class UserLoader extends AsyncTask<String, String, KinteraUser>
	{
		String username="";
		String password="";
		boolean loadSuccessful = false;
        @Override
        protected KinteraUser doInBackground(String... params)
        {
            //Begin loading work
            KinteraUser user = new KinteraUser();
            URL url;
            try
            {
                //Get username and password from params
                username = params[0].trim();
                password = params[1];

                //Set path
                String path = USER_WEBSERVICE_PATH;

                //Connect to the webservice
                String urlParams  = "username=" + username + "&password=" + password;
                byte[] postData = urlParams.getBytes( StandardCharsets.UTF_8 );

                url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                //Write parameters to POST
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParams);
                wr.flush();
                wr.close();

                //Parse JSON response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if(conn.getResponseCode() == 200)
                {
                    String jsonRep = reader.readLine().trim();
                    if(jsonRep.equals("Error"))
                        publishProgress("Invalid Credentials");
                    else
                    {
                        JSONObject o = new JSONObject(jsonRep);
                        user = parseUserJson(o);
                        loadSuccessful = true;
                    }
                }
                else
                {
                    publishProgress("Sorry, we are currently experiencing server problems!");
                }

            } catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                publishProgress("Sorry, we are currently experiencing technical problems!");
                //e.printStackTrace();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                publishProgress("Could not load user data! Check internet connection.");
                //e.printStackTrace();
            } catch (JSONException e)
            {
                // TODO Auto-generated catch block
                publishProgress("Sorry, we are currently experiencing technical problems!");
                //e.printStackTrace();
            }

            return user;
        }
		
		protected void onProgressUpdate(String... params)
		{
			makeToast(params[0]);
		}
		protected void onPostExecute(KinteraUser user)
		{	
			//If the load was successful, refresh the user activity
			if(loadSuccessful)
			{
				//Write user data to cache
				CacheManager.clearCacheFile(UserActivity.this, CacheManager.USER_FILENAME);
				CacheManager.writeObjectToCacheFile(UserActivity.this, user, CacheManager.USER_FILENAME);
				
				//Refresh the page
				setFields(user);
				
				UserActivity.this.user = user;
				
				//Clear loading overlay
				findViewById(R.id.user_loading_overlay).setVisibility(View.GONE);
			}
			else
			{
			}
		}
		
		private KinteraUser parseUserJson(JSONObject o) throws JSONException
		{
			//Parse json
			String realName = o.getString("ParticipantName");
			String pageURL = o.getString("PersonalPageUrl");
			double fundGoal = Double.parseDouble(o.getString("PersonalGoal"));
			double fundRaised = Double.parseDouble(o.getString("PersonalRaised"));
			
			KinteraUser user = new KinteraUser(username, realName, fundGoal, fundRaised, pageURL);
			return user;
		}
	}
	
}
