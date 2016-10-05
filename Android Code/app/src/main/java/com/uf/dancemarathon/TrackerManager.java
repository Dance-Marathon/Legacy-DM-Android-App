package com.uf.dancemarathon;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * This class provides functionality for google analytics
 * @author Chris
 *
 */
public class TrackerManager
{

	public final static String ABOUT_ACTIVITY_NAME = "about_screen";
    public final static String CONTACTUS_ACTIVITY_NAME = "Contact Us Screen";
    public final static String EVENT_ACTIVITY_PREFIX = "Event: ";
    public final static String FAQ_ACTIVITY_NAME = "FAQ Screen";
    public final static String HOME_ACTIVITY_NAME = "Main Screen";
    public final static String LOGIN_ACTIVITY_NAME = "Login Screen";
    public final static String SPONSOR_ACTIVITY_NAME = "Sponsor Screen";
    public final static String USER_ACTIVITY_NAME = "User Screen";

	/**
	 * Send a screen view hit using the default app tracker
	 * @param a The application object
	 * @param screenName The name of the screen
	 */
	public static void sendScreenView(MyApplication a, String screenName)
	{
		Tracker t = a.getTracker(MyApplication.TrackerName.APP_TRACKER);
		t.setScreenName(screenName);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	/**
	 * Send a screen view hit using the input tracker
	 * @param screenName The screen name
	 * @param t The tracker to use
	 */
	public static void sendScreenView(String screenName, Tracker t)
	{
		t.setScreenName(screenName);
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	/**
	 * Send an event hit using the default tracker
	 * @param a The application object
	 * @param category The category of the event
	 * @param action The action that occurred
	 * @param label The label of the event
	 */
	public static void sendEvent(MyApplication a, String category, String action, String label)
	{
		Tracker t = a.getTracker(MyApplication.TrackerName.APP_TRACKER);
		t.send(new HitBuilders.EventBuilder()
		.setCategory(category)
		.setAction(action)
		.setLabel(label)
		.build());
	}
}
