package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.uf.dancemarathon.FontSetter.fontName;


/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment
{
	private Context c;
	private AnnouncementsLoader loader;
	
	//Website Paths that will be used if config file read fails//
	private final String DEFAULT_GAME_PATH = "http://www.google.com";
	private final String DEFAULT_WEBSITE_PATH = "http://www.floridadm.org/";
	private final String DEFAULT_DONATE_PATH = "http://events.dancemarathon.com/index.cfm?fuseaction=donorDrive.event&eventID=786";

    
	public HomeFragment()
	{
		// Required empty public constructor
	}

	public static HomeFragment newInstance(Context c)
	{
		HomeFragment f = new HomeFragment();
		f.c = c;
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_home, container, false);

		//Get textviews and set fonts
		TextView header_text = (TextView) v.findViewById(R.id.header_text);
		TextView announcement_header = (TextView) v.findViewById(R.id.announcements_title);
		TextView game_text = (TextView) v.findViewById(R.id.game);
		TextView web_text = (TextView) v.findViewById(R.id.website);
		TextView donate = (TextView) v.findViewById(R.id.donate);
		
		FontSetter.setFont(getActivity(), fontName.ALTM, header_text, game_text, web_text, donate);
		FontSetter.setFont(getActivity(), fontName.ALTB, announcement_header);

		//Set button listeners
		setButtonListeners(v);
		
		//Try to read data from cache
		 Object o = CacheManager.readObjectFromCacheFile(getActivity(), CacheManager.ANNOUNCEMENTS_FILENAME);

		 //If failed, force update
		if(o == null)
		{
			loader = new AnnouncementsLoader();
			loader.execute();
		}
		//Else show cache events
		else
		{
			ArrayList<Announcement> ments = (ArrayList<Announcement>) o;
			//List must be greater than zero to show cache data
			if(ments.size() > 0)
				 showAnnouncements(ments, v);
			else
			 {
				loader = new AnnouncementsLoader();
				loader.execute();
			 }
		}
		
		return v;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void onStop()
	{
		super.onStop();
		if(loader != null)
			loader.cancel(true);
	}

	
	/**
	 * Update the announcements listview with the input arraylist
	 * @param ments The new announcements
	 * @param v The view containing the listview
	 */
	private void showAnnouncements(ArrayList<Announcement> ments, View v)
	{
		final ListView list = (ListView) v.findViewById(R.id.announcements_list);
		AnnouncementsAdapter adapter = new AnnouncementsAdapter(getActivity(),ments);
		list.setAdapter(adapter);
		list.setClickable(false);
	}
	
	/**
	 * This method displays an error toast
	 */
	private void displayErrorToast()
	{
		Toast toast = Toast.makeText(getActivity(), "Could not load Announcements", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	
	//Button Handling//
	/**
	 * This method sets the listeners for the home screen's buttons
	 * @param v The view the buttons belong to
	 */
	private void setButtonListeners(View v)
	{
		final Button gameButton = (Button) v.findViewById(R.id.game);
		final Button websiteButton = (Button) v.findViewById(R.id.website);
		final Button donateButton = (Button) v.findViewById(R.id.donate);
		
		gameButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sendButtonHit(gameButton);

				//Start game activity
				Intent intent = new Intent(getActivity(), GameActivity.class);
				startActivity(intent);
			}
		});
		
		websiteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sendButtonHit(websiteButton);
				openLink(websiteButton);
			}
		});
		
		donateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				sendButtonHit(donateButton);
				openLink(donateButton);
			}
		});
	}
	
	/**
	 * This method implements google analytics to track the button clicks
	 * @param b The button to track
	 */
	private void sendButtonHit(final Button b)
    {
		String buttonName = b.getText().toString();
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "HomeActivity");
			TrackerManager.sendEvent((MyApplication) getActivity().getApplication(), "Button", "Clicked", buttonName);
		}
	}
	
	/**
	 * Called by the buttons to open browser webpages.
	 * @param view The button which called this method
	 */
	public void openLink(View view)
	{
        String gamePath;
        String websitePath;
        String donatePath;
		//Try to get settings
		try {
			ConfigFileReader cReader = new ConfigFileReader(getActivity());
			gamePath = cReader.getSetting("gamePath");
			websitePath = cReader.getSetting("websitePath");
			donatePath = cReader.getSetting("donatePath");
		} catch (Exception e) {
			//Default paths
            gamePath = DEFAULT_GAME_PATH;
            websitePath = DEFAULT_WEBSITE_PATH;
            donatePath = DEFAULT_DONATE_PATH;
		}
		
		//Open the links
		int id = view.getId();
        switch(id)
        {
            case R.id.game: openWebsite(gamePath); break;
            case R.id.website: openWebsite(websitePath); break;
            case R.id.donate: openWebsite(donatePath); break;
        }
	}

	
	/**
	 * This class is responsible for loading the events. It is necessary because Android
	 * does not allow you to have loading operations on the same thread as the UI.
	 */
	private class AnnouncementsLoader extends AsyncTask<Void, Double, ArrayList<Announcement>>
	{
		private boolean loadSuccessful;
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		//This method will perform the request to the web service and try to obtain the events
		@Override
		protected ArrayList<Announcement> doInBackground(Void... params)
		{
			
			ArrayList<Announcement> announcements = new ArrayList<Announcement>();
			try
			{	
				String path = new ConfigFileReader(getActivity()).getSetting("announcementsPath");
				URL url = new URL(path); //The path to the webservice 
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoInput(true);
				conn.setDoOutput(true);

				String query = "{\"query\": {\"recordType\": \"Announcement\"}}";

				//Write params
				byte[] paramData = query.getBytes();
				int paramLength = paramData.length;
				conn.setRequestProperty("Content-Length", Integer.toString(paramLength));
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.write(paramData);
				wr.close();

				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String announcementsJSON = "";
				while ((line = reader.readLine()) != null)
					announcementsJSON += line;

				reader.close();

				announcements = parseAnnouncementsJSON(new JSONObject(announcementsJSON));
				
				//Write data to cache
				CacheManager.writeObjectToCacheFile(getActivity(), announcements, CacheManager.ANNOUNCEMENTS_FILENAME);
				
				//Set success flag to true
				loadSuccessful = true;
				
				
			} catch (Exception e)
			{
				//e.printStackTrace();
				loadSuccessful = false;
			}
			
			return announcements;
		}
		
			
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		//This method will update the UI after the load is finished.
		protected void onPostExecute(ArrayList<Announcement> announcements)
		{
			if(loadSuccessful)
				showAnnouncements(announcements, getView());
			else
				displayErrorToast();
		}
		
		/**
		 * @param aJson The JSON object containing the events
		 * @return An arraylist of events
		 * @throws JSONException if parse fails
		 */
		protected ArrayList<Announcement> parseAnnouncementsJSON(JSONObject aJson)
		{
            ArrayList<Announcement> announcements = new ArrayList<Announcement>();
            try {
                JSONArray arr = aJson.getJSONArray("records");

                for (int i = 0; i < arr.length(); i++) {
                    String text = arr.getJSONObject(i).getJSONObject("fields").getJSONObject("text").getString("value").trim();
                    long date = arr.getJSONObject(i).getJSONObject("fields").getJSONObject("date").getLong("value");
                    Date d = new Date(date);
                    Announcement a = new Announcement(text, d);
                    if (a.hasOccurred())
                        announcements.add(a);
                }

            } catch(JSONException e){
            }

			return announcements; 
		}
	}
	
	/**
	 * Called by the buttons to open browser webpages.
	 * @param link the website url to go to.
	 */
	public void openWebsite(String link)
	{
		Log.d("link", link);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(intent);
	}
}
