package com.uf.dancemarathon;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class represents an announcement
 * @author Chris Whitten
 *
 */
public class Announcement implements Comparable<Announcement>, Serializable
{
	/**
	 * First serializable id
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The announcement text
	 */
	public String text;
	/**
	 * The date the announcement was sent on
	 */
	private Date date;
	
	public Announcement(String text, String date, String dateFormat) throws ParseException{
		this.text = text;
		setDate(date, dateFormat);
	}

	public Announcement(String text, Date date){
		this.text = text;
		this.date = date;
	}
	
	/**
	 * This method parses the input string to create set this object's date
	 * @param date The string date
	 * @param dateFormat The format the date is in. See {@link SimpleDateFormat}
	 * @throws ParseException If the date could not be parsed
	 */
	public void setDate(String date, String dateFormat) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
		this.date = df.parse(date);
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public Date getDate()
	{
		return date;
	}

	@Override
	public int compareTo(Announcement another) {
		
		if(this.date.getTime() < another.date.getTime())
			return 1;
		else if(this.date.getTime() > another.date.getTime())
			return -1;
		else
			return 0;
	}

	
	public boolean hasOccurred(){
		if(date.getTime() > Calendar.getInstance().getTimeInMillis())
			return false;
		else
			return true;
	}
	
	
	
	
	
}
