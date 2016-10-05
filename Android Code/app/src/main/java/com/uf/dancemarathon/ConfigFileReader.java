package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * This class is responsible for reading fields from the config file.
 * @author Chris Whitten
 *
 */
public class ConfigFileReader {
	
	/**
	 * The config object representing the settings
	 */
	private JSONObject config;
	
	/**
	 * @param c The context containing the config file
	 * @throws JSONException If file read failed
	 * @throws IOException If file read failed
	 */
	public ConfigFileReader(Context c) throws IOException, JSONException 
	{
		config = readSettings(c);
	}
	
	/**
	 * @param c The context to use
	 * @return A {@link JSONObject} containing the settings
	 * @throws IOException If config file could not be opened
	 * @throws JSONException If JSON parsing failed
	 */
	private JSONObject readSettings(Context c) throws IOException, JSONException
	{
		String json = "";
		String next ="";
		
		//Read from the file
		BufferedReader reader = new BufferedReader(new InputStreamReader(c.getAssets().open("config.json")));
		while((next=reader.readLine()) != null)
			json+=next;
		reader.close();
		
		//Parse the json
		return new JSONObject(json);
	}
	
	/**
	 * @param name The name of the setting to retrieve
	 * @return The value of the setting
	 * @throws JSONException If the setting provided does not exist
	 */
	protected String getSetting(String name) throws JSONException
	{
		return config.getString(name);
	}
	
}
