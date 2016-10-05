package com.uf.dancemarathon;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseArray;




/**
 * @author Chris Whitten
 * This service is responsible for sending notifications to the user
 * when DM events are close at hand.
 */
public class NotificationService extends Service {
	
	/**
	 * The lesser proximity to judge events by
	 */
	private static int eventProx1 = 5;
	/**
	 * The greater proximity to judge events by
	 */
	private static int eventProx2 = 15;
	
	/**
	 * Keeps track of the number of notifications this service has published
	 */
	private int numActiveNotifications;
	
	private long currentTime; 
	
	//Set up receiver to receive TIME_TICK intents
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			//The time ticks every minute
			if(intent.getAction().equals(Intent.ACTION_TIME_TICK))
			{
				//testNotification("Test");
				//Update current time
				currentTime = Calendar.getInstance().getTimeInMillis();
				setupEventNotifications();
				
				////Log.d("Notifications", "Done with event notification setup");
			}
			
			//Recreate the service and delete the old one
			NotificationService.this.stopSelf();
			context.startService(new Intent(context, NotificationService.class));
			
		}
		
	};;
	
	public NotificationService() {
		super();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		// TODO Auto-generated method stub
		numActiveNotifications = 0;
		////Log.d("Notification", "Registering receiver");
		this.registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		return Service.START_STICKY;
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}
	
	/**
	 * This method does all the necessary gruntwork to setup the event notifications
	 * for all events currently in the cache file.
	 */
	private void setupEventNotifications()
	{
		Object o = CacheManager.readObjectFromCacheFile(this, CacheManager.EVENTS_FILENAME);
		//Stop this service if there is no event cache file
		if(o == null)
			this.stopSelf();
		else
		{
			//Log.d("debug", "In else");
			//if(verifyEventArrayList((ArrayList<?>) o))
			{
				////Log.d("debug", "In if");
				ArrayList<Event> allEvents = (ArrayList<Event>) o;
				SparseArray<ArrayList<Event>> upcomingEvents = new SparseArray<ArrayList<Event>>();
				
				//Test Events
				try {
					Event t1 = new Event("1","Test Event", "blah", "2015-01-16 15:47:00", "2015-01-13 22:54:00", "2015-01-13 06:00:00", "blah");
					Event t2 = new Event("2","Test Event 2", "blah", "2015-01-16 15:37:00", "2015-01-14 22:54:00", "2015-01-13 06:00:00", "blah");
					allEvents.add(t1);
					allEvents.add(t2);
					//createEventNotification(t1, 5, 1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				
				//Add events that are 5 and 15 mins away to the hashmap
				upcomingEvents.put(eventProx1, checkForUpcomingEvents(allEvents,eventProx1));
				upcomingEvents.put(eventProx2, checkForUpcomingEvents(allEvents,eventProx2));
				
				//Create the notifications
				createEventNotifications(upcomingEvents.get(eventProx1), eventProx1);
				createEventNotifications(upcomingEvents.get(eventProx2), eventProx2);
			}
			
		}
	}
	
	/**
	 * This method creates individual notifications for each event
	 * in the input arraylist.
	 * @param events The events
	 * @param proximity Minutes until the events start
	 */
	private void createEventNotifications(ArrayList<Event> events, int proximity)
	{
		Iterator<Event> i = events.iterator();
		while(i.hasNext())
		{
			Event e = i.next();
			numActiveNotifications++;
			//Log.d("debug", "Creating " + e.getTitle());
			createEventNotification(e, proximity, numActiveNotifications);
		}
	}
	
	/**
	 * This method creates an individual notification for the specified event
	 * @param e The event
	 * @param proximity Minutes until the event starts
	 */
	private void createEventNotification(Event e, int proximity, int mId)
	{
		//Set vibration pattern
		long[] pattern = {1, 1000};
		
		//Set the pending intent for when the user clicks the notification
		PendingIntent pIntent = getMainPendingIntent();
				
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.launcher_icon)
		        .setContentTitle("Event: " + e.getTitle())
		        .setContentText("Happening in " + proximity + " minutes!")
		        .setAutoCancel(true)
		        .setVibrate(pattern)
		        .setContentIntent(pIntent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(mId, mBuilder.build());
	}
	
	
	/**
	 * This method checks for events which are within the designated time proximity.
	 * @param events The list of events
	 * @param timeProximity The proximity an event must be to be considered upcoming
	 * @return The list of upcoming events
	 */
	private ArrayList<Event> checkForUpcomingEvents(ArrayList<Event> events, double timeProximity)
	{
		ArrayList<Event> upcoming = new ArrayList<Event>();
		double minInMillis = 1 * 60 * 1000;
		
		Iterator<Event> i = events.iterator();
		while(i.hasNext())
		{
			Event e = i.next();
			
			//Get time difference
			long eventTime = e.getStartDate().getTime();
			long timeDiff = eventTime - currentTime;
			
			//Get minute difference
			double minDiff = (long) Math.ceil(timeDiff / minInMillis); //Need to do ceil to account for off by 1 error
			
			//String logString = "timeDiff: " + String.valueOf(timeDiff) + " minDiff: " + String.valueOf(minDiff);
			//Log.d("Upcoming", logString);
			if(minDiff == timeProximity)
			{
				upcoming.add(e);
				//Log.d("Upcoming", e.getTitle());
			}
		}
		
		//Log.d("Upcoming", String.valueOf(upcoming.size()));
		return upcoming;
		
	}
	
	/**
	 * This method creates a pending intent to open the HomeActivity when
	 * the notification is pressed.
	 * @return The pending intent to use
	 */
	private PendingIntent getMainPendingIntent()
	{
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("start_source", "Service");
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(HomeActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		return pIntent;
	}
	
	/**
	* This method just produces a basic test notification
	 */
	private void testNotification(String title)
	{
		//Set vibration pattern
		long[] pattern = {1, 750, 500, 750};
				
		//Set the pending intent for when the user clicks the notification
		PendingIntent pIntent = getMainPendingIntent();
		
		//Create notification
		NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.launcher_icon)
	        .setContentTitle(title)
	        .setContentText("Hello World!")
	        .setAutoCancel(true)
	        .setVibrate(pattern)
	        .setContentIntent(pIntent);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(1, mBuilder.build());
	}
	
	/**
	 * This method verifies that every object in the list is of type {@link Event}
	 * @param list
	 * @return True if every object is Event. False otherwise
	 */
	private boolean verifyEventArrayList(ArrayList<?> list)
	{
		Iterator<?> i = list.iterator();
		while(i.hasNext())
		{
			Object o = i.next();
			if(!(o instanceof Event))
				return false;
		}
		
		return true;
	}

	
	@Override
	//This method is a required override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}


	
	


}
