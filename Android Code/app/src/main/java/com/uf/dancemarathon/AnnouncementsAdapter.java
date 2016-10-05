package com.uf.dancemarathon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


import com.uf.dancemarathon.FontSetter.fontName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class AnnouncementsAdapter extends ArrayAdapter<Announcement>
{
	private Context context;
	private ArrayList<Announcement> announcements = new ArrayList<Announcement>();
	
	public AnnouncementsAdapter(Context c, ArrayList<Announcement> announcements)
	{
		//Must make call to the parent constructor
		super(c, R.layout.announcements_item_view, announcements);
		
		Collections.sort(announcements);
		
		this.context = c;
		this.announcements = announcements;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.announcements.size();
	}

	@Override
	public Announcement getItem(int position) {
		// TODO Auto-generated method stub
		return this.announcements.get(position);
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
        
        //If convert view is null, we need to make the individual view for the event
        if(convertView == null)
        {
	        //Get the individual item view
	        itemView = inflater.inflate(R.layout.announcements_item_view, parent, false);
	        
	        //Set all the values for the view
	        setItemView(itemView, position);
	        
        }
        //Else we can use the recycled view passed in as convertView
        else
        {
        	itemView = convertView;
        	setItemView(itemView, position); //We must set the recycled view with the new information
        }

        return itemView;
	}
	
	private void setItemView(View itemView, int position)
	{
		Announcement a = this.announcements.get(position);
		
		//Get title and location text views
        TextView text_announce = (TextView) itemView.findViewById(R.id.announcements_text);
        TextView date = (TextView) itemView.findViewById(R.id.announcements_date);
        
        //Set text views //
        text_announce.setText(a.text);

        //Set time
        String displayFormat = "MM/dd  hh:mm aa";
        SimpleDateFormat df = new SimpleDateFormat(displayFormat, Locale.US);
        String timeText = df.format(a.getDate());
        date.setText(timeText);
        
        // Set font
        FontSetter.setFont(context, fontName.ALTR, text_announce, date);
	}
	
}