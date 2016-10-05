package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class LoginFragment extends Fragment
{

	public LoginFragment()
	{
		// Required empty public constructor
	}

	public static LoginFragment newInstance()
	{
		LoginFragment lf = new LoginFragment();
		return lf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		//Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		
		//Hide the keyboard if this fragment is clicked
		v.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				hideKeyboard();
			}
			
		});
		
		//Set the actions to perform if //Login button is clicked
		v.findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (isFormFilled()) {
                    //Get the username and password from the //Login form
                    EditText usernameF = (EditText) getView().findViewById(R.id.username_field);
                    EditText passwordF = (EditText) getView().findViewById(R.id.password_field);
                    String username = usernameF.getText().toString();
                    String password = passwordF.getText().toString();

                    //Hide login button
                    Button loginB = (Button) getView().findViewById(R.id.login_button);
                    loginB.setVisibility(View.GONE);

                    //Show progress bar
                    ProgressBar bar = (ProgressBar) getView().findViewById(R.id.login_loading_wheel);
                    bar.setVisibility(View.VISIBLE);

                    //Execute load with credentials
                    new UserLoader().execute(username, password);
                } else
                    makeToast("Fields cannot be blank!");
            }

            //Check to make sure the form is filled in
            private Boolean isFormFilled() {
                int uSize = ((EditText) getView().findViewById(R.id.username_field)).getText().length();
                int pSize = ((EditText) getView().findViewById(R.id.password_field)).getText().length();
                if (uSize > 0 && pSize > 0)
                    return true;
                else
                    return false;
            }

        });
		
		return v;
	}
	
	/**
	 * This method hides the keyboard on whichever activity this fragment is attached to
	 */
	private void hideKeyboard()
	{
		//Hide keyboard 
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	}
	
	/**
	 * This method shows a toast with the given message
	 * @param message The message to show
	 */
	private void makeToast(String message)
	{
		Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.show();
	}
	
	/**
	 * @author Chris Whitten
	 * This class is responsible for loading all the kintera information about the user.
	 * The onProgressUpdate() method is used to display any error messages as toasts
	 *
	 */
	private class UserLoader extends AsyncTask<String, String, KinteraUser>
	{
		private String username="";
		private String password="";
        private final String USER_WEBSERVICE_PATH = "http://dev.floridadm.org/app/kintera.php";

		boolean loadSuccessful = false;
		@Override
		protected KinteraUser doInBackground(String... params)
		{	
			//Begin loading work
			KinteraUser user = new KinteraUser();
			URL url;
			try
			{
				//Get username and password from params
				username = params[0].trim();
				password = params[1];
				
				//Set path
				String path = USER_WEBSERVICE_PATH;
				
				//Connect to the webservice
                String urlParams  = "username=" + username + "&password=" + password;
                byte[] postData = urlParams.getBytes( StandardCharsets.UTF_8 );

				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                //Write parameters to POST
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParams);
                wr.flush();
                wr.close();
				
				//Parse JSON response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				if(conn.getResponseCode() == 200)
				{
					String jsonRep = reader.readLine().trim();
					if(jsonRep.equals("Error"))
						publishProgress("Invalid Credentials");
					else
					{
						JSONObject o = new JSONObject(jsonRep);
						user = parseUserJson(o);
						loadSuccessful = true;
					}
				}
				else
				{
					publishProgress("Sorry, we are currently experiencing server problems!");
				}
				
			} catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				publishProgress("Sorry, we are currently experiencing technical problems!");
				//e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				publishProgress("Could not load user data! Check internet connection.");
				//e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				publishProgress("Sorry, we are currently experiencing technical problems!");
				//e.printStackTrace();
			}
			
			return user;
		}
		
		protected void onProgressUpdate(String... params)
		{
			makeToast(params[0]);
		}
		
		protected void onPostExecute(KinteraUser user)
		{	
			//If the load was successful, start the user activity
			if(loadSuccessful)
			{
				Intent intent = new Intent(getActivity(), UserActivity.class);
				Bundle b = new Bundle();
				b.putParcelable("user", user);
                b.putString("password", password);
				intent.putExtras(b);
				startActivity(intent);
			
				//Write user data to cache
				CacheManager.clearCacheFile(getActivity(), CacheManager.USER_FILENAME);
				CacheManager.writeObjectToCacheFile(getActivity(), user, CacheManager.USER_FILENAME);
			}
			else
			{
				//Cancel the pass back
				getActivity().setResult(Activity.RESULT_CANCELED, new Intent());
			}
			
			//Hide the indeterminate progress wheel
			ProgressBar bar = (ProgressBar) getView().findViewById(R.id.login_loading_wheel);
			bar.setVisibility(View.GONE);
			
			//Show the login button
			Button loginB = (Button) getView().findViewById(R.id.login_button);
			loginB.setVisibility(View.VISIBLE);
		}
		
		private KinteraUser parseUserJson(JSONObject o) throws JSONException
		{
			//Parse json
			String realName = o.getString("ParticipantName");
			String pageURL = o.getString("PersonalPageUrl");
			double fundGoal = Double.parseDouble(o.getString("PersonalGoal"));
			double fundRaised = Double.parseDouble(o.getString("PersonalRaised"));
			
			KinteraUser user = new KinteraUser(username,realName, fundGoal, fundRaised, pageURL);
			return user;
		}
	}
	
	
}
