package com.uf.dancemarathon;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;

/**
 * This class is necessary to implement google analytics.
 * See <i>https://developers.google.com/analytics/devguides/collection/android/v4/</i>
 * for more information.
 * @author Chris Whitten
 *
 */
public class MyApplication extends Application
{
	private static final String PROPERTY_ID = "UA-31255631-4";
	private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public MyApplication()
	{
		super();
	}
	
	public enum TrackerName {
	    APP_TRACKER, // Tracker used only in this app.
	    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
	    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	  }

	synchronized Tracker getTracker(TrackerName trackerId) {
	    if (!mTrackers.containsKey(trackerId)) {
	    	
	      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
	      Tracker t = analytics.newTracker(PROPERTY_ID);
	      mTrackers.put(trackerId, t);
	    }
	    return mTrackers.get(trackerId);
	  }
}
