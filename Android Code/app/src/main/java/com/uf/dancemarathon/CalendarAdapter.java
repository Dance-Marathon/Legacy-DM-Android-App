package com.uf.dancemarathon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uf.dancemarathon.FontSetter.fontName;

public class CalendarAdapter extends ArrayAdapter<Event>
{

	private Context context;
	/**
	 * The events to display
	 */
	private ArrayList<Event> events;
	
	public CalendarAdapter(Context c, ArrayList<Event> events)
	{
		//Must make call to the parent constructor
		super(c, R.layout.timeline_item_view, events);
		
		Collections.sort(events);
		
		this.events = events;
		this.context = c;
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return events.size();
	}

	@Override
	public Event getItem(int position)
	{
		// TODO Auto-generated method stub
		return events.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View itemView;
		//Create inflater 
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Event e = events.get(position);

        //If e doesn't have an image yet, show item view with some basic info and a loading wheel
        if(!e.hasImage()) {
            //Get the individual item view
            itemView = inflater.inflate(R.layout.timeline_item_view, parent, false);

            //Set all the values for the view
            setItemView(itemView, position);
        }
        //Else, show the image
        else
        {
            //Inflate the imageview
            itemView = inflater.inflate(R.layout.event_image_item_view, parent, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.event_item_image);
            imageView.setImageDrawable(new BitmapDrawable(context.getResources(), e.getImage()));

            TextView timeView = (TextView) itemView.findViewById(R.id.event_item_image_time);
            setStartTimeString(timeView, e);
            timeView.bringToFront();
            FontSetter.setFont(context, fontName.ALTM, timeView);
        }

        return itemView;
	}
	
	/**
	 * This method sets the values of all the views which comprise the individual event view.
	 * @param itemView The view to modify
	 * @param position The position of the item
	 */
	private void setItemView(View itemView, int position)
	{
		Event e = events.get(position);
		
		//Get title and location text views
        TextView title = (TextView) itemView.findViewById(R.id.tlineitem_title);
        TextView time = (TextView) itemView.findViewById(R.id.tlineitem_startTime);
        TextView month = (TextView) itemView.findViewById(R.id.tlineitem_month);
        TextView day = (TextView) itemView.findViewById(R.id.tlineitem_day);
        
        //Set text views //
        
        //Set title
        String titleText = e.getTitle();
        title.setText(makeCondensedString(titleText, 24));

        
        //Set start time
        setStartTimeString(time, e);
        
       
        //Set months
        String monthText = e.getMonthText(true);
        month.setText(monthText);
        
        //Set day
        String dayText = Integer.toString(e.getDay(true));
        day.setText(dayText);
        
        // Set Fonts
        FontSetter.setFont(context, fontName.AGBReg, title, time, month, day);
        
        
       
	}

	/**
	 * This method returns a string with as many words as can fit in the
	 * input number of characters 
	 * @param rawString The unformatted string
	 * @param maxCharacters The maximum number of characters
	 * @return The new string
	 */
	private String makeCondensedString(String rawString, int maxCharacters)
	{
		String[] words = rawString.split(" ");
		String newString ="";
		for(int i = 0; i < words.length; i++)
		{
			String nextWord = words[i];
			if(newString.length() + nextWord.length() > maxCharacters)
				break;
			else
				newString+=" " + nextWord;
		}
		return newString.trim();
	}
	
	private void setStartTimeString(TextView view, Event e)
	{
		//Check to see if the event is happening now
		long startTime = e.getStartDate().getTime() ;
		long endTime = e.getEndDate().getTime();
		long currentTime = Calendar.getInstance(Locale.US).getTimeInMillis();
		
		//If happening now, set to in progress text
		if(startTime < currentTime && endTime > currentTime)
		{
			String display = context.getResources().getString(R.string.event_in_progress);
			view.setText(display);
		}
		//Else set to event start date
		else
		{
			//Set time
	        String displayFormat = "hh:mm aa";
	        SimpleDateFormat df = new SimpleDateFormat(displayFormat, Locale.US);
	        String timeText = df.format(e.getStartDate());
	        view.setText(timeText);
		}
	}

}
