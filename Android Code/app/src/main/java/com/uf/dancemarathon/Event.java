package com.uf.dancemarathon;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;



/**
 * @author Chris Whitten
 * This class represents a DM event.
 */
public class Event implements Serializable, Comparable<Event>, Parcelable
{
	/**
	 * This ID is important when it comes to keeping event serializable
	 */
	private static final long serialVersionUID = 10L;
	private String id;
	/**
	 * The title of the event
	 */
	private String title;
	/**
	 * The name or address of the location of the event
	 */
	private String location;
	private final String timeStampFormat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * A timestamp of the start date in the format yyyy-MM-dd HH:mm:ss
	 */
	private String t_startDate;
	/**
	 * A timestamp of the end date in the format yyyy-MM-dd HH:mm:ss
	 */
	private String t_endDate;
	/**
	 * A timestamp of the last modified date in the format yyyy-MM-dd HH:mm:ss
	 */
	private String t_lastMod;
	
	//The associated Date objects for the above timestamps
	private Date startDate;
	private Date endDate;
	private Date lastMod;
	
	/**
	 * A description of the event
	 */
	private String description; 
	/**
	 * The type of event it is (i.e. check-ins, hospitality night)
	 */
	private String category;

    private String imageURL;
	private Bitmap image;
	
	/**
	 * @param id The event id as specified on the server
	 * @param title The title of the event
	 * @param location The name or address of the location of the event
	 * @param t_startDate The startDate in string format
	 * @param t_endDate The endDate in string format
	 * @param t_lastMod The last modified date in string format
	 * @param description The description of the event
	 * @throws ParseException if the dates where unable to be parsed
	 */
	public Event(String id, String title, String location, String t_startDate,
			String t_endDate, String t_lastMod, String description) throws ParseException 
	{
		this.id = id;
		this.title = title;
		setLocation(location);
		this.t_startDate = t_startDate;
		this.t_endDate = t_endDate;
		this.t_lastMod = t_lastMod;
		this.description = description;
		this.category = null;
		
		parseTimeStamps();
	}

    public Event(String id, String title, String location, long startTime,
                long endTime, long lastMod, String description) throws ParseException
    {
        this.id = id;
        this.title = title;
        setLocation(location);
        this.description = description;
        this.category = null;

        parseIntegerTimeStamps(startTime, endTime, lastMod);
    }

	/**
	 * This method will parse the timestamps of the event to give values to the {@link Date} objects
	 */
	private void parseTimeStamps() throws ParseException
	{
		//This object allows us to parse a timestamp into a Date object
		SimpleDateFormat df = new SimpleDateFormat(timeStampFormat, Locale.US);
		startDate = df.parse(t_startDate);
		endDate = df.parse(t_endDate);
		lastMod = df.parse(t_lastMod);
	}

	/**
	 * Set start and end dates given integer millisecond offsets from a reference time
	 * @param start
	 * @param end
	 */
	private void parseIntegerTimeStamps(long start, long end, long lastMod){

		setStartDate(new Date(start));
		setEndDate(new Date(end));
        setLastMod(new Date(lastMod));

	}

	/**
	 * @return the id
	 */
	protected String getId()
	{
		return id;
	}


	/**
	 * @return the title
	 */
	protected String getTitle()
	{
		return title;
	}


	/**
	 * @return the location
	 */
	protected String getLocation()
	{
		return location;
	}


	/**
	 * @return the t_startDate
	 */
	protected String getT_startDate()
	{
		return t_startDate;
	}


	/**
	 * @return the t_endDate
	 */
	protected String getT_endDate()
	{
		return t_endDate;
	}


	/**
	 * @return the t_lastMod
	 */
	protected String getT_lastMod()
	{
		return t_lastMod;
	}


	/**
	 * @return the description
	 */
	protected String getDescription()
	{
		return description;
	}


	/**
	 * @param id the id to set
	 */
	protected void setId(String id)
	{
		this.id = id;
	}


	/**
	 * @param title the title to set
	 */
	protected void setTitle(String title)
	{
		this.title = title;
	}


	/**
	 * @param location the location to set
	 */
	protected void setLocation(String location)
	{
		if(location.trim().length() > 0)
			this.location = location;
		else
			this.location = "No Location";
	}


	/**
	 * @param t_startDate the t_startDate to set
	 * @throws ParseException if the date was unable to be parsed
	 */
	protected void setT_startDate(String t_startDate) throws ParseException
	{
		this.t_startDate = t_startDate;
		parseTimeStamps();
	}


	/**
	 * @param t_endDate the t_endDate to set
	 */
	protected void setT_endDate(String t_endDate)
	{
		this.t_endDate = t_endDate;
	}


	/**
	 * @param t_lastMod the t_lastMod to set
	 */
	protected void setT_lastMod(String t_lastMod)
	{
		this.t_lastMod = t_lastMod;
	}


	/**
	 * @param description the description to set
	 */
	protected void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * This method gets the integer representation of the month of either the start or end date.
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return Integer represenation of the month for start or end date
	 */
	public int getMonth(boolean useStartDate)
	{
		Calendar c = Calendar.getInstance();
		
		if(useStartDate)
			c.setTime(startDate);
		else
			c.setTime(endDate);
		
		return c.get(Calendar.MONTH);
	}
	
	/**
	 * 
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return 3-character string representation of the month
	 */
	public String getMonthText(boolean useStartDate)
	{
		int month = getMonth(useStartDate);
		
		switch(month)
		{
		case Calendar.JANUARY:	return "Jan";
		case Calendar.FEBRUARY:	return "Feb";
		case Calendar.MARCH:    return "Mar";
		case Calendar.APRIL: 	return "Apr";
		case Calendar.MAY: 		return "May";
		case Calendar.JUNE:		return "Jun";
		case Calendar.JULY: 	return "Jul";
		case Calendar.AUGUST: 	return "Aug";
		case Calendar.SEPTEMBER:return "Sep";
		case Calendar.OCTOBER: 	return "Oct";
		case Calendar.NOVEMBER: return "Nov";
		case Calendar.DECEMBER: return "Dec";
		default: {//Log.d("bad month", Integer.toString(month));
					return "Nul";
				}
		}
	}
	
	/**
	 * This method gets the integer representation of the day of either the start or end date.
	 * @param useStartDate Specify whether to use the start date or the end date
	 * @return Integer represenation of the day for start or end date
	 */
	public int getDay(boolean useStartDate)
	{
		Calendar c = Calendar.getInstance();
		
		if(useStartDate)
			c.setTime(startDate);
		else
			c.setTime(endDate);
		
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public String toString()
	{
		String rep = "";
		rep = rep.concat(getTitle() + "\t");
		rep = rep.concat(getLocation() + "\t");
		rep = rep.concat(getT_startDate() + "\t");
		
		return rep;
	}



	/**
	 * @return the timeStampFormat
	 */
	protected String getTimeStampFormat()
	{
		return timeStampFormat;
	}



	/**
	 * @return the startDate
	 */
	protected Date getStartDate()
	{
		return startDate;
	}



	/**
	 * @return the endDate
	 */
	protected Date getEndDate()
	{
		return endDate;
	}



	/**
	 * @return the lastMod
	 */
	protected Date getLastMod()
	{
		return lastMod;
	}



	/**
	 * @param startDate the startDate to set
	 */
	protected void setStartDate(Date startDate)
	{
		SimpleDateFormat df = new SimpleDateFormat(timeStampFormat, Locale.US);
		this.startDate = startDate;
		this.t_startDate = df.format(startDate);
	}



	/**
	 * @param endDate the endDate to set
	 */
	protected void setEndDate(Date endDate)
	{
		SimpleDateFormat df = new SimpleDateFormat(timeStampFormat, Locale.US);
		this.endDate = endDate;
		this.t_endDate = df.format(endDate);
	}



	/**
	 * @param lastMod the lastMod to set
	 */
	protected void setLastMod(Date lastMod)
	{
        SimpleDateFormat df = new SimpleDateFormat(timeStampFormat, Locale.US);
		this.lastMod = lastMod;
        this.t_lastMod = df.format(lastMod);
	}

	/**
	 * Get the start date in the specified format.
	 * @param format The format to use. See {@link SimpleDateFormat}
	 * @return The formatted string
	 */
	protected String getFormattedStartDate(String format)
	{
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.US);
        String stimeText = df.format(startDate);
		
        return stimeText;
	}
	
	/**
	 * Get the end date in the specified format.
	 * @param format The format to use. See {@link SimpleDateFormat}
	 * @return The formatted string
	 */
	protected String getFormattedEndDate(String format)
	{
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.US);
        String etimeText = df.format(endDate);
		
        return etimeText;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public int compareTo(Event another)
	{
		if(this.startDate.getTime() < another.getStartDate().getTime())
			return -1;
		else if(this.startDate.getTime() > another.getStartDate().getTime())
			return 1;
		else
			return 0;
	}

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(location);
		dest.writeString(t_startDate);
		dest.writeString(t_endDate);
		dest.writeString(t_lastMod);
		dest.writeString(description);
        dest.writeParcelable(image, 0);
	}
	
	private Event(Parcel in)
	{
		id = in.readString();
		title = in.readString();
		location = in.readString();
		t_startDate = in.readString();
		t_endDate = in.readString();
		t_lastMod = in.readString();
		description = in.readString();
		image = in.readParcelable(Bitmap.class.getClassLoader());

		try {
			parseTimeStamps();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new ClassCastException();
		}
	}
	

	public static final Parcelable.Creator<Event> CREATOR
    		= new Parcelable.Creator<Event>() {
		
		public Event createFromParcel(Parcel in) {
		    return new Event(in);
		}
		
		public Event[] newArray(int size) {
		    return new Event[size];
		}
	};

	
	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean hasImage(){
        return (image == null) ? false : true;
    }
}
