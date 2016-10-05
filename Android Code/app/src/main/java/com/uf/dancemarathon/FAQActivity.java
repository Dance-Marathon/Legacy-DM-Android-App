package com.uf.dancemarathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.uf.dancemarathon.FontSetter.fontName;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity shows FAQs in a listview
 * @author Chris Whitten
 *
 */
public class FAQActivity extends AppCompatActivity {

	private String ACTION_BAR_TITLE = "FAQs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		
		//Get the listview
		ExpandableListView list = (ExpandableListView) findViewById(R.id.faq_list);
		
		//Get the faqs
		try {
			ArrayList<FAQ> faqs = parseJSONData("faq.json");
            list.setAdapter(new faqAdapter(this, faqs));
            list.setGroupIndicator(null);
            list.setDivider(null);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			displayErrorToast();
			list.setVisibility(View.GONE);
			//e.printStackTrace();
		}

		//Customize action bar
		ActionBar bar = getSupportActionBar();
		TextView customBar = ActionBarUtility.customizeActionBar(this, bar, R.color.action_bar_color, R.color.White, Gravity.CENTER, 20, ACTION_BAR_TITLE);
		FontSetter.setFont(this, fontName.ALTB, customBar);
	}

	protected void onStart()
	{
		super.onStart();
		
		//Register google analytics page hit
		int canTrack = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplication());
		if(canTrack == ConnectionResult.SUCCESS)
		{
			//Log.d("Tracking", "FAQActivity");
			TrackerManager.sendScreenView((MyApplication) getApplication(), TrackerManager.FAQ_ACTIVITY_NAME);
		}
	}
	
	/**
	 * This method displays an error toast
	 */
	private void displayErrorToast()
	{
		Toast toast = Toast.makeText(this, "Could not display FAQ Page", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * This class serves as an adapter for FAQ objects. 
	 * It is used to populate the listview.
	 * @author Chris Whitten
	 *
	 */
	private class faqAdapter extends BaseExpandableListAdapter
	{
		private Context c;
		private ArrayList<FAQ> faqs;
		private LayoutInflater inflater;
		public faqAdapter(Context c, ArrayList<FAQ> faqs)
		{
			this.faqs = faqs;
			this.c = c;
			this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getGroupCount() {
			return faqs.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return faqs.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return ((FAQ) faqs.get(groupPosition)).answer;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            FAQ f = faqs.get(groupPosition);

            View v;
            if(convertView == null)
                v = inflater.inflate(R.layout.faq_group_item, parent, false);
            else
                v = convertView;


            TextView question = (TextView) v.findViewById(R.id.faq_item_question);
            FontSetter.setFont(c, fontName.ALTB, question);
            question.setText(f.question);

			return v;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            FAQ f = faqs.get(groupPosition);

            View v;
            if(convertView == null)
                v = inflater.inflate(R.layout.faq_list_item, parent, false);
            else
                v = convertView;


            TextView answer = (TextView) v.findViewById(R.id.faq_item_answer);
            FontSetter.setFont(c, fontName.ALTM, answer);
            answer.setText(f.answer);

            return v;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}
	
	
	/**
	 * Parses the given file for FAQ objects
	 * @param fileName The file to use
	 * @return The list of FAQ objects
	 * @throws JSONException
	 * @throws IOException
	 */
	private ArrayList<FAQ> parseJSONData(String fileName) throws JSONException, IOException
	{
		ArrayList<FAQ> faqs = new ArrayList<FAQ>();
		String json="";
		String next="";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getAssets().open(fileName)));
		while((next=reader.readLine()) != null)
			json+=next;
		reader.close();
		
		if(json.length() > 1)
		{
			//Log.d("q", "here");
			JSONArray arr = new JSONArray(json);
			for(int i=0; i < arr.length(); i++)
			{
				JSONObject o = (JSONObject) arr.get(i);
				String question = o.getString("Question");
				String answer = o.getString("Answer");
				//Log.d("q", question);
				faqs.add(new FAQ(question, answer));
			}
		}
			
		return faqs;
	}
	
	/**
	 * This class represents an FAQ
	 * @author Chris Whitten
	 *
	 */
	private class FAQ {
		public String question;
		public String answer;
		
		public FAQ(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}
		
	}
}
