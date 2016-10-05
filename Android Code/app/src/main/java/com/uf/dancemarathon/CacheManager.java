package com.uf.dancemarathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Chris Whitten
 * This class is responsible for reading and writing to cache files on the Android system
 * as well as managing cache space.
 */
public class CacheManager
{

	public final static String USER_FILENAME = "user";
    public final static String ANNOUNCEMENTS_FILENAME = "announcements";
    public final static String EVENTS_FILENAME = "events";

	/**
	 * This method will write the object to a cache file. If the cache file does not exist,
	 * then a new file will be created with the given path.
	 * @param c The context to use
	 * @param o The object to write
	 * @param fileName The name of the cache file
	 * @return Whether or not the write was successful
	 */
	public static boolean writeObjectToCacheFile(Context c, Object o, String fileName)
	{
		File f =  new File(c.getCacheDir(), fileName);
		try
		{
			if(!f.isFile())
				File.createTempFile(fileName, null, c.getCacheDir());
			ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(f));
			ous.writeObject(o);
			ous.close();
			
			return true;
			
		} catch (FileNotFoundException e)
		{
			//e.printStackTrace();
			return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method will read the first object in the cache file
	 * @param c The context to use
	 * @param fileName The cache file name
	 * @return The object
	 */
	public static Object readObjectFromCacheFile(Context c, String fileName)
	{
		File f =  new File(c.getCacheDir(), fileName);
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Object o = ois.readObject();
			ois.close();
			return o;
			
		} catch (StreamCorruptedException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
	}
	
	/** 
	 * Clear the cache file
	 * @param c the context to use
	 * @param fileName The name of the cache file
	 * @return Whether or not the clear was successful
	 */
	public static boolean clearCacheFile(Context c, String fileName)
	{
		File f =  new File(c.getCacheDir(), fileName);
		try
		{
			//If the file doesn't exist return false
			if(!f.isFile())
				return false;
			
			//Delete the file and remake it
			f.delete();
			File.createTempFile(fileName, null, c.getCacheDir());
			
			return true;
			
		} catch (FileNotFoundException e)
		{
			//e.printStackTrace();
			return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}
}
