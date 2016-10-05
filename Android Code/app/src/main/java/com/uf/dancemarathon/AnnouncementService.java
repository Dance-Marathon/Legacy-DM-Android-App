package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class AnnouncementService extends Service {
	
	private int lastSize;
	
	//Set up receiver to receive TIME_TICK intents
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			//The time ticks every minute
			if(intent.getAction().equals(Intent.ACTION_TIME_TICK))
			{
				new AnnouncementsLoader().execute();
			}
		}
		
	};;
		
	public AnnouncementService() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		this.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		lastSize = readCache().size();
		//Log.d("service","in start");
		return Service.START_STICKY;
	}

	
	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}


	
	/**
	 * Read the cache for announcements
	 * @return The cache announcements if they exist. New arraylist otherwise.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Announcement> readCache()
	{
		Object o = CacheManager.readObjectFromCacheFile(this, CacheManager.ANNOUNCEMENTS_FILENAME);
		if(o != null)
			return (ArrayList<Announcement>) o;
		else
			return new ArrayList<Announcement>();
	}
	
	/**
	 * Get announcements and write to cache. 
	 * @return true if there are new announcements
	 */
	private boolean getNewAnnouncements()
	{
		ArrayList<Announcement> announcements = new ArrayList<Announcement>();
		boolean isNew = false;
		try {
			String path = new ConfigFileReader(this.getApplicationContext()).getSetting("announcementsPath");
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
			
			//Log.d("service", String.valueOf(lastSize));
			//If new announcements have been found, update ments and cache
			if(announcements.size() > lastSize)
			{
				CacheManager.writeObjectToCacheFile(this, announcements, CacheManager.ANNOUNCEMENTS_FILENAME);
				isNew = true;
			}	
			lastSize = announcements.size();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return isNew;
			
		
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


	
	/**
	 * This method creates a pending intent to open the HomeActivity when
	 * the notification is pressed.
	 * @return The pending intent to use
	 */
	private PendingIntent getMainPendingIntent()
	{
		Intent intent = new Intent(this, HomeActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(HomeActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		return pIntent;
	}
	
	@Override	
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * This class handles loading announcements and notifying the user if there are new announcements.
	 */
	private class AnnouncementsLoader extends AsyncTask<Void, Double, Boolean>
	{
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		//This method will perform the request to the web service and try to obtain the events
		@Override
		protected Boolean doInBackground(Void... params)
		{
			return getNewAnnouncements();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean newAnnouncementsExist) {
			// TODO Auto-generated method stub
			if(newAnnouncementsExist)
				notifyUser(18);
		}
		
		private void notifyUser(int mId)
		{
			//Set vibration pattern
			long[] pattern = {1, 1000};
			
			//Set the pending intent for when the user clicks the notification
			PendingIntent pIntent = getMainPendingIntent();
					
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(AnnouncementService.this)
			        .setSmallIcon(R.drawable.launcher_icon)
			        .setContentTitle("New Announcements Available!")
			        .setContentText("Click to see new announcements")
			        .setAutoCancel(true)
			        .setVibrate(pattern)
			        .setContentIntent(pIntent);
				
				NotificationManager mNotificationManager =
					    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					// mId allows you to update the notification later on.
					mNotificationManager.notify(mId, mBuilder.build());
		}
		
		
	}

}
