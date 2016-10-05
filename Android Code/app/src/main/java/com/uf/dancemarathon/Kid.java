package com.uf.dancemarathon;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Locale;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Kid implements Parcelable
{
	private String name;
	private int age;
	private String story;
	private String image_name;
	private String youtube_id;
	
	public Kid(String name, int age, String story, String image_name, String youtube_id) throws ParseException
	{
		this.name = name;
		this.age = age;
		this.story = story;
		this.image_name = image_name;
		this.youtube_id = youtube_id;
	}
	
	public Kid()
	{
	}
	
	protected int getImageId(Context c)
	{
		String image_name = this.image_name.toLowerCase(Locale.ENGLISH).replace(".png", "");
		int imageId = c.getResources().getIdentifier(image_name,"drawable", c.getPackageName());
		return imageId;
	}
	
	// Methods to make this class Parcelable //
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub
		dest.writeString(this.name);
		dest.writeInt(this.age);
		dest.writeString(this.story);
		dest.writeString(this.image_name);
		dest.writeString(this.youtube_id);
	}
	
	private Kid(Parcel in)
	{
		name = in.readString();
		age = in.readInt();
		story = in.readString();
		image_name = in.readString();
		youtube_id = in.readString();
	}
	

	public static final Parcelable.Creator<Kid> CREATOR
    		= new Parcelable.Creator<Kid>() {
		
		public Kid createFromParcel(Parcel in) {
		    return new Kid(in);
		}
		
		public Kid[] newArray(int size) {
		    return new Kid[size];
		}
	};
	
	//------------------------------//
	protected String getName()
	{
		return name;
	}
	
	protected int getAge()
	{
		return age;
	}
	
	public String getStory()
	{
		return story;
	}
	
	protected String getImage_name()
	{
		return image_name;
	}
	
	protected String getYoutube_id()
	{
		return youtube_id;
	}
	
	protected void setName(String name)
	{
		this.name = name;
	}
	
	protected void setAge(int age)
	{
		this.age = age;
	}
	
	protected void setStory(String story)
    {
		this.story = story;
	}

    public boolean hasStory()
    {
        if(story != null && story.length() > 10)
            return true;
        else
            return false;
    }
	
	protected void setImage_name(String image_name)
	{
		this.image_name = image_name;
	}
	
	protected void setYoutube_id(String youtube_id)
	{
		this.youtube_id = youtube_id;
	}

	public boolean hasMilestone(){
		return youtube_id.length() > 0;
	}
	public static Comparator<Kid> COMPARE_BY_NAME = new Comparator<Kid>() {
        public int compare(Kid one, Kid other) {
            return one.getName().compareTo(other.getName());
        }
    };
	
}